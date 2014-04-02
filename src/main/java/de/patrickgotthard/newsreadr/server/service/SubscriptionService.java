package de.patrickgotthard.newsreadr.server.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.expr.BooleanExpression;

import de.patrickgotthard.newsreadr.server.persistence.entity.Entry;
import de.patrickgotthard.newsreadr.server.persistence.entity.Feed;
import de.patrickgotthard.newsreadr.server.persistence.entity.Folder;
import de.patrickgotthard.newsreadr.server.persistence.entity.Subscription;
import de.patrickgotthard.newsreadr.server.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.persistence.entity.UserEntry;
import de.patrickgotthard.newsreadr.server.persistence.expression.FolderExpression;
import de.patrickgotthard.newsreadr.server.persistence.expression.SubscriptionExpression;
import de.patrickgotthard.newsreadr.server.persistence.expression.UserEntryExpression;
import de.patrickgotthard.newsreadr.server.persistence.repository.EntryRepository;
import de.patrickgotthard.newsreadr.server.persistence.repository.FeedRepository;
import de.patrickgotthard.newsreadr.server.persistence.repository.FolderRepository;
import de.patrickgotthard.newsreadr.server.persistence.repository.SubscriptionRepository;
import de.patrickgotthard.newsreadr.server.persistence.repository.UserEntryRepository;
import de.patrickgotthard.newsreadr.server.service.data.ServiceException;
import de.patrickgotthard.newsreadr.server.service.data.opml.Body;
import de.patrickgotthard.newsreadr.server.service.data.opml.Head;
import de.patrickgotthard.newsreadr.server.service.data.opml.Opml;
import de.patrickgotthard.newsreadr.server.service.data.opml.Outline;
import de.patrickgotthard.newsreadr.server.util.StringUtil;
import de.patrickgotthard.newsreadr.shared.request.AddSubscriptionRequest;
import de.patrickgotthard.newsreadr.shared.request.RemoveSubscriptionRequest;
import de.patrickgotthard.newsreadr.shared.request.UpdateSubscriptionRequest;
import de.patrickgotthard.newsreadr.shared.response.GetSubscriptionsResponse;
import de.patrickgotthard.newsreadr.shared.response.Response;
import de.patrickgotthard.newsreadr.shared.response.data.FolderNode;
import de.patrickgotthard.newsreadr.shared.response.data.Node;
import de.patrickgotthard.newsreadr.shared.response.data.Node.Type;
import de.patrickgotthard.newsreadr.shared.response.data.SubscriptionNode;
import de.patrickgotthard.newsreadr.shared.response.data.VirtualNode;

