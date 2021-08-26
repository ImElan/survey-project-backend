package com.accolite.survey.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.survey.Model.AuthResponse;
import com.accolite.survey.Model.GrantAccessBody;
import com.accolite.survey.entity.User;
import com.accolite.survey.service.Auth.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

	@Autowired
	private AuthService authService;
	
//	@PostMapping("/login")
//	public ResponseEntity<AuthResponse> login(@RequestBody User user) {
//		return authService.login(user);
//	}
	
	@GetMapping("/login/oauth/google")
	public ResponseEntity<AuthResponse> loginWithGoogle(@RequestHeader("Authorization") String idToken) {
		return authService.loginWithGoogle(idToken);
	}
	
	@GetMapping("/refreshToken/getToken")
	public ResponseEntity<AuthResponse> getAccessTokenUsingRefreshToken(@RequestHeader("Authorization") String refreshToken) {
		return authService.getAccessTokenUsingRefreshToken(refreshToken);
	}
	
	@GetMapping("/logout") 
	public ResponseEntity<Object> logout(@RequestHeader("Authorization") String bearedToken) {
		return authService.logout(bearedToken);
	}
	
	@GetMapping("/authenticatedRouteCheck")
	public String authenticatedRouteCheck(@RequestHeader("Authorization") String bearerToken) {
		return authService.authRouteCheck(bearerToken);
	}
	
	@GetMapping("/authenticatedRouteWithRolesCheck")
	public String authenticatedRouteWithRolesCheck(@RequestHeader("Authorization") String bearerToken) {
		return authService.authRouteWithRolesCheck(bearerToken);
	}
	
	@PostMapping("/grantAccess")
	public ResponseEntity<Object> grantAccess(@RequestBody User user,@RequestHeader("Authorization") String bearerToken) {
		return authService.grantAccess(user, bearerToken);
	}
	
	@PostMapping("/admin/grantAccess")
	public ResponseEntity<Object> grantAccess(@RequestBody GrantAccessBody body, @RequestHeader("Authorization") String bearerToken) {	
		return authService.grantAccess(body.getEmails(), body.getRole(), bearerToken);
	}
	
}
