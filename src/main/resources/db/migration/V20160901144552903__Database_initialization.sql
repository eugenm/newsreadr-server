DROP PROCEDURE IF EXISTS migration;
DELIMITER $$
CREATE PROCEDURE migration()
BEGIN

    CREATE TABLE `users_tmp` (
        `user_id` INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
        `username` VARCHAR(32) NOT NULL UNIQUE,
        `password` VARCHAR(60) NOT NULL,
        `role` VARCHAR(5) NOT NULL
    );

    CREATE TABLE `subscriptions_tmp` (
        `subscription_id` INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
        `user_id` INT UNSIGNED NOT NULL DEFAULT 0,
        `url` VARCHAR(1024) NOT NULL,
        `title` VARCHAR(512) NOT NULL,
        INDEX `idx_user_id` (`user_id` ASC),
        CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users_tmp` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
    );

    CREATE TABLE `entries_tmp` (
        `entry_id` INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
        `subscription_id` INT UNSIGNED NOT NULL DEFAULT 0,
        `uri` VARCHAR(1024) NOT NULL,
        `url` VARCHAR(1024) NOT NULL,
        `title` VARCHAR(512) NOT NULL,
        `content` LONGTEXT NOT NULL,
        `published` DATETIME NOT NULL,
        `read` BIT(1) NOT NULL,
        `bookmarked` BIT(1) NOT NULL,
        INDEX `idx_subscription_id` (`subscription_id` ASC),
        INDEX `idx_published` (`published` ASC),
        INDEX `idx_read` (`read` ASC),
        INDEX `idx_bookmarked` (`bookmarked` ASC),
        CONSTRAINT `fk_subscription` FOREIGN KEY (`subscription_id`) REFERENCES `subscriptions_tmp` (`subscription_id`) ON DELETE CASCADE ON UPDATE CASCADE
    );

    IF EXISTS(SELECT 1 FROM information_schema.tables WHERE table_schema = (SELECT DATABASE()) AND table_name = 'databasechangelog') THEN

        INSERT INTO users_tmp(`user_id`, `username`, `password`, `role`)
        SELECT      id, username, password, role
        FROM        users;

        INSERT INTO subscriptions_tmp(`subscription_id`, `user_id`, `url`, `title`)
        SELECT      s.id, u.id, f.url, s.title
        FROM        subscriptions s
        JOIN        users u ON u.id = s.user_id
        JOIN        feeds f ON f.id = s.feed_id;

        INSERT INTO entries_tmp(`subscription_id`, `uri`, `url`, `title`, `content`, `published`, `read`, `bookmarked`)
        SELECT      s.id, e.uri, e.url, e.title, e.content, e.publish_date, u.marked_read, u.bookmarked
        FROM        user_entries u
        JOIN        subscriptions s ON s.id = u.subscription_id
        JOIN        entries e ON e.id = u.entry_id;
        
        DROP TABLE databasechangelog;
        DROP TABLE databasechangeloglock;
        DROP TABLE user_entries;
        DROP TABLE subscriptions;
        DROP TABLE folders;
        DROP TABLE users;
        DROP TABLE entries;
        DROP TABLE feeds;

    END IF;
    
    RENAME TABLE users_tmp TO users;
    RENAME TABLE subscriptions_tmp TO subscriptions;
    RENAME TABLE entries_tmp TO entries;

END $$

DELIMITER ;
CALL migration();
DROP PROCEDURE migration;