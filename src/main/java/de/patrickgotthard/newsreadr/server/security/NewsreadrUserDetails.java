package de.patrickgotthard.newsreadr.server.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.patrickgotthard.newsreadr.server.util.ListUtil;
import de.patrickgotthard.newsreadr.shared.response.data.Role;

public class NewsreadrUserDetails implements UserDetails {

    private static final long serialVersionUID = 2014085622302456977L;

    private final long userId;
    private final String username;
    private final String password;
    private final Role role;

    public NewsreadrUserDetails(final long userId, final String username, final String password, final Role role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ListUtil.toList(new SimpleGrantedAuthority("ROLE_" + role.name()));
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
