package com.educandoweb.course.productsControllerTest;

import com.educandoweb.course.entities.Order;
import com.educandoweb.course.entities.OrderItem;
import com.educandoweb.course.entities.Product;
import com.educandoweb.course.controller.ProductController;
import com.educandoweb.course.entities.dto.ProductDto;
import com.educandoweb.course.services.ProductService;
import static org.hamcrest.Matchers.*;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hibernate.internal.util.collections.CollectionHelper.setOf;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    private Product product;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        product = new Product(1L, "Cell Phone", "Iphone 15 pro",
                1500.00, "img", 7);
    }

    @Test
    public void testFindAll() throws Exception {
        List<Product> productList = Collections.singletonList(product);
        when(productService.findAll()).thenReturn(productList);

        ResultActions result = mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(product.getId()))
                .andExpect(jsonPath("$[0].name").value(product.getName()));
    }
    @Test
    public void testFindById() throws Exception{
        when(productService.findById(1L)).thenReturn(product);

        ResultActions result = mockMvc.perform(get("/products/1")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()));
    }
    @Test
    public void testInsert() throws Exception{
        when(productService.insert(any(ProductDto.class))).thenReturn(product);

        String productJson = "{\"id\": 1, \"name\": \"Cell Phone\", \"description\": " +
                "\"Iphone 15 pro\", \"price\": 1500.00, \"imgUrl\": \"image\", \"quantityInStock\": 7}";

        ResultActions result = mockMvc.perform(post("/products")
                        .content(productJson)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()));
    }
    @Test
    public void testUpdate() throws Exception{

        Product product = new Product(1L, "Cell Phone", "Iphone 15 pro", 1500.0, "imgUrl", 7);
        ProductDto productDto = new ProductDto(product);

        when(productService.update(eq(1L), any(ProductDto.class))).thenReturn(product);

        String productJson = "{\"id\": 1, \"name\": \"Cell Phone\", \"description\": " +
                "\"Iphone 15 pro\", \"price\": 1500.00, \"imgUrl\": \"image\", \"quantityInStock\": 7}";

        ResultActions result = mockMvc.perform(put("/products/1")
                        .content(productJson)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.quantityInStock").value(product.getQuantityInStock()));
    }
    @Test
    public void testDelete() throws Exception{
        Long productId = 1L;
        doNothing().when(productService).delete(productId);

        ResultActions result = mockMvc.perform(delete("/products/1", productId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
        verify(productService, times(1)).delete(productId);
    }
    @Test
    public void testFindOrdersByProductId() throws Exception{
        Order order1 = new Order();
        order1.setId(1L);

        Order order2 = new Order();
        order2.setId(2L);

        OrderItem orderItem1 = new OrderItem(order1, product, 7, 10.0);
        OrderItem orderItem2 = new OrderItem(order2, product, 7, 10.0);

        Set<OrderItem> items = setOf(orderItem1, orderItem2);
        product.setItems(items);

        when(productService.findOrdersByProductId(1L)).thenReturn(Set.of(order1, order2));

        ResultActions result = mockMvc.perform(get("/products/1/orders")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].id").value(Matchers.containsInAnyOrder(1, 2)));
    }
}
