package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class ProductSummaryItem{

	@SerializedName("ABV")
	private String aBV;

	@SerializedName("QTY")
	private String qTY;

	@SerializedName("COUNT")
	private String cOUNT;

	@SerializedName("PRODID")
	private String pRODID;

	public void setABV(String aBV){
		this.aBV = aBV;
	}

	public String getABV(){
		return aBV;
	}

	public void setQTY(String qTY){
		this.qTY = qTY;
	}

	public String getQTY(){
		return qTY;
	}

	public void setCOUNT(String cOUNT){
		this.cOUNT = cOUNT;
	}

	public String getCOUNT(){
		return cOUNT;
	}

	public void setPRODID(String pRODID){
		this.pRODID = pRODID;
	}

	public String getPRODID(){
		return pRODID;
	}

	@Override
 	public String toString(){
		return 
			"ProductSummaryItem{" + 
			"aBV = '" + aBV + '\'' + 
			",qTY = '" + qTY + '\'' + 
			",cOUNT = '" + cOUNT + '\'' + 
			",pRODID = '" + pRODID + '\'' + 
			"}";
		}
}