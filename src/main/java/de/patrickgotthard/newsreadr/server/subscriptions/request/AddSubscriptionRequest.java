package de.patrickgotthard.newsreadr.server.subscriptions.request;

import org.hibernate.validator.constraints.NotBlank;

import de.patrickgotthard.newsreadr.server.common.util.ObjectUtil;

public class AddSubscriptionRequest {

    @NotBlank
    private String url;

    private String title;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(final String url) {
        this.url = url;
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
