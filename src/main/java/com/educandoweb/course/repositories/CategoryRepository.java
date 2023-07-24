package com.educandoweb.course.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.educandoweb.course.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	@Query(value= "SELECT obj FROM Category obj JOIN FETCH obj.products")
	List<Category> searchAll();
	
}
