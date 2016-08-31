package de.patrickgotthard.newsreadr.server.users.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import de.patrickgotthard.newsreadr.server.common.util.ObjectUtil;

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
        return ObjectUtil.toString(this);
    }

}
