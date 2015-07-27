package de.patrickgotthard.newsreadr.server.subscriptions.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UpdateSubscriptionRequest {

    @NotNull
    @Min(1)
    private Long subscriptionId;

    @Min(1)
    private Long folderId;

    private String title;

    public UpdateSubscriptionRequest() {
    }

    private UpdateSubscriptionRequest(final Builder builder) {
        this.subscriptionId = builder.subscriptionId;
        this.folderId = builder.folderId;
        this.title = builder.title;
    }

    public Long getSubscriptionId() {
        return this.subscriptionId;
    }

    public void setSubscriptionId(final Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public Long getFolderId() {
        return this.folderId;
    }

    public void setFolderId(final Long folderId) {
        this.folderId = folderId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UpdateSubscriptionRequest [subscriptionId=");
        builder.append(this.subscriptionId);
        builder.append(", folderId=");
        builder.append(this.folderId);
        builder.append(", title=");
        builder.append(this.title);
        builder.append("]");
        return builder.toString();
    }

    public static class Builder {

        private Long subscriptionId;
        private Long folderId;
        private String title;

        public Builder setSubscriptionId(final Long subscriptionId) {
            this.subscriptionId = subscriptionId;
            return this;
        }

        public Builder setFolderId(final Long folderId) {
            this.folderId = folderId;
            return this;
        }

        public Builder setTitle(final String title) {
            this.title = title;
            return this;
        }

        public UpdateSubscriptionRequest build() {
            return new UpdateSubscriptionRequest(this);
        }

    }

}
