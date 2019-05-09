package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class FetchExpdtRes{

	@SerializedName("expfetch")
	private List<ExpfetchItem> expfetch;

	@SerializedName("error")
	private boolean error;

	public void setExpfetch(List<ExpfetchItem> expfetch){
		this.expfetch = expfetch;
	}

	public List<ExpfetchItem> getExpfetch(){
		return expfetch;
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
			"FetchExpdtRes{" + 
			"expfetch = '" + expfetch + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}