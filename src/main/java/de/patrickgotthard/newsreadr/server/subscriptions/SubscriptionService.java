package de.patrickgotthard.newsreadr.server.subscriptions;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.querydsl.core.BooleanBuilder;
import com.rometools.rome.feed.synd.SyndFeed;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Entry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QSubscription;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Subscription;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.EntryRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.SubscriptionRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserRepository;
import de.patrickgotthard.newsreadr.server.common.rest.AlreadyExistsException;
import de.patrickgotthard.newsreadr.server.common.rest.NotFoundException;
import de.patrickgotthard.newsreadr.server.common.rest.ServerException;
import de.patrickgotthard.newsreadr.server.common.tx.TransactionHelper;
import de.patrickgotthard.newsreadr.server.common.util.CollectionUtil;
import de.patrickgotthard.newsreadr.server.common.util.StringUtil;
import de.patrickgotthard.newsreadr.server.feeds.FeedService;
import de.patrickgotthard.newsreadr.server.subscriptions.opml.Body;
import de.patrickgotthard.newsreadr.server.subscriptions.opml.Head;
import de.patrickgotthard.newsreadr.server.subscriptions.opml.Opml;
import de.patrickgotthard.newsreadr.server.subscriptions.opml.Outline;
import de.patrickgotthard.newsreadr.server.subscriptions.request.AddSubscriptionRequest;
import de.patrickgotthard.newsreadr.server.subscriptions.request.RemoveSubscriptionRequest;
import de.patrickgotthard.newsreadr.server.subscriptions.request.UpdateSubscriptionRequest;
import de.patrickgotthard.newsreadr.server.subscriptions.response.Node;
import de.patrickgotthard.newsreadr.server.subscriptions.response.Node.Type;

