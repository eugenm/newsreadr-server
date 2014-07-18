package de.patrickgotthard.newsreadr.server.bookmarks;

import org.springframework.beans.factory.annotation.Autowired;

import de.patrickgotthard.newsreadr.server.common.web.ApiController;
import de.patrickgotthard.newsreadr.server.common.web.ApiRequestMapping;
import de.patrickgotthard.newsreadr.shared.request.AddBookmarkRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveBookmarkRequest;
import de.patrickgotthard.newsreadr.shared.response.Response;

@ApiController
class BookmarkController {

    private final BookmarkService bookmarkService;

    @Autowired
    BookmarkController(final BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @ApiRequestMapping(AddBookmarkRequest.class)
    Response addBookmark(final AddBookmarkRequest request) {
        return bookmarkService.addBookmark(request);
    }

    @ApiRequestMapping(RemoveBookmarkRequest.class)
    Response removeBookmark(final RemoveBookmarkRequest request) {
        return bookmarkService.removeBookmark(request);
    }

}