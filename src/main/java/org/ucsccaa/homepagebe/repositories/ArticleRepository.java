package org.ucsccaa.homepagebe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ucsccaa.homepagebe.domains.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    public List<Article> findByCategory(String category);
}
