package org.ucsccaa.homepagebe.services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ucsccaa.homepagebe.domains.Article;
import org.ucsccaa.homepagebe.repositories.ArticleRepository;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository repository;

    public Long addArticle(Article article) {
        if (article == null)
            throw new RuntimeException("ARTICLE CANNOT BE NULL");
        return repository.save(article).getId();
    }

    public Article updateArticle(Article article) {
        if (article == null)
            throw new RuntimeException("ARTICLE CANNOT BE NULL");
        if (article.getId() == null) 
            throw new RuntimeException("ARTICLE ID CANNOT BE NULL");
        return repository.existsById(article.getId()) ? repository.save(article) : null;
    }

    public Article getArticle(Long id) {
        if (id == null)
            throw new RuntimeException("ID CANNOT BE NULL");
        Optional<Article> article = repository.findById(id);
        return article.isPresent() ? article.get() : null;
    }

    public List<Article> getAll() {
        return repository.findAll();
    }

    public List<Article> getArticleByCategory(String category, Integer page) {
        if (category == null)
            throw new RuntimeException("CATEGORY CANNOT BE NULL");
        if (page == null) 
            throw new RuntimeException("PAGE NUMBER CANNOT BE NULL");
        if (page < 0) 
            throw new RuntimeException("INVALID PAGE NUMBER");
        if (page == 0) {
            Optional<Article> article = repository.findFirstByCategoryOrderByPosttimeDesc(category);
            if (!article.isPresent()) 
                return new ArrayList<Article>();
            else 
                return new ArrayList<Article>(){{ add(article.get()); }};
        }
        List<Article> result = repository.findByCategoryOrderByPosttimeDesc(category);
        int fromIndex = (page-1) * 15, toIndex = page * 15;
        if (fromIndex > result.size())
            return new ArrayList<Article>();
        return result.subList(fromIndex, toIndex > result.size() ? result.size() : toIndex);
    }

    public Boolean deleteArticleById(Long id) {
        if (id == null) 
            throw new RuntimeException("ID CANNOT BE NULL");
        Optional<Article> article = repository.findById(id);
        if (article.isPresent()) {
            repository.delete(article.get());
            return true;
        } else
            return false;
    }
}
