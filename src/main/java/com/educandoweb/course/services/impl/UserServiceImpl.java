package com.educandoweb.course.services.impl;

import com.educandoweb.course.entities.User;
import com.educandoweb.course.entities.dto.UserDto;
import com.educandoweb.course.repositories.UserRepository;
import com.educandoweb.course.services.UserService;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

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
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        return new UserDto(userRepository.save(user));
    }
    @Transactional
    @Override
    public void delete(Long id){
        try{
            userRepository.deleteById(id);
            log.info("User with id {} has been successfully deletec. ", id);
        } catch(EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("User not found with id " + id);
        }
    }
    @Override
    public UserDto update(Long id, UserDto userDto){
        try {
            User user = userRepository.getReferenceById(id);
            updateUserFields(user, userDto);
            return new UserDto(userRepository.save(user));
        } catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
    }

    private void updateUserFields(User entity, UserDto obj) {
        entity.setPhone(obj.getPhone());
        entity.setEmail(obj.getEmail());
        entity.setPassword(obj.getPassword());
    }
}
