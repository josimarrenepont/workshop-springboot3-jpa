package com.educandoweb.course.categoryControllerTest;

import com.educandoweb.course.controller.CategoryController;
import com.educandoweb.course.entities.Category;
import com.educandoweb.course.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;
    private Category category;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice()
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        category = new Category(1L, "New Category");
    }
    @Test
    void testFindAll() throws Exception{
        List<Category> categoryList = Collections.singletonList(category);

        when(categoryService.findAll()).thenReturn(List.of(category));

        ResultActions result = mockMvc.perform(get("/categories")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(category.getId()))
                .andExpect(jsonPath("$[0].name").value(category.getName()));
    }
    @Test
    void testFindById() throws Exception{
        when(categoryService.findById(1L)).thenReturn(category);

        ResultActions result = mockMvc.perform(get("/categories/1")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value(category.getName()));
    }
    @Test
    void testCreateCategory() throws Exception{
        when(categoryService.createCategory(any(Category.class))).thenReturn(category);

        String categoryJson = "{\"id\": 1, \"name\": \"New Category\"}";

        ResultActions result = mockMvc.perform(post("/categories")
                .content(categoryJson)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value(category.getName()));
    }
    @Test
    void testUpdate() throws Exception{
        Category category = new Category(1L, "New Category");
        when(categoryService.update(eq(1L) , any(Category.class))).thenReturn(category);

        String categoryJson = "{\"id\": 1, \"name\": \"New Category\"}";

        ResultActions result = mockMvc.perform(put("/categories/1")
                .content(categoryJson)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value(category.getName()));
    }
}