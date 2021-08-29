package com.accolite.survey.DAO.Auth;

import org.springframework.http.ResponseEntity;

import com.accolite.survey.Model.AuthResponse;
import com.accolite.survey.entity.User;
import com.accolite.survey.entity.UserRoles;

public interface AuthDAO {
//	ResponseEntity<AuthResponse> login(User user);
	ResponseEntity<AuthResponse> loginWithGoogle(String idToken);
	ResponseEntity<AuthResponse> getAccessTokenUsingRefreshToken(String refreshToken);
	ResponseEntity<Object> logout(String bearerToken);
	String authRouteCheck(String bearerToken);
	String authRouteWithRolesCheck(String bearerToken);
	ResponseEntity<Object> grantAccess(User user, String bearerToken);
	ResponseEntity<Object> grantAccess(String[] emails, UserRoles role, String bearerToken); 
	User getUserByEmail(String email);
}
