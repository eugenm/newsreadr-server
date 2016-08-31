package de.patrickgotthard.newsreadr.server.subscriptions.request;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

import de.patrickgotthard.newsreadr.server.common.util.ObjectUtil;

public class AddSubscriptionRequest {

    @NotBlank
    private String url;

    @Min(1)
    private Long folderId;

    private String title;

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
        return ObjectUtil.toString(this);
    }

}
