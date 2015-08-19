package de.patrickgotthard.newsreadr.server.users.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Role;

public class AddUserRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private Role role;

    public AddUserRequest() {
    }

    private AddUserRequest(final Builder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.role = builder.role;
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
        builder.append("AddUserRequest [username=");
        builder.append(this.username);
        builder.append(", password=");
        builder.append(this.password);
        builder.append(", role=");
        builder.append(this.role);
        builder.append("]");
        return builder.toString();
    }

    public static class Builder {

        private String username;
        private String password;
        private Role role;

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

        public AddUserRequest build() {
            return new AddUserRequest(this);
        }

    }

}
