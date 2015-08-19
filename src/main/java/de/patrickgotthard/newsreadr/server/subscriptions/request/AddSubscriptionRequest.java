package de.patrickgotthard.newsreadr.server.subscriptions.request;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

public class AddSubscriptionRequest {

    @NotBlank
    private String url;

    @Min(1)
    private Long folderId;

    private String title;

    public AddSubscriptionRequest() {
    }

    private AddSubscriptionRequest(final Builder builder) {
        this.url = builder.url;
        this.folderId = builder.folderId;
        this.title = builder.title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(final String url) {
        this.url = url;
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
        builder.append("AddSubscriptionRequest [url=");
        builder.append(this.url);
        builder.append(", folderId=");
        builder.append(this.folderId);
        builder.append(", title=");
        builder.append(this.title);
        builder.append("]");
        return builder.toString();
    }

    public static class Builder {

        private String url;
        private Long folderId;
        private String title;

        public Builder setUrl(final String url) {
            this.url = url;
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

        public AddSubscriptionRequest build() {
            return new AddSubscriptionRequest(this);
        }

    }

}
