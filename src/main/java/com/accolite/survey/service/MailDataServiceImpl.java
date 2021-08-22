package com.accolite.survey.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accolite.survey.DAO.MailDataDAO;
import com.accolite.survey.entity.MailData;

@Service
public class MailDataServiceImpl implements MailDataService {
	
	@Autowired
	MailDataDAO md;

	//Add in MailData
		
		public void saveMailData(MailData m){	
			md.save(m);	
		}
		
		//Fetch All from MailData
		public List<MailData> getDatafromMD()
		{
			
			return md.findAll();
			
		}
	
}
