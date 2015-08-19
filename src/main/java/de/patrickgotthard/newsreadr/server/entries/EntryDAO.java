package de.patrickgotthard.newsreadr.server.entries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.QEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QSubscription;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.QUserEntry;
import de.patrickgotthard.newsreadr.server.common.persistence.entity.User;
import de.patrickgotthard.newsreadr.server.entries.response.EntrySummary;

@Repository
class EntryDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Long getLatestEntryId(final User user) {
        final QUserEntry userEntry = QUserEntry.userEntry;
        final BooleanExpression entryBelongsToUser = userEntry.subscription.user.eq(user);
        return new JPAQuery(this.entityManager).from(userEntry).where(entryBelongsToUser).uniqueResult(userEntry.id.max());
    }

    public List<EntrySummary> findEntries(final Predicate predicate, final Pageable pageable) {

        final QUserEntry userEntry = QUserEntry.userEntry;
        final QSubscription subscription = userEntry.subscription;
        final QEntry entry = userEntry.entry;

        final OrderSpecifier<Date> publishDateDesc = entry.publishDate.desc();
        final OrderSpecifier<String> titleAsc = entry.title.asc();

        final int limit = pageable.getPageSize();
        final int offset = pageable.getOffset();

        // @formatter:off
        final List<Tuple> tuples = new JPAQuery(this.entityManager)
            .from(userEntry)
            .where(predicate)
            .orderBy(publishDateDesc, titleAsc)
            .limit(limit)
            .offset(offset)
            .list(userEntry.id, subscription.title, entry.url, entry.title, entry.publishDate, userEntry.read, userEntry.bookmarked);
        // @formatter:on

        final List<EntrySummary> summaries = new ArrayList<>();
        tuples.forEach(tuple -> {
            final EntrySummary summary = new EntrySummary();
            summary.setId(tuple.get(userEntry.id));
            summary.setSubscription(tuple.get(subscription.title));
            summary.setUrl(tuple.get(entry.url));
            summary.setTitle(tuple.get(entry.title));
            summary.setPublishDate(tuple.get(entry.publishDate));
            summary.setRead(tuple.get(userEntry.read));
            summary.setBookmarked(tuple.get(userEntry.bookmarked));
            summaries.add(summary);
        });
        return summaries;

    }

}
