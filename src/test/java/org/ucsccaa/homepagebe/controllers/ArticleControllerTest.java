package org.ucsccaa.homepagebe.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.ucsccaa.homepagebe.HomepageBeApplication;
import org.ucsccaa.homepagebe.domains.Article;
import org.ucsccaa.homepagebe.services.ArticleService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomepageBeApplication.class)
public class ArticleControllerTest {
    protected MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private ArticleService service;
    @InjectMocks
    private ArticleController controller;
    private final Article expectedArticle = new Article((long)1, "category", "title", "author", "content", null, "img", "html");

    @Before
    public void Before() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void addArticleTest() throws Exception {
        when(service.addArticle(any())).thenReturn(expectedArticle.getId());
        String json = objectMapper.writeValueAsString(expectedArticle);
        mockMvc.perform(MockMvcRequestBuilders.post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload")
                        .value("http://localhost/articles/" + expectedArticle.getId().toString())); 
    }

    @Test
    public void addArticleNullTest() throws Exception {
        doThrow(new RuntimeException("ARTICLE CANNOT BE NULL")).when(service).addArticle(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/articles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void updateArticleTest() throws Exception {
        String json = objectMapper.writeValueAsString(expectedArticle);
        when(service.updateArticle(any())).thenReturn(expectedArticle);
        mockMvc.perform(MockMvcRequestBuilders.put("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.id").value(expectedArticle.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.author").value(expectedArticle.getAuthor()));
    }

    @Test
    public void updateArticleNotFoundTest() throws Exception {
        String json = objectMapper.writeValueAsString(expectedArticle);
        when(service.updateArticle(any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    public void getArticleByIdTest() throws Exception {
        when(service.getArticle(anyLong())).thenReturn(expectedArticle);
        mockMvc.perform(MockMvcRequestBuilders.get("/articles/" + expectedArticle.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.id").value(expectedArticle.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.author").value(expectedArticle.getAuthor()));
    }

    @Test
    public void getArticleByIdNotFoundTest() throws Exception {
        when(service.getArticle(anyLong())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/articles/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NOT_FOUND"));
    }

    @Test
    public void deleteArticleByIdTest() throws Exception {
        when(service.deleteArticleById(anyLong())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/articles/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    public void deleteArticleByIdNotFoundTest() throws Exception {
        when(service.deleteArticleById(anyLong())).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders.delete("/articles/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("NOT_FOUND"));
    }
}
