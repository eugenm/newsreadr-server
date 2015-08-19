package de.patrickgotthard.newsreadr.server.folders.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class UpdateFolderRequest {

    @NotNull
    @Min(1)
    private Long folderId;

    @NotBlank
    private String title;

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
        builder.append("UpdateFolderRequest [folderId=");
        builder.append(this.folderId);
        builder.append(", title=");
        builder.append(this.title);
        builder.append("]");
        return builder.toString();
    }

}
