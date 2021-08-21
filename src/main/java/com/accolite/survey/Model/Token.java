package com.accolite.survey.Model;

import java.util.Date;

public class Token {
	private String tokenId;
	private Date expirationDate;
	
	public Token(String tokenId, Date expiration) {
		this.tokenId = tokenId;
		this.expirationDate = expiration;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public Date getExpiration() {
		return expirationDate;
	}

	public void setExpiration(Date expiration) {
		this.expirationDate = expiration;
	}

	@Override
	public String toString() {
		return "Token [tokenId=" + tokenId + ", expiration=" + expirationDate + "]";
	}
}
