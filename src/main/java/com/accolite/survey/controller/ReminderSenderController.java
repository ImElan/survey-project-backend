package com.accolite.survey.controller;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.accolite.survey.DAO.ResponsesDAO;
import com.accolite.survey.DAO.UserDAO;
import com.accolite.survey.entity.MailData;
import com.accolite.survey.entity.Responses;
import com.accolite.survey.entity.SurveyFormConfig;
import com.accolite.survey.entity.User;
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
@RequestMapping("/maildata")

public class ReminderSenderController {
	
	@Autowired
	MailDataService md;
	
	@Autowired
	UserDAO ud;
	
	@Autowired
	private JavaMailSender mailSender;
	
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
	
	
	//Add in MailData
	@PostMapping("/adddata")
	@ResponseBody
	public void saveMailData(@RequestBody MailData m){	
		md.saveMailData(m);	
	}
	
	
	//Fetch from Maildata
	@GetMapping("/getdata")
	@ResponseBody
	public List<MailData> getDatafrom()
	{
		
		return md.getDatafromMD();
		
	
	}
	
	
	
	@GetMapping("/getresponse")
	@ResponseBody
	public List<Responses> sendResponse() {
		
		return rs.getAllResponses();
		
	}
	
	
	
	@GetMapping("/sendremindermail")
	@ResponseBody
	public void sendreminder() throws Exception
	{
		List<MailData> l=md.getDatafromMD();
	    for(MailData i:l)
	    {
	    	String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
	    	String lsdate=i.getLast_sent_date();
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    	Calendar c = Calendar.getInstance();
	    	try{
	    	   
	    	   c.setTime(sdf.parse(lsdate));
	    	}catch(ParseException e){
	    		e.printStackTrace();
	    	 }
	    	   
	    	int remindDays = 10;
		    List<SurveyFormConfig> li=cs.getAllSurveyFormConfig();
		    for(SurveyFormConfig j:li)
		    {
		    	remindDays=j.getRemindAfterNumberOfDays();
		    }
	    	c.add(Calendar.DAY_OF_MONTH, remindDays);  
	    	String addDate = sdf.format(c.getTime());
	    	
//	    	System.out.println(today);
//	    	System.out.println(addDate);
//	    	System.out.println(remindDays);
	    	
	    	if(addDate.equals(today))
	    	{    		
	    		String formid=i.getFormid();
	    		String email=i.getEmail();
	    		String name=i.getName();
	    		
	    		User u=ud.getByEmail(email);
	    		String uid=u.getId();
	    		
	    		Optional<Responses> r=rd.findByFormIdandUserId(formid, uid);
//	    		System.out.println(r);
	    		if(r.isEmpty())
	    		{
	    			System.out.println(sendHTMLEmailWithAttachment(email,formid,name));
	    			i.setLast_sent_date(today);
	    			md.saveMailData(i);
	    		}
	    		
	    		
	    	}	
	    	
	    	
	    	
	    }
	}
	
	
	
	
	
	public String sendHTMLEmailWithAttachment(String to,String formid,String name) throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, freemarker.core.ParseException, IOException, TemplateException{
		
		
		
		
		String from = "accolite.survey@gmail.com";
//		String to = "prasana.seshadri@gmail.com";
		
		String URL="accolite.survey.com/forms/"+formid;
		
		Map<String, Object> m = new HashMap<>();
		m.put("Name", name);
		m.put("URL", URL);
				
		Template t = config.getTemplate("reminder.html");
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, m);
		
		
		

		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setSubject("Reminder Alert!!!!!");
		helper.setFrom(from);
		helper.setTo(to);
		helper.setText(html,true);
		
//		FileSystemResource file = new FileSystemResource(new File("g:\\MyEbooks\\Freelance for Programmers\\SuccessFreelance-Preview.pdf"));
//		helper.addAttachment("FreelanceSuccess.pdf", file);

		mailSender.send(message);
		
//		model.addAttribute("message", "Reminder mail sent");
		return "Reminder mail sent";
	}
	

}
