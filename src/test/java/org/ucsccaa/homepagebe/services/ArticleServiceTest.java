package org.ucsccaa.homepagebe.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;
import org.ucsccaa.homepagebe.domains.Article;
import org.ucsccaa.homepagebe.repositories.ArticlePagingRepository;
import org.ucsccaa.homepagebe.repositories.ArticleRepository;

@RunWith(MockitoJUnitRunner.class)
public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticlePagingRepository articlePagingRepository;

    @InjectMocks
    private ArticleService service;

    private final List <String> resourceList = new ArrayList<>();
    private final List<String> categories = new ArrayList<>();
    private final Article expectedArticle = new Article(1, "category", "title", "author", "brief","content", "cover", "timestamp", resourceList);

    @Test
    public void getCategoriesTest() {
        when(articleRepository.getAllCategories()).thenReturn(categories);
        List<String> result = service.getCategories();
        Assert.assertEquals(result, categories);
    }

    @Test
    public void getArticleByCategory() {
        Page<Article> pageArticle = Page.empty();
        when(articlePagingRepository.findAllByCategoryIs(any(), any())).thenReturn(pageArticle);
        List<Article> result = service.getArticleByCategory(expectedArticle.getCategory(), 1);
        Assert.assertEquals(result, pageArticle.getContent());
    }

    @Test
    public void getArticleTest() {
        when(articleRepository.findById(any())).thenReturn(Optional.of(expectedArticle));
        Article article = service.getArticle(expectedArticle.getId());
        assertEquals(expectedArticle.getId(), article.getId());
    }

    @Test
    public void getArticleTest_notfound() {
        when(articleRepository.findById(any())).thenReturn(Optional.empty());
        Article article = service.getArticle(1);
        assertNull(article);
    }

    @Test(expected = RuntimeException.class)
    public void getArticleTest_null() {
        service.getArticle(null);
    }

    @Test
    public void getArticleByPagingTest() {
        Page<Article> pageArticle = Page.empty();
        when(articlePagingRepository.findAll((Pageable) any())).thenReturn(pageArticle);
        List<Article> result = service.getArticleByPaging(1);
        Assert.assertEquals(result, pageArticle.getContent());
    }

    @Test
    public void addArticleTest() {
        when(articleRepository.save(any())).thenReturn(expectedArticle);
        Article result = service.addArticle(expectedArticle);
        Assert.assertEquals(result, expectedArticle);
    }

    @Test(expected = RuntimeException.class)
    public void addArticleTest_null() {
        service.addArticle(null);
    }

    @Test
    public void updateArticleTest() {
        when(articleRepository.existsById(any())).thenReturn(true);
        when(articleRepository.save(any())).thenReturn(expectedArticle);
        Article result = service.updateArticle(expectedArticle);
        Assert.assertEquals(expectedArticle.getId(), result.getId());
    }

    @Test(expected = RuntimeException.class)
    public void updateArticleTest_null() {
        service.updateArticle(null);
    }

    @Test(expected = RuntimeException.class)
    public void updateArticleTest_nullId() {
        Article article = new Article(null, "category", "title", "author", "brief","content", "cover", "timestamp", resourceList);
        service.updateArticle(article);
    }

    @Test(expected = RuntimeException.class)
    public void updateArticleTest_notfound() {
        when(articleRepository.existsById(any())).thenReturn(false);
        service.updateArticle(expectedArticle);
    }

    @Test
    public void deleteArticleByIdTest() {
        service.deleteArticleById(expectedArticle.getId());
    }

    @Test(expected = RuntimeException.class)
    public void deleteArticleByIdNullTest() {
        service.deleteArticleById(null);
    }
}