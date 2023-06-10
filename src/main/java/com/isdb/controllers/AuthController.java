package com.isdb.controllers;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.isdb.dtos.auth.AuthenticationDto;
import com.isdb.dtos.auth.CreateUserDto;
import com.isdb.dtos.auth.ResponseAuthDto;
import com.isdb.dtos.auth.ResponseTokenDto;
import com.isdb.services.interfaces.UsersServiceInterface;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	private static Logger logger = Logger.getLogger(AuthController.class.toString());
	
	@Autowired
	private UsersServiceInterface usersService;
	
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ResponseAuthDto> createUser(@RequestBody CreateUserDto dto) {
		logger.info("Create user");
		ResponseAuthDto response = this.usersService.createUser(dto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/confirmation/{confirmationToken}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseAuthDto> confirmationUser(@PathVariable("confirmationToken") String confirmationToken) {
		logger.info("Confirm user");
		ResponseAuthDto response = this.usersService.confirmUser(confirmationToken);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseTokenDto> loginUser(@RequestBody AuthenticationDto dto) {
		logger.info("Authenticate user");
		ResponseTokenDto response = this.usersService.authenticateUser(dto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}
