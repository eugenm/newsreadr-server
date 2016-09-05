package de.patrickgotthard.newsreadr.server.entries.response;

import java.util.Date;

public class EntrySummary {

    private String subscription;
    private Long id;
    private String url;
    private String title;
    private Date publishDate;
    private Boolean read;
    private Boolean bookmarked;

    public String getSubscription() {
        return this.subscription;
    }

    public void setSubscription(final String subscription) {
        this.subscription = subscription;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    public Date getPublishDate() {
        return this.publishDate;
    }

    public void setPublishDate(final Date publishDate) {
        this.publishDate = publishDate;
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
