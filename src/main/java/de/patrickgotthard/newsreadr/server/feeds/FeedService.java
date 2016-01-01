package de.patrickgotthard.newsreadr.server.feeds;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.types.expr.BooleanExpression;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Entry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Feed;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QFeed;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.FeedRepository;
import de.patrickgotthard.newsreadr.server.common.rest.ServerException;
import de.patrickgotthard.newsreadr.server.common.util.StringUtil;

@Service
public class FeedService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedService.class);
    private static final int THIRTY_SECONDS = 30_000;

    private final FeedRepository feedRepository;

    @Autowired
    public FeedService(final FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public Feed fetch(final String feedUrl) {
        try {

            System.setProperty("http.agent", "newsreadr");

            final URLConnection connection = new URL(feedUrl).openConnection();
            connection.setReadTimeout(THIRTY_SECONDS);

            final XmlReader reader = new XmlReader(connection);

            final SyndFeedInput input = new SyndFeedInput();
            input.setAllowDoctypes(true);

            final SyndFeed syndFeed = input.build(reader);

            return convertFeed(syndFeed, feedUrl);

        } catch (final IOException | IllegalArgumentException | FeedException e) {
            throw new ServerException(e);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void removeFeedsWithoutSubscribers() {
        LOG.debug("Removing feed without subscribers");
        final BooleanExpression unused = QFeed.feed.subscriptions.isEmpty();
        final List<Feed> unusedFeeds = this.feedRepository.findAll(unused);
        this.feedRepository.delete(unusedFeeds);
        LOG.debug("Removed {} feeds without subscribers", unusedFeeds.size());
    }

    /**
     * Converts a {@link SyndFeed} into a {@link Feed} object.
     *
     * @param syndFeed The {@link SyndFeed} to convert
     * @param feedUrl URL of the feed
     * @return The converted feed object
     */
    private static Feed convertFeed(final SyndFeed syndFeed, final String feedUrl) {

        final String title = extractTitle(syndFeed);
        final List<SyndEntry> syndEntries = syndFeed.getEntries();
        final Set<Entry> entries = convertEntries(syndEntries);

        final Feed feed = new Feed();
        feed.setUrl(feedUrl);
        feed.setTitle(title);
        feed.setEntries(entries);
        return feed;

    }

    /**
     * Extract the title of the feed.
     *
     * @param syndFeed The {@link SyndFeed} to extract the title from
     * @return The title of the {@link SyndFeed} when existing. Otherwise returns the URL of the feed
     */
    private static String extractTitle(final SyndFeed syndFeed) {
        String title = syndFeed.getTitle();
        if (StringUtil.isBlank(title)) {
            title = syndFeed.getLink();
        }
        return title.trim();
    }

    /**
     * Converts a Collection of {@link SyndEntry} into the corresponding {@link Entry} objects.
     *
     * @param syndEntries Collection of {@link SyndEntry} to convert
     * @return The converted entries
     */
    private static Set<Entry> convertEntries(final Collection<SyndEntry> syndEntries) {
        final Set<Entry> entries = new LinkedHashSet<>();
        syndEntries.forEach(syndEntry -> {
            final Entry entry = convertEntry(syndEntry);
            entries.add(entry);
        });
        return entries;
    }

    /**
     * Converts a {@link SyndEntry} into the corresponding {@link Entry} object.
     *
     * @param syndEntry The {@link SyndEntry} to convert
     * @return The converted entry
     */
    private static Entry convertEntry(final SyndEntry syndEntry) {

        final Date publishDate = extractPublishDate(syndEntry);
        final String uri = syndEntry.getUri();
        final String url = syndEntry.getLink();
        final String title = syndEntry.getTitle();
        final String content = extractContent(syndEntry);

        final Entry entry = new Entry();
        entry.setUri(uri);
        entry.setUrl(url);
        entry.setTitle(title);
        entry.setContent(content);
        entry.setPublishDate(publishDate);
        return entry;

    }

    /**
     * Extracts the publish date from a {@link SyndEntry}.
     *
     * @param syndEntry The {@link SyndEntry} to extract the publish date from
     * @return The publish date of the {@link SyndEntry} if existing. Otherwise returns the date of the last update.
     */
    private static Date extractPublishDate(final SyndEntry syndEntry) {
        Date publishDate = syndEntry.getPublishedDate();
        if (publishDate == null) {
            publishDate = syndEntry.getUpdatedDate();
        }
        return publishDate;
    }

    /**
     * Extracts the content of a {@link SyndEntry}.
     *
     * @param syndEntry {@link SyndEntry} to extract the content from
     * @return Content of the {@link SyndEntry}
     */
    private static String extractContent(final SyndEntry syndEntry) {
        final StringBuilder content = new StringBuilder();
        final List<SyndContent> syndContents = syndEntry.getContents();
        if (!syndContents.isEmpty()) {
            syndContents.forEach(syndContent -> {
                final String value = syndContent.getValue();
                final String trimmed = value.trim();
                content.append(trimmed);
            });
        } else {
            final SyndContent description = syndEntry.getDescription();
            if (description == null) {
                content.append("<p>The entry does not provide any content. Please open the article directly.</p>");
            } else {
                final String value = description.getValue();
                final String trimmed = value.trim();
                content.append(String.format("<p>%s</p>", trimmed));
            }
        }
        return content.toString();
    }

}
