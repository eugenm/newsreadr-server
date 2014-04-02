package de.patrickgotthard.newsreadr.server.persistence.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "subscriptions")
public class Subscription extends AbstractUserEntity {

    @ManyToOne
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.REMOVE)
    private Set<UserEntry> userEntries;

    public Subscription() {
    }

    private Subscription(final User user, final Folder folder, final Feed feed, final String title, final Set<UserEntry> userEntries) {
        super(user);
        this.folder = folder;
        this.feed = feed;
        this.title = title;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(final Folder folder) {
        this.folder = folder;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(final Feed feed) {
        this.feed = feed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Set<UserEntry> getUserEntries() {
        return userEntries;
    }

    public void setUserEntries(final Set<UserEntry> userEntries) {
        this.userEntries = userEntries;
    }

    public static class Builder {

        private User user;
        private Folder folder;
        private Feed feed;
        private String title;
        private Set<UserEntry> userEntries;

        public Builder setUser(final User user) {
            this.user = user;
            return this;
        }

        public Builder setFolder(final Folder folder) {
            this.folder = folder;
            return this;
        }

        public Builder setFeed(final Feed feed) {
            this.feed = feed;
            return this;
        }

        public Builder setTitle(final String title) {
            this.title = title;
            return this;
        }

        public Builder setUserEntries(final Set<UserEntry> userEntries) {
            this.userEntries = userEntries;
            return this;
        }

        public Subscription build() {
            return new Subscription(user, folder, feed, title, userEntries);
        }

    }

}
