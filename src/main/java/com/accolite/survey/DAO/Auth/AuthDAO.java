package com.accolite.survey.DAO.Auth;

import org.springframework.http.ResponseEntity;

import com.accolite.survey.Model.AuthResponse;
import com.accolite.survey.entity.User;

public interface AuthDAO {
	ResponseEntity<AuthResponse> login(User user);
	String authRouteCheck(String bearerToken);
	String authRouteWithRolesCheck(String bearerToken);
}
