package com.accolite.survey.service.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.Auth.AuthDAO;
import com.accolite.survey.Model.AuthResponse;
import com.accolite.survey.entity.User;

@Service
public class AuthServiceImplementation implements AuthService {

	@Autowired
	private AuthDAO authDao;
	
	@Override
	public ResponseEntity<AuthResponse> login(User user) {
		return authDao.login(user);
	}

	@Override
	public String authRouteCheck(String bearerToken) {
		return authDao.authRouteCheck(bearerToken);
	}
	
}
