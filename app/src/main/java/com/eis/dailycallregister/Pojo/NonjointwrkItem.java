package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class NonjointwrkItem{

	@SerializedName("ENAME")
	private String eNAME;

	@SerializedName("ECODE")
	private String eCODE;

	public void setENAME(String eNAME){
		this.eNAME = eNAME;
	}

	public String getENAME(){
		return eNAME;
	}

	public void setECODE(String eCODE){
		this.eCODE = eCODE;
	}

	public String getECODE(){
		return eCODE;
	}

	@Override
 	public String toString(){
		return 
			"NonjointwrkItem{" + 
			"eNAME = '" + eNAME + '\'' + 
			",eCODE = '" + eCODE + '\'' + 
			"}";
		}
}