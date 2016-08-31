package de.patrickgotthard.newsreadr.server.subscriptions.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import de.patrickgotthard.newsreadr.server.common.util.ObjectUtil;

public class RemoveSubscriptionRequest {

    @NotNull
    @Min(1)
    private Long subscriptionId;

    public Long getSubscriptionId() {
        return this.subscriptionId;
    }

    public void setSubscriptionId(final Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }

}
