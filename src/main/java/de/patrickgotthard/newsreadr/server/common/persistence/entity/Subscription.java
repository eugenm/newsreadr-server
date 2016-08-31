package de.patrickgotthard.newsreadr.server.common.persistence.entity;

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

    public Folder getFolder() {
        return this.folder;
    }

    public void setFolder(final Folder folder) {
        this.folder = folder;
    }

    public Feed getFeed() {
        return this.feed;
    }

    public void setFeed(final Feed feed) {
        this.feed = feed;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Set<UserEntry> getUserEntries() {
        return this.userEntries;
    }

    public void setUserEntries(final Set<UserEntry> userEntries) {
        this.userEntries = userEntries;
    }

}
