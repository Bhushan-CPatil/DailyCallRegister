package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetPopupQuesRes{

	@SerializedName("questionslist")
	private List<QuestionslistItem> questionslist;

	@SerializedName("error")
	private boolean error;

	public void setQuestionslist(List<QuestionslistItem> questionslist){
		this.questionslist = questionslist;
	}

	public List<QuestionslistItem> getQuestionslist(){
		return questionslist;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	@Override
 	public String toString(){
		return 
			"GetPopupQuesRes{" + 
			"questionslist = '" + questionslist + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}