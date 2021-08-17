package com.accolite.survey.service.Auth;

import org.springframework.http.ResponseEntity;

import com.accolite.survey.Model.AuthResponse;
import com.accolite.survey.entity.User;

public interface AuthService {
	public ResponseEntity<AuthResponse> login(User user);

	public String authRouteCheck(String bearerToken);
}
