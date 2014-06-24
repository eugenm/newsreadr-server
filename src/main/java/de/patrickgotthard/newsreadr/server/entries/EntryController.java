package de.patrickgotthard.newsreadr.server.entries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.shared.request.GetEntriesRequest;
import de.patrickgotthard.newsreadr.shared.request.GetEntryRequest;
import de.patrickgotthard.newsreadr.shared.request.MarkEntriesAsReadRequest;
import de.patrickgotthard.newsreadr.shared.response.GetEntriesResponse;
import de.patrickgotthard.newsreadr.shared.response.GetEntryResponse;
import de.patrickgotthard.newsreadr.shared.response.Response;

@RestController
@RequestMapping("/api")
class EntryController {

    private final EntryService entryService;

    @Autowired
    EntryController(final EntryService entryService) {
        this.entryService = entryService;
    }

    @RequestMapping(params = GetEntriesRequest.METHOD)
    GetEntriesResponse getEntries(final GetEntriesRequest request) {
        return entryService.getEntries(request);
    }

    @RequestMapping(params = GetEntryRequest.METHOD)
    GetEntryResponse getEntry(final GetEntryRequest request) {
        return entryService.getEntry(request);
    }

    @RequestMapping(params = MarkEntriesAsReadRequest.METHOD)
    Response markEntriesAsRead(final MarkEntriesAsReadRequest request) {
        return entryService.markEntriesAsRead(request);
    }

}
