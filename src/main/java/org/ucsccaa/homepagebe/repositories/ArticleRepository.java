package org.ucsccaa.homepagebe.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ucsccaa.homepagebe.domains.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    public List<Article> findByCategoryOrderByPosttimeDesc(String category);
    public Optional<Article> findFirstByCategoryOrderByPosttimeDesc(String category);
}
