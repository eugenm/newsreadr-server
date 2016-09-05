package de.patrickgotthard.newsreadr.server.subscriptions.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import de.patrickgotthard.newsreadr.server.common.util.ObjectUtil;

public class UpdateSubscriptionRequest {

    @NotNull
    @Min(1)
    private Long subscriptionId;

    private String title;

    public Long getSubscriptionId() {
        return this.subscriptionId;
    }

    public void setSubscriptionId(final Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }

}
