package de.patrickgotthard.newsreadr.server.persistence.entity;

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

    public Entry() {
    }

    private Entry(final Feed feed, final String uri, final String url, final String title, final String content, final Date publishDate,
            final Set<UserEntry> userEntries) {
        this.feed = feed;
        this.uri = uri;
        this.url = url;
        this.title = title;
        this.content = content;
        this.publishDate = publishDate;
        this.userEntries = userEntries;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(final Feed feed) {
        this.feed = feed;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(final Date publishDate) {
        this.publishDate = publishDate;
    }

    public Set<UserEntry> getUserEntries() {
        return userEntries;
    }

    public void setUserEntries(final Set<UserEntry> userEntries) {
        this.userEntries = userEntries;
    }

    public static class Builder {

        private Feed feed;
        private String uri;
        private String url;
        private String title;
        private String content;
        private Date publishDate;
        private Set<UserEntry> userEntries;

        public Builder setFeed(final Feed feed) {
            this.feed = feed;
            return this;
        }

        public Builder setUri(final String uri) {
            this.uri = uri;
            return this;
        }

        public Builder setUrl(final String url) {
            this.url = url;
            return this;
        }

        public Builder setTitle(final String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(final String content) {
            this.content = content;
            return this;
        }

        public Builder setPublishDate(final Date publishDate) {
            this.publishDate = publishDate;
            return this;
        }

        public Builder setUserEntries(final Set<UserEntry> userEntries) {
            this.userEntries = userEntries;
            return this;
        }

        public Entry build() {
            return new Entry(feed, uri, url, title, content, publishDate, userEntries);
        }

    }

}
