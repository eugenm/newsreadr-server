package de.patrickgotthard.newsreadr.server.common.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

import de.patrickgotthard.newsreadr.server.common.persistence.entity.AbstractEntity;

@NoRepositoryBean
interface NewsreadrRepository<T extends AbstractEntity> extends JpaRepository<T, Long>, QueryDslPredicateExecutor<T> {

    @Override
    List<T> findAll(Predicate predicate);

    @Override
    List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);

    @Override
    List<T> findAll(OrderSpecifier<?>... orders);

    @Override
    List<T> findAll(Predicate predicate, Sort sort);

}
