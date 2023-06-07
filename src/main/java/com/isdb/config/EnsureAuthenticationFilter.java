package com.isdb.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.isdb.exceptions.ResourceNotFoundException;
import com.isdb.models.User;
import com.isdb.repositories.UsersRepository;
import com.isdb.services.implementations.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class EnsureAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UsersRepository usersRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		
		if(authHeader == null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		if(!authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = authHeader.substring(7);
		
		if(!this.jwtService.isValidToken(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		User userDetails = this.usersRepository.findByEmail(this.jwtService.extractUsername(token)).orElseThrow(() -> new ResourceNotFoundException("User not found"));
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getEmail(), null, userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);		
	}

}