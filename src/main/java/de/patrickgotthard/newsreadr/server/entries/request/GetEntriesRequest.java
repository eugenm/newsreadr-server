package de.patrickgotthard.newsreadr.server.entries.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import de.patrickgotthard.newsreadr.server.common.util.ObjectUtil;
import de.patrickgotthard.newsreadr.server.subscriptions.response.Node.Type;

public class GetEntriesRequest {

    @NotNull
    private Type type;

    @Min(1)
    private Long id;

    @Min(1)
    private Long latestEntryId;

    @NotNull
    private Boolean unreadOnly;

    @NotNull
    @Min(0)
    private Integer page;

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

    public Boolean getUnreadOnly() {
        return this.unreadOnly;
    }

    public void setUnreadOnly(final Boolean unreadOnly) {
        this.unreadOnly = unreadOnly;
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(final Integer page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }

}
