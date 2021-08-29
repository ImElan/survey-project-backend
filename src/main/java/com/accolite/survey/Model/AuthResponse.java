package com.accolite.survey.Model;

import org.springframework.stereotype.Component;

import com.accolite.survey.entity.User;

@Component
public class AuthResponse {
	private Token accessToken;
	private Token refreshToken;
	private User user;

	public AuthResponse() {
		
	}

	public AuthResponse(Token accessToken, Token refreshToken, User user) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.user = user;
	}

	public Token getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(Token accessToken) {
		this.accessToken = accessToken;
	}

	public Token getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(Token refreshToken) {
		this.refreshToken = refreshToken;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "AuthResponse [accessToken=" + accessToken + ", refreshToken=" + refreshToken + ", user=" + user + "]";
	}	
}
