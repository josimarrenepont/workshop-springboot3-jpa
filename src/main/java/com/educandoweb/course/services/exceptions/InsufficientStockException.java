package com.educandoweb.course.services.exceptions;

public class InsufficientStockException extends RuntimeException{
        public InsufficientStockException(String message){
            super(message);
        }
}
