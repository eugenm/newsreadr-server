package de.patrickgotthard.newsreadr.server.users.response;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Role;

public class UserDTO {

    private Long userId;
    private String username;
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

    public Role getRole() {
        return this.role;
    }

    public void setRole(final Role role) {
        this.role = role;
    }

}
