package com.accolite.survey.controller;


import java.io.FileInputStream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.accolite.survey.entity.MailData;
import com.accolite.survey.entity.SurveyFormConfig;
import com.accolite.survey.service.ConfigFormService;
import com.accolite.survey.service.FormService;
import com.accolite.survey.service.MailDataService;
import com.accolite.survey.service.ResponsesService;
import com.accolite.survey.service.UserService;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class SurveySender {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration config;
	
	@Autowired
	MailDataService md;

	
	@Autowired
	ResponsesService rs;
	
	@Autowired
	FormService fs;
	
	@Autowired
	UserService us;
	
	@Autowired
	ConfigFormService cs;


	public static final String SAMPLE_XLSX_FILE_PATH = "/Users/gokkul/Desktop/Accolite Employee.xlsx";

	@GetMapping("/accolite_survey_mail/{id}")
	public String sendHTMLEmailWithAttachment(@PathVariable String id, Model model)
			throws Exception {
		String Path = new File("").getAbsolutePath();
		FileInputStream fis = new FileInputStream(Path+"//Accolite Employee.xlsx"); 
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheet("Employee");
		XSSFRow row = sheet.getRow(0);
		ArrayList<String> mailid = new ArrayList<String>();
		ArrayList<Integer> empid = new ArrayList<Integer>();
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> mailsentdate = new ArrayList<String>();
		ArrayList<String> doj = new ArrayList<String>();
		ArrayList<String> formid = new ArrayList<String>();
		
		
		int days=180;
		List<SurveyFormConfig> s = cs.getAllSurveyFormConfig();
		for(SurveyFormConfig q:s)
		{
			days=q.getDaysAfterWhichEmailShouldBeSent();
		}
		
		String fid=id;
		
		int col_num = -1;

		for (int i = 0; i < row.getLastCellNum(); i++) {
			if (row.getCell(i).getStringCellValue().trim().equals("Employee DOJ"))
				col_num = i;
		}
		XSSFRow row2 = sheet.getRow(0);
		int col_num2 = -1;

		for (int i = 0; i < row2.getLastCellNum(); i++) {
			if (row2.getCell(i).getStringCellValue().trim().equals("Employee Mail"))
				col_num2 = i;
		}
		XSSFRow row3 = sheet.getRow(0);
		int col_num3 = -1;

		for (int i = 0; i < row3.getLastCellNum(); i++) {
			if (row3.getCell(i).getStringCellValue().trim().equals("Employee Name"))
				col_num3 = i;
		}
		XSSFRow row4 = sheet.getRow(0);
		int col_num4 = -1;

		for (int i = 0; i < row4.getLastCellNum(); i++) {
			if (row4.getCell(i).getStringCellValue().trim().equals("Employee ID"))
				col_num4 = i;
		}

		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			row2 = sheet.getRow(i);
			row3 = sheet.getRow(i);
			row4 = sheet.getRow(i);
			XSSFCell cell = row.getCell(col_num);
			XSSFCell cell2 = row2.getCell(col_num2);
			XSSFCell cell3 = row3.getCell(col_num3);
			XSSFCell cell4 = row4.getCell(col_num4);
			String value2 = cell2.getStringCellValue();
			String value3 = cell3.getStringCellValue();
			int value4 = (int) cell4.getNumericCellValue();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
			String value = sdf.format(cell.getDateCellValue());
			String value5 = sdf.format(cell.getDateCellValue());
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(value));
			c.add(Calendar.DATE, days);
			value = sdf.format(c.getTime());
			SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			Date date1 = format.parse(value);
			Date date2 = format.parse(sdf2.format(date));
			if (date1.compareTo(date2) == 0) {
				mailid.add(value2);
				empid.add(value4);
				name.add(value3);
				mailsentdate.add(value);
				doj.add(value5); 
				formid.add(fid);

			}
		}
		boolean ans = mailid.isEmpty();
		if (ans == true) {
			empid.clear();
			name.clear();
			mailsentdate.clear();
			mailid.clear();
			doj.clear();
			formid.clear();
			System.out.println("\nNo Recipients today !!");
			model.addAttribute("message", "No Recipients today !!");
			fis.close();
			workbook.close();
			return extracted();
		}
		else
		{
		String to[] = new String[mailid.size()];
		for (int i = 0; i < mailid.size(); i++)
			to[i] = mailid.get(i);
		
		for(int i=0;i<mailid.size();i++) {
		String from = "accolite.survey@gmail.com";
		String URL="accolite.survey.com/forms/"+formid.get(i)+"";
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setSubject("HR Connect Survey - Response Required !!");
		helper.setFrom(from);
		helper.setTo(to[i]);
		
		
		Map<String, Object> m = new HashMap<>();
		m.put("Name", name.get(i));
		m.put("Days", days);
		m.put("URL", URL);
				
		Template t = config.getTemplate("survey.html");
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, m);
		
		helper.setText(html,true);
		
		MailData w=new MailData();
		w.setEmail(to[i]);
		w.setName(name.get(i));
		w.setLast_sent_date(mailsentdate.get(i));
		w.setFormid(formid.get(i));
		md.saveMailData(w);

		
		System.out.println("\nEmail Sent to " + name.get(i)+ " with Employee ID : " + empid.get(i) + " and Mail ID : "
				+ to[i] + " and the Date after completion of " + days
				+ " is " + mailsentdate.get(i) + " with DOJ : "+doj.get(i) +" ");
		mailSender.send(message);
		}
		model.addAttribute("message",
				"SUCCESS !! Accolite Survey link sent successfully to the employees "+name+" and they have completed "+days+" Days in Accolite");
		empid.clear();
		name.clear();
		doj.clear();
		mailsentdate.clear();
		mailid.clear();
		formid.clear();
		Arrays.fill( to, null );
		fis.close();
		workbook.close();
		return extracted();
	}
	}

	private String extracted() {
		return "result";
	}

}
