package com.accolite.survey.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.survey.DAO.Auth.AuthDAOImplementation;
import com.accolite.survey.Model.TokenType;
import com.accolite.survey.entity.MailData;
import com.accolite.survey.entity.SurveyFormConfig;
import com.accolite.survey.entity.User;
import com.accolite.survey.entity.UserRoles;
import com.accolite.survey.service.ConfigFormService;
import com.accolite.survey.service.FormService;
import com.accolite.survey.service.MailDataService;
import com.accolite.survey.service.ResponsesService;
import com.accolite.survey.service.UserService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

import freemarker.template.Configuration;
import freemarker.template.Template;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class SurveySender {
	@Autowired
	 AuthDAOImplementation authdao;
	

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

	int mainno_of_days_after_mail;
	String mainformid;
	String mainfrom_date;
	String mainto_date;

	@PostMapping("/accolite/filter_employees")
	@ResponseBody
	public String filterEmployee(@RequestBody MailData maildata,@RequestHeader("Authorization") String bearerToken) throws Exception {

		
		User user = authdao.isAuthenticated(bearerToken,TokenType.ACCESS);
		UserRoles[] roles = {UserRoles.HR};
		authdao.restrictTo(roles, user);
		
		
		
		
		// Default value for 'the no of days' after mail
        
		int no_of_days_after_mail = 180;

		// Initialising the received data from PostMapping

		no_of_days_after_mail = maildata.getNo_of_days_after_mail();
		String formid = maildata.getFormid();
		String from_date = maildata.getFrom_date();
		String to_date = maildata.getTo_date();

		// Initialising the received data from PostMapping to the global variable

		mainformid = formid;
		mainno_of_days_after_mail = no_of_days_after_mail;
		mainfrom_date = from_date;
		mainto_date = to_date;

		// Getting data from Accolite API with Authorization token

		URL url = new URL("https://apps.accolite.com/apps/api/employees/getEmployeeDetails");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestProperty("Authorization", "Bearer"
				+ " 8091108D9AE409BB2ECCA39BE1A945EBF8F827D000C73E41B59333F743FFBC63ED5086965A621EB78B458810DD7D1E77409A6A18431A29E58370935841266B5CF4AB892F");

		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String output;

		StringBuffer response = new StringBuffer();
		while ((output = in.readLine()) != null) {
			response.append(output);
		}

		in.close();

		// Original Accolite Employees details in JSON String

		String x = response.toString();

		// Original Accolite Employees details converted from JSON String to JSON Array

		JSONArray array = new JSONArray(x);

		// New JSON Array initialisation for storing the Employee details who completed -
		// Z days between X and Y date ranges that has to be sent to Bhargavi who shows -
		// the details in Frontend

		JSONArray ja = new JSONArray();

		System.out.println("\n\n");

		for (int i = 0; i < array.length(); i++) {

			JSONObject obj = array.getJSONObject(i);
			String empname = obj.getString("empname");
			String emailId = obj.getString("emailId");
			String doj = obj.getString("doj");
			String empcode = obj.getString("empCode");
			String bu = obj.getString("bu");

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
			Date varDate = null;
			try {
				varDate = dateFormat.parse(doj);
				dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String mailToBeSendOn = dateFormat.format(varDate);
				String convdoj = dateFormat.format(varDate);

				// Adding no.of days to DOJ

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Calendar c = Calendar.getInstance();
				c.setTime(sdf.parse(convdoj));
				c.add(Calendar.DATE, no_of_days_after_mail);
				mailToBeSendOn = sdf.format(c.getTime());

				Date d2 = sdf.parse(mailToBeSendOn);
				Date d1 = sdf.parse(from_date);
				Date d3 = sdf.parse(to_date);

				if (d2.compareTo(d1) >= 0) {
					if (d2.compareTo(d3) <= 0) {

						// Reject the entity if emailid, doj, empname, empcode is empty

						if (emailId != "" && doj != "" && empname != "" && empcode != "" && bu!="")

						{

							// Converting each filtered entities from the original JSON array to JSON - 
							// Objects to store them in a new JSON Array for Bhargavi

							JSONObject jsonObject = new JSONObject();
							jsonObject.put("Employee Name", empname);
							jsonObject.put("Email Mail ID", emailId);
							jsonObject.put("Employee DOJ", convdoj);
							jsonObject.put("Employee Code", empcode);

							// Adding each JSON object to new JSON Array

							ja.put(jsonObject);

							// Printing for test purposes
							
							System.out.println("Employee Name : " + empname);
							System.out.println("Employee Mail ID : " + emailId);
							System.out.println("Employee DOJ : " + convdoj);
							System.out.println("Employee Code : " + empcode);
							System.out.println("Employee BU : " + bu);
							System.out.println("Mail Should Be Sent On : " + mailToBeSendOn);
							System.out.println("\n--------------------------------\n");

						}

						else

						{
							System.out.println(
									"\nRejected Entity ! Still an Eligible entity, but rejected as one of the following important entity is missing : \n\nEmployee DOJ : "
											+ doj + "\nEmployee Name : " + empname + "\nEmployee ID : " + empcode
											+ "\nEmployee Email ID : " + emailId + "\nEmployee BU : " + bu + "\n");
							System.out.println("\n--------------------------------\n");

						}
					}

					else

					{

						// Out of given Date range

						System.out.println("Rejected Entity ! " + empname + " with employee ID " + empcode
								+ " is ineligible for receiving the Accolite Survey Mail");
						System.out.println("\n--------------------------------\n");

					}
				} else {

					// Out of given Date range

					System.out.println("Rejected Entity ! " + empname + " with employee ID " + empcode
							+ " is ineligible for receiving the Accolite Survey Mail");
					System.out.println("\n--------------------------------\n");

				}
			} catch (ParseException e) {

				// Showing the rejected data for the dirty date entity

				System.out.println("\nRejected Entity ! The DOJ : " + doj + " is not in proper format for the employee "
						+ empname + " with employee id " + empcode + " and his/her Mail ID is " + emailId
						+ " working in " + bu + " Buisness Unit \n");
				System.out.println("\n--------------------------------\n");
				continue;
			}

		}

		return ja.toString();

	}

	@PostMapping("/accolite/send_email")
	@ResponseBody
	public String sendMail(@RequestBody MailData maildata,@RequestHeader("Authorization") String bearerToken) throws Exception {


		User user = authdao.isAuthenticated(bearerToken,TokenType.ACCESS);
		UserRoles[] roles = {UserRoles.HR};
		authdao.restrictTo(roles, user);
		
		
		
		
		// To check an Illegal API Call

		if (mainformid == null && mainfrom_date == null && mainto_date == null) {
			return "Illegal API call !";
		}

		// Initialising the received to array from PostMapping to newto list

		String to[] = maildata.getTomailid();

		List<String> newto = Arrays.asList(to);

		ArrayList<String> mailedemp = new ArrayList<>();

		// Getting data from Accolite API with Authorization token

		URL url = new URL("https://apps.accolite.com/apps/api/employees/getEmployeeDetails");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setRequestProperty("Authorization", "Bearer"
				+ " 8091108D9AE409BB2ECCA39BE1A945EBF8F827D000C73E41B59333F743FFBC63ED5086965A621EB78B458810DD7D1E77409A6A18431A29E58370935841266B5CF4AB892F");

		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String output;

		StringBuffer response = new StringBuffer();
		while ((output = in.readLine()) != null) {
			response.append(output);
		}

		in.close();

		// Original Accolite Employees details in JSON String

		String x = response.toString();

		// Original Accolite Employees details converted from JSON String to JSON Array

		JSONArray array = new JSONArray(x);

		for (int i = 0; i < array.length(); i++) {

			JSONObject obj = array.getJSONObject(i);
			String empname = obj.getString("empname");
			String emailId = obj.getString("emailId");
			String doj = obj.getString("doj");
			String empcode = obj.getString("empCode");
			String bu = obj.getString("bu");

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
			Date varDate = null;
			try {
				varDate = dateFormat.parse(doj);
				dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				String mailToBeSendOn = dateFormat.format(varDate);
				String convdoj = dateFormat.format(varDate);

				// Date calculation

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				Calendar c = Calendar.getInstance();
				c.setTime(sdf.parse(convdoj));
				c.add(Calendar.DATE, mainno_of_days_after_mail);
				mailToBeSendOn = sdf.format(c.getTime());

				Date d2 = sdf.parse(mailToBeSendOn);
				Date d1 = sdf.parse(mainfrom_date);
				Date d3 = sdf.parse(mainto_date);

				if (d2.compareTo(d1) >= 0) {
					if (d2.compareTo(d3) <= 0) {

						// Reject the entity if emailid, doj, empname, empcode is empty

						if (emailId != "" && doj != "" && empname != "" && empcode != "")

						{

							// If the eleigble entity's mail id lies in the original employee details, fetch -
							// the data to store into database for reuseage for Prasana for reminder mail

							if (newto.contains(emailId)) {

								String from = "accolite.survey@gmail.com";

								String URL = "http://localhost:3000/forms/" + mainformid + "";

								MimeMessage message = mailSender.createMimeMessage();
								MimeMessageHelper helper = new MimeMessageHelper(message, true);
								helper.setSubject("HR Connect Survey - Response Required !!");
								helper.setFrom(from);
								helper.setTo(emailId);

								Map<String, Object> m = new HashMap<>();
								m.put("Name", empname);
								m.put("Days", mainno_of_days_after_mail);
								m.put("URL", URL);

								Template t = config.getTemplate("survey.html");
								String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, m);

								helper.setText(html, true);
								mailSender.send(message);

								// Save the details into database for Prasana

								MailData w = new MailData();
								mailedemp.add(empname);
								w.setEmail(emailId);
								w.setName(empname);
								w.setFormid(mainformid);
								w.setLast_sent_date(mailToBeSendOn);
								w.setNo_of_days_after_mail(mainno_of_days_after_mail);
								w.setUrl(URL);
								w.setShowform(true);
								w.setRemindercount(0);
								md.saveMailData(w);
								
								// Printing for test purposes

								System.out.println("\n\nAccolite Survey link sent successfully to the employee "
										+ empname + " and he/she have completed " + mainno_of_days_after_mail
										+ " Days in Accolite between " + mainfrom_date + " and " + mainto_date
										+ " range \n\n");

							}

						}
					}

				}
			}

			catch (ParseException e)

			{

				continue;
			}

		}
		
		String res ="SUCCESS !! Accolite Survey link sent successfully to the employee " + mailedemp
				+ " and he/she have completed " + mainno_of_days_after_mail + " Days in Accolite between "
				+ mainfrom_date + " and " + mainto_date + " range ";
		
		// Final initialisation on the global variables to null, to check an Illegal API -
		// Call
		
		mainformid = null;
		mainfrom_date = null;
		mainto_date = null;
		
		return res;
		
	}

}