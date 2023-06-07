package com.isdb.services.interfaces;

import com.isdb.dtos.auth.AuthenticationDto;
import com.isdb.dtos.auth.CreateUserDto;
import com.isdb.dtos.auth.ResponseAuthDto;
import com.isdb.dtos.auth.ResponseTokenDto;

public interface UsersServiceInterface {
	
	ResponseAuthDto createUser(CreateUserDto dto);
	ResponseAuthDto confirmUser(String confirmationToken);
	ResponseTokenDto authenticateUser(AuthenticationDto authenticationDto);
	
}
