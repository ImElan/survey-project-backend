package com.accolite.survey.service.Auth;

import org.springframework.http.ResponseEntity;

import com.accolite.survey.Model.AuthResponse;

public interface AuthService {
//	ResponseEntity<AuthResponse> login(User user);
	ResponseEntity<AuthResponse> loginWithGoogle(String bearerToken);
	String authRouteCheck(String bearerToken);
	String authRouteWithRolesCheck(String bearerToken);
}
