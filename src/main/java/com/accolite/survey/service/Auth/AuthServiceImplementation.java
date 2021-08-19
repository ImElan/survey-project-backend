package com.accolite.survey.service.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.Auth.AuthDAO;
import com.accolite.survey.Model.AuthResponse;
import com.accolite.survey.entity.UserRoles;

@Service
public class AuthServiceImplementation implements AuthService {

	@Autowired
	private AuthDAO authDao;
	
//	@Override
//	public ResponseEntity<AuthResponse> login(User user) {
//		return authDao.login(user);
//	}

	@Override
	public String authRouteCheck(String bearerToken) {
		return authDao.authRouteCheck(bearerToken);
	}

	@Override
	public String authRouteWithRolesCheck(String bearerToken) {
		return authDao.authRouteWithRolesCheck(bearerToken);
	}

	@Override
	public ResponseEntity<AuthResponse> loginWithGoogle(String bearerToken) {
		return authDao.loginWithGoogle(bearerToken);
	}

	@Override
	public ResponseEntity<Object> grantAccess(String[] emails, UserRoles role, String bearerToken) {
		return authDao.grantAccess(emails, role, bearerToken);
	}
	
}
