package de.patrickgotthard.newsreadr.server.entries;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.QEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QSubscription;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QUserEntry;
import de.patrickgotthard.newsreadr.server.entries.response.EntrySummary;

@Repository
class EntryDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Long getLatestEntryId(final long currentUserId) {
        final QUserEntry userEntry = QUserEntry.userEntry;
        final BooleanExpression filter = userEntry.subscription.user.id.eq(currentUserId);
        return new JPAQuery<>(this.entityManager).select(userEntry.id.max()).from(userEntry).where(filter).fetchOne();
    }

    public List<EntrySummary> findEntries(final Predicate predicate, final Pageable pageable) {

        final QUserEntry userEntry = QUserEntry.userEntry;
        final QSubscription subscription = userEntry.subscription;
        final QEntry entry = userEntry.entry;

        // @formatter:off
        return new JPAQuery<>(this.entityManager)
            .select(userEntry.id, subscription.title, entry.url, entry.title, entry.publishDate, userEntry.read, userEntry.bookmarked)
            .from(userEntry)
            .where(predicate)
            .orderBy(entry.publishDate.desc(), entry.title.asc())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch()
            .stream()
            .map(tuple -> {
                final EntrySummary summary = new EntrySummary();
                summary.setId(tuple.get(userEntry.id));
                summary.setSubscription(tuple.get(subscription.title));
                summary.setUrl(tuple.get(entry.url));
                summary.setTitle(tuple.get(entry.title));
                summary.setPublishDate(tuple.get(entry.publishDate));
                summary.setRead(tuple.get(userEntry.read));
                summary.setBookmarked(tuple.get(userEntry.bookmarked));
                return summary;
            }).collect(toList());
        // @formatter:on

    }

}
