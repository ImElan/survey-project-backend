package com.accolite.survey.service;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.ResponsesDAO;
import com.accolite.survey.Exception.Id.IdNotFoundException;
import com.accolite.survey.Exception.Id.InvalidIdException;
import com.accolite.survey.controller.MyException;
import com.accolite.survey.entity.Responses;


@Service
@Transactional
public class ResponsesServiceImplementation implements ResponsesService {
	
	@Autowired
	ResponsesDAO responsedao;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
    private JavaMailSender mailSender;

	@Override
	public Responses addResponse(Responses response) throws MyException {
		List<Responses> ans = getResponseByFormId(response.getFormId());
		for(int i=0 ; i<ans.size() ; i++) {
			if(ans.get(i).getUserId().equals(response.getUserId())) {
				throw new MyException("This user can't fill this form as it's been already filled by this userId\n") ;
			}
		}
		return responsedao.insert(response);
	}

	@Override
	public List<Responses> getAllResponses() {
		return responsedao.findAll();
	}

	@Override
	public List<Responses> getResponseByFormId(String formid) {
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
	public Sheet createResponseCopy(Responses response) {
		        
		        //Create workbook in .xlsx format
				Workbook workbook = new XSSFWorkbook();
				Sheet sh = workbook.createSheet("Survey Response");
				try {
					
					//Create top row with column headings
					String[] columnHeadings = {"S.No.","Questions","Answers"};
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
					Map<String, String> ques_ans = new HashMap<String, String>();
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

					FileOutputStream fileOut = new FileOutputStream("temp.xlsx");
					workbook.write(fileOut);
					fileOut.close();
					workbook.close();
					//System.out.println("Creation of spreadsheet successful.."+ sh.getSheetName());
					
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				return sh;
	}

	@Override
	public String sendEmailWithAttachment(String toEmail, Sheet attachment) throws MessagingException {
		String subject = "Accolite Digital India Pvt Ltd - Employee Survey";
		String body = "Thank you for taking the survey for happiness index. Your copy of response is attached here.";
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setFrom("accolite.survey@gmail.com");
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body);

        FileSystemResource fileSystem = new FileSystemResource("temp.xlsx");
        mimeMessageHelper.addAttachment(fileSystem.getFilename(), fileSystem);
        
        mailSender.send(mimeMessage);
        return "Mail Sent successfully";
		
	}

}
