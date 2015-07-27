package de.patrickgotthard.newsreadr.server.common.persistence.entity;

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

    private Feed(final Builder builder) {
        this.setId(builder.id);
        this.url = builder.url;
        this.title = builder.title;
        this.subscriptions = builder.subscriptions;
        this.entries = builder.entries;
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

    public Set<Subscription> getSubscriptions() {
        return this.subscriptions;
    }

    public void setSubscriptions(final Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Set<Entry> getEntries() {
        return this.entries;
    }

    public void setEntries(final Set<Entry> entries) {
        this.entries = entries;
    }

    public static class Builder {

        private Long id;
        private String url;
        private String title;
        private Set<Subscription> subscriptions;
        private Set<Entry> entries;

        public Builder setId(final Long id) {
            this.id = id;
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

        public Builder setSubscriptions(final Set<Subscription> subscriptions) {
            this.subscriptions = subscriptions;
            return this;
        }

        public Builder setEntries(final Set<Entry> entries) {
            this.entries = entries;
            return this;
        }

        public Feed build() {
            return new Feed(this);
        }

    }

}
