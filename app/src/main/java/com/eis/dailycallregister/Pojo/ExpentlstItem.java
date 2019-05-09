package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class ExpentlstItem{

	@SerializedName("amount")
	private String amount;

	@SerializedName("EDESC")
	private String eDESC;

	@SerializedName("EXPCD")
	private String eXPCD;

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setEDESC(String eDESC){
		this.eDESC = eDESC;
	}

	public String getEDESC(){
		return eDESC;
	}

	public void setEXPCD(String eXPCD){
		this.eXPCD = eXPCD;
	}

	public String getEXPCD(){
		return eXPCD;
	}

	@Override
 	public String toString(){
		return 
			"ExpentlstItem{" + 
			"amount = '" + amount + '\'' + 
			",eDESC = '" + eDESC + '\'' + 
			",eXPCD = '" + eXPCD + '\'' + 
			"}";
		}
}