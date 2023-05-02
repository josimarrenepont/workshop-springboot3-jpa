package com.educandoweb.course.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educandoweb.course.entities.User;

@RestController //
@RequestMapping (value = "/users") // 
public class UserResource  {

	@GetMapping
	public ResponseEntity<User> findAll(){ // VERIFICAR SE A CONEXÃO ESTÁ FUNCIONANDO
		 User u = new User(1L, "maria", "maria@gmail.com","12345", "9999-9999"); 
		 return ResponseEntity.ok().body(u);
	}
	
}
