package com.isdb.exceptions;

public class InvalidRequiredFieldException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidRequiredFieldException(String message) {
		super(message);
	}
	
}
