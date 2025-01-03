package com.educandoweb.course.services.impl;

import com.educandoweb.course.entities.User;
import com.educandoweb.course.entities.dto.UserDto;
import com.educandoweb.course.repositories.UserRepository;
import com.educandoweb.course.services.UserService;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserDto::new)
                .toList();
    }
    @Override
    public UserDto findById(Long id){
        return userRepository.findById(id).map(UserDto::new)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }
    @Override
    public UserDto insert(UserDto userDto){
        User user = new User();
        user.setName(userDto.getName());
        user.setPhone(user.getPhone());
        user.setEmail(user.getEmail());
        user.setPassword(userDto.getPassword());

        return new UserDto(userRepository.save(user));
    }
    @Override
    public void delete(Long id){
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }
    @Override
    public UserDto update(Long id, UserDto userDto){
        try {
            User user = userRepository.getReferenceById(id);
            updateDate(user, userDto);
            return new UserDto(userRepository.save(user));
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
    }

    private void updateDate(User entity, UserDto obj) {
        entity.setPhone(obj.getPhone());
        entity.setEmail(obj.getEmail());
        entity.setPassword(obj.getPassword());
    }
}
