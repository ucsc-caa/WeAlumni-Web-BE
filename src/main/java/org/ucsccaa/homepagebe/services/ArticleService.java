package org.ucsccaa.homepagebe.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.ucsccaa.homepagebe.domains.Article;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.BadRequestException;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.BadResourceException;
import org.ucsccaa.homepagebe.repositories.ArticlePagingRepository;
import org.ucsccaa.homepagebe.repositories.ArticleRepository;
import org.ucsccaa.homepagebe.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticlePagingRepository articlePagingRepository;

    private final Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");

    public Article addArticle(Article article) {
        if (article == null)
            throw new BadResourceException("empty article");
        if (!StringUtils.isEmpty(article.getCategory()) && !categories.contains(article.getCategory()))
            categories.add(article.getCategory());

        article.setTimestamp(CommonUtils.getCurrentDateTime());
        logger.info("Succeeded to add a new article: id-{}, title-{}", article.getId(), article.getTitle());
        return articleRepository.save(article);
    }

    public Article updateArticle(Article article) {
        if (article == null)
            throw new BadResourceException("empty article");
        if (article.getId() == null)
            throw new BadResourceException("bad id: empty or null");
        if (articleRepository.existsById(article.getId()))
            throw new BadResourceException("no resource in id - " + article.getId());
        if (!StringUtils.isEmpty(article.getCategory()) && !categories.contains(article.getCategory()))
            categories.add(article.getCategory());

        article.setTimestamp(CommonUtils.getCurrentDateTime());
        logger.info("Succeeded to update an article: id-{}, title-{}", article.getId(), article.getTitle());
        return articleRepository.save(article);
    }

    public Article getArticle(Integer id) {
        if (id == null)
            throw new BadRequestException("Param id cannot be null");
        Optional<Article> article = articleRepository.findById(id);
        return article.orElse(null);
    }

    public List<Article> getArticleByCategory(String category, Integer page) {
        Page<Article> articles;
        PageRequest pageRequest = PageRequest.of(page, 10, sort);
        if (category == null)
            articles = articlePagingRepository.findAll(pageRequest);
        else
            articles = articlePagingRepository.findAllByCategoryIs(category, pageRequest);

        return articles.getContent();
    }

    private final List<String> categories = new ArrayList<>();
    public List<String> getCategories() {
        if (categories.isEmpty())
            categories.addAll(articleRepository.getAllCategories());
        return categories;
    }

    public void deleteArticleById(Integer id) {
        if (id == null)
            throw new BadRequestException("Param id cannot be null");
        articleRepository.deleteById(id);
    }
}
