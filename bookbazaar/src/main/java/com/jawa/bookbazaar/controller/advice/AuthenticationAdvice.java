package com.jawa.bookbazaar.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jawa.bookbazaar.error.ApiError;
import com.jawa.bookbazaar.exception.InsufficientUserCredentialsException;
import com.jawa.bookbazaar.exception.InvalidUserException;
import com.jawa.bookbazaar.exception.UnauthenticatedUserException;

@ControllerAdvice
public class AuthenticationAdvice extends ResponseEntityExceptionHandler{

	@ExceptionHandler(UnauthenticatedUserException.class)
	public ResponseEntity<ApiError> unAuthenticatedUser(UnauthenticatedUserException exception) {
		String message = "Logged in user id and requested user id not matching";
		return buildResponseEntity(new ApiError(HttpStatus.UNAUTHORIZED, message, exception));
	}
	
	@ExceptionHandler(InvalidUserException.class)
	public ResponseEntity<ApiError> invalidUser(InvalidUserException exception) {
		String message = "Invalid logging credentials";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message, exception));
	}
	
	@ExceptionHandler(InsufficientUserCredentialsException.class)
	public ResponseEntity<ApiError> badCredentials(InsufficientUserCredentialsException exception) {
		String message = "Signup credentials missing";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message, exception));
	}
	
	private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<ApiError>(apiError, apiError.getHttpStatus());
	}

	
}
