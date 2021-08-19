package com.accolite.survey.Model;

import org.springframework.stereotype.Component;

import com.accolite.survey.entity.User;

@Component
public class AuthResponse {
	private Token token;
	private User user;

	public AuthResponse() {
		
	}	
	
	public AuthResponse(Token token, User user) {
		this.token = token;
		this.user = user;
	}

	public Token getTokenId() {
		return token;
	}

	public void setTokenId(Token token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "AuthResponse [token=" + token + ", user=" + user + "]";
	}
}
