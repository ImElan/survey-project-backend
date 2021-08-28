package com.accolite.survey.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "FormConfigCollection")
public class SurveyFormConfig {
	@Id
	private String id;
	
	@NotNull(message = "No. of days after which email is sent can't be empty")
	@Min(value = 1,message = "No. of days after which email is sent should be positive")
	@Max(value = 500,message = "No. of days after which email is sent should be less than 500")
	@Field("days")
	@Indexed(unique = true)
	private int daysAfterWhichEmailShouldBeSent;
	
	
	@NotNull
	@Min(value = 1,message = "Minimum no of question allowed should be atleast 1")
	@Field("minQuestion")
    private int minNUmberOfQuestionsAllowed;
	
	
	@NotNull
	@Min(value = 1,message = "Maximum no of questions should be positive")
	@Max(value = 100,message = "maxQuestion should be <= 100")
	@Field("maxQuestion")
    private int maxNumberOfQuestionsAllowed;
	
	@NotNull
	@Min(value = 1,message = "Reminder days should be positive")
	@Max(value = 100,message = "Reminder days should be less than 100")
	@Field("remind_days")
    private int remindAfterNumberOfDays; 
	
	
	@Field("remind_count")
	int maxReminderCount; // Max number of times to remind
    
   
	//Default Constructor
    public SurveyFormConfig() {

    }
	// Constructor
    public SurveyFormConfig(String id,int minNUmberOfQuestionsAllowed, int maxNumberOfQuestionsAllowed,int daysAfterWhichEmailShouldBeSent,int remindAfterNumberOfDays ) {
    	this.id = id;
        this.minNUmberOfQuestionsAllowed = minNUmberOfQuestionsAllowed;
        this.maxNumberOfQuestionsAllowed = maxNumberOfQuestionsAllowed;
        this.daysAfterWhichEmailShouldBeSent = daysAfterWhichEmailShouldBeSent;
        this.remindAfterNumberOfDays = remindAfterNumberOfDays;
    }
    
    
    public int getMaxReminderCount() {
		return maxReminderCount;
	}
	public void setMaxReminderCount(int maxReminderCount) {
		this.maxReminderCount = maxReminderCount;
	}
    
    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
    
    public int getMinNUmberOfQuestionsAllowed() {
        return minNUmberOfQuestionsAllowed;
    }
    
    public void setMinNUmberOfQuestionsAllowed(int minNUmberOfQuestionsAllowed) {
        this.minNUmberOfQuestionsAllowed = minNUmberOfQuestionsAllowed;
    }
    
    public int getMaxNumberOfQuestionsAllowed() {
        return maxNumberOfQuestionsAllowed;
    }
    
    public void setMaxNumberOfQuestionsAllowed(int maxNumberOfQuestionsAllowed) {
        this.maxNumberOfQuestionsAllowed = maxNumberOfQuestionsAllowed;
    }
    
    public int getDaysAfterWhichEmailShouldBeSent() {
        return daysAfterWhichEmailShouldBeSent;
    }
    
    public void setDaysAfterWhichEmailShouldBeSent(int daysAfterWhichEmailShouldBeSent) {
        this.daysAfterWhichEmailShouldBeSent = daysAfterWhichEmailShouldBeSent;
    }
    
    public int getRemindAfterNumberOfDays() {
        return remindAfterNumberOfDays;
    }
    
    public void setRemindAfterNumberOfDays(int remindAfterNumberOfDays) {
        this.remindAfterNumberOfDays = remindAfterNumberOfDays;
    }
    

}