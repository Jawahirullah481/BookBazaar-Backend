package com.jawa.bookbazaar.exception;

public class BookOutOfStockException extends RuntimeException{

	public BookOutOfStockException(String message) {
		super(message);
	}
	
}
