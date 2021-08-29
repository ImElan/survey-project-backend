package com.accolite.survey.Model;

import java.util.Arrays;

import com.accolite.survey.entity.UserRoles;

public class GrantAccessBody {
	private String[] emails;
	private UserRoles role;
	
	public GrantAccessBody(String[] emails, UserRoles role) {
		this.emails = emails;
		this.role = role;
	}

	public String[] getEmails() {
		return emails;
	}

	public void setEmails(String[] emails) {
		this.emails = emails;
	}

	public UserRoles getRole() {
		return role;
	}

	public void setRole(UserRoles role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "GrantAccessBody [emails=" + Arrays.toString(emails) + ", role=" + role + "]";
	}
}
