package com.educandoweb.course.services.exceptions;

public class StockUpdateException extends RuntimeException{
    public StockUpdateException(String message, Throwable cause){
        super(message, cause);
    }
}
