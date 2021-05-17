package org.ucsccaa.homepagebe.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ucsccaa.homepagebe.domains.Article;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;
import org.ucsccaa.homepagebe.models.GeneralResponse;
import org.ucsccaa.homepagebe.services.ArticleService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @ApiOperation("Add new Article")
    @PostMapping
    public ResponseEntity<GeneralResponse> addArticle(@RequestBody Article article) {
        try {
            Article savedArticle = articleService.addArticle(article);
            return new ResponseEntity<>(new GeneralResponse<>(2102, "Resource Created", savedArticle), HttpStatus.CREATED);
        } catch (GenericServiceException e) {
            return e.getExceptionHandler().getResponseEntity();
        }
    }

    @ApiOperation("Update existed Article by ID")
    @PutMapping
    public ResponseEntity<GeneralResponse> updateArticle(@RequestBody Article article) {
        try {
            Article updatedArticle = articleService.updateArticle(article);
            return new ResponseEntity<>(new GeneralResponse(updatedArticle), HttpStatus.OK);
        } catch (GenericServiceException e) {
            return e.getExceptionHandler().getResponseEntity();
        }
    }

    @ApiOperation("Get Article by ID")
    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getArticle(@PathVariable Integer id) {
        try {
            Article article = articleService.getArticle(id);
            if (article == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(new GeneralResponse<>(article), HttpStatus.OK);
        } catch (GenericServiceException e) {
            return e.getExceptionHandler().getResponseEntity();
        }
    }

    @ApiOperation("Delete Article by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteArticleById(@PathVariable Integer id) {
        try {
            articleService.deleteArticleById(id);
            return new ResponseEntity<>(new GeneralResponse<>(), HttpStatus.OK);
        } catch (GenericServiceException e) {
            return e.getExceptionHandler().getResponseEntity();
        }
    }

    @ApiOperation("Get Articles by Category and Page. If category is not given, will ignore category filtering.")
    @GetMapping
    public ResponseEntity<GeneralResponse> getArticleByCategory(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer page) {
        try {
            if (page == null) page = 0;
            List<Article> articles = articleService.getArticleByCategory(category, page);
            return new ResponseEntity<>(new GeneralResponse(articles), HttpStatus.OK);
        } catch (GenericServiceException e) {
            return e.getExceptionHandler().getResponseEntity();
        }
    }

    @ApiOperation("Get a list of all Categories")
    @GetMapping("/categories")
    public ResponseEntity<GeneralResponse<List<String>>> getCategories() {
        return new ResponseEntity<>(new GeneralResponse<>(articleService.getCategories()), HttpStatus.OK);
    }
}
