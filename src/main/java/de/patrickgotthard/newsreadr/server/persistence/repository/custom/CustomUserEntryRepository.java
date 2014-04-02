package de.patrickgotthard.newsreadr.server.persistence.repository.custom;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.mysema.query.types.Predicate;

import de.patrickgotthard.newsreadr.server.persistence.entity.UserEntry;
import de.patrickgotthard.newsreadr.shared.response.data.EntrySummary;

public interface CustomUserEntryRepository {

    long getLatestEntryId(Predicate predicate);

    List<EntrySummary> findEntries(Predicate predicate, Pageable pageable);

    long countUnread(Predicate predicate);

    void markEntriesAsRead(List<UserEntry> entries);

}
