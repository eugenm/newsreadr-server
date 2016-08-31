package de.patrickgotthard.newsreadr.server.subscriptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.Entry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Feed;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Folder;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QFeed;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QFolder;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QSubscription;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QUserEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.Subscription;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.UserEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.EntryRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.FeedRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.FolderRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.SubscriptionRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserEntryRepository;
import de.patrickgotthard.newsreadr.server.common.persistence.repository.UserRepository;
import de.patrickgotthard.newsreadr.server.common.rest.AlreadyExistsException;
import de.patrickgotthard.newsreadr.server.common.rest.NotFoundException;
import de.patrickgotthard.newsreadr.server.common.rest.ServerException;
import de.patrickgotthard.newsreadr.server.common.tx.TransactionHelper;
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

    private final FeedRepository feedRepository;
    private final EntryRepository entryRepository;
    private final UserRepository userRepository;
    private final UserEntryRepository userEntryRepository;
    private final FolderRepository folderRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final FeedService feedService;
    private final TransactionHelper transactionHelper;

    @Autowired
    public SubscriptionService(final FeedRepository feedRepository, final EntryRepository entryRepository, final UserRepository userRepository,
        final UserEntryRepository userEntryRepository, final FolderRepository folderRepository, final SubscriptionRepository subscriptionRepository,
        final FeedService feedService, final TransactionHelper transactionHelper) {
        this.feedRepository = feedRepository;
        this.entryRepository = entryRepository;
        this.userRepository = userRepository;
        this.userEntryRepository = userEntryRepository;
        this.folderRepository = folderRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.feedService = feedService;
        this.transactionHelper = transactionHelper;
    }

    public void addSubscription(final AddSubscriptionRequest request, final long currentUserId) {

        this.transactionHelper.executeInNewTransaction(() -> {

            LOG.debug("Adding subscription {}", request);

            Folder folder = null;

            final Long folderId = request.getFolderId();
            if (folderId != null) {
                folder = this.loadFolder(folderId, currentUserId);
                if (folder == null) {
                    throw new NotFoundException("Folder does not exist");
                }
            }

            final String url = StringUtil.cleanUrl(request.getUrl());
            Feed feed = this.feedRepository.findOne(QFeed.feed.url.eq(url));

            Set<Entry> entries;
            if (feed == null) {

                feed = this.feedService.fetch(url);
                feed = this.feedRepository.save(feed);

                entries = new LinkedHashSet<>();
                for (final Entry entry1 : feed.getEntries()) {
                    entry1.setFeed(feed);
                    entries.add(this.entryRepository.save(entry1));
                }

            } else {
                entries = feed.getEntries();
            }

            final BooleanBuilder filter = new BooleanBuilder();
            filter.and(QSubscription.subscription.user.id.eq(currentUserId));
            filter.and(QSubscription.subscription.feed.eq(feed));

            final boolean subscribed = this.subscriptionRepository.count(filter) > 0;
            if (subscribed) {

                throw new AlreadyExistsException("Subscription already exists");

            } else {

                String title = request.getTitle();
                if (StringUtil.isBlank(title)) {
                    title = feed.getTitle();
                }

                final User currentUser = this.userRepository.findOne(currentUserId);

                Subscription subscription = new Subscription();
                subscription.setUser(currentUser);
                subscription.setFolder(folder);
                subscription.setFeed(feed);
                subscription.setTitle(title);

                subscription = this.subscriptionRepository.save(subscription);

                for (final Entry entry : entries) {

                    final UserEntry userEntry = new UserEntry();
                    userEntry.setSubscription(subscription);
                    userEntry.setEntry(entry);
                    userEntry.setRead(false);
                    userEntry.setBookmarked(false);

                    this.userEntryRepository.save(userEntry);

                }

                LOG.debug("Added subscription {}", request);

            }

            return null;

        });

    }

    @Transactional
    public List<Node> getSubscriptions(final long currentUserId) {

        LOG.debug("Listing subscriptions");

        // common query expressions
        final BooleanExpression belongsToUser = QUserEntry.userEntry.subscription.user.id.eq(currentUserId);
        final BooleanExpression isUnread = QUserEntry.userEntry.read.isFalse();

        // total node
        final long totalUnread = this.userEntryRepository.count(belongsToUser.and(isUnread));
        final Node allEntriesNode = new Node.Builder().type(Type.ALL).title("All entries").unread(totalUnread).build();

        // bookmarks node
        final BooleanExpression isBookmarked = QUserEntry.userEntry.bookmarked.isTrue();
        final long bookmarksUnread = this.userEntryRepository.count(belongsToUser.and(isUnread).and(isBookmarked));
        final Node bookmarksNode = new Node.Builder().type(Type.BOOKMARKS).title("Bookmarks").unread(bookmarksUnread).build();

        // folder nodes
        final BooleanExpression foldersBelongingToUser = QFolder.folder.user.id.eq(currentUserId);
        final OrderSpecifier<String> orderByFolderTitleAsc = QFolder.folder.title.asc();
        final List<Folder> folders = this.folderRepository.findAll(foldersBelongingToUser, orderByFolderTitleAsc);

        final List<Node> folderNodes = new ArrayList<>();
        for (final Folder folder : folders) {

            final Long folderId = folder.getId();
            final String folderTitle = folder.getTitle();
            long folderUnread = 0;

            // subscription nodes (in current folder)
            final BooleanExpression subscriptionsBelongingToUser = QSubscription.subscription.folder.eq(folder);
            final OrderSpecifier<String> orderBySubscriptionTitleAsc = QSubscription.subscription.title.asc();
            final List<Subscription> subscriptions = this.subscriptionRepository.findAll(subscriptionsBelongingToUser, orderBySubscriptionTitleAsc);

            final List<Node> subscriptionNodes = new ArrayList<>();
            for (final Subscription subscription : subscriptions) {
                final Node subscriptionNode = this.convert(currentUserId, folderId, subscription);
                folderUnread += subscriptionNode.getUnread();
                subscriptionNodes.add(subscriptionNode);
            }

            // assemble folder node
            folderNodes.add(new Node.Builder().type(Type.FOLDER).id(folderId).title(folderTitle).unread(folderUnread).subscriptions(subscriptionNodes).build());

        }

        // subscription nodes (without folder)
        final BooleanExpression subscriptionBelongsToUser = QSubscription.subscription.user.id.eq(currentUserId);
        final BooleanExpression hasNoFolder = QSubscription.subscription.folder.isNull();
        final OrderSpecifier<String> orderBySubscriptionTitleAsc = QSubscription.subscription.title.asc();
        final List<Subscription> subscriptionsWithoutFolder = this.subscriptionRepository.findAll(subscriptionBelongsToUser.and(hasNoFolder),
                orderBySubscriptionTitleAsc);

        final List<Node> subscriptionNodes = new ArrayList<>();
        for (final Subscription subscription : subscriptionsWithoutFolder) {
            subscriptionNodes.add(this.convert(currentUserId, null, subscription));
        }

        // assemble and return result
        final List<Node> nodes = new ArrayList<>();
        nodes.add(allEntriesNode);
        nodes.add(bookmarksNode);
        nodes.addAll(folderNodes);
        nodes.addAll(subscriptionNodes);
        return nodes;

    }

    @Transactional
    public void updateSubscription(final UpdateSubscriptionRequest request, final long currentUserId) {

        LOG.debug("Updating subscriptions: {}", request);

        final Long subscriptionId = request.getSubscriptionId();
        final Subscription subscription = this.loadSubscription(subscriptionId, currentUserId);
        if (subscription == null) {
            throw new NotFoundException("Subscription does not exist");
        }

        final Folder folder;

        final Long folderId = request.getFolderId();
        if (folderId == null) {
            folder = null;
        } else {
            folder = this.loadFolder(folderId, currentUserId);
            if (folder == null) {
                throw new NotFoundException("Folder does not exist");
            }
        }
        subscription.setFolder(folder);

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
        if (subscription == null) {
            throw new NotFoundException("Subscription does not exist");
        }

        // delete subscription
        this.subscriptionRepository.delete(subscription);

        // remove feeds without suscribers
        this.feedService.removeFeedsWithoutSubscribers();

        LOG.debug("Successfully removed subscription: {}", request);

    }

    @Transactional
    public List<String> importSubscriptions(final MultipartFile opmlFile, final long currentUserId) {

        final User currentUser = this.userRepository.findOne(currentUserId);

        LOG.debug("Importing subscriptions: {}", opmlFile);

        final InputStream input;
        try {
            input = opmlFile.getInputStream();
        } catch (final IOException e) {
            throw new ServerException(e);
        }

        final List<String> failed = new ArrayList<>();

        final Opml opml = JAXB.unmarshal(input, Opml.class);
        for (final Outline outline : opml.getBody().getOutlines()) {

            final String title = outline.getTitle();
            final String xmlUrl = outline.getXmlUrl();

            if (StringUtil.isBlank(xmlUrl)) {

                final BooleanBuilder filter = new BooleanBuilder();
                filter.and(QFolder.folder.user.id.eq(currentUserId));
                filter.and(QFolder.folder.title.eq(title));

                Folder folder = this.folderRepository.findOne(filter);
                if (folder == null) {

                    folder = new Folder();
                    folder.setUser(currentUser);
                    folder.setTitle(title);

                    folder = this.folderRepository.save(folder);

                }

                final Long folderId = folder.getId();

                final List<Outline> children = outline.getOutlines();
                for (final Outline child : children) {

                    final String childUrl = child.getXmlUrl();
                    final String childTitle = child.getTitle();

                    if (StringUtil.isNotBlank(childUrl)) {

                        try {

                            final AddSubscriptionRequest request = new AddSubscriptionRequest();
                            request.setUrl(childUrl);
                            request.setFolderId(folderId);
                            request.setTitle(childTitle);

                            this.addSubscription(request, currentUserId);

                        } catch (final Exception e) {
                            failed.add(childTitle);
                            LOG.debug("Unable to subscribe feed: " + xmlUrl, e);
                        }

                    }

                }

            } else {

                try {

                    final AddSubscriptionRequest request = new AddSubscriptionRequest();
                    request.setUrl(xmlUrl);
                    request.setTitle(title);

                    this.addSubscription(request, currentUserId);

                } catch (final Exception e) {
                    failed.add(title);
                    LOG.debug("Unable to subscribe feed: " + xmlUrl, e);
                }

            }

        }

        LOG.debug("Successfully imported subscription: {}", opmlFile);

        return failed;

    }

    @Transactional(readOnly = true)
    public String exportSubscriptions(final long currentUserId) throws JAXBException {

        LOG.debug("Exporting subscriptions");

        try {

            final List<Outline> outlines = new ArrayList<>();

            final List<Node> nodes = this.getSubscriptions(currentUserId);

            for (final Node node : nodes) {

                final Type type = node.getType();

                if (Type.FOLDER.equals(type)) {

                    final List<Outline> children = new ArrayList<>();

                    for (final Node subscriptionNode : node.getSubscriptions()) {

                        final String subscriptionUrl = subscriptionNode.getUrl();
                        final String subscriptionTitle = subscriptionNode.getTitle();

                        final Outline subscription = new Outline();
                        subscription.setType("rss");
                        subscription.setText(subscriptionTitle);
                        subscription.setTitle(subscriptionTitle);
                        subscription.setXmlUrl(subscriptionUrl);
                        children.add(subscription);

                    }

                    final String folderTitle = node.getTitle();

                    final Outline folder = new Outline();
                    folder.setType("folder");
                    folder.setText(folderTitle);
                    folder.setTitle(folderTitle);
                    folder.setOutlines(children);
                    outlines.add(folder);

                } else if (Type.SUBSCRIPTION.equals(type)) {

                    final String subscriptionUrl = node.getUrl();
                    final String subscriptionTitle = node.getTitle();

                    final Outline subscription = new Outline();
                    subscription.setType("rss");
                    subscription.setText(subscriptionTitle);
                    subscription.setTitle(subscriptionTitle);
                    subscription.setXmlUrl(subscriptionUrl);
                    outlines.add(subscription);

                }

            }

            final Head head = new Head();
            head.setTitle("Subscription export from newsreadr");

            final Body body = new Body();
            body.setOutlines(outlines);

            final Opml opml = new Opml();
            opml.setVersion("1.0");
            opml.setHead(head);
            opml.setBody(body);

            final JAXBContext context = JAXBContext.newInstance(Opml.class);
            final Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name());
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            final StringWriter stringWriter = new StringWriter();
            marshaller.marshal(opml, stringWriter);
            return stringWriter.toString();

        } catch (final JAXBException e) {
            LOG.debug("An error occured while exporting subscriptions", e);
            throw e;
        }

    }

    private Folder loadFolder(final long folderId, final long currentUserId) {
        final BooleanBuilder folderFilter = new BooleanBuilder();
        folderFilter.and(QFolder.folder.id.eq(folderId));
        folderFilter.and(QFolder.folder.user.id.eq(currentUserId));
        return this.folderRepository.findOne(folderFilter);
    }

    private Subscription loadSubscription(final long subscriptionId, final long currentUserId) {
        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(QSubscription.subscription.id.eq(subscriptionId));
        filter.and(QSubscription.subscription.user.id.eq(currentUserId));
        return this.subscriptionRepository.findOne(filter);
    }

    private Node convert(final long currentUserId, final Long folderId, final Subscription subscription) {

        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(QUserEntry.userEntry.subscription.user.id.eq(currentUserId));
        filter.and(QUserEntry.userEntry.read.isFalse());
        filter.and(QUserEntry.userEntry.subscription.eq(subscription));

        final long unread = this.userEntryRepository.count(filter);

        return new Node.Builder().type(Type.SUBSCRIPTION).id(subscription.getId()).folderId(folderId).url(subscription.getFeed().getUrl())
                .title(subscription.getTitle()).unread(unread).build();

    }

}
