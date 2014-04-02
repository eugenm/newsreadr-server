package de.patrickgotthard.newsreadr.server.persistence.entity;

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

    public Folder() {
    }

    private Folder(final User user, final String title, final Set<Subscription> subscriptions) {
        super(user);
        this.title = title;
        this.subscriptions = subscriptions;
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

    public static class Builder {

        private User user;
        private String title;
        private Set<Subscription> subscriptions;

        public Builder setUser(final User user) {
            this.user = user;
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

        public Folder build() {
            return new Folder(user, title, subscriptions);
        }

    }

}
