package de.patrickgotthard.newsreadr.server.users.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Role;
import de.patrickgotthard.newsreadr.server.common.util.ObjectUtil;

public class AddUserRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private Role role;

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
