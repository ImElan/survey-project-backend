package com.accolite.survey.entity;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="MailData")
public class MailData {

@Id
String id;

String formid;
String doj;
String empcode;
String acc;
String bu;
String email;
String url;
String name;
String last_sent_date;

boolean showform;
int remindercount;

String from_date;
String to_date;
int no_of_days_after_mail;
String tomailid[];

//Date last_sent_date2;
//int remindAfterNumberOfDays;
//int  maxReminderCount;


//public int getRemindAfterNumberOfDays() {
//return remindAfterNumberOfDays;
//}
//public void setRemindAfterNumberOfDays(int remindAfterNumberOfDays) {
//this.remindAfterNumberOfDays = remindAfterNumberOfDays;
//}
//public int getMaxReminderCount() {
//return maxReminderCount;
//}
//public void setMaxReminderCount(int maxReminderCount) {
//this.maxReminderCount = maxReminderCount;
//}
//public Date getLast_sent_date2() {
//return last_sent_date2;
//}
//public void setLast_sent_date2(Date last_sent_date2) {
//this.last_sent_date2 = last_sent_date2;
//}
public String getDoj() {
return doj;
}
public void setDoj(String doj) {
this.doj = doj;
}
public String getEmpcode() {
return empcode;
}
public void setEmpcode(String empcode) {
this.empcode = empcode;
}
public String getAcc() {
return acc;
}
public void setAcc(String acc) {
this.acc = acc;
}
public String getBu() {
return bu;
}
public void setBu(String bu) {
this.bu = bu;
}
public String getUrl() {
return url;
}
public void setUrl(String url) {
this.url = url;
}
public String getFrom_date() {
return from_date;
}
public void setFrom_date(String from_date) {
this.from_date = from_date;
}
public String getTo_date() {
return to_date;
}
public void setTo_date(String to_date) {
this.to_date = to_date;
}
public int getNo_of_days_after_mail() {
return no_of_days_after_mail;
}
public void setNo_of_days_after_mail(int no_of_days_after_mail) {
this.no_of_days_after_mail = no_of_days_after_mail;
}
public String[] getTomailid() {
return tomailid;
}
public void setTomailid(String[] tomailid) {
this.tomailid = tomailid;
}

public String getFormid() {
return formid;
}
public int getRemindercount() {
return remindercount;
}
public void setRemindercount(int remindercount) {
this.remindercount = remindercount;
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
public boolean isShowform() {
return showform;
}
public void setShowform(boolean showform) {
this.showform = showform;
}
public void setEmail(String email) {
this.email = email;
}


public String getLast_sent_date() {
return last_sent_date;
}
public void setLast_sent_date(String last_sent_date) {
this.last_sent_date = last_sent_date;
}





}