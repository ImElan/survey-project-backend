package com.accolite.survey.controller;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accolite.survey.DAO.MailDataDAO;
import com.accolite.survey.DAO.ResponsesDAO;
import com.accolite.survey.DAO.UserDAO;
import com.accolite.survey.DAO.Auth.AuthDAOImplementation;
import com.accolite.survey.Model.TokenType;
import com.accolite.survey.entity.MailData;
import com.accolite.survey.entity.Responses;
import com.accolite.survey.entity.SurveyFormConfig;
import com.accolite.survey.entity.User;
import com.accolite.survey.entity.UserRoles;
import com.accolite.survey.service.ConfigFormService;

import com.accolite.survey.service.MailDataService;
import com.accolite.survey.service.ResponsesService;

import com.accolite.survey.service.UserService;

import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;


@Controller

//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/accolite")
public class ReminderSenderController {
	@Autowired
	AuthDAOImplementation authdao;

	@Autowired
	MailDataService ms;

	@Autowired
	UserDAO ud;

	@Autowired
	MailDataDAO md;

	@Autowired
	ResponsesService rs;

	@Autowired
	ResponsesDAO rd;

	@Autowired
	UserService us;

	@Autowired
	ConfigFormService cs;

	@Autowired
	private Configuration config;


	@Autowired
	private JavaMailSender mailSender;




	@GetMapping("/senddetails/{formid}")
	@ResponseBody
	String returnDetails(@PathVariable String formid) throws Exception
	{

		JSONArray ja = new JSONArray();
		int remindDays = 10;
		int maxCount=3;

		List<SurveyFormConfig> li=cs.getAllSurveyFormConfig();
		for(SurveyFormConfig j:li)
		{
			remindDays=j.getRemindAfterNumberOfDays();
			maxCount=j.getMaxReminderCount();
		}

		List<MailData> l=md.getByIdandCount(formid,maxCount+1);
		
		for(MailData i : l)
		{
			
			String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			String lsdate=i.getLast_sent_date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar c = Calendar.getInstance();
			try
			{

				c.setTime(sdf.parse(lsdate));
			}
			catch(ParseException e)
			{
				e.printStackTrace();
			}

			c.add(Calendar.DAY_OF_MONTH, remindDays);  
			String addDate = sdf.format(c.getTime());

			int count=i.getRemindercount();

			Date d1=sdf.parse(addDate);
			Date d2=sdf.parse(today);

//			System.out.println(d1);
//			System.out.println(d2);

			if(count<maxCount)
			{

				if(d1.compareTo(d2)<=0)
				{    		
//					String formid=i.getFormid();
//					String url=i.getUrl();

					String email=i.getEmail();
					String name=i.getName();
					String bu=i.getBu();
					String acc=i.getAcc();
					String convdoj=i.getDoj();
					String empcode=i.getEmpcode();

					User u=ud.getByEmail(email);
					Optional<Responses> r = null;
					if(u!=null)
					{
						String uid=u.getId();
						r=rd.findByFormIdandUserId(formid, uid);
					}
					if(r==null || u==null || r.isEmpty() )
					{
						JSONObject jsonObject = new JSONObject();

						jsonObject.put("Employee Name", name);
						jsonObject.put("Email Mail ID", email);
						jsonObject.put("Employee DOJ", convdoj);
						jsonObject.put("Employee Code", empcode);
						jsonObject.put("Employee BU", bu);
						jsonObject.put("Employee Account", acc);

						ja.put(jsonObject);
					}
				}

			}
			else
			{
				Date d3=sdf.parse(lsdate);
				System.out.println(d3.compareTo(d2));
				if(d3.compareTo(d2)<0)
				{
					i.setShowform(false);
					ms.saveMailData(i);
				}
			}


		}

		return ja.toString();
	}


	@PostMapping("/send_reminder")
	@ResponseBody
	public void sendMail(@RequestBody ReminderSupport rs) throws Exception
	{
		String formid=rs.getFormid();

		String toemails[]=rs.getToemails();


		System.out.println(formid);

		for(String i : toemails)
		{
			
			MailData m=md.getByIdandMail(formid,i);

			String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			
			String name=m.getName();
			int count=m.getRemindercount();



			int maxCount=10;

			List<SurveyFormConfig> li=cs.getAllSurveyFormConfig();
			for(SurveyFormConfig j:li)
			{
				maxCount=j.getMaxReminderCount();
			}

			System.out.println(sendHTMLEmailWithAttachment(i,formid,name,count,maxCount,today));
			m.setLast_sent_date(today);
			m.setRemindercount(count+1);
			ms.saveMailData(m);

		}
	}




















	//	@GetMapping("/sendremindermail")
	//	@ResponseBody
	//	public void sendreminder() throws Exception
	//	{
	//		List<MailData> l=md.getDatafromMD();
	//	    for(MailData i:l)
	//	    {
	//	    	String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
	//	    	String lsdate=i.getLast_sent_date();
	//	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	//	    	Calendar c = Calendar.getInstance();
	//	    	try{
	//	    	   
	//	    	   c.setTime(sdf.parse(lsdate));
	//	    	}catch(ParseException e){
	//	    		e.printStackTrace();
	//	    	 }
	//	    	   
	//	    	int remindDays = 10;
	//	    	int maxCount=3;
	//	    	
	//	    	
	//		    List<SurveyFormConfig> li=cs.getAllSurveyFormConfig();
	//		    for(SurveyFormConfig j:li)
	//		    {
	//		    	remindDays=j.getRemindAfterNumberOfDays();
	//		    	maxCount=j.getMaxReminderCount();
	//		    }
	//	    	c.add(Calendar.DAY_OF_MONTH, remindDays);  
	//	    	String addDate = sdf.format(c.getTime());
	//	    	
	//	    	int count=i.getRemindercount();
	//	    	
	//	    	
	//	    	
	//	    	if(count<maxCount)
	//	    	{
	//	    	if(addDate.equals(today))
	//	    	{    		
	//	    		String formid=i.getFormid();
	//	    		String email=i.getEmail();
	//	    		String name=i.getName();
	//	    		
	//	    		User u=ud.getByEmail(email);
	//	    		String uid=u.getId();
	//	    		
	//	    		Optional<Responses> r=rd.findByFormIdandUserId(formid, uid);
	////	    		System.out.println(r);
	//	    		if(r.isEmpty())
	//	    		{
	//	    			System.out.println(sendHTMLEmailWithAttachment(email,formid,name,count,maxCount,today));
	//	    			i.setLast_sent_date(today);
	//	    			i.setRemindercount(count+1);
	//	    			md.saveMailData(i);
	//	    		}
	//	    		
	//	    		
	//	    	}
	//	    	}
	//	    	else
	//	    	{
	//	    		i.setShowform(false);
	//	    		md.saveMailData(i);
	//	    	}
	//	    	
	//	    	
	//	    	
	//	    }
	//	}
	//	
	//	
	//	
	//	
	//	
	public String sendHTMLEmailWithAttachment(String to,String formid,String name,int count,int maxCount,String today) throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, freemarker.core.ParseException, IOException, TemplateException
	{




		String from = "accolite.survey@gmail.com";
		//		String tow = "nandini.sharma@accolitedigital.com";

		String URL="http://localhost:3000/forms/"+formid;

		Map<String, Object> m = new HashMap<>();
		m.put("Name", name);
		m.put("URL", URL);
		m.put("Date",today);
		Template t = null;
		if(count!=(maxCount-1))
		{
			t = config.getTemplate("reminder.html");
		}
		else
		{
			t = config.getTemplate("final_reminder.html");
		}

		String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, m);





		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		if(count!=(maxCount-1))
		{
			helper.setSubject("Gentle Reminder");
		}
		else
		{
			helper.setSubject("Final Reminder");
		}


		helper.setFrom(from);
		helper.setTo(to);
		helper.setText(html,true);

		//		FileSystemResource file = new FileSystemResource(new File("g:\\MyEbooks\\Freelance for Programmers\\SuccessFreelance-Preview.pdf"));
		//		helper.addAttachment("FreelanceSuccess.pdf", file);

		mailSender.send(message);

		return "Reminder mail sent to "+to;
	}










	@GetMapping("/showform/{formid}")
	@ResponseBody
	boolean showForm(@PathVariable String formid,@RequestHeader("Authorization") String bearerToken )
	{
		System.out.println(formid);

		User u = authdao.isAuthenticated(bearerToken,TokenType.ACCESS);
		String email=u.getEmail();
		MailData x=md.getByIdandMail(formid, email);
		return x.isShowform();
	}



}
