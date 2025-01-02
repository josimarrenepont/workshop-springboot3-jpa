package com.educandoweb.course.controller.exceptions;

import java.time.Instant;

import com.educandoweb.course.services.exceptions.InsufficientStockException;
import com.educandoweb.course.services.exceptions.StockUpdateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.educandoweb.course.services.exceptions.DatabaseException;
import com.educandoweb.course.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resouceNotFound(ResourceNotFoundException e, HttpServletRequest request) {

		String error = "Resource not found";
		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> dataBase(DatabaseException e, HttpServletRequest request) {

		String error = "Database Error";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<StandardError> insufficientStock(InsufficientStockException e, HttpServletRequest request){
		String error = "Insufficient Stock";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError stockError = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(stockError);
	}
	@ExceptionHandler(StockUpdateException.class)
	public ResponseEntity<StandardError> stockUpdate(StockUpdateException e, HttpServletRequest request){
		String error = "Stock update error";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError StockUpdateError = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
		return ResponseEntity.status(status).body(StockUpdateError);
	}
}
