# Shivam Patidar (Employee id: 5137)

# Task Assigned : Method to save the responses in the database

1: Downloaded maven project with dependencies.

2: Application.properties contain the port number and name of the database i.e 27017 and Survey (DB name)

3: Packages :-

a. backend : main.java 

b. Configuration : mongoDB.java (enabling monogoDB repository for SurveyRepository.class)

c. Controller : SurveyController.java (having two API's for getting all the responses from                  Database (Survey) and to insert the responses into the database)

d. Repository : SurveyRepository.java interface (Extending the mongoDB repositories or can say methods)

e. Response_Entity : There should be a Responses entity class (whose object will be used to store the responses using the PostMapping("/write") api as shown in SurveyRepository.java) 


                