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

    public Folder() {
    }

    private Folder(final Builder builder) {
        this.setId(builder.id);
        this.setUser(builder.user);
        this.title = builder.title;
        this.subscriptions = builder.subscriptions;
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

    public static class Builder {

        private Long id;
        private User user;
        private String title;
        private Set<Subscription> subscriptions;

        public Builder setId(final Long id) {
            this.id = id;
            return this;
        }

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
            return new Folder(this);
        }

    }

}
