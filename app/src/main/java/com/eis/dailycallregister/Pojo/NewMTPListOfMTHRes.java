package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NewMTPListOfMTHRes{

	@SerializedName("error")
	private boolean error;

	@SerializedName("mtptownlist")
	private List<MtptownlistItem> mtptownlist;

	@SerializedName("confirmed")
	private boolean confirmed;

	@SerializedName("errormsg")
	private String errormsg;

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setMtptownlist(List<MtptownlistItem> mtptownlist){
		this.mtptownlist = mtptownlist;
	}

	public List<MtptownlistItem> getMtptownlist(){
		return mtptownlist;
	}

	public void setConfirmed(boolean confirmed){
		this.confirmed = confirmed;
	}

	public boolean isConfirmed(){
		return confirmed;
	}

	public void setErrormsg(String errormsg){
		this.errormsg = errormsg;
	}

	public String getErrormsg(){
		return errormsg;
	}

	@Override
 	public String toString(){
		return 
			"NewMTPListOfMTHRes{" + 
			"error = '" + error + '\'' + 
			",mtptownlist = '" + mtptownlist + '\'' + 
			",confirmed = '" + confirmed + '\'' + 
			",errormsg = '" + errormsg + '\'' + 
			"}";
		}
}