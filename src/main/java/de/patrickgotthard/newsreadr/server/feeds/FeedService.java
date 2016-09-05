package de.patrickgotthard.newsreadr.server.feeds;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Entry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Subscription;
import de.patrickgotthard.newsreadr.server.common.rest.ServerException;
import de.patrickgotthard.newsreadr.server.common.util.DateUtil;
import de.patrickgotthard.newsreadr.server.common.util.StringUtil;

@Service
public class FeedService {

    private final RestTemplate restTemplate;

    @Autowired
    public FeedService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches and parses a feed.
     *
     * @param url URL of the feed to fetch
     * @return Parsed feed
     */
    public SyndFeed fetch(final String url) {
        try {
            final String xml = this.restTemplate.getForObject(url, String.class);
            return this.parseFeed(xml);
        } catch (final Exception e) {
            throw new ServerException(e);
        }
    }

    /**
     * Parses a feed.
     *
     * @param xml The XML to parse
     * @return Parsed Feed
     * @throws FeedException when parsing the feed failed
     */
    private SyndFeed parseFeed(final String xml) throws FeedException {
        final StringReader reader = new StringReader(xml);
        final SyndFeedInput input = new SyndFeedInput();
        input.setAllowDoctypes(true);
        return input.build(reader);
    }

    /**
     * Converts a {@link SyndFeed} into a {@link Subscription} object.
     *
     * @param syndFeed The {@link SyndFeed} to convert
     * @param url URL of the feed
     * @return The feed as subscription
     */
    public Subscription convertFeed(final SyndFeed syndFeed, final String url) {
        final Subscription subscription = new Subscription();
        subscription.setUrl(url);
        subscription.setTitle(this.getTitle(syndFeed));
        subscription.setEntries(this.getEntries(syndFeed));
        return subscription;
    }

    /**
     * Extracts the entries of a feed.
     *
     * @param syndFeed The feed to get the entries from
     * @return The converted entries
     */
    public Set<Entry> getEntries(final SyndFeed syndFeed) {
        return syndFeed.getEntries().parallelStream().map(this::convertEntry).collect(toSet());
    }

    /**
     * Extract the title of the feed.
     *
     * @param syndFeed The feed to extract the title from
     * @return The title of the feed when existing, otherwise the URL of the feed
     */
    private String getTitle(final SyndFeed syndFeed) {
        String title = syndFeed.getTitle();
        if (StringUtil.isBlank(title)) {
            title = syndFeed.getLink();
        }
        return title.trim();
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
        entry.setPublished(this.getPublishDate(syndEntry));
        return entry;
    }

    /**
     * Entracts the content of an entry. Some feeds doesn't offer contents. In this case the description will be used.
     *
     * @param syndEntry The entry to get the content from
     * @return Content of the entry when existing, otherwise the description
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
     * Extracts the publish date from an entry. Sometimes feeds doesn't offer a publish date. In this case the update date will be used.
     *
     * @param syndEntry Entry to get the publish date from
     * @return Publish date when existing, otherwise the update date
     */
    private LocalDateTime getPublishDate(final SyndEntry syndEntry) {
        Date publishDate = syndEntry.getPublishedDate();
        if (publishDate == null) {
            publishDate = syndEntry.getUpdatedDate();
        }
        return DateUtil.toLocalDateTime(publishDate);
    }

}
