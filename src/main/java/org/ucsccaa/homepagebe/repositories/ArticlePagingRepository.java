package org.ucsccaa.homepagebe.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.ucsccaa.homepagebe.domains.Article;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ArticlePagingRepository extends PagingAndSortingRepository<Article, Integer> {
    Page<Article> findAllByCategoryIs(String category, Pageable pageable);
}
