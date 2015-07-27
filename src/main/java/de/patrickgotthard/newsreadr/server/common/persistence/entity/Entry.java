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

    public Entry() {
    }

    private Entry(final Builder builder) {
        this.setId(builder.id);
        this.feed = builder.feed;
        this.uri = builder.uri;
        this.url = builder.url;
        this.title = builder.title;
        this.content = builder.content;
        this.publishDate = builder.publishDate;
        this.userEntries = builder.userEntries;
    }

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

    public static class Builder {

        private Long id;
        private Feed feed;
        private String uri;
        private String url;
        private String title;
        private String content;
        private Date publishDate;
        private Set<UserEntry> userEntries;

        public Builder setId(final Long id) {
            this.id = id;
            return this;
        }

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
            return new Entry(this);
        }

    }

}
