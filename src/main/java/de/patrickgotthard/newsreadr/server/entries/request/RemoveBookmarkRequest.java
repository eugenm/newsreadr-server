package de.patrickgotthard.newsreadr.server.entries.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RemoveBookmarkRequest {

    @NotNull
    @Min(1)
    private Long userEntryId;

    public Long getUserEntryId() {
        return this.userEntryId;
    }

    public void setUserEntryId(final Long userEntryId) {
        this.userEntryId = userEntryId;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("RemoveBookmarkRequest [userEntryId=");
        builder.append(this.userEntryId);
        builder.append("]");
        return builder.toString();
    }

}
