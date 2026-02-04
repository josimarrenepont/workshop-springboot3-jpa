package com.educandoweb.course.services.impl;

import com.educandoweb.course.entities.Permission;
import com.educandoweb.course.entities.User;
import com.educandoweb.course.entities.dto.UserDto;
import com.educandoweb.course.repositories.PermissionRepository;
import com.educandoweb.course.repositories.UserRepository;
import com.educandoweb.course.services.UserService;
import com.educandoweb.course.services.exceptions.DatabaseException;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, PermissionRepository permissionRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
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
    public User insert(UserDto userDto) {

        User user = new User();

        user.setUserName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        Permission userRole = permissionRepository
                .findByDescription("ROLE_USER")
                .orElseThrow(() ->
                        new RuntimeException("Default role ROLE_USER not found"));

        user.setPermissions(Set.of(userRole));

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void delete(Long id){
        try{
            userRepository.deleteById(id);
            log.info("User with id {} has been successfully deletec. ", id);
        } catch(EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("User not found with id " + id);
        } catch (DataIntegrityViolationException e){
            throw new DatabaseException(e.getMessage());
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
        entity.setPassword(passwordEncoder.encode(obj.getPassword()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Searching for user with username: {} ", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));
    }
}
