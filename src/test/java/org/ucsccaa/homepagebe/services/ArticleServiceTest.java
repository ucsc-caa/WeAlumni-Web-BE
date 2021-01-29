package org.ucsccaa.homepagebe.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.ucsccaa.homepagebe.HomepageBeApplication;
import org.ucsccaa.homepagebe.domains.Article;
import org.ucsccaa.homepagebe.repositories.ArticleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomepageBeApplication.class)
public class ArticleServiceTest {
    @Mock
    private ArticleRepository repository;
    @InjectMocks
    private ArticleService service;
    private final Article expectedArticle = new Article((long)1, "category", "title", "author", "content", null, "img", "html");
    
    @Test
    public void addArticleTest() {
        when(repository.save(any())).thenReturn(expectedArticle);
        Long id = service.addArticle(expectedArticle);
        assertEquals(expectedArticle.getId(), id);
    }

    @Test(expected = RuntimeException.class)
    public void addArticleNullTest() {
        service.addArticle(null);
    }

    @Test
    public void updateArticleTest() {
        when(repository.existsById(anyLong())).thenReturn(true);
        when(repository.save(any())).thenReturn(expectedArticle);
        Article article = service.updateArticle(expectedArticle);
        assertNotNull(article);
        assertEquals(expectedArticle.getId(), article.getId());
    }

    @Test(expected = RuntimeException.class)
    public void updateArticleIdNullTest() {
        service.updateArticle(new Article());
    }

    @Test
    public void updateArticleNotFoundTest() {
        when(repository.existsById(anyLong())).thenReturn(false);
        Article article = service.updateArticle(expectedArticle);
        assertEquals(null, article);
    }

    @Test
    public void getArticleTest() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(expectedArticle));
        Article article = service.getArticle(expectedArticle.getId());
        assertEquals(expectedArticle.getId(), article.getId());
    }

    @Test(expected = RuntimeException.class)
    public void getArticleIdNullTest() {
        service.getArticle(null);
    }

    @Test
    public void getArticleByCategoryPageOneTest() {
        List<Article> expectedList = new ArrayList<Article>(){{add(expectedArticle);}};
        when(repository.findByCategoryOrderByPosttimeAsc(anyString())).thenReturn(expectedList);
        List<Article> list = service.getArticleByCategory(expectedArticle.getCategory(), 1);
        assertEquals(expectedList, list);
    }

    @Test
    public void getArticleByCategoryPageZeroTest() {
        List<Article> expectedList = new ArrayList<Article>(){{add(expectedArticle);}};
        when(repository.findFirstByCategoryOrderByPosttimeDesc(anyString())).thenReturn(Optional.of(expectedArticle));
        List<Article> list = service.getArticleByCategory(expectedArticle.getCategory(), 0);
        assertEquals(expectedList, list);
    }

    @Test(expected = RuntimeException.class)
    public void getArticleByCategoryPageInvalidTest() {
        service.getArticleByCategory(expectedArticle.getCategory(), -1);
    }

    @Test
    public void deleteArticleByIdTest() {
        when(repository.findById(expectedArticle.getId())).thenReturn(Optional.of(expectedArticle));
        boolean result = service.deleteArticleById(expectedArticle.getId());
        assertEquals(true, result);
    }

    @Test
    public void deleteArticleByIdNotFoundTest() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        boolean result = service.deleteArticleById((long)1);
        assertEquals(false, result);
    }

    
}
