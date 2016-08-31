package de.patrickgotthard.newsreadr.server.folders.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import de.patrickgotthard.newsreadr.server.common.util.ObjectUtil;

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
        return ObjectUtil.toString(this);
    }

}
