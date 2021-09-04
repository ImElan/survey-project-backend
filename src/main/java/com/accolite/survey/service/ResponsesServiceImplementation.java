package com.accolite.survey.service;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.accolite.survey.DAO.ResponsesDAO;
import com.accolite.survey.DAO.Auth.AuthDAOImplementation;
import com.accolite.survey.Exception.Id.IdNotFoundException;
import com.accolite.survey.Exception.Id.InvalidIdException;
import com.accolite.survey.Model.TokenType;
import com.accolite.survey.controller.MyException;
import com.accolite.survey.entity.Form;
import com.accolite.survey.entity.Responses;
import com.accolite.survey.entity.User;
import com.accolite.survey.entity.UserRoles;

import freemarker.template.Template;


@Service
@Transactional
public class ResponsesServiceImplementation implements ResponsesService {
	@Autowired
	 AuthDAOImplementation authdao;
	
	@Autowired
	ResponsesDAO responsedao;
	
	@Autowired
	ResponsesService responseService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	FormService formService;

	@Override
	public Responses addResponse(Responses response,String bearerToken) throws MyException, MessagingException {
		User user = authdao.isAuthenticated(bearerToken,TokenType.ACCESS);
		System.out.println(user);
		UserRoles[] roles = {UserRoles.EMPLOYEE};
		authdao.restrictTo(roles, user);
		
		
		List<Responses> ans = getResponseByFormId(response.getFormId(),bearerToken);
		for(int i=0 ; i<ans.size() ; i++) {
			if(ans.get(i).getUserId().equals(response.getUserId())) {
				throw new MyException("This user can't fill this form as it's been already filled by this userId\n") ;
			}
		}
		
		response.setUserId(user.getId());
        Form form = formService.getFormByID(response.getFormId(), bearerToken);
        //System.out.println("\n\n\n\n\n\n"+form.getFormTitle()+"\n\n\n\n");
		Responses newResponse = responsedao.insert(response);
		if(response.getSendCopy() == 1) {
			Sheet copy = responseService.createResponseCopy(response, form.getFormTitle());
		    responseService.sendEmailWithAttachment(user.getEmail(), user.getName(), form.getFormTitle(), copy);
		}
		return newResponse;
	}

	@Override
	public List<Responses> getAllResponses(String bearerToken) {
		User user = authdao.isAuthenticated(bearerToken,TokenType.ACCESS);
		UserRoles[] roles = {UserRoles.HR};
		authdao.restrictTo(roles, user);
		return responsedao.findAll();
	}

	@Override
	public List<Responses> getResponseByFormId(String formid,String bearerToken) {
		User user = authdao.isAuthenticated(bearerToken,TokenType.ACCESS);
		UserRoles[] roles = {UserRoles.HR,UserRoles.EMPLOYEE};
		authdao.restrictTo(roles, user);
		List<Responses> response = responsedao.findAll();
		List<Responses> ans = new ArrayList<>();
		for(int i=0 ; i<response.size() ; i++) {
			if(response.get(i).getFormId().equals(formid)) {
				ans.add(response.get(i));
			}
		}
		return ans;
	}
	
	@Override
	public Responses check(String user_id, String form_id) throws MyException {
		
		
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and("formid").is(form_id);
		System.out.println("user id" +user_id);
		if(user_id != null && !user_id.isEmpty()) {
			
			criteria.and("userid").is(user_id);
			
		} else {
			
			throw new InvalidIdException("User id can not be null or empty");
			
		}
		
		query.addCriteria(criteria);
		List<Responses> responses = mongoTemplate.find(query, Responses.class);
		
		if(responses == null || responses.isEmpty()) {		
			throw new IdNotFoundException("Response with particular user id is not found");	
		}
		
		else {	
			return responses.get(0);
		}
	}

	
	@Override
	public Sheet createResponseCopy(Responses response, String title) {
		        
		        //Create workbook in .xlsx format
				Workbook workbook = new XSSFWorkbook();
				Sheet sh = workbook.createSheet(title+"_Response");
				try {
									
					String[] columnHeadings = {"Q.No.","Questions","Answers"};
					Font headerFont = workbook.createFont();
					headerFont.setBold(true);
					headerFont.setFontHeightInPoints((short)12);
					headerFont.setColor(IndexedColors.BLACK.index);
					
					//Create a CellStyle with the font
					CellStyle headerStyle = workbook.createCellStyle();
					headerStyle.setFont(headerFont);
					headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
					
					//Create the header row
					Row headerRow = sh.createRow(0);
					
					//Iterate over the column headings to create columns
					for(int i = 0; i < columnHeadings.length; i++) {
						Cell cell = headerRow.createCell(i);
						cell.setCellValue(columnHeadings[i]);
						cell.setCellStyle(headerStyle);
					}
					
					//Freeze Header Row
					sh.createFreezePane(0, 1);

					//create question-answer map
					ArrayList<String> questions = response.getQuestions();
					ArrayList<String> answers = response.getAnswers();
					Map<String, String> ques_ans = new LinkedHashMap<String, String>();
					for(int i = 0; i < questions.size(); i++) {
						ques_ans.put(questions.get(i), answers.get(i));
						
					}
					
					//fill data into spreadsheet
					int rownum = 1;
					for(Map.Entry<String,String> entry : ques_ans.entrySet()) {  

						Row row = sh.createRow(rownum++);
						row.createCell(0).setCellValue(rownum-1);
						row.createCell(1).setCellValue(entry.getKey());
		     			row.createCell(2).setCellValue(entry.getValue());
					}
					
					//Auto-size columns
					for(int i=0; i<columnHeadings.length; i++) {
						sh.autoSizeColumn(i);
					}

					FileOutputStream fileOut = new FileOutputStream(title+"_response.xlsx");
					workbook.write(fileOut);
					fileOut.close();
					workbook.close();
					
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				return sh;
	}

	
	@Override
	public String sendEmailWithAttachment(String toEmail, String userName, String title, Sheet attachment) throws MessagingException {
		
		String subject = title + " - Accolite Digital India Pvt Ltd.";
		String body = "Hello "+userName+" !\n\nThank you for taking the survey. Your copy of response is attached here.\n\nThanks and Regards,\nHR Team";
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom("accolite.survey@gmail.com");
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body);

        FileSystemResource fileSystem = new FileSystemResource(title+"_response.xlsx");
        mimeMessageHelper.addAttachment(fileSystem.getFilename(), fileSystem);
        
        mailSender.send(mimeMessage);
        return "Copy of response sent successfully";
		
	}

	@Override
	public Responses updateResponse(Responses responses) {
		// TODO Auto-generated method stub
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and("formid").is(responses.getFormId());

		criteria.and("userid").is(responses.getUserId());
		query.addCriteria(criteria);
		List<Responses> returnedResponses = mongoTemplate.find(query, Responses.class);
		Responses oldResponses=returnedResponses.get(0);
		oldResponses.setAnswers(responses.getAnswers());
		responsedao.save(oldResponses);
		return oldResponses;
	}

}
