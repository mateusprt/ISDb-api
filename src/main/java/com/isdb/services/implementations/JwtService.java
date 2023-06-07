package com.isdb.services.implementations;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.isdb.exceptions.ResourceNotFoundException;
import com.isdb.models.User;
import com.isdb.repositories.UsersRepository;
import com.isdb.services.interfaces.JwtServiceInterface;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService implements JwtServiceInterface {
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Value("${security.jwt.secret}")
	private  String SECRET;

	private Long JWT_EXPIRATION = 86400000L;

	@Override
	public String generateJwtToken(User user) {
		return Jwts
				.builder()
				.setIssuer("api")
				.setSubject(user.getEmail())
				.claim("authorities", user.getAuthorities())
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION))
				.signWith(this.getKey())
				.compact();
	}
	
	public boolean isValidToken(String token) {
		User unconfirmedAcount = this.usersRepository.findByEmail(this.extractUsername(token)).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		boolean confirmedAccount = !unconfirmedAcount.getUnconfirmed();
		boolean tokenNotExpired = !isTokenExpired(token);
		return confirmedAccount && tokenNotExpired;
	}
	
	private boolean isTokenExpired(String token) {
		return this.extractExpirationDate(token).before(new Date());
	}
	
	public String extractUsername(String token) {
		return this.extractClaim(token, Claims::getSubject);
	}
	
	private Date extractExpirationDate(String token) {
		return this.extractClaim(token, Claims::getExpiration);
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = this.extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts
					.parserBuilder()
					.setSigningKey(this.getKey())
					.build()
					.parseClaimsJws(token)
					.getBody();
	}
	
	private Key getKey() {
		byte[] secretInBytes = this.SECRET.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(secretInBytes);
	}

}
