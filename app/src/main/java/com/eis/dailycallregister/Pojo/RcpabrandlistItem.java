package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class RcpabrandlistItem{

	@SerializedName("pname")
	private String pname;

	@SerializedName("RX")
	private String rX;

	@SerializedName("RX_TYPE")
	private String rXTYPE;

	@SerializedName("prodid")
	private String prodid;

	public void setPname(String pname){
		this.pname = pname;
	}

	public String getPname(){
		return pname;
	}

	public void setRX(String rX){
		this.rX = rX;
	}

	public String getRX(){
		return rX;
	}

	public void setRXTYPE(String rXTYPE){
		this.rXTYPE = rXTYPE;
	}

	public String getRXTYPE(){
		return rXTYPE;
	}

	public void setProdid(String prodid){
		this.prodid = prodid;
	}

	public String getProdid(){
		return prodid;
	}

	@Override
 	public String toString(){
		return 
			"RcpabrandlistItem{" + 
			"pname = '" + pname + '\'' + 
			",rX = '" + rX + '\'' + 
			",rX_TYPE = '" + rXTYPE + '\'' + 
			",prodid = '" + prodid + '\'' + 
			"}";
		}
}