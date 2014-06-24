package de.patrickgotthard.newsreadr.server.userentries;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.mysema.query.types.Predicate;

import de.patrickgotthard.newsreadr.shared.response.data.EntrySummary;

interface CustomUserEntryRepository {

    Long getLatestEntryId(Predicate predicate);

    List<EntrySummary> findEntries(Predicate predicate, Pageable pageable);

    long countUnread(Predicate predicate);

    void markEntriesAsRead(List<UserEntry> entries);

}
