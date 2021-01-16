package org.ucsccaa.homepagebe.controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ucsccaa.homepagebe.domains.Article;
import org.ucsccaa.homepagebe.models.ServiceResponse;
import org.ucsccaa.homepagebe.models.Status;
import org.ucsccaa.homepagebe.services.ArticleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Article RESTful API")
@RestController
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleService service;

    @ApiOperation("Add new Article")
    @PostMapping
    public ServiceResponse<URI> addArticle(@RequestBody Article article, HttpServletRequest req) throws URISyntaxException {
        try {
            Long id = service.addArticle(article);
            return new ServiceResponse<>(new URI(req.getRequestURL() + "/" + id));
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update existed Article by ID")
    @PutMapping
    public ServiceResponse<Article> updateArticle(@RequestBody Article article) {
        Article updatedArticle = null;
        try {
            updatedArticle = service.updateArticle(article);
            if (updatedArticle == null)
                return new ServiceResponse<>(Status.NOT_FOUND, "ARTICLE NOT FOUND");
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
        return new ServiceResponse<>(updatedArticle);
    }

    @ApiOperation("Get Article by ID")
    @GetMapping("/{id}")
    public ServiceResponse<Article> getArticle(@PathVariable Long id) {
        Article article = null;
        try {
            article = service.getArticle(id);
            if (article == null)
                return new ServiceResponse<>(Status.NOT_FOUND, "ID NOT FOUND");
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
        return new ServiceResponse<>(article);
    }

    @ApiOperation("Get all Articles")
    @GetMapping
    public ServiceResponse<List<Article>> getAll() {
        return new ServiceResponse<>(service.getAll());
    }

    @ApiOperation("Get articles by category")
    @GetMapping("/_category")
    public ServiceResponse<List<Article>> getArticleByCategory(@RequestBody Article article) {
        List<Article> list;
        try {
            list = service.getArticleByCategory(article.getCategory());
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
        return new ServiceResponse<>(list);
    }

    @ApiOperation("Delete Article by ID")
    @DeleteMapping("/{id}")
    public ServiceResponse<Object> deleteArticleById(@PathVariable Long id) {
        try {
            boolean deleted = service.deleteArticleById(id);
            if (!deleted)
                return new ServiceResponse<>(Status.NOT_FOUND, "ID NOT FOUND");
        } catch (Exception e) {
            return new ServiceResponse<>(Status.ERROR, e.getMessage());
        }
        return new ServiceResponse<>();
    }
}
