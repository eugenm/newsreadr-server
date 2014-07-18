package de.patrickgotthard.newsreadr.server.entries;

import org.springframework.beans.factory.annotation.Autowired;

import de.patrickgotthard.newsreadr.server.common.web.ApiController;
import de.patrickgotthard.newsreadr.server.common.web.ApiRequestMapping;
import de.patrickgotthard.newsreadr.shared.request.GetEntriesRequest;
import de.patrickgotthard.newsreadr.shared.request.GetEntryRequest;
import de.patrickgotthard.newsreadr.shared.request.MarkEntriesAsReadRequest;
import de.patrickgotthard.newsreadr.shared.response.GetEntriesResponse;
import de.patrickgotthard.newsreadr.shared.response.GetEntryResponse;
import de.patrickgotthard.newsreadr.shared.response.Response;

@ApiController
class EntryController {

    private final EntryService entryService;

    @Autowired
    EntryController(final EntryService entryService) {
        this.entryService = entryService;
    }

    @ApiRequestMapping(GetEntriesRequest.class)
    GetEntriesResponse getEntries(final GetEntriesRequest request) {
        return entryService.getEntries(request);
    }

    @ApiRequestMapping(GetEntryRequest.class)
    GetEntryResponse getEntry(final GetEntryRequest request) {
        return entryService.getEntry(request);
    }

    @ApiRequestMapping(MarkEntriesAsReadRequest.class)
    Response markEntriesAsRead(final MarkEntriesAsReadRequest request) {
        return entryService.markEntriesAsRead(request);
    }

}
