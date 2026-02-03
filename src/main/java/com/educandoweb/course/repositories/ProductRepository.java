package com.educandoweb.course.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.educandoweb.course.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	@Query(value= "SELECT obj FROM Product obj LEFT JOIN FETCH obj.categories")
	List<Product> findAll();
}
