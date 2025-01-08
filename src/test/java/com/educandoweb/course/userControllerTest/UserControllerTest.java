package com.educandoweb.course.userControllerTest;

import com.educandoweb.course.controller.UserController;
import com.educandoweb.course.entities.User;
import com.educandoweb.course.entities.dto.UserDto;
import com.educandoweb.course.services.UserService;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice()
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        user = new User(1L, "user", "user@email.com", "1234567", "1234567");
        userDto = new UserDto(user);
    }
    @Test
    void testFindAll() throws Exception {
        List<UserDto> users = Collections.singletonList(userDto);

        when(userService.findAll()).thenReturn(List.of(userDto));

        ResultActions result = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userDto.getId()))
                .andExpect(jsonPath("$[0].name").value(userDto.getName()))
                .andExpect(jsonPath("$[0].email").value(userDto.getEmail()));
    }
    @Test
    void testFindById() throws Exception {
        when(userService.findById(1L)).thenReturn(userDto);

        ResultActions result = mockMvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }
    @Test
    void testInsert() throws Exception{

        when(userService.insert(any(UserDto.class))).thenReturn(user);

        String userJson = "{\"name\": \"user\",  \"email\": \"user@email.com\", " +
                "\"phone\": \"1234567\", \"password\": \"1234567\"}";

        ResultActions result = mockMvc.perform(post("/users")
                        .content(userJson)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.phone").value(userDto.getPhone()))
                .andExpect(jsonPath("$.password").value(userDto.getPassword()));

        verify(userService).insert(any(UserDto.class));
    }
    @Test
    void testUpdate() throws Exception{
        when(userService.update(eq(1L), any(UserDto.class))).thenReturn(userDto);
        String userJson = "{\"name\": \"user\",  \"email\": \"user@email.com\", " +
                "\"phone\": \"1234567\", \"password\": \"1234567\"}";

        ResultActions result = mockMvc.perform(put("/users/1")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.phone").value(userDto.getPhone()));
    }
    @Test
    void testDelete() throws Exception{
        Long id = 1L;
        doNothing().when(userService).delete(id);

        ResultActions result = mockMvc.perform(delete("/users/1", id)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
        verify(userService, times(1)).delete(id);
    }
}
