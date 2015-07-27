package de.patrickgotthard.newsreadr.server.common.persistence.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User extends AbstractEntity implements UserDetails {

    private static final long serialVersionUID = 7901173554811211675L;

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
        this.setId(builder.id);
        this.username = builder.username;
        this.password = builder.password;
        this.role = builder.role;
        this.folders = builder.folders;
        this.subscriptions = builder.subscriptions;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(final Role role) {
        this.role = role;
    }

    public Set<Folder> getFolders() {
        return this.folders;
    }

    public void setFolders(final Set<Folder> folders) {
        this.folders = folders;
    }

    public Set<Subscription> getSubscriptions() {
        return this.subscriptions;
    }

    public void setSubscriptions(final Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final String roleName = "ROLE_" + this.role.name();
        final SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleName);
        return Arrays.asList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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
