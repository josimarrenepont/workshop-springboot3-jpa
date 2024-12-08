package com.educandoweb.course.controller;

import java.net.URI;
import java.util.List;
import com.educandoweb.course.entities.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.educandoweb.course.entities.User;
import com.educandoweb.course.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService){
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<List<UserDto>> findAll(){
		return ResponseEntity.ok(userService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDto> findById(@PathVariable Long id) {
		return ResponseEntity.ok(userService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<UserDto> insert(@RequestBody UserDto userDto){
		UserDto createdUser = userService.insert(userDto);
		return ResponseEntity.created(URI.create("/users" + createdUser.getId())).body(createdUser);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}
	@PutMapping("/{id}")
	public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto userDto){
		UserDto updatedUser = userService.update(id, userDto);
		return ResponseEntity.ok(updatedUser);
	}
}