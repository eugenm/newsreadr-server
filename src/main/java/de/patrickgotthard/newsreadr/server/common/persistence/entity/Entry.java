package de.patrickgotthard.newsreadr.server.common.persistence.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "entries")
public class Entry extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Column(name = "uri")
    private String uri;

    @Column(name = "url")
    private String url;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "publish_date")
    private Date publishDate;

    @OneToMany(mappedBy = "entry", cascade = CascadeType.REMOVE)
    private Set<UserEntry> userEntries;

    public Feed getFeed() {
        return this.feed;
    }

    public void setFeed(final Feed feed) {
        this.feed = feed;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public Date getPublishDate() {
        return this.publishDate;
    }

    public void setPublishDate(final Date publishDate) {
        this.publishDate = publishDate;
    }

    public Set<UserEntry> getUserEntries() {
        return this.userEntries;
    }

    public void setUserEntries(final Set<UserEntry> userEntries) {
        this.userEntries = userEntries;
    }

}
