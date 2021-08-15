package com.accolite.survey.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;





public class Composite_primary_key implements Serializable {

	@Id
	private Composite_primary_key id;
	private int studentId;
	private int groupId;

	        // getters and setters

}
