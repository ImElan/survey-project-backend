package com.accolite.survey.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

//entity class for receiving the data inside the variables
@Document(collection="demo_data")
public class demo_data {

	@Id
	private String id;
	@Field("fname")
	private String fname;
	@Field("lname")
	private String lname;
	
	public demo_data() {}
	
	public demo_data(String id, String fname, String lname) {
		this.id = id;
		this.fname = fname;
		this.lname = lname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	
}
