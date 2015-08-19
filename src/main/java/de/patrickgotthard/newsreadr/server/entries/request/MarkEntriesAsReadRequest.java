package de.patrickgotthard.newsreadr.server.entries.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import de.patrickgotthard.newsreadr.server.subscriptions.response.Node.Type;

public class MarkEntriesAsReadRequest {

    @NotNull
    private Type type;

    @Min(1)
    private Long id;

    @NotNull
    @Min(1)
    private Long latestEntryId;

    public Type getType() {
        return this.type;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getLatestEntryId() {
        return this.latestEntryId;
    }

    public void setLatestEntryId(final Long latestEntryId) {
        this.latestEntryId = latestEntryId;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("MarkEntriesAsReadRequest [type=");
        builder.append(this.type);
        builder.append(", id=");
        builder.append(this.id);
        builder.append(", latestEntryId=");
        builder.append(this.latestEntryId);
        builder.append("]");
        return builder.toString();
    }

}
