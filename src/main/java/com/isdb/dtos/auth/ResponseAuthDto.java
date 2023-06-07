package com.isdb.dtos.auth;

public class ResponseAuthDto {
	
	private String message;
	
	public ResponseAuthDto() {
	}
	
	public ResponseAuthDto(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
