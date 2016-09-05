package de.patrickgotthard.newsreadr.server.common.persistence.entity;

import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "subscriptions")
@AttributeOverride(name = "id", column = @Column(name = "subscription_id"))
public class Subscription extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "url")
    private String url;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.REMOVE)
    private Set<Entry> entries;

    public User getUser() {
        return this.user;
    }

    public void setUser(final User user) {
        this.user = user;
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

    public Set<Entry> getEntries() {
        return this.entries;
    }

    public void setEntries(final Set<Entry> entries) {
        this.entries = entries;
    }

}
