package de.patrickgotthard.newsreadr.server.persistence.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.patrickgotthard.newsreadr.shared.response.data.Role;

@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<Folder> folders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Set<Subscription> subscriptions;

    public User() {
    }

    private User(final Builder builder) {
        setId(builder.id);
        username = builder.username;
        password = builder.password;
        role = builder.role;
        folders = builder.folders;
        subscriptions = builder.subscriptions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(final Role role) {
        this.role = role;
    }

    public Set<Folder> getFolders() {
        return folders;
    }

    public void setFolders(final Set<Folder> folders) {
        this.folders = folders;
    }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(final Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public static class Builder {

        private Long id;
        private String username;
        private String password;
        private Role role;
        private Set<Folder> folders;
        private Set<Subscription> subscriptions;

        public Builder setId(final Long id) {
            this.id = id;
            return this;
        }

        public Builder setUsername(final String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(final String password) {
            this.password = password;
            return this;
        }

        public Builder setRole(final Role role) {
            this.role = role;
            return this;
        }

        public Builder setFolders(final Set<Folder> folders) {
            this.folders = folders;
            return this;
        }

        public Builder setSubscriptions(final Set<Subscription> subscriptions) {
            this.subscriptions = subscriptions;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }

}
