package com.accolite.survey.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Document("user")
public class User {
	
	@Id
	@Field(name = "employeeId")
	private String employeeId;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Field(name = "password")
	private String password;
	
	@Field(name = "name")
	private String name;
	
	@Indexed(unique = true)
	@Field(name = "email")
	private String email;
	
	@Field(name = "role")
	private String role;
	
	@Field(name = "dateOfJoining")
	private String dateOfJoining;
	
	public User(String employeeId, String password, String name, String email, String role, String dateOfJoining) {
		this.employeeId = employeeId;
		this.password = password;
		this.name = name;
		this.email = email;
		this.role = role;
		this.dateOfJoining = dateOfJoining;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getDateOfJoining() {
		return dateOfJoining;
	}
	public void setDateOfJoining(String dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	@Override
	public String toString() {
		return "User [employeeId=" + employeeId + ", password=" + password + ", name=" + name + ", email=" + email
				+ ", role=" + role + ", dateOfJoining=" + dateOfJoining + "]";
	}
}
