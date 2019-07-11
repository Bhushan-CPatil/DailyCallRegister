package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class RcpapulsechemistItem{

	@SerializedName("cntcd")
	private String cntcd;

	@SerializedName("stname")
	private String stname;

	public void setCntcd(String cntcd){
		this.cntcd = cntcd;
	}

	public String getCntcd(){
		return cntcd;
	}

	public void setStname(String stname){
		this.stname = stname;
	}

	public String getStname(){
		return stname;
	}

	@Override
 	public String toString(){
		return 
			"RcpapulsechemistItem{" + 
			"cntcd = '" + cntcd + '\'' + 
			",stname = '" + stname + '\'' + 
			"}";
		}
}