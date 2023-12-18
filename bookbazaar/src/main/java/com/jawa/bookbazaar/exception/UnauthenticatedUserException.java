package com.jawa.bookbazaar.exception;

public class UnauthenticatedUserException extends RuntimeException {

	public UnauthenticatedUserException(String errorMessage) {
		super(errorMessage);
	}
}
