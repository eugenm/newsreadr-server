package de.patrickgotthard.newsreadr.server.entries;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.expr.BooleanExpression;

import de.patrickgotthard.newsreadr.server.common.exception.ServiceException;
import de.patrickgotthard.newsreadr.server.common.util.Assert;
import de.patrickgotthard.newsreadr.server.security.SecurityService;
import de.patrickgotthard.newsreadr.server.subscriptions.Subscription;
import de.patrickgotthard.newsreadr.server.userentries.UserEntry;
import de.patrickgotthard.newsreadr.server.userentries.UserEntryExpression;
import de.patrickgotthard.newsreadr.server.userentries.UserEntryRepository;
import de.patrickgotthard.newsreadr.shared.request.GetEntriesRequest;
import de.patrickgotthard.newsreadr.shared.request.GetEntryRequest;
import de.patrickgotthard.newsreadr.shared.request.MarkEntriesAsReadRequest;
import de.patrickgotthard.newsreadr.shared.response.GetEntriesResponse;
import de.patrickgotthard.newsreadr.shared.response.GetEntryResponse;
import de.patrickgotthard.newsreadr.shared.response.Response;
import de.patrickgotthard.newsreadr.shared.response.data.EntrySummary;
import de.patrickgotthard.newsreadr.shared.response.data.Node;

@Service
class EntryService {

    private static final Logger LOG = LoggerFactory.getLogger(EntryService.class);

    private static final String FEED_ALL = Node.Type.ALL.name();
    private static final String FEED_BOOKMARKS = Node.Type.BOOKMARKS.name();
    private static final String FOLDER_REGEX = Node.Type.FOLDER.name() + "-(\\d+)";
    private static final String SUBSCRIPTION_REGEX = Node.Type.SUBSCRIPTION.name() + "-(\\d+)";

    private final UserEntryRepository userEntryRepository;
    private final SecurityService securityService;

    @Autowired
    EntryService(final UserEntryRepository userEntryRepository, final SecurityService securityService) {
        this.userEntryRepository = userEntryRepository;
        this.securityService = securityService;
    }

    @Transactional(readOnly = true)
    GetEntriesResponse getEntries(final GetEntriesRequest request) {

        LOG.debug("Getting entries: {}", request);

        final long currentUserId = securityService.getCurrentUserId();
        final BooleanExpression entryBelongsToUser = UserEntryExpression.belongsToUser(currentUserId);

        final BooleanBuilder query = new BooleanBuilder();
        query.and(entryBelongsToUser);

        final String feed = request.getFeed();
        if (FEED_ALL.equals(feed)) {
            // no filter to apply
        } else if (FEED_BOOKMARKS.equals(feed)) {
            query.and(UserEntryExpression.isBookmarked());
        } else if (Pattern.matches(SUBSCRIPTION_REGEX, feed)) {
            final long subscriptionId = extractId(SUBSCRIPTION_REGEX, feed);
            query.and(UserEntryExpression.belongsToSubscription(subscriptionId));
        } else if (Pattern.matches(FOLDER_REGEX, feed)) {
            final long folderId = extractId(FOLDER_REGEX, feed);
            query.and(UserEntryExpression.belongsToFolder(folderId));
        } else {
            throw new IllegalArgumentException("Unknown feed: " + feed);
        }

        final Long latestEntryId = request.getLatestEntryId();
        if (latestEntryId != null) {
            query.and(UserEntryExpression.idIsLowerThanOrEqualTo(latestEntryId));
        }

        final boolean unreadOnly = request.isUnreadOnly();
        if (unreadOnly) {
            query.and(UserEntryExpression.isUnread());
        }

        final int page = request.getPage();
        final PageRequest pageRequest = new PageRequest(page, 50);

        final Long newLatestEntry = userEntryRepository.getLatestEntryId(entryBelongsToUser);

        final List<EntrySummary> entries = userEntryRepository.findEntries(query, pageRequest);

        return new GetEntriesResponse(newLatestEntry, entries);

    }

    @Transactional
    GetEntryResponse getEntry(final GetEntryRequest request) {

        LOG.debug("Getting entry: {}", request);

        final long userEntryId = request.getUserEntryId();
        final UserEntry userEntry = userEntryRepository.findOne(userEntryId);
        Assert.notNull(userEntry);

        final Subscription subscription = userEntry.getSubscription();
        if (subscription == null) {
            throw ServiceException.withMessage("Subscription cannot be found");
        }

        if (securityService.notBelongsToUser(subscription)) {
            throw ServiceException.withMessage("You are not subscribing the feed the entry belongs to");
        }

        // mark as read
        userEntry.setRead(true);
        userEntryRepository.save(userEntry);

        final String rawContent = userEntry.getEntry().getContent();

        final Whitelist whitelist = Whitelist.relaxed();
        whitelist.addTags("figure", "figcaption");
        final String content = Jsoup.clean(rawContent, whitelist);

        return new GetEntryResponse(content);

    }

    @Transactional
    Response markEntriesAsRead(final MarkEntriesAsReadRequest request) {

        LOG.debug("Marking entries as read: {}", request);

        final long currentUserId = securityService.getCurrentUserId();
        final String feed = request.getFeed();
        final long latestEntryId = request.getLatestEntryId();

        final BooleanBuilder query = new BooleanBuilder();
        query.and(UserEntryExpression.belongsToUser(currentUserId));
        query.and(UserEntryExpression.isUnread());
        query.and(UserEntryExpression.idIsLowerThanOrEqualTo(latestEntryId));

        if (FEED_ALL.equals(feed)) {
            // no filter to apply
        } else if (FEED_BOOKMARKS.equals(feed)) {
            query.and(UserEntryExpression.isBookmarked());
        } else if (Pattern.matches(SUBSCRIPTION_REGEX, feed)) {
            final long subscriptionId = extractId(SUBSCRIPTION_REGEX, feed);
            query.and(UserEntryExpression.belongsToSubscription(subscriptionId));
        } else if (Pattern.matches(FOLDER_REGEX, feed)) {
            final long folderId = extractId(FOLDER_REGEX, feed);
            query.and(UserEntryExpression.belongsToFolder(folderId));
        } else {
            throw ServiceException.withMessage("Unknown feed: {}", feed);
        }

        final List<UserEntry> entries = userEntryRepository.findAll(query);
        if (!entries.isEmpty()) {
            userEntryRepository.markEntriesAsRead(entries);
        }

        return Response.success();

    }

    private long extractId(final String regex, final String feed) {
        final Matcher matcher = Pattern.compile(regex).matcher(feed);
        matcher.find();
        final String idString = matcher.group(1);
        return Long.parseLong(idString);
    }
}
