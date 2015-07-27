package de.patrickgotthard.newsreadr.server.users.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RemoveUserRequest {

    @NotNull
    @Min(1)
    private Long userId;

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("RemoveUserRequest [userId=");
        builder.append(this.userId);
        builder.append("]");
        return builder.toString();
    }

}
