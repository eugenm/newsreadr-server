package de.patrickgotthard.newsreadr.server.folders.request;

import org.hibernate.validator.constraints.NotBlank;

import de.patrickgotthard.newsreadr.server.common.util.ObjectUtil;

public class AddFolderRequest {

    @NotBlank
    private String title;

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
