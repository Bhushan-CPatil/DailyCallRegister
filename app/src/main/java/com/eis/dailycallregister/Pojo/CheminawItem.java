package com.eis.dailycallregister.Pojo;


import com.google.gson.annotations.SerializedName;

public class CheminawItem{

	@SerializedName("STTYPE")
	private String sTTYPE;

	@SerializedName("STCD")
	private String sTCD;

	@SerializedName("STNAME")
	private String sTNAME;

	@SerializedName("CNTCD")
	private String cNTCD;

	public void setSTTYPE(String sTTYPE){
		this.sTTYPE = sTTYPE;
	}

	public String getSTTYPE(){
		return sTTYPE;
	}

	public void setSTCD(String sTCD){
		this.sTCD = sTCD;
	}

	public String getSTCD(){
		return sTCD;
	}

	public void setSTNAME(String sTNAME){
		this.sTNAME = sTNAME;
	}

	public String getSTNAME(){
		return sTNAME;
	}

	public void setCNTCD(String cNTCD){
		this.cNTCD = cNTCD;
	}

	public String getCNTCD(){
		return cNTCD;
	}

	@Override
 	public String toString(){
		return 
			"CheminawItem{" + 
			"sTTYPE = '" + sTTYPE + '\'' + 
			",sTCD = '" + sTCD + '\'' + 
			",sTNAME = '" + sTNAME + '\'' + 
			",cNTCD = '" + cNTCD + '\'' + 
			"}";
		}
}