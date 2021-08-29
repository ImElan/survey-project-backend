package com.accolite.survey.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document("Users")
public class User {
	@Id
	private String id;
	
	@Field(name = "name")
	private String name;
	
	@Indexed(unique = true)
	@Field(name = "email")
	private String email;
	
	@Field(name = "role")
	private UserRoles role;
	
	@Field(name = "googleId")
	private String googleId;
	
	@Field(name = "accessToken")
	private String accessToken;
	
	@Field(name = "accessTokenExpirationDate")
	private Date accessTokenExpirationDate;
	
	public User() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserRoles getRole() {
		return role;
	}

	public void setRole(UserRoles role) {
		this.role = role;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	@JsonIgnore
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	@JsonIgnore
	public Date getAccessTokenExpirationDate() {
		return accessTokenExpirationDate;
	}

	public void setAccessTokenExpirationDate(Date accessTokenExpirationDate) {
		this.accessTokenExpirationDate = accessTokenExpirationDate;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", role=" + role + ", googleId=" + googleId
				+ ", accessToken=" + accessToken + ", accessTokenExpirationDate=" + accessTokenExpirationDate + "]";
	}
}
