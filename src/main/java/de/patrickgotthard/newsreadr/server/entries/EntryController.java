package de.patrickgotthard.newsreadr.server.entries;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.entries.request.AddBookmarkRequest;
import de.patrickgotthard.newsreadr.server.entries.request.GetEntriesRequest;
import de.patrickgotthard.newsreadr.server.entries.request.GetEntryRequest;
import de.patrickgotthard.newsreadr.server.entries.request.MarkEntriesAsReadRequest;
import de.patrickgotthard.newsreadr.server.entries.request.RemoveBookmarkRequest;
import de.patrickgotthard.newsreadr.server.entries.response.GetEntriesResponse;
import de.patrickgotthard.newsreadr.server.entries.response.GetEntryResponse;

@RestController
@RequestMapping("/api/entries")
class EntryController {

    private final EntryService entryService;

    @Autowired
    public EntryController(final EntryService entryService) {
        this.entryService = entryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public GetEntriesResponse getEntries(@Valid final GetEntriesRequest request, final User currentUser) {
        return this.entryService.getEntries(request, currentUser);
    }

    @RequestMapping(value = "/{userEntryId}", method = RequestMethod.GET)
    public GetEntryResponse getEntry(@Valid final GetEntryRequest request, final User currentUser) {
        return this.entryService.getEntry(request, currentUser);
    }

    @RequestMapping(value = "/markRead", method = RequestMethod.PUT)
    public void markEntriesAsRead(@Valid final MarkEntriesAsReadRequest request, final User currentUser) {
        this.entryService.markEntriesAsRead(request, currentUser);
    }

    @RequestMapping(value = "/{userEntryId}/bookmark", method = RequestMethod.POST)
    public void addBookmark(@Valid final AddBookmarkRequest request, final User currentUser) {
        this.entryService.addBookmark(request, currentUser);
    }

    @RequestMapping(value = "/{userEntryId}/bookmark", method = RequestMethod.DELETE)
    public void removeBookmark(@Valid final RemoveBookmarkRequest request, final User currentUser) {
        this.entryService.removeBookmark(request, currentUser);
    }

}