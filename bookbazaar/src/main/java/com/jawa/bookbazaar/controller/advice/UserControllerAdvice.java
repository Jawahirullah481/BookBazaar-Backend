package com.jawa.bookbazaar.controller.advice;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jawa.bookbazaar.error.ApiError;
import com.jawa.bookbazaar.exception.AddressNotAvailableException;
import com.jawa.bookbazaar.exception.BookOutOfStockException;
import com.jawa.bookbazaar.exception.DuplicateUserException;
import com.jawa.bookbazaar.exception.InvalidBookIsbnException;
import com.jawa.bookbazaar.exception.InvalidBookNameException;
import com.jawa.bookbazaar.exception.InvalidBookPriceException;
import com.jawa.bookbazaar.exception.InvalidOrderIdException;
import com.jawa.bookbazaar.exception.InvalidOrderStatusException;
import com.jawa.bookbazaar.exception.InvalidUserIdException;
import com.jawa.bookbazaar.exception.OrderNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;


@ControllerAdvice
public class UserControllerAdvice extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(DuplicateUserException.class)
	public ResponseEntity<Object> duplicateUser(DuplicateUserException exception) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception);
		apiError.setMessage("username or email already exist for this website. Try another one");
		return buildResponseEntity(apiError);
	}
	

	@ExceptionHandler(InvalidBookIsbnException.class)
	public ResponseEntity<Object> invalidBookIsbn(InvalidBookIsbnException exception) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, exception);
		apiError.setMessage("Invalid Book ISBN Number");
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(BookOutOfStockException.class)
	public ResponseEntity<Object> bookOutOfStock(BookOutOfStockException exception) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, exception);
		apiError.setMessage("Unable to add book to cart. It's out of stock");
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(InvalidBookNameException.class)
	public ResponseEntity<Object> invalidBookName(InvalidBookIsbnException exception) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, exception);
		apiError.setMessage("Invalid Book Name");
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(InvalidBookPriceException.class)
	public ResponseEntity<Object> invalidBookPrice(InvalidBookPriceException exception) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, exception);
		apiError.setMessage("Invalid Book Price");
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(AddressNotAvailableException.class)
	public ResponseEntity<Object> addressUnavailable(AddressNotAvailableException exception) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, exception);
		apiError.setMessage("Address Not Available");
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<Object> orderOutOfStock(OrderNotFoundException exception) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, exception);
		apiError.setMessage("Invalid order id");
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(InvalidOrderStatusException.class)
	public ResponseEntity<Object> orderOutOfStock(InvalidOrderStatusException exception) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, exception);
		apiError.setMessage("Invalid order id");
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(InvalidUserIdException.class)
	public ResponseEntity<Object> orderOutOfStock(InvalidUserIdException exception) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, exception);
		apiError.setMessage("Invalid user id");
		return buildResponseEntity(apiError);
	}
	
	@ExceptionHandler(InvalidOrderIdException.class)
	public ResponseEntity<Object> orderOutOfStock(InvalidOrderIdException exception) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, exception);
		apiError.setMessage("Invalid order id");
		return buildResponseEntity(apiError);
	}
	
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String error = ex.getParameterName() + " parameter is missing";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
		return buildResponseEntity(
				new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), ex));
	}
	
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
		apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
		return buildResponseEntity(apiError);
	}


	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage("Validation error");
		apiError.addValidationErrors(ex.getConstraintViolations());
		return buildResponseEntity(apiError);
	}

	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String error = "Malformed JSON request";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
	}
	
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String error = "Error writing JSON output";
		return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
	}

	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(
				String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> entityNotFound(EntityNotFoundException exception) {
		return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, exception));
	}
	
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
			WebRequest request) {
		if (ex.getCause() instanceof ConstraintViolationException) {
			return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
		}
		return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
	}

	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
		apiError.setDebugMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}
	

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getHttpStatus());
	}

}
