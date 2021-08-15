package com.accolite.survey.service;

import java.io.IOException;
//import java.lang.reflect.Type;
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

//import com.accolite.survey.entity.QuestionType;
//import com.accolite.survey.entity.SurveyQuestion;
//import com.accolite.survey.entity.Views;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

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
	public String getAllForms() throws JsonGenerationException, JsonMappingException, IOException {
		List<Form> arr = formRepository.findAll();
		ObjectMapper mapper = new ObjectMapper();
		
        String jsonInString = mapper.writeValueAsString(arr);

//        String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arr);
        

        JSONArray formj = new JSONArray(jsonInString);

        
        for(int i=0;i<formj.length();i++)
        {
        	JSONObject obj = formj.getJSONObject(i);
        	JSONArray survq = obj.getJSONArray("surveyQuestions");

        	JSONArray temp = new JSONArray();
        	for(int j=0;j<survq.length();j++)
        	{
            	JSONObject ob = survq.getJSONObject(j);
            	String val = ob.getString("questionType");
        		if(val.equals("SINGLE"))
        		{
//        			System.out.println("inside single");
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

        	}
        }
        System.out.println(formj);
        
//        Gson gson = new Gson(); 
        
//        Type userListType = new TypeToken<List<Form>>(){}.getType();
         
//        List<Form> userArray = gson.fromJson(formj.toString(), userListType); 
  
//        formj = formj
        return formj.toString();
	}
	
}