@Service
class SubscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;
    private final EntryRepository entryRepository;
    private final UserRepository userRepository;
    private final SubscriptionDAO subscriptionDAO;
    private final FeedService feedService;
    private final TransactionHelper transactionHelper;

    @Autowired
    public SubscriptionService(final SubscriptionRepository subscriptionRepository, final EntryRepository entryRepository, final UserRepository userRepository,
            final SubscriptionDAO subscriptionDAO, final FeedService feedService, final TransactionHelper transactionHelper) {
        this.subscriptionRepository = subscriptionRepository;
        this.entryRepository = entryRepository;
        this.userRepository = userRepository;
        this.subscriptionDAO = subscriptionDAO;
        this.feedService = feedService;
        this.transactionHelper = transactionHelper;
    }

    public void addSubscription(final AddSubscriptionRequest request, final long currentUserId) {

        this.transactionHelper.executeInNewTransaction(() -> {

            LOG.debug("Adding subscription {}", request);

            // fetch feed
            final String url = request.getUrl();
            final SyndFeed syndFeed = this.feedService.fetch(url);

            // check whether the subscription already exists
            final BooleanBuilder existsFilter = new BooleanBuilder();
            existsFilter.and(QSubscription.subscription.user.id.eq(currentUserId));
            existsFilter.and(QSubscription.subscription.url.eq(url));

            final boolean exists = this.subscriptionRepository.exists(existsFilter);
            if (exists) {
                throw new AlreadyExistsException("You are already subscribing the given feed");
            }

            // load the current user
            final User currentUser = this.userRepository.findOne(currentUserId);

            // persist subscription
            final Subscription feed = this.feedService.convertFeed(syndFeed, url);
            feed.setUser(currentUser);
            final String title = request.getTitle();
            if (StringUtil.isNotBlank(title)) {
                feed.setTitle(title);
            }
            final Subscription subscription = this.subscriptionRepository.save(feed);

            // persist entries
            final Set<Entry> entries = subscription.getEntries();
            entries.parallelStream().forEach(entry -> entry.setSubscription(subscription));
            this.entryRepository.save(entries);

            LOG.debug("Added subscription {}", request);

            return null;

        });

    }

    @Transactional
    public List<Node> getSubscriptions(final long currentUserId) {

        LOG.debug("Listing subscriptions");

        // subscription nodes
        final List<Node> subscriptionNodes = this.subscriptionDAO.getSubscriptions(currentUserId);

        // total node
        final Long totalUnread = subscriptionNodes.parallelStream().mapToLong(Node::getUnread).sum();

        final Node allEntriesNode = new Node();
        allEntriesNode.setType(Type.ALL);
        allEntriesNode.setTitle("All entries");
        allEntriesNode.setUnread(totalUnread);

        // bookmarks node
        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(QEntry.entry.subscription.user.id.eq(currentUserId));
        filter.and(QEntry.entry.read.isFalse());
        filter.and(QEntry.entry.bookmarked.isTrue());
        final long bookmarksUnread = this.entryRepository.count(filter);

        final Node bookmarksNode = new Node();
        bookmarksNode.setType(Type.BOOKMARKS);
        bookmarksNode.setTitle("Bookmarks");
        bookmarksNode.setUnread(bookmarksUnread);

        // assemble and return result
        final List<Node> nodes = new ArrayList<>();
        nodes.add(allEntriesNode);
        nodes.add(bookmarksNode);
        nodes.addAll(subscriptionNodes);
        return nodes;

    }

    @Transactional
    public void updateSubscription(final UpdateSubscriptionRequest request, final long currentUserId) {

        LOG.debug("Updating subscription: {}", request);

        final Long subscriptionId = request.getSubscriptionId();
        final Subscription subscription = this.loadSubscription(subscriptionId, currentUserId);

        final String title = request.getTitle();
        if (StringUtil.isNotBlank(title)) {
            subscription.setTitle(title);
        }

        this.subscriptionRepository.save(subscription);

        LOG.debug("Successfully updated subscription: {}", request);

    }

    @Transactional
    public void removeSubscription(final RemoveSubscriptionRequest request, final long currentUserId) {

        LOG.debug("Removing subscription: {}", request);

        // load subscription
        final Long subscriptionId = request.getSubscriptionId();
        final Subscription subscription = this.loadSubscription(subscriptionId, currentUserId);

        // delete subscription
        this.subscriptionRepository.delete(subscription);

        LOG.debug("Successfully removed subscription: {}", request);

    }

    @Transactional
    public List<String> importSubscriptions(final MultipartFile opmlFile, final long currentUserId) {

        LOG.debug("Importing subscriptions");

        final Opml opml = this.parseOpml(opmlFile);
        final Collection<Outline> outlines = this.getRelevantOutlines(opml.getBody().getOutlines());

        final List<String> failed = new ArrayList<>();

        for (final Outline outline : outlines) {

            final String url = outline.getXmlUrl();
            final String title = outline.getTitle();

            final AddSubscriptionRequest request = new AddSubscriptionRequest();
            request.setUrl(url);
            request.setTitle(title);

            try {
                this.addSubscription(request, currentUserId);
            } catch (final Exception e) {
                failed.add(title);
                LOG.debug("Unable to add subscription: " + url, e);
            }

        }

        LOG.debug("Successfully imported subscriptions");

        return failed;

    }

    @Transactional(readOnly = true)
    public String exportSubscriptions(final long currentUserId) {

        LOG.debug("Exporting subscriptions");

        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(QSubscription.subscription.user.id.eq(currentUserId));

        final QSort orderByTitleAsc = new QSort(QSubscription.subscription.title.asc());

        final List<Outline> outlines = this.subscriptionRepository.findAll(filter, orderByTitleAsc).parallelStream().map(subscription -> {

            final String title = subscription.getTitle();
            final String url = subscription.getUrl();

            final Outline outline = new Outline();
            outline.setType("rss");
            outline.setText(title);
            outline.setTitle(title);
            outline.setXmlUrl(url);
            return outline;

        }).collect(toList());

        final Head head = new Head();
        head.setTitle("Subscription export from newsreadr");

        final Body body = new Body();
        body.setOutlines(outlines);

        final Opml opml = new Opml();
        opml.setVersion("1.0");
        opml.setHead(head);
        opml.setBody(body);

        try {

            final StringWriter stringWriter = new StringWriter();
            JAXB.marshal(opml, stringWriter);
            return stringWriter.toString();

        } catch (final DataBindingException e) {
            throw new ServerException("Unable to export subscriptions", e);
        }

    }

    private Opml parseOpml(final MultipartFile opmlFile) {
        try (InputStream input = opmlFile.getInputStream()) {
            return JAXB.unmarshal(input, Opml.class);
        } catch (final IOException e) {
            throw new ServerException(e);
        }
    }

    private Collection<Outline> getRelevantOutlines(final Collection<Outline> outlines) {

        final List<Outline> relevant = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(outlines)) {

            for (final Outline outline : outlines) {

                final String feedUrl = outline.getXmlUrl();
                if (StringUtil.isNotBlank(feedUrl)) {
                    relevant.add(outline);
                }

                final List<Outline> children = outline.getOutlines();
                relevant.addAll(this.getRelevantOutlines(children));

            }

        }

        return relevant;

    }

    private Subscription loadSubscription(final long subscriptionId, final long currentUserId) {

        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(QSubscription.subscription.id.eq(subscriptionId));
        filter.and(QSubscription.subscription.user.id.eq(currentUserId));
        final Subscription subscription = this.subscriptionRepository.findOne(filter);

        if (subscription == null) {
            throw new NotFoundException("Subscription does not exist");
        }

        return subscription;

    }

}
