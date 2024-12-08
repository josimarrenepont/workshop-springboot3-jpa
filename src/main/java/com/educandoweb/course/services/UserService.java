package com.educandoweb.course.services;


import com.educandoweb.course.entities.dto.UserDto;

import java.util.List;

public interface UserService{

	List<UserDto> findAll();
	UserDto findById(Long id);
	UserDto insert(UserDto userDto);
	void delete(Long id);
	UserDto update(Long id, UserDto userDto);
}