package com.isdb.services.implementations;

import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.isdb.dtos.auth.AuthenticationDto;
import com.isdb.dtos.auth.CreateUserDto;
import com.isdb.dtos.auth.ResponseAuthDto;
import com.isdb.dtos.auth.ResponseTokenDto;
import com.isdb.exceptions.InvalidRequiredFieldException;
import com.isdb.exceptions.ResourceNotFoundException;
import com.isdb.models.Role;
import com.isdb.models.User;
import com.isdb.repositories.UsersRepository;
import com.isdb.services.interfaces.JwtServiceInterface;
import com.isdb.services.interfaces.UsersServiceInterface;

@Service
public class UsersService implements UsersServiceInterface, UserDetailsService {
	
	private static Logger logger = Logger.getLogger(UsersService.class.toString());
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtServiceInterface jwtService;
	
	@Override
	public ResponseAuthDto createUser(CreateUserDto dto) {
		if(dto.getEmail() == "") {
			logger.warning("email can't be blank");
			throw new InvalidRequiredFieldException("email can't be blank");
		}
		
		logger.info("Find a user by email = " + dto.getEmail());
		User userFound = this.usersRepository.findByEmail(dto.getEmail()).orElse(null);
		
		if(userFound != null) {
			logger.warning("User already exists");
			throw new BadCredentialsException("email already exists");
		}
		
		logger.info("User not found");
		
		if(dto.getPassword() == "") {
			logger.warning("password can't be blank");
			throw new InvalidRequiredFieldException("password can't be blank");
		}
		
		logger.info("Check if password has required minimum lenght");
		boolean passwordValid = this.passwordHasRequiredMinimumLenght(dto.getPassword());
		
		if(!passwordValid) {
			logger.warning("passsword must be a minimum length of 5 characters");
			throw new BadCredentialsException("passsword must be a minimum length of 5 characters");
		}
		
		logger.info("Check if password and password_confirmation match");
		if(!dto.getPassword().equals(dto.getPasswordConfirmation())) {
			logger.warning("passsword and password_confirmation must be equals");
			throw new BadCredentialsException("passsword and password_confirmation must be equals");
		}
		
		User user = new User();
		user.setEmail(dto.getEmail());
		user.setPassword(this.hashPassword(dto.getPassword()));
		user.setConfirmationToken(this.generateUniqueToken());
		user.setConfirmationTokenSentAt(new Date());
		user.setUnconfirmed(true);
		user.setRole(Role.ADMIN);
		
		logger.info("Creating user");
		this.usersRepository.save(user);
		logger.info("User created successfully");
		
		ResponseAuthDto responseDto = new ResponseAuthDto("Conta criada com sucesso. Um link de confirmação foi enviado para o seu email.");
		
		return responseDto;
	}
	
	@Override
	public ResponseAuthDto confirmUser(String confirmationToken) {
		logger.info("Find user by confirmation_token");
		User userFound = this.usersRepository.findByConfirmationToken(confirmationToken).orElse(null);
		
		if(userFound == null) {
			logger.warning("User not found");
			throw new ResourceNotFoundException("User not found");
		}
		
		logger.info("User found");
		userFound.setConfirmationToken(null);
		userFound.setConfirmedAt(new Date());
		userFound.setUnconfirmed(false);
		
		logger.info("Updating user");
		this.usersRepository.save(userFound);
		logger.info("User updated");
		
		ResponseAuthDto responseDto = new ResponseAuthDto("Conta verificada com sucesso!");		
		return responseDto;
	}
	
	@Override
	public ResponseTokenDto authenticateUser(AuthenticationDto authenticationDto) {
		logger.info("Find user by email = " + authenticationDto.getEmail());
		User userFound = this.usersRepository.findByEmail(authenticationDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Email or password are incorrect"));
		
		logger.info("User found = " + "id=" + userFound.getId() + ", email = " + userFound.getEmail());
		
		logger.info("Check user is confirmed");
		if(userFound.getUnconfirmed()) {
			logger.warning("Account not confirmed");
			throw new BadCredentialsException("Email or password are incorrect");
		}
		
		logger.info("User confirmed");
		
		boolean passwordMatch = this.passwordMatch(userFound, authenticationDto.getPassword());
		
		if(!passwordMatch) {
			logger.warning("Password does not match");
			throw new BadCredentialsException("Email or password are incorrect");
		}
		
		logger.info("Generating JWT token");
		String token = this.jwtService.generateJwtToken(userFound);
		logger.info("JWT token generated successfully");
		ResponseTokenDto responseDto = new ResponseTokenDto(token);
		return responseDto;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userFound = this.usersRepository.findByEmail(username).orElseThrow(() -> new BadCredentialsException("User not found"));
		return userFound;
	}
	
	private boolean passwordHasRequiredMinimumLenght(String password) {
		return password.length() > 4;
	}
	
	private String hashPassword(String password) {
		return this.passwordEncoder.encode(password);
	}
	
	private boolean passwordMatch(User user, String password) {
		return this.passwordEncoder.matches(password, user.getPassword());
	}
	
	private String generateUniqueToken() {
		return UUID.randomUUID().toString();
	}
	
}
