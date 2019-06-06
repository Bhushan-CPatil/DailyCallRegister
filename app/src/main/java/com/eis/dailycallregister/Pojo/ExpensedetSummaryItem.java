package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class ExpensedetSummaryItem{

	@SerializedName("AMOUNT")
	private String aMOUNT;

	@SerializedName("EDESC")
	private String eDESC;

	public void setAMOUNT(String aMOUNT){
		this.aMOUNT = aMOUNT;
	}

	public String getAMOUNT(){
		return aMOUNT;
	}

	public void setEDESC(String eDESC){
		this.eDESC = eDESC;
	}

	public String getEDESC(){
		return eDESC;
	}

	@Override
 	public String toString(){
		return 
			"ExpensedetSummaryItem{" + 
			"aMOUNT = '" + aMOUNT + '\'' + 
			",eDESC = '" + eDESC + '\'' + 
			"}";
		}
}