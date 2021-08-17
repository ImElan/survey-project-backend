package com.accolite.survey.Model;

import org.springframework.stereotype.Component;

import com.accolite.survey.entity.User;

@Component
public class AuthResponse {
	private String tokenId;
	private User user;

	public AuthResponse() {
		
	}	
	
	public AuthResponse(String tokenId, User user) {
		this.tokenId = tokenId;
		this.user = user;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "AuthResponse [tokenId=" + tokenId + ", user=" + user + "]";
	}	
}
