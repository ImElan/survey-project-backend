package com.accolite.survey.service.Auth;

import org.springframework.http.ResponseEntity;

import com.accolite.survey.Model.AuthResponse;
import com.accolite.survey.entity.User;

public interface AuthService {
	ResponseEntity<AuthResponse> login(User user);
	String authRouteCheck(String bearerToken);
	String authRouteWithRolesCheck(String bearerToken);
}
