package de.patrickgotthard.newsreadr.server.common.persistence.entity;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "entries")
@AttributeOverride(name = "id", column = @Column(name = "entry_id"))
public class Entry extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @Column(name = "uri")
    private String uri;

    @Column(name = "url")
    private String url;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "published")
    private Date published;

    @Column(name = "`read`")
    private Boolean read = false;

    @Column(name = "bookmarked")
    private Boolean bookmarked = false;

    public Subscription getSubscription() {
        return this.subscription;
    }

    public void setSubscription(final Subscription subscription) {
        this.subscription = subscription;
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

    public Date getPublished() {
        return this.published;
    }

    public void setPublished(final Date published) {
        this.published = published;
    }

    public Boolean getRead() {
        return this.read;
    }

    public void setRead(final Boolean read) {
        this.read = read;
    }

    public Boolean getBookmarked() {
        return this.bookmarked;
    }

    public void setBookmarked(final Boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

}
