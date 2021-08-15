package com.accolite.survey.service;

import java.io.IOException;
import java.util.List;

import org.json.*;
//import org.apache.tomcat.util.json.JSONParser;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.stereotype.Service;

import com.accolite.survey.DAO.FormsRepository;
import com.accolite.survey.entity.Form;
import com.accolite.survey.entity.QuestionType;
import com.accolite.survey.entity.SurveyQuestion;
import com.accolite.survey.entity.Views;
//import com.accolite.survey.entity.QuestionType;
//import com.accolite.survey.entity.SurveyQuestion;

@Service
public class FormService {
	
	private final FormsRepository formRepository;
	
	public FormService(FormsRepository formRepository) {
//		super();
		this.formRepository = formRepository;
	}

	// Method to submit a Form into a database
	public boolean addForm(Form form) {
		if(form.getFormTitle()!= null && form.getFormDescription()!=null)
		{
			if(form.getSurveyQuestions().size() >=1 && form.getSurveyQuestions().size() <=10)
			{
				formRepository.insert(form);
				return true;
			}
		}
		return false;
	}
	
	// Method to get different Form submit into a database
	public List<Form> getAllForms() throws JsonGenerationException, JsonMappingException, IOException {
		List<Form> arr = formRepository.findAll();
		ObjectMapper mapper = new ObjectMapper();
		
//        Convert object to JSON string
        String jsonInString = mapper.writeValueAsString(arr);
//        System.out.println(jsonInString);
        
        //Convert object to JSON string and pretty print
//        jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arr);
//        System.out.println(jsonInString);
        
        
//        mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);
//
//        SurveyQuestion fr = arr.get(0).getSurveyQuestions().get(0);
//        
//        
//        String jsonInString = mapper.writerWithView(Views.optionsOnly.class).writeValueAsString(fr);
//        System.out.println(jsonInString);
//        
//        jsonInString = mapper.writerWithView(Views.starsOnly.class).writeValueAsString(arr);
//        System.out.println(jsonInString);

        JSONArray formj = new JSONArray(jsonInString);
//        System.out.println(arrj);
        
        for(int i=0;i<formj.length();i++)
        {
        	JSONObject obj = formj.getJSONObject(i);
        	JSONArray survq = obj.getJSONArray("surveyQuestions");
//        	System.out.println(survq);
//        	System.out.println(object);
        	JSONArray temp = new JSONArray();
        	for(int j=0;j<survq.length();j++)
        	{
//        		System.out.println(survq.get(j));
            	JSONObject ob = survq.getJSONObject(j);
            	JSONObject newob = new JSONObject();
//            	newob.put(jsonInString, false)
            	String val = ob.getString("questionType");
        		if(val.equals("SINGLE"))
        		{
        			System.out.println("inside single");
        			ob.remove("stars");
        			ob.remove("paragraph");
        		}
        		else if(val.equals("MULTIPLE"))
        		{
        			ob.remove("stars");
        			ob.remove("paragraph");
        		}
        		else if(val.equals("DESCRIPTIVE"))
        		{
        			ob.remove("options");
        			ob.remove("stars");
        		}
        		else if(val.equals("STAR"))
        		{
        			ob.remove("options");
        			ob.remove("paragraph");
        		}
//        		System.out.println(ob);
//        		temp.put(ob);
        	}
//        	System.out.println(temp);
        }
        System.out.println(formj);
        
		
		return formRepository.findAll();
	}
	
}







