package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class TestqueslstItem{

	@SerializedName("Option4")
	private String option4;

	@SerializedName("Option3")
	private String option3;

	@SerializedName("Answer")
	private String answer;

	@SerializedName("TestId")
	private String testId;

	@SerializedName("Option2")
	private String option2;

	@SerializedName("Option1")
	private String option1;

	@SerializedName("QuestionId")
	private String questionId;

	@SerializedName("Question")
	private String question;

	@SerializedName("Marks")
	private String marks;

	@SerializedName("ansgiven")
	private String ansgiven;

	public void setOption4(String option4){
		this.option4 = option4;
	}

	public String getOption4(){
		return option4;
	}

	public void setOption3(String option3){
		this.option3 = option3;
	}

	public String getOption3(){
		return option3;
	}

	public void setAnswer(String answer){
		this.answer = answer;
	}

	public String getAnswer(){
		return answer;
	}

	public void setTestId(String testId){
		this.testId = testId;
	}

	public String getTestId(){
		return testId;
	}

	public void setOption2(String option2){
		this.option2 = option2;
	}

	public String getOption2(){
		return option2;
	}

	public void setOption1(String option1){
		this.option1 = option1;
	}

	public String getOption1(){
		return option1;
	}

	public void setQuestionId(String questionId){
		this.questionId = questionId;
	}

	public String getQuestionId(){
		return questionId;
	}

	public void setQuestion(String question){
		this.question = question;
	}

	public String getQuestion(){
		return question;
	}

	public void setMarks(String marks){
		this.marks = marks;
	}

	public String getMarks(){
		return marks;
	}

	public void setAnsgiven(String ansgiven){
		this.ansgiven = ansgiven;
	}

	public String getAnsgiven(){
		return ansgiven;
	}

	@Override
 	public String toString(){
		return 
			"TestqueslstItem{" + 
			"option4 = '" + option4 + '\'' + 
			",option3 = '" + option3 + '\'' + 
			",answer = '" + answer + '\'' + 
			",testId = '" + testId + '\'' + 
			",option2 = '" + option2 + '\'' + 
			",option1 = '" + option1 + '\'' + 
			",questionId = '" + questionId + '\'' + 
			",question = '" + question + '\'' + 
			",marks = '" + marks + '\'' + 
			",ansgiven = '" + ansgiven + '\'' + 
			"}";
		}
}