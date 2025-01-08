package com.educandoweb.course.categorySericeTest;

import com.educandoweb.course.entities.Category;
import com.educandoweb.course.entities.Product;
import com.educandoweb.course.entities.dto.ProductDto;
import com.educandoweb.course.repositories.CategoryRepository;
import com.educandoweb.course.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        category = new Category(1L, "New Category");
    }
    @Test
    void testFindAll(){
        when(categoryRepository.searchAll()).thenReturn(Collections.singletonList(category));

        List<Category> categories = categoryService.findAll();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals(category.getId(), categories.get(0).getId());
        verify(categoryRepository, times(1)).searchAll();
    }
}