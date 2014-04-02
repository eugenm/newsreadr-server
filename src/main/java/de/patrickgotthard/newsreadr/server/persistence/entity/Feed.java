package de.patrickgotthard.newsreadr.server.persistence.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "feeds")
public class Feed extends AbstractEntity {

    @Column(name = "url")
    private String url;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "feed")
    private Set<Subscription> subscriptions;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    private Set<Entry> entries;

    public Feed() {
    }

    private Feed(final String url, final String title, final Set<Subscription> subscriptions, final Set<Entry> entries) {
        this.url = url;
        this.title = title;
        this.subscriptions = subscriptions;
        this.entries = entries;
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

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(final Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Set<Entry> getEntries() {
        return entries;
    }

    public void setEntries(final Set<Entry> entries) {
        this.entries = entries;
    }

    public static class Builder {

        private String url;
        private String title;
        private Set<Subscription> subscriptions;
        private Set<Entry> entries;

        public Builder setUrl(final String url) {
            this.url = url;
            return this;
        }

        public Builder setTitle(final String title) {
            this.title = title;
            return this;
        }

        public Builder setSubscriptions(final Set<Subscription> subscriptions) {
            this.subscriptions = subscriptions;
            return this;
        }

        public Builder setEntries(final Set<Entry> entries) {
            this.entries = entries;
            return this;
        }

        public Feed build() {
            return new Feed(url, title, subscriptions, entries);
        }

    }

}
