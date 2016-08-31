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

}
