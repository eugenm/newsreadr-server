package de.patrickgotthard.newsreadr.server.users.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Role;
import de.patrickgotthard.newsreadr.server.common.util.ObjectUtil;

public class UpdateUserRequest {

    @NotNull
    @Min(1)
    private Long userId;

    private String username;

    private String password;

    private Role role;

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

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

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }

}
