package de.patrickgotthard.newsreadr.server.common.rest;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;

public class NewsreadrUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String password;
    private Collection<SimpleGrantedAuthority> authorities;

    public NewsreadrUserDetails(final User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(final Collection<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
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

}
