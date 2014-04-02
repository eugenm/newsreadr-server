package de.patrickgotthard.newsreadr.server.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.types.expr.BooleanExpression;

import de.patrickgotthard.newsreadr.server.persistence.entity.Feed;
import de.patrickgotthard.newsreadr.server.persistence.expression.FeedExpression;
import de.patrickgotthard.newsreadr.server.persistence.repository.FeedRepository;

@Component
class SharedService {

    private static final Logger LOG = LoggerFactory.getLogger(SharedService.class);

    private final FeedRepository feedRepository;

    @Autowired
    SharedService(final FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    void removeFeedsWithoutSubscribers() {
        LOG.debug("Removing feed without subscribers");
        final BooleanExpression unused = FeedExpression.isUnused();
        final List<Feed> unusedFeeds = feedRepository.findAll(unused);
        feedRepository.delete(unusedFeeds);
        LOG.debug("Removed {} feeds without subscribers", unusedFeeds.size());
    }

}
