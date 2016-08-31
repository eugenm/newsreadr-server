package de.patrickgotthard.newsreadr.server.folders.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import de.patrickgotthard.newsreadr.server.common.util.ObjectUtil;

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
        return ObjectUtil.toString(this);
    }

}
