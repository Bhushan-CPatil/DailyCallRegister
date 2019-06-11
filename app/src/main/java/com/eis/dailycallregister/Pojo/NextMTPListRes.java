package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NextMTPListRes{

	@SerializedName("mtppendlist")
	private List<MtppendlistItem> mtppendlist;

	@SerializedName("error")
	private boolean error;

	@SerializedName("confirmed")
	private boolean confirmed;

	@SerializedName("errormsg")
	private String errormsg;

	public void setMtppendlist(List<MtppendlistItem> mtppendlist){
		this.mtppendlist = mtppendlist;
	}

	public List<MtppendlistItem> getMtppendlist(){
		return mtppendlist;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
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
			"NextMTPListRes{" + 
			"mtppendlist = '" + mtppendlist + '\'' + 
			",error = '" + error + '\'' + 
			",confirmed = '" + confirmed + '\'' + 
			",errormsg = '" + errormsg + '\'' + 
			"}";
		}
}