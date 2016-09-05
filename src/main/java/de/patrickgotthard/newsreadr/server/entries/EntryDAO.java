package de.patrickgotthard.newsreadr.server.entries;

import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.QEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QSubscription;
import de.patrickgotthard.newsreadr.server.common.rest.ServerException;
import de.patrickgotthard.newsreadr.server.entries.request.GetEntriesRequest;
import de.patrickgotthard.newsreadr.server.entries.response.EntrySummary;
import de.patrickgotthard.newsreadr.server.subscriptions.response.Node.Type;

@Repository
class EntryDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Long getLatestEntryId(final long currentUserId) {
        final QEntry userEntry = QEntry.entry;
        final BooleanExpression filter = userEntry.subscription.user.id.eq(currentUserId);
        return new JPAQuery<>(this.entityManager).select(userEntry.id.max()).from(userEntry).where(filter).fetchOne();
    }

    public List<EntrySummary> findEntries(final GetEntriesRequest request, final long currentUserId) {

        final QEntry entry = QEntry.entry;
        final QSubscription subscription = entry.subscription;

        final StringPath subscriptionTitle = subscription.title;
        final NumberPath<Long> id = entry.id;
        final StringPath url = entry.url;
        final StringPath title = entry.title;
        final DateTimePath<Date> published = entry.published;
        final BooleanPath read = entry.read;
        final BooleanPath bookmarked = entry.bookmarked;

        final BooleanBuilder filter = new BooleanBuilder();
        filter.and(subscription.user.id.eq(currentUserId));

        final Type type = request.getType();
        switch (type) {
            case ALL:
                // no filter to apply
                break;
            case BOOKMARKS:
                filter.and(bookmarked.isTrue());
                break;
            case SUBSCRIPTION:
                filter.and(entry.subscription.id.eq(request.getId()));
                break;
            default:
                throw new ServerException("Unknown feed type: " + type);
        }

        final Long latestEntryId = request.getLatestEntryId();
        if (latestEntryId != null) {
            filter.and(id.loe(latestEntryId));
        }

        final boolean unreadOnly = request.getUnreadOnly();
        if (unreadOnly) {
            filter.and(read.isFalse());
        }

        final int page = request.getPage();
        final int size = 50;
        final PageRequest pageRequest = new PageRequest(page, size);

        // @formatter:off
        return new JPAQuery<>(this.entityManager)
            .select(subscriptionTitle,  id, url, title, published, read, bookmarked)
            .from(entry)
            .where(filter)
            .orderBy(published.desc())
            .limit(pageRequest.getPageSize())
            .offset(pageRequest.getOffset())
            .fetch()
            .stream()
            .map(tuple -> {
                final EntrySummary summary = new EntrySummary();
                summary.setSubscription(tuple.get(subscriptionTitle));
                summary.setId(tuple.get(id));
                summary.setUrl(tuple.get(url));
                summary.setTitle(tuple.get(title));
                summary.setPublishDate(tuple.get(published));
                summary.setRead(tuple.get(read));
                summary.setBookmarked(tuple.get(bookmarked));
                return summary;
            }).collect(toList());
        // @formatter:on

    }

}
