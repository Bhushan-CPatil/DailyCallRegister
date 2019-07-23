package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class TestlstItem{

	@SerializedName("TestId")
	private String testId;

	@SerializedName("TestName")
	private String testName;

	@SerializedName("Month")
	private String month;

	@SerializedName("Year")
	private String year;

	@SerializedName("ActiveDateTo")
	private String activeDateTo;

	@SerializedName("NoOfQuestions")
	private String noOfQuestions;

	@SerializedName("TotalTime")
	private String totalTime;

	@SerializedName("AddedByUserId")
	private String addedByUserId;

	@SerializedName("ActiveDateFrom")
	private String activeDateFrom;

	public void setTestId(String testId){
		this.testId = testId;
	}

	public String getTestId(){
		return testId;
	}

	public void setTestName(String testName){
		this.testName = testName;
	}

	public String getTestName(){
		return testName;
	}

	public void setMonth(String month){
		this.month = month;
	}

	public String getMonth(){
		return month;
	}

	public void setYear(String year){
		this.year = year;
	}

	public String getYear(){
		return year;
	}

	public void setActiveDateTo(String activeDateTo){
		this.activeDateTo = activeDateTo;
	}

	public String getActiveDateTo(){
		return activeDateTo;
	}

	public void setNoOfQuestions(String noOfQuestions){
		this.noOfQuestions = noOfQuestions;
	}

	public String getNoOfQuestions(){
		return noOfQuestions;
	}

	public void setTotalTime(String totalTime){
		this.totalTime = totalTime;
	}

	public String getTotalTime(){
		return totalTime;
	}

	public void setAddedByUserId(String addedByUserId){
		this.addedByUserId = addedByUserId;
	}

	public String getAddedByUserId(){
		return addedByUserId;
	}

	public void setActiveDateFrom(String activeDateFrom){
		this.activeDateFrom = activeDateFrom;
	}

	public String getActiveDateFrom(){
		return activeDateFrom;
	}

	@Override
 	public String toString(){
		return 
			"TestlstItem{" + 
			"testId = '" + testId + '\'' + 
			",testName = '" + testName + '\'' + 
			",month = '" + month + '\'' + 
			",year = '" + year + '\'' + 
			",activeDateTo = '" + activeDateTo + '\'' + 
			",noOfQuestions = '" + noOfQuestions + '\'' + 
			",totalTime = '" + totalTime + '\'' + 
			",addedByUserId = '" + addedByUserId + '\'' + 
			",activeDateFrom = '" + activeDateFrom + '\'' + 
			"}";
		}
}