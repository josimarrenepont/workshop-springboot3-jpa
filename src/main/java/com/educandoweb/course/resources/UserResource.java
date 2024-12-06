package com.educandoweb.course.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import com.educandoweb.course.entities.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.educandoweb.course.entities.User;
import com.educandoweb.course.services.UserService;

@RestController
@RequestMapping(value = "/api/users")
public class UserResource {

	@Autowired
	private UserService service;

	@GetMapping
	public ResponseEntity<List<UserDto>> findAll() {
		List<User> list = service.findAll();
		List<UserDto> userDtos = list.stream().map(UserDto::new).collect(Collectors.toList());
		return ResponseEntity.ok().body(userDtos);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Object> findById(@PathVariable Long id) {
		User obj = service.findById(id);
		UserDto userDto = new UserDto(obj);
		return ResponseEntity.ok().body(userDto);
	}
	
	@PostMapping
	public ResponseEntity<UserDto> insert(@RequestBody UserDto userDto){
		User user = service.insert(userDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).body(new UserDto(user));
		
	}
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody UserDto userDto){
		User obj = service.update(id, userDto);
		UserDto dto = new UserDto(obj);
		return ResponseEntity.ok().body(dto);
	}
}