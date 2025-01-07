package com.educandoweb.course.services;


import com.educandoweb.course.entities.User;
import com.educandoweb.course.entities.dto.UserDto;

import java.util.List;

public interface UserService{

	List<UserDto> findAll();
	UserDto findById(Long id);
	User insert(UserDto userDto);
	void delete(Long id);
	UserDto update(Long id, UserDto userDto);
}