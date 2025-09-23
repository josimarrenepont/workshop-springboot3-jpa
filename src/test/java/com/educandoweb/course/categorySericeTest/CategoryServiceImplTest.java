package com.educandoweb.course.categorySericeTest;

import com.educandoweb.course.entities.Category;
import com.educandoweb.course.repositories.CategoryRepository;
import com.educandoweb.course.services.impl.CategoryServiceImpl;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    private Category category;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        category = new Category(1L, "New Category");
    }
    @Test
    void testFindAll(){
        when(categoryRepository.searchAll()).thenReturn(Collections.singletonList(category));

        List<Category> categories = categoryServiceImpl.findAll();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals(category.getId(), categories.get(0).getId());
        verify(categoryRepository, times(1)).searchAll();
    }
    @Test
    void testFindById(){
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryServiceImpl.findById(1L);

        assertNotNull(result);
        assertEquals(category.getId(), result.getId());

        verify(categoryRepository, times(1)).findById(1L);
    }
    @Test
    void testFindById_ResourceNotFound(){
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryServiceImpl.findById(1L);
        });
        verify(categoryRepository, times(1)).findById(1L);
    }
    @Test
    void testCreateCategory(){
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryServiceImpl.createCategory(category);

        assertNotNull(result);
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());

        verify(categoryRepository, times(1)).save(any(Category.class));
    }
    @Test
    void testCategoryUpdate(){
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryServiceImpl.update(1L, category);

        assertNotNull(result);
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());

        verify(categoryRepository, times(1)).getReferenceById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
}
