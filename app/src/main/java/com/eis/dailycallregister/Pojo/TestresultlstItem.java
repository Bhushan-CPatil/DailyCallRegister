package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class TestresultlstItem{

	@SerializedName("TestId")
	private String testId;

	@SerializedName("TestName")
	private String testName;

	@SerializedName("Percentage")
	private String percentage;

	@SerializedName("UserId")
	private String userId;

	@SerializedName("AttemptNo")
	private String attemptNo;

	@SerializedName("TimeTaken")
	private String timeTaken;

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

	public void setPercentage(String percentage){
		this.percentage = percentage;
	}

	public String getPercentage(){
		return percentage;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setAttemptNo(String attemptNo){
		this.attemptNo = attemptNo;
	}

	public String getAttemptNo(){
		return attemptNo;
	}

	public void setTimeTaken(String timeTaken){
		this.timeTaken = timeTaken;
	}

	public String getTimeTaken(){
		return timeTaken;
	}

	@Override
 	public String toString(){
		return 
			"TestresultlstItem{" + 
			"testId = '" + testId + '\'' + 
			",testName = '" + testName + '\'' + 
			",percentage = '" + percentage + '\'' + 
			",userId = '" + userId + '\'' + 
			",attemptNo = '" + attemptNo + '\'' + 
			",timeTaken = '" + timeTaken + '\'' + 
			"}";
		}
}