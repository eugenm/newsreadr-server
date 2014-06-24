package de.patrickgotthard.newsreadr.server.bookmarks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.shared.request.AddBookmarkRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveBookmarkRequest;
import de.patrickgotthard.newsreadr.shared.response.Response;

@RestController
@RequestMapping("/api")
class BookmarkController {

    private final BookmarkService bookmarkService;

    @Autowired
    BookmarkController(final BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @RequestMapping(params = AddBookmarkRequest.METHOD)
    Response addBookmark(final AddBookmarkRequest request) {
        return bookmarkService.addBookmark(request);
    }

    @RequestMapping(params = RemoveBookmarkRequest.METHOD)
    Response removeBookmark(final RemoveBookmarkRequest request) {
        return bookmarkService.removeBookmark(request);
    }

}