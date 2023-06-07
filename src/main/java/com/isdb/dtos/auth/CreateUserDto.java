package com.isdb.dtos.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserDto {
	
	private String email;
	private String password;
	
	@JsonProperty("password_confirmation")
	private String passwordConfirmation;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
	
	
	
}
