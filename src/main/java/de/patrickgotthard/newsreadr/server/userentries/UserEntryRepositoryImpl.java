package de.patrickgotthard.newsreadr.server.userentries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.Predicate;

import de.patrickgotthard.newsreadr.server.entries.QEntry;
import de.patrickgotthard.newsreadr.server.subscriptions.QSubscription;
import de.patrickgotthard.newsreadr.shared.response.data.EntrySummary;

@Repository
class UserEntryRepositoryImpl implements CustomUserEntryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long getLatestEntryId(final Predicate predicate) {
        final QUserEntry qUserEntry = QUserEntry.userEntry;
        return new JPAQuery(entityManager).from(qUserEntry).where(predicate).uniqueResult(qUserEntry.id.max());
    }

    @Override
    public List<EntrySummary> findEntries(final Predicate predicate, final Pageable pageable) {

        final QUserEntry qUserEntry = QUserEntry.userEntry;
        final QEntry qEntry = QEntry.entry;
        final QSubscription qSubscription = QSubscription.subscription;

        // @formatter:off
        final List<Tuple> tuples = new JPAQuery(entityManager)
        .from(qUserEntry)
        .join(qUserEntry.subscription, qSubscription)
        .join(qUserEntry.entry, qEntry)
        .where(predicate)
        .orderBy(qEntry.publishDate.desc(), qEntry.title.asc())
        .limit(pageable.getPageSize())
        .offset(pageable.getOffset())
        .list(qUserEntry.id, qSubscription.title, qEntry.url, qEntry.title, qEntry.publishDate, qUserEntry.read, qUserEntry.bookmarked);
        // @formatter:on

        final List<EntrySummary> summaries = new ArrayList<>();
        for (final Tuple tuple : tuples) {
            final Long id = tuple.get(qUserEntry.id);
            final String subscription = tuple.get(qSubscription.title);
            final String url = tuple.get(qEntry.url);
            final String title = tuple.get(qEntry.title);
            final Date publishDate = tuple.get(qEntry.publishDate);
            final Boolean read = tuple.get(qUserEntry.read);
            final Boolean bookmarked = tuple.get(qUserEntry.bookmarked);
            summaries.add(new EntrySummary(id, subscription, url, title, publishDate, read, bookmarked));
        }
        return summaries;

    }

    @Override
    public long countUnread(final Predicate predicate) {

        final QSubscription qSubscription = QSubscription.subscription;
        final QUserEntry qUserEntry = QUserEntry.userEntry;

        // @formatter:off
        return new JPAQuery(entityManager)
        .from(qUserEntry)
        .join(qUserEntry.subscription, qSubscription)
        .where(predicate)
        .count();
        // @formatter:on

    }

    @Override
    public void markEntriesAsRead(final List<UserEntry> entries) {
        final QUserEntry qUserEntry = QUserEntry.userEntry;
        new JPAUpdateClause(entityManager, qUserEntry).set(qUserEntry.read, true).where(qUserEntry.in(entries)).execute();
    }

}
