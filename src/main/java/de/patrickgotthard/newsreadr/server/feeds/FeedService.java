package de.patrickgotthard.newsreadr.server.feeds;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Entry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Feed;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QFeed;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.FeedRepository;
import de.patrickgotthard.newsreadr.server.common.rest.ServerException;
import de.patrickgotthard.newsreadr.server.common.util.StringUtil;

@Service
public class FeedService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedService.class);

    private static final int MAX_REDIRECTS = 5;

    private final RestTemplate restTemplate;
    private final FeedRepository feedRepository;

    @Autowired
    public FeedService(final RestTemplate restTemplate, final FeedRepository feedRepository) {
        this.restTemplate = restTemplate;
        this.feedRepository = feedRepository;
    }

    public Feed fetch(final String feedUrl) {

        try {

            String location = feedUrl;

            ResponseEntity<String> entity = null;
            for (int i = 0; i <= MAX_REDIRECTS; i++) {

                entity = this.restTemplate.getForEntity(location, String.class);

                final HttpStatus statusCode = entity.getStatusCode();
                if (statusCode.is3xxRedirection()) {
                    if (i == MAX_REDIRECTS) {
                        throw new ServerException("Too many redirects");
                    }
                    location = entity.getHeaders().getFirst("Location");
                } else {
                    break;
                }

            }

            final String content = entity.getBody();
            final StringReader reader = new StringReader(content);

            final SyndFeedInput input = new SyndFeedInput();
            input.setAllowDoctypes(true);
            final SyndFeed syndFeed = input.build(reader);

            return this.convertFeed(syndFeed, feedUrl);

        } catch (final Exception e) {
            throw new ServerException(e);
        }

    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void removeFeedsWithoutSubscribers() {
        LOG.debug("Removing feed without subscribers");
        final BooleanExpression filter = QFeed.feed.subscriptions.isEmpty();
        final List<Feed> feeds = this.feedRepository.findAll(filter);
        this.feedRepository.delete(feeds);
        LOG.debug("Removed {} feeds without subscribers", feeds.size());
    }

    /**
     * Converts a {@link SyndFeed} into a {@link Feed} object.
     *
     * @param syndFeed The {@link SyndFeed} to convert
     * @param feedUrl URL of the feed
     * @return The converted feed object
     */
    private Feed convertFeed(final SyndFeed syndFeed, final String feedUrl) {
        final Feed feed = new Feed();
        feed.setUrl(feedUrl);
        feed.setTitle(this.getTitle(syndFeed));
        feed.setEntries(this.getEntries(syndFeed));
        return feed;
    }

    /**
     * Extract the title of the feed.
     *
     * @param syndFeed The {@link SyndFeed} to extract the title from
     * @return The title of the {@link SyndFeed} when existing. Otherwise returns the URL of the feed
     */
    private String getTitle(final SyndFeed syndFeed) {
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
    private Set<Entry> getEntries(final SyndFeed syndFeed) {
        return syndFeed.getEntries().parallelStream().map(this::convertEntry).collect(toSet());
    }

    /**
     * Converts a {@link SyndEntry} into the corresponding {@link Entry} object.
     *
     * @param syndEntry The {@link SyndEntry} to convert
     * @return The converted entry
     */
    private Entry convertEntry(final SyndEntry syndEntry) {
        final Entry entry = new Entry();
        entry.setUri(syndEntry.getUri());
        entry.setUrl(syndEntry.getLink());
        entry.setTitle(syndEntry.getTitle());
        entry.setContent(this.getContent(syndEntry));
        entry.setPublishDate(this.getPublishDate(syndEntry));
        return entry;
    }

    /**
     * Extracts the content of a {@link SyndEntry}.
     *
     * @param syndEntry {@link SyndEntry} to extract the content from
     * @return Content of the {@link SyndEntry}
     */
    private String getContent(final SyndEntry syndEntry) {
        final List<SyndContent> contents = syndEntry.getContents();
        if (contents.isEmpty()) {
            return syndEntry.getDescription().getValue().trim();
        } else {
            return contents.parallelStream().map(SyndContent::getValue).map(String::trim).collect(joining());
        }
    }

    /**
     * Extracts the publish date from a {@link SyndEntry}.
     *
     * @param syndEntry The {@link SyndEntry} to extract the publish date from
     * @return The publish date of the {@link SyndEntry} if existing. Otherwise returns the date of the last update.
     */
    private Date getPublishDate(final SyndEntry syndEntry) {
        Date publishDate = syndEntry.getPublishedDate();
        if (publishDate == null) {
            publishDate = syndEntry.getUpdatedDate();
        }
        return publishDate;
    }

}
