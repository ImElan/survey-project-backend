package com.accolite.survey.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.accolite.survey.entity.MailData;

public interface MailDataService {
	public void saveMailData( MailData m);
	public List<MailData> getDatafromMD();

}
