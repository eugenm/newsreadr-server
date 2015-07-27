package de.patrickgotthard.newsreadr.server.folders.request;

import org.hibernate.validator.constraints.NotBlank;

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
        final StringBuilder builder = new StringBuilder();
        builder.append("AddFolderRequest [title=");
        builder.append(this.title);
        builder.append("]");
        return builder.toString();
    }

}
