package com.accolite.survey.entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "FormConfigCollection")
public class SurveyFormConfig {
	@Id
	private String id;
	
	@Field("days")
	@Indexed(unique = true)
	private int daysAfterWhichEmailShouldBeSent;
	
	@Field("minQuestion")
    private int minNUmberOfQuestionsAllowed;
	
	@Field("maxQuestion")
    private int maxNumberOfQuestionsAllowed;
	
	@Field("remind_days")
    private int remindAfterNumberOfDays; 
    
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
    
    @Override
    public String toString() {
    	return "MinQuestion = " + minNUmberOfQuestionsAllowed + ", MaxQuestion ="
    			+ " " + maxNumberOfQuestionsAllowed + " Email Send After " + daysAfterWhichEmailShouldBeSent + " "
    					+ "days"  + " Reminder " + remindAfterNumberOfDays;
    }
    

}