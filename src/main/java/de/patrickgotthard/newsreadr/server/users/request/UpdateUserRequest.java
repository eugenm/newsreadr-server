package de.patrickgotthard.newsreadr.server.users.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Role;

public class UpdateUserRequest {

    @NotNull
    @Min(1)
    private Long userId;

    private String username;

    private String password;

    private Role role;

    public UpdateUserRequest() {
    }

    private UpdateUserRequest(final Builder builder) {
        this.userId = builder.userId;
        this.username = builder.username;
        this.password = builder.password;
        this.role = builder.role;
    }

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
        final StringBuilder builder = new StringBuilder();
        builder.append("UpdateUserRequest [userId=");
        builder.append(this.userId);
        builder.append(", username=");
        builder.append(this.username);
        builder.append(", password=");
        builder.append(this.password);
        builder.append(", role=");
        builder.append(this.role);
        builder.append("]");
        return builder.toString();
    }

    public static class Builder {

        private Long userId;
        private String username;
        private String password;
        private Role role;

        public Builder setUserId(final Long userId) {
            this.userId = userId;
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

        public UpdateUserRequest build() {
            return new UpdateUserRequest(this);
        }

    }

}
