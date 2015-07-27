package de.patrickgotthard.newsreadr.server.entries;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.BooleanBuilder;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.QUserEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.UserEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserEntryRepository;
import de.patrickgotthard.newsreadr.server.common.rest.NotFoundException;
import de.patrickgotthard.newsreadr.server.common.rest.ServerException;
import de.patrickgotthard.newsreadr.server.entries.request.AddBookmarkRequest;
import de.patrickgotthard.newsreadr.server.entries.request.GetEntriesRequest;
import de.patrickgotthard.newsreadr.server.entries.request.GetEntryRequest;
import de.patrickgotthard.newsreadr.server.entries.request.MarkEntriesAsReadRequest;
import de.patrickgotthard.newsreadr.server.entries.request.RemoveBookmarkRequest;
import de.patrickgotthard.newsreadr.server.entries.response.EntrySummary;
import de.patrickgotthard.newsreadr.server.entries.response.GetEntriesResponse;
import de.patrickgotthard.newsreadr.server.subscriptions.response.Node.Type;

@Service
class EntryService {

    private static final Logger LOG = LoggerFactory.getLogger(EntryService.class);

    private final UserEntryRepository userEntryRepository;
    private final EntryDAO entryDAO;

    @Autowired
    public EntryService(final UserEntryRepository userEntryRepository, final EntryDAO entryDAO) {
        this.userEntryRepository = userEntryRepository;
        this.entryDAO = entryDAO;
    }

    @Transactional(readOnly = true)
    public GetEntriesResponse getEntries(final GetEntriesRequest request, final User currentUser) {

        LOG.debug("Getting entries: {}", request);

        final QUserEntry userEntry = QUserEntry.userEntry;

        final BooleanBuilder query = new BooleanBuilder();
        query.and(userEntry.subscription.user.eq(currentUser));

        final Type type = request.getType();
        switch (type) {
            case ALL:
                // no filter to apply
                break;
            case BOOKMARKS:
                query.and(userEntry.bookmarked.isTrue());
                break;
            case FOLDER:
                query.and(userEntry.subscription.folder.id.eq(request.getId()));
                break;
            case SUBSCRIPTION:
                query.and(userEntry.subscription.id.eq(request.getId()));
                break;
            default:
                throw new ServerException("Unknown feed type: " + type);
        }

        final Long latestEntryId = request.getLatestEntryId();
        if (latestEntryId != null) {
            query.and(userEntry.id.loe(latestEntryId));
        }

        final boolean unreadOnly = request.getUnreadOnly();
        if (unreadOnly) {
            query.and(userEntry.read.isFalse());
        }

        final Long newLatestEntry = this.entryDAO.getLatestEntryId(currentUser);

        final int page = request.getPage();
        final PageRequest pageRequest = new PageRequest(page, 50);
        final List<EntrySummary> entries = this.entryDAO.findEntries(query, pageRequest);

        final GetEntriesResponse response = new GetEntriesResponse();
        response.setLatestEntryId(newLatestEntry);
        response.setEntries(entries);
        return response;

    }

    @Transactional
    public String getEntry(final GetEntryRequest request, final User currentUser) {

        LOG.debug("Getting entry: {}", request);

        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(QUserEntry.userEntry.id.eq(request.getUserEntryId()));
        filter.and(QUserEntry.userEntry.subscription.user.eq(currentUser));

        final UserEntry userEntry = this.userEntryRepository.findOne(filter);
        if (userEntry == null) {
            throw new NotFoundException("The requested entry does not exist");
        }

        // mark as read
        userEntry.setRead(true);
        this.userEntryRepository.save(userEntry);

        final String content = userEntry.getEntry().getContent();

        final Whitelist whitelist = Whitelist.relaxed();
        whitelist.addTags("figure", "figcaption");
        return Jsoup.clean(content, whitelist);

    }

    @Transactional
    public void markEntriesAsRead(final MarkEntriesAsReadRequest request, final User currentUser) {

        LOG.debug("Marking entries as read: {}", request);

        final QUserEntry userEntry = QUserEntry.userEntry;

        final BooleanBuilder query = new BooleanBuilder();
        query.and(userEntry.subscription.user.eq(currentUser));
        query.and(userEntry.read.isFalse());
        query.and(userEntry.id.loe(request.getLatestEntryId()));

        final Type type = request.getType();
        switch (type) {
            case ALL:
                // no filter to apply
                break;
            case BOOKMARKS:
                query.and(userEntry.bookmarked.isTrue());
                break;
            case FOLDER:
                query.and(userEntry.subscription.folder.id.eq(request.getId()));
                break;
            case SUBSCRIPTION:
                query.and(userEntry.subscription.id.eq(request.getId()));
                break;
            default:
                throw new ServerException("Unknown feed type: " + type);
        }

        final List<UserEntry> entries = this.userEntryRepository.findAll(query);
        for (final UserEntry entry : entries) {
            entry.setRead(true);
        }
        this.userEntryRepository.save(entries);

    }

    @Transactional
    public void addBookmark(final AddBookmarkRequest request, final User currentUser) {
        final long userEntryId = request.getUserEntryId();
        this.toggleBookmark(userEntryId, true, currentUser);
    }

    @Transactional
    public void removeBookmark(final RemoveBookmarkRequest request, final User currentUser) {
        final long userEntryId = request.getUserEntryId();
        this.toggleBookmark(userEntryId, false, currentUser);
    }

    private void toggleBookmark(final long userEntryId, final boolean bookmark, final User currentUser) {

        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(QUserEntry.userEntry.id.eq(userEntryId));
        filter.and(QUserEntry.userEntry.subscription.user.eq(currentUser));

        final UserEntry userEntry = this.userEntryRepository.findOne(filter);
        if (userEntry == null) {
            throw new NotFoundException("The requested entry does not exist");
        } else {
            userEntry.setBookmarked(bookmark);
            this.userEntryRepository.save(userEntry);
        }

    }

}