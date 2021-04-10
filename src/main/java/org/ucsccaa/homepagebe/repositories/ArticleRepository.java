package org.ucsccaa.homepagebe.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ucsccaa.homepagebe.domains.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    public Page<Article> findByCategory(String category, Pageable pageable);
    public Optional<Article> findFirstByCategoryOrderByTimestampDesc(String category);
}
