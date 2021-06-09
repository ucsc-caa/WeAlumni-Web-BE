package org.ucsccaa.homepagebe.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.ucsccaa.homepagebe.domains.Article;
import org.ucsccaa.homepagebe.exceptions.customizedExceptions.BadRequestException;
import org.ucsccaa.homepagebe.services.ArticleService;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class ArticleControllerTest {

    protected MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ArticleService service;

    @InjectMocks
    private ArticleController controller;

    private final List <String> resourceList = new ArrayList<>();
    private final List<String> categories = new ArrayList<>();
    private final List<Article> articleList = new ArrayList<>();
    private final Article expectedArticle = new Article(1, "category", "title", "author", "brief","content", "cover", "timestamp", resourceList);

    @Before
    public void Before() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getCategoriesTest() throws Exception{
        when(service.getCategories()).thenReturn(categories);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/article/categories");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").exists());
    }

    @Test
    public void getArticleByCategoryTest() throws Exception{
        when(service.getArticleByCategory(any(), any())).thenReturn(articleList);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/article/category/category/page/1");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").exists());
    }

    @Test
    public void getArticleTest() throws Exception {
        when(service.getArticle(any())).thenReturn(expectedArticle);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/article/1");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").exists());
    }

    @Test
    public void getArticleTest_notfound() throws Exception {
        when(service.getArticle(any())).thenReturn(null);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/article/1");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getArticleTest_exception() throws Exception {
        when(service.getArticle(any())).thenThrow(new BadRequestException("Param id cannot be null"));
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/article/1");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request: reason - Param id cannot be null"));
    }

    @Test
    public void getArticleInPagingTest() throws Exception {
        when(service.getArticleByPaging(any())).thenReturn(articleList);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/article/page/1");

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").exists());
    }

    @Test
    public void addArticleTest() throws Exception {
        when(service.addArticle(any())).thenReturn(expectedArticle);
        String json = objectMapper.writeValueAsString(expectedArticle);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(2102))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Resource Created"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.id").value(1));
    }

    @Test
    public void addArticleTest_exception() throws Exception {
        when(service.addArticle(any())).thenThrow(new BadRequestException("empty article"));
        String json = objectMapper.writeValueAsString(expectedArticle);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post("/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request: reason - empty article"));
    }

    @Test
    public void updateArticleTest() throws Exception {
        when(service.updateArticle(any())).thenReturn(expectedArticle);
        String json = objectMapper.writeValueAsString(expectedArticle);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.id").value(1));
    }

    @Test
    public void updateArticleTest_exception() throws Exception {
        when(service.updateArticle(any())).thenThrow(new BadRequestException("empty article"));
        String json = objectMapper.writeValueAsString(expectedArticle);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .put("/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request: reason - empty article"));
    }

    @Test
    public void deleteArticleByIdTest() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/article/1");
        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void deleteArticleByIdTest_exception() throws Exception {
        doThrow(new BadRequestException("Param id cannot be null")).when(service).deleteArticleById(1);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .delete("/article/1");
        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request: reason - Param id cannot be null"));

    }
}
