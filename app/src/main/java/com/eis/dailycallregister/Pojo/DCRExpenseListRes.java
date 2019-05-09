package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class DCRExpenseListRes{

	@SerializedName("expentlst")
	private List<ExpentlstItem> expentlst;

	@SerializedName("error")
	private boolean error;

	public void setExpentlst(List<ExpentlstItem> expentlst){
		this.expentlst = expentlst;
	}

	public List<ExpentlstItem> getExpentlst(){
		return expentlst;
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
			"DCRExpenseListRes{" + 
			"expentlst = '" + expentlst + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}