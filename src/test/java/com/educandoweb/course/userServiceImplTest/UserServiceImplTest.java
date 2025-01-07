package com.educandoweb.course.userServiceImplTest;

import com.educandoweb.course.entities.User;
import com.educandoweb.course.entities.dto.UserDto;
import com.educandoweb.course.repositories.UserRepository;
import com.educandoweb.course.services.exceptions.DatabaseException;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import com.educandoweb.course.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp(){
        user = new User(1L, "user", "user@email.com", "1234567", "1234567");
        userDto = new UserDto(user);
    }
    @Test
    void testFindAll(){
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserDto> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        UserDto dto = result.get(0);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
        verify(userRepository, times(1)).findAll();
    }
    @Test
    void testFindById(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserDto result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
    }
    @Test
    void testFindById_DoesNotExist(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));
    }
    @Test
    void testInsert(){
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto userDto = new UserDto(user);
        User result = userService.insert(userDto);

        assertNotNull(result);
        assertEquals(userDto.getName(), result.getName());
    }
    @Test
    void testUpdate(){
        when(userRepository.getReferenceById(1L)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userDto.setEmail("marcos@gmail.com");
        UserDto result = userService.update( 1L, userDto);

        assertNotNull(result);
        assertEquals(userDto.getPhone(), result.getPhone());
        assertEquals(userDto.getPassword(), result.getPassword());
        assertEquals(userDto.getEmail(), result.getEmail());
        verify(userRepository, times(1)).getReferenceById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    void testDelete(){
        doNothing().when(userRepository).deleteById(1L);

            userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
    @Test
    void testDeleteResourceNotFoundException(){

        doThrow(ResourceNotFoundException.class).when(userRepository).deleteById(1L);

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.delete(1L);
        });

        verify(userRepository, times(1)).deleteById(1L);
    }
    @Test
    void testDeleteDatabaseException(){
        doThrow(DatabaseException.class).when(userRepository).deleteById(1L);

        assertThrows(DatabaseException.class, () -> {
            userService.delete(1L);
        });
        verify(userRepository, times(1)).deleteById(1L);
    }
}
