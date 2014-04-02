package de.patrickgotthard.newsreadr.server.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;

import de.patrickgotthard.newsreadr.server.persistence.entity.AbstractEntity;

@NoRepositoryBean
public interface NewsreadrRepository<T extends AbstractEntity> extends JpaRepository<T, Long>, QueryDslPredicateExecutor<T> {

    @Override
    List<T> findAll(Predicate predicate);

    @Override
    List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);

}
