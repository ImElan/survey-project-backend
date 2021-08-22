package com.accolite.survey.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="MailData")
public class MailData {
	
	@Id
	String id;
		
	String formid;
	String email;
	String url;
	String name;
	String last_sent_date;
	
	
	public String getFormid() {
		return formid;
	}
	public void setFormid(String formid) {
		this.formid = formid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLast_sent_date() {
		return last_sent_date;
	}
	public void setLast_sent_date(String last_sent_date) {
		this.last_sent_date = last_sent_date;
	}
	
	
	
	

}