@Service
public class SubscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionService.class);

    private final FeedRepository feedRepository;
    private final EntryRepository entryRepository;
    private final UserEntryRepository userEntryRepository;
    private final FolderRepository folderRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SharedService sharedService;
    private final FeedService feedService;
    private final SecurityService securityService;

    @Autowired
    public SubscriptionService(final FeedRepository feedRepository, final EntryRepository entryRepository, final UserEntryRepository userEntryRepository,
            final FolderRepository folderRepository, final SubscriptionRepository subscriptionRepository, final SharedService sharedService,
            final FeedService feedService, final SecurityService securityService) {
        this.feedRepository = feedRepository;
        this.entryRepository = entryRepository;
        this.userEntryRepository = userEntryRepository;
        this.folderRepository = folderRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.sharedService = sharedService;
        this.feedService = feedService;
        this.securityService = securityService;
    }

    @Transactional
    public Response addSubscription(final AddSubscriptionRequest request) {

        LOG.debug("Adding subscription {}", request);

        final Long folderId = request.getFolderId();
        Folder folder = null;
        if (folderId != null) {

            folder = folderRepository.findOne(folderId);

            if (folder == null) {
                throw new ServiceException("Folder does not exist");
            }

            if (securityService.notBelongsToUser(folder)) {
                throw new ServiceException("The selected folder does not belong to the user");
            }

        }

        final User currentUser = securityService.getCurrentUser();

        final String url = StringUtil.cleanUrl(request.getUrl());
        Feed feed = feedRepository.findByUrl(url);

        Set<Entry> entries;
        if (feed == null) {

            feed = feedService.fetch(url);
            feed = feedRepository.save(feed);

            entries = new LinkedHashSet<>();
            for (final Entry entry : feed.getEntries()) {
                entry.setFeed(feed);
                entries.add(entryRepository.save(entry));
            }

        } else {
            entries = feed.getEntries();
        }

        final boolean notSubscribed = subscriptionRepository.countByUserAndFeed(currentUser, feed) == 0;
        if (notSubscribed) {

            String title = request.getTitle();
            if (StringUtil.isBlank(title)) {
                title = feed.getTitle();
            }

            Subscription subscription = new Subscription.Builder().setUser(currentUser).setFolder(folder).setFeed(feed).setTitle(title).build();
            subscription = subscriptionRepository.save(subscription);

            for (final Entry entry : entries) {
                final UserEntry userEntry = new UserEntry.Builder().setSubscription(subscription).setEntry(entry).setRead(false).setBookmarked(false).build();
                userEntryRepository.save(userEntry);
            }

            LOG.debug("Finished adding subscription {}", request);
            return Response.success();

        } else {
            LOG.debug("User already subscribes the feed {}", request);
            throw new ServiceException("You are already subscribing the feed");
        }

    }

    @Transactional
    public GetSubscriptionsResponse getSubscriptions() {

        LOG.debug("Listing subscriptions");

        // extract user id
        final Long currentUserId = securityService.getCurrentUserId();

        // common query expressions
        final BooleanExpression belongsToUser = UserEntryExpression.belongsToUser(currentUserId);
        final BooleanExpression isUnread = UserEntryExpression.isUnread();

        // total node
        final long totalUnread = userEntryRepository.count(belongsToUser.and(isUnread));
        final VirtualNode allEntriesNode = new VirtualNode(Type.ALL, "All entries", totalUnread);

        // bookmarks node
        final BooleanExpression isBookmarked = UserEntryExpression.isBookmarked();
        final long bookmarksUnread = userEntryRepository.count(belongsToUser.and(isUnread).and(isBookmarked));
        final VirtualNode bookmarksNode = new VirtualNode(Type.BOOKMARKS, "Bookmarks", bookmarksUnread);

        // folder nodes
        final BooleanExpression folderBelongsToUser = FolderExpression.belongsToUser(currentUserId);
        final List<Folder> subscriptionsWithFolder = subscriptionRepository.getSubscriptionsWithFolder(folderBelongsToUser);

        final List<FolderNode> folderNodes = new ArrayList<FolderNode>();
        for (final Folder folder : subscriptionsWithFolder) {

            final Long folderId = folder.getId();
            long unread = 0;

            // subscription nodes in folder
            final List<SubscriptionNode> subscriptionNodes = new ArrayList<>();
            for (final Subscription subscription : folder.getSubscriptions()) {
                final SubscriptionNode subscriptionNode = convert(currentUserId, folderId, subscription);
                unread += subscriptionNode.getUnread();
                subscriptionNodes.add(subscriptionNode);
            }
            sortNodes(subscriptionNodes);

            // add folder node
            folderNodes.add(new FolderNode(folderId, folder.getTitle(), unread, subscriptionNodes));

        }
        sortNodes(folderNodes);

        // subscription nodes (without folder)
        final BooleanExpression subscriptionBelongsToUser = SubscriptionExpression.belongsToUser(currentUserId);
        final BooleanExpression hasNoFolder = SubscriptionExpression.hasNoFolder();
        final List<Subscription> subscriptionsWithoutFolder = subscriptionRepository.getSubscriptionsWithoutFolder(subscriptionBelongsToUser.and(hasNoFolder));

        final List<SubscriptionNode> subscriptionNodes = new ArrayList<>();
        for (final Subscription subscription : subscriptionsWithoutFolder) {
            subscriptionNodes.add(convert(currentUserId, null, subscription));
        }

        // sort subscriptions
        sortNodes(subscriptionNodes);

        // assemble and return result
        final List<Node> nodes = new ArrayList<>();
        nodes.add(allEntriesNode);
        nodes.add(bookmarksNode);
        nodes.addAll(folderNodes);
        nodes.addAll(subscriptionNodes);

        return new GetSubscriptionsResponse(nodes);

    }

    private SubscriptionNode convert(final long userId, final Long folderId, final Subscription subscription) {

        final Long subscriptionId = subscription.getId();
        final Feed feed = subscription.getFeed();
        final String feedUrl = feed.getUrl();
        final String subscriptionTitle = subscription.getTitle();

        final BooleanBuilder query = new BooleanBuilder();
        query.and(UserEntryExpression.belongsToUser(userId));
        query.and(UserEntryExpression.isUnread());
        query.and(UserEntryExpression.belongsToSubscription(subscriptionId));

        final long unread = userEntryRepository.countUnread(query);
        return new SubscriptionNode(subscriptionId, folderId, feedUrl, subscriptionTitle, unread);

    }

    private void sortNodes(final List<? extends Node> nodes) {
        Collections.sort(nodes, new Comparator<Node>() {
            @Override
            public int compare(final Node o1, final Node o2) {
                return StringUtil.compare(o1.getTitle(), o2.getTitle());
            }
        });
    }

    @Transactional
    public Response updateSubscription(final UpdateSubscriptionRequest request) {

        LOG.debug("Updating subscriptions: {}", request);

        final long subscriptionId = request.getSubscriptionId();
        final Subscription subscription = subscriptionRepository.findOne(subscriptionId);

        if (subscription == null) {
            throw new ServiceException("The subscription does not exist");
        }

        if (securityService.notBelongsToUser(subscription)) {
            throw new ServiceException("The subscription does not belong to the user");
        }

        final Long folderId = request.getFolderId();
        if (folderId == null) {
            subscription.setFolder(null);
        } else {

            final Folder folder = folderRepository.findOne(folderId);

            if (folder == null) {
                throw new ServiceException("The folder does not exist");
            }

            if (securityService.notBelongsToUser(folder)) {
                throw new ServiceException("The folder does not belong to the user");
            }

            subscription.setFolder(folder);

        }

        final String title = request.getTitle();
        if (StringUtil.isNotBlank(title)) {
            subscription.setTitle(title);
        }

        subscriptionRepository.save(subscription);

        LOG.debug("Successfully updated subscription: {}", request);
        return Response.success();

    }

    @Transactional
    public Response removeSubscription(final RemoveSubscriptionRequest request) {

        LOG.debug("Removing subscription: {}", request);

        final long subscriptionId = request.getSubscriptionId();
        final Subscription subscription = subscriptionRepository.findOne(subscriptionId);

        if (subscription == null) {
            throw new ServiceException("The subscription does not exist");
        }

        if (securityService.notBelongsToUser(subscription)) {
            throw new ServiceException("The subscription does not belong to the user");
        }

        subscriptionRepository.delete(subscription);

        sharedService.removeFeedsWithoutSubscribers();

        LOG.debug("Successfully removed subscription: {}", request);
        return Response.success();

    }

    @Transactional
    public Response importSubscriptions(final MultipartFile opmlFile) {

        LOG.debug("Importing subscriptions: {}", opmlFile);

        try {

            final User currentUser = securityService.getCurrentUser();

            final InputStream input = opmlFile.getInputStream();

            final JAXBContext context = JAXBContext.newInstance(Opml.class);
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            final Opml opml = (Opml) unmarshaller.unmarshal(input);

            for (final Outline outline : opml.getBody().getOutlines()) {

                final String title = outline.getTitle();
                final String xmlUrl = outline.getXmlUrl();

                if (StringUtil.isBlank(xmlUrl)) {

                    Folder folder = folderRepository.findByUserAndTitle(currentUser, title);
                    if (folder == null) {
                        folder = new Folder.Builder().setUser(currentUser).setTitle(title).build();
                        folder = folderRepository.save(folder);
                    }

                    final Long folderId = folder.getId();

                    final List<Outline> children = outline.getOutlines();
                    for (final Outline child : children) {

                        final String childUrl = child.getXmlUrl();
                        final String childTitle = child.getTitle();

                        if (StringUtil.isBlank(childUrl)) {
                            LOG.debug("Child has no url: {}", child);
                        } else {
                            addSubscription(new AddSubscriptionRequest(childUrl, folderId, childTitle));
                        }

                    }

                } else {
                    addSubscription(new AddSubscriptionRequest(xmlUrl, null, title));
                }

            }

            LOG.debug("Successfully imported subscription: {}", opmlFile);
            return Response.success();

        } catch (IOException | JAXBException e) {
            LOG.debug("An error occured while importing subscriptions: " + opmlFile, e);
            throw new ServiceException("An error occured while importing subscriptions");
        }

    }

    @Transactional(readOnly = true)
    public String exportSubscriptions() throws JAXBException {

        LOG.debug("Exporting subscriptions");

        try {

            final List<Outline> outlines = new ArrayList<Outline>();

            final List<Node> nodes = getSubscriptions().getNodes();

            for (final Node node : nodes) {

                if (node instanceof FolderNode) {

                    final FolderNode folderNode = (FolderNode) node;

                    final List<Outline> children = new ArrayList<Outline>();

                    for (final SubscriptionNode subscriptionNode : folderNode.getSubscriptions()) {

                        final String subscriptionUrl = subscriptionNode.getUrl();
                        final String subscriptionTitle = subscriptionNode.getTitle();

                        final Outline subscription = new Outline();
                        subscription.setType("rss");
                        subscription.setText(subscriptionTitle);
                        subscription.setTitle(subscriptionTitle);
                        subscription.setXmlUrl(subscriptionUrl);
                        children.add(subscription);

                    }

                    final String folderTitle = folderNode.getTitle();

                    final Outline folder = new Outline();
                    folder.setType("folder");
                    folder.setText(folderTitle);
                    folder.setTitle(folderTitle);
                    folder.setOutlines(children);
                    outlines.add(folder);

                } else if (node instanceof SubscriptionNode) {

                    final SubscriptionNode subscriptionNode = (SubscriptionNode) node;
                    final String subscriptionUrl = subscriptionNode.getUrl();
                    final String subscriptionTitle = subscriptionNode.getTitle();

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
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            final StringWriter stringWriter = new StringWriter();
            marshaller.marshal(opml, stringWriter);
            return stringWriter.toString();

        } catch (final JAXBException e) {
            LOG.debug("An error occured while exporting subscriptions", e);
            throw e;
        }

    }
}
