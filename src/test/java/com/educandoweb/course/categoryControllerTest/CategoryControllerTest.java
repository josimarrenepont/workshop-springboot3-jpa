package com.educandoweb.course.categoryControllerTest;

import com.educandoweb.course.controller.CategoryController;
import com.educandoweb.course.entities.Category;
import com.educandoweb.course.repositories.CategoryRepository;
import com.educandoweb.course.services.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private CategoryRepository categoryRepository;


    private ObjectMapper objectMapper = new ObjectMapper();
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Informatics");
    }

    @Test
    void testFindAll() throws Exception {
        when(categoryService.findAll()).thenReturn(Collections.singletonList(category));

        mockMvc.perform(get("/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(category.getId()))
                .andExpect(jsonPath("$[0].name").value(category.getName()));
    }

    @Test
    void testFindById() throws Exception {
        when(categoryService.findById(1L)).thenReturn(category);

        mockMvc.perform(get("/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value(category.getName()));
    }

    @Test
    void testCreateCategory() throws Exception {
        when(categoryService.createCategory(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value(category.getName()));
    }

    @Test
    void testUpdateCategory() throws Exception {
        when(categoryService.update(eq(1L), any(Category.class))).thenReturn(category);

        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value(category.getName()));
    }
}
