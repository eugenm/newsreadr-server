package de.patrickgotthard.newsreadr.server.entries.response;

import java.util.List;

public class GetEntriesResponse {

    private Long latestEntryId;
    private List<EntrySummary> entries;

    public Long getLatestEntryId() {
        return this.latestEntryId;
    }

    public void setLatestEntryId(final Long latestEntryId) {
        this.latestEntryId = latestEntryId;
    }

    public List<EntrySummary> getEntries() {
        return this.entries;
    }

    public void setEntries(final List<EntrySummary> entries) {
        this.entries = entries;
    }

}
