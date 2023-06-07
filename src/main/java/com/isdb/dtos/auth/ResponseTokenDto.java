package com.isdb.dtos.auth;

public class ResponseTokenDto {
	
	private String token;

	public ResponseTokenDto() {
	}

	public ResponseTokenDto(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	

}
