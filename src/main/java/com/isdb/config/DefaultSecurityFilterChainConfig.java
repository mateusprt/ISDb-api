package com.isdb.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class DefaultSecurityFilterChainConfig {
	
	@Autowired
	private EnsureAuthenticationFilter ensureAuthenticationFilter;
	
	@Bean
	public SecurityFilterChain configureDefaultSecurityFilterChain(HttpSecurity http) throws Exception  {
		http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
				
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedOrigins(List.of("*"));
					config.setAllowedMethods(List.of("*"));
					config.setAllowCredentials(true);
					config.setAllowedHeaders(List.of("Authorization"));
					config.setMaxAge(3600L);
					return config;
				}
			}))
			.authorizeHttpRequests(auth -> 
				auth.requestMatchers("/api/v1/auth/**")
					.permitAll()
					.anyRequest()
					.authenticated()
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(ensureAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
	
}
