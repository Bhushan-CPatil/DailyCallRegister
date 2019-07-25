package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class QuizMainRes{

	@SerializedName("testqueslst")
	private List<TestqueslstItem> testqueslst;

	public void setTestqueslst(List<TestqueslstItem> testqueslst){
		this.testqueslst = testqueslst;
	}

	public List<TestqueslstItem> getTestqueslst(){
		return testqueslst;
	}

	@Override
 	public String toString(){
		return 
			"QuizMainRes{" + 
			"testqueslst = '" + testqueslst + '\'' + 
			"}";
		}
}