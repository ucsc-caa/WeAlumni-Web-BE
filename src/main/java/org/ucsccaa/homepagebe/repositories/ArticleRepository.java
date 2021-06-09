package org.ucsccaa.homepagebe.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.ucsccaa.homepagebe.domains.Article;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    @Query(value = "SELECT a.category FROM Article a GROUP BY a.category")
    List<String> getAllCategories();
}
