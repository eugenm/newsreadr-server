package de.patrickgotthard.newsreadr.server.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.patrickgotthard.newsreadr.server.service.BookmarkService;
import de.patrickgotthard.newsreadr.shared.request.AddBookmarkRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveBookmarkRequest;
import de.patrickgotthard.newsreadr.shared.response.Response;

@RestController
@RequestMapping("/api")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(final BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @RequestMapping(params = AddBookmarkRequest.METHOD)
    public Response addBookmark(final AddBookmarkRequest request) {
        return bookmarkService.addBookmark(request);
    }

    @RequestMapping(params = RemoveBookmarkRequest.METHOD)
    public Response removeBookmark(final RemoveBookmarkRequest request) {
        return bookmarkService.removeBookmark(request);
    }

}