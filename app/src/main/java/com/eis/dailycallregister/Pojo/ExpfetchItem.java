package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class ExpfetchItem{

	@SerializedName("amount")
	private String amount;

	@SerializedName("edesc")
	private String edesc;

	@SerializedName("expcd")
	private String expcd;

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setEdesc(String edesc){
		this.edesc = edesc;
	}

	public String getEdesc(){
		return edesc;
	}

	public void setExpcd(String expcd){
		this.expcd = expcd;
	}

	public String getExpcd(){
		return expcd;
	}

	@Override
 	public String toString(){
		return 
			"ExpfetchItem{" + 
			"amount = '" + amount + '\'' + 
			",edesc = '" + edesc + '\'' + 
			",expcd = '" + expcd + '\'' + 
			"}";
		}
}