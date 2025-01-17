package com.educandoweb.course.controller;

import java.net.URI;
import java.util.List;

import com.educandoweb.course.entities.User;
import com.educandoweb.course.entities.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.educandoweb.course.services.UserService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	public ResponseEntity<UserDto> insert(@RequestBody @Validated UserDto userDto){
		User user = userService.insert(userDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).body(new UserDto(user));
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