package de.patrickgotthard.newsreadr.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.patrickgotthard.newsreadr.server.persistence.entity.Subscription;
import de.patrickgotthard.newsreadr.server.persistence.entity.UserEntry;
import de.patrickgotthard.newsreadr.server.persistence.repository.UserEntryRepository;
import de.patrickgotthard.newsreadr.shared.request.AddBookmarkRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveBookmarkRequest;
import de.patrickgotthard.newsreadr.shared.response.Response;

@Service
public class BookmarkService {

    private final UserEntryRepository userEntryRepository;
    private final SecurityService securityService;

    @Autowired
    public BookmarkService(final UserEntryRepository userEntryRepository, final SecurityService securityService) {
        this.userEntryRepository = userEntryRepository;
        this.securityService = securityService;
    }

    @Transactional
    public Response addBookmark(final AddBookmarkRequest request) {
        final long userEntryId = request.getUserEntryId();
        toggleBookmark(userEntryId, true);
        return Response.success();
    }

    @Transactional
    public Response removeBookmark(final RemoveBookmarkRequest request) {
        final long userEntryId = request.getUserEntryId();
        toggleBookmark(userEntryId, false);
        return Response.success();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    private void toggleBookmark(final long userEntryId, final boolean bookmark) {

        final UserEntry userEntry = userEntryRepository.findOne(userEntryId);
        Assert.notNull(userEntry);

        final Subscription subscription = userEntry.getSubscription();
        securityService.validateOwnership(subscription);

        userEntry.setBookmarked(bookmark);
        userEntryRepository.save(userEntry);

    }

}
