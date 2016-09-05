package de.patrickgotthard.newsreadr.server.entries;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Entry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QSubscription;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.EntryRepository;
import de.patrickgotthard.newsreadr.server.common.rest.NotFoundException;
import de.patrickgotthard.newsreadr.server.common.rest.ServerException;
import de.patrickgotthard.newsreadr.server.entries.request.AddBookmarkRequest;
import de.patrickgotthard.newsreadr.server.entries.request.GetEntriesRequest;
import de.patrickgotthard.newsreadr.server.entries.request.GetEntryRequest;
import de.patrickgotthard.newsreadr.server.entries.request.MarkEntriesAsReadRequest;
import de.patrickgotthard.newsreadr.server.entries.request.RemoveBookmarkRequest;
import de.patrickgotthard.newsreadr.server.entries.response.EntrySummary;
import de.patrickgotthard.newsreadr.server.entries.response.GetEntriesResponse;
import de.patrickgotthard.newsreadr.server.entries.response.GetEntryResponse;
import de.patrickgotthard.newsreadr.server.subscriptions.response.Node.Type;

@Service
class EntryService {

    private static final Logger LOG = LoggerFactory.getLogger(EntryService.class);

    private final EntryRepository entryRepository;
    private final EntryDAO entryDAO;

    @Autowired
    public EntryService(final EntryRepository entryRepository, final EntryDAO entryDAO) {
        this.entryRepository = entryRepository;
        this.entryDAO = entryDAO;
    }

    @Transactional(readOnly = true)
    public GetEntriesResponse getEntries(final GetEntriesRequest request, final long currentUserId) {

        LOG.debug("Loading entries: {}", request);
        final Long latestEntryId = this.entryDAO.getLatestEntryId(currentUserId);
        final List<EntrySummary> entries = this.entryDAO.findEntries(request, currentUserId);
        LOG.debug("Loaded entries");

        final GetEntriesResponse response = new GetEntriesResponse();
        response.setLatestEntryId(latestEntryId);
        response.setEntries(entries);
        return response;

    }

    @Transactional
    public GetEntryResponse getEntry(final GetEntryRequest request, final long currentUserId) {

        LOG.debug("Getting entry: {}", request);

        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(QEntry.entry.id.eq(request.getUserEntryId()));
        filter.and(QEntry.entry.subscription.user.id.eq(currentUserId));

        final Entry entry = this.entryRepository.findOne(filter);
        if (entry == null) {
            throw new NotFoundException("The requested entry does not exist");
        }

        // mark as read
        entry.setRead(true);
        this.entryRepository.save(entry);

        final String content = entry.getContent();

        final Whitelist whitelist = Whitelist.relaxed();
        whitelist.addTags("figure", "figcaption");

        final GetEntryResponse response = new GetEntryResponse();
        response.setContent(Jsoup.clean(content, whitelist));
        return response;

    }

    @Transactional
    public void markEntriesAsRead(final MarkEntriesAsReadRequest request, final long currentUserId) {

        LOG.debug("Marking entries as read: {}", request);

        final QEntry entry = QEntry.entry;
        final QSubscription subscription = entry.subscription;

        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(subscription.user.id.eq(currentUserId));
        filter.and(entry.read.isFalse());
        filter.and(entry.id.loe(request.getLatestEntryId()));

        final Type type = request.getType();
        switch (type) {
            case ALL:
                // no filter to apply
                break;
            case BOOKMARKS:
                filter.and(entry.bookmarked.isTrue());
                break;
            case SUBSCRIPTION:
                filter.and(subscription.id.eq(request.getId()));
                break;
            default:
                throw new ServerException("Unknown feed type: " + type);
        }

        final List<Entry> entries = this.entryRepository.findAll(filter);
        for (final Entry curEntry : entries) {
            curEntry.setRead(true);
        }
        this.entryRepository.save(entries);

    }

    @Transactional
    public void addBookmark(final AddBookmarkRequest request, final long currentUserId) {
        LOG.debug("Adding bookmark: {}", request);
        final long userEntryId = request.getUserEntryId();
        this.toggleBookmark(userEntryId, true, currentUserId);
        LOG.debug("Added bookmark");
    }

    @Transactional
    public void removeBookmark(final RemoveBookmarkRequest request, final long currentUserId) {
        LOG.debug("Removing bookmark: {}", request);
        final long userEntryId = request.getUserEntryId();
        this.toggleBookmark(userEntryId, false, currentUserId);
        LOG.debug("Removed bookmark");
    }

    private void toggleBookmark(final long userEntryId, final boolean bookmark, final long currentUserId) {

        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(QEntry.entry.id.eq(userEntryId));
        filter.and(QEntry.entry.subscription.user.id.eq(currentUserId));

        final Entry entry = this.entryRepository.findOne(filter);
        if (entry == null) {
            throw new NotFoundException("The requested entry does not exist");
        } else {
            entry.setBookmarked(bookmark);
            this.entryRepository.save(entry);
        }

    }

}
