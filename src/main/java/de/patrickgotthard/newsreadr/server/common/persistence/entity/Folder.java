package de.patrickgotthard.newsreadr.server.common.persistence.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "folders")
public class Folder extends AbstractUserEntity {

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE)
    private Set<Subscription> subscriptions;

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

}
