package de.patrickgotthard.newsreadr.server.folders.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RemoveFolderRequest {

    @NotNull
    @Min(1)
    private Long folderId;

    public Long getFolderId() {
        return this.folderId;
    }

    public void setFolderId(final Long folderId) {
        this.folderId = folderId;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("RemoveFolderRequest [folderId=");
        builder.append(this.folderId);
        builder.append("]");
        return builder.toString();
    }

}
