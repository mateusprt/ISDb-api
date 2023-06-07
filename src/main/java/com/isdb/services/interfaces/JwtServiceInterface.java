package com.isdb.services.interfaces;

import com.isdb.models.User;

public interface JwtServiceInterface {
	
	String generateJwtToken(User user);
	
}
