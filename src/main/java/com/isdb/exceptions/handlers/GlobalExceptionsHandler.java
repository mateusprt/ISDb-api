package com.isdb.exceptions.handlers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.isdb.exceptions.InvalidRequiredFieldException;
import com.isdb.exceptions.ResourceAlreadyExistsException;
import com.isdb.exceptions.ResourceNotFoundException;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionsHandler {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseException> handleAllExceptions(Exception e, WebRequest request) {
		ResponseException response = new ResponseException(List.of("Internal server error"), request.getDescription(false));
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
	public ResponseEntity<ResponseException> handleInvalidDataAccessApiUsageExceptions(InvalidDataAccessApiUsageException e, WebRequest request) {
		ResponseException response = new ResponseException(List.of("Request body malformed"), request.getDescription(false));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ResponseException> handleConstraintViolationExceptions(ConstraintViolationException e, WebRequest request) {
		List<String> errors = new ArrayList<>();
        e.getConstraintViolations().forEach(violation -> {
            errors.add(violation.getMessage());
        });
        ResponseException exceptionResponse = new ResponseException(errors, request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ResponseException> handleResourceAlreadyExistsExceptions(ResourceAlreadyExistsException e, WebRequest request) {
		ResponseException exceptionResponse = new ResponseException(List.of(e.getMessage()), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResponseException> handleResourcenotFoundExceptionExecptions(ResourceNotFoundException e, WebRequest request) {
		ResponseException exceptionResponse = new ResponseException(List.of(e.getMessage()), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidRequiredFieldException.class)
	public ResponseEntity<ResponseException> handleInvalidRequiredFieldExceptions(InvalidRequiredFieldException e, WebRequest request) {
		ResponseException exceptionResponse = new ResponseException(List.of(e.getMessage()), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ResponseException> handleBadCredentialsExceptions(BadCredentialsException e, WebRequest request) {
		ResponseException exceptionResponse = new ResponseException(List.of(e.getMessage()), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	

}
