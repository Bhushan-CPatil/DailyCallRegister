package com.eis.dailycallregister.Pojo;


import com.google.gson.annotations.SerializedName;


public class DcrproductlistItem{

	@SerializedName("PNAME")
	private String pNAME;

	@SerializedName("GRP")
	private String gRP;

	@SerializedName("ABV")
	private String aBV;

	@SerializedName("QTY")
	private String qTY;

	@SerializedName("DETFLAG")
	private String dETFLAG;

	@SerializedName("RxQTY")
	private String rxQTY;

	@SerializedName("DEMO")
	private String dEMO;

	@SerializedName("PRODID")
	private String pRODID;

	@SerializedName("BAL")
	private String BAL;

	public String getBAL() {
		return BAL;
	}

	public void setBAL(String BAL) {
		this.BAL = BAL;
	}

	public void setPNAME(String pNAME){
		this.pNAME = pNAME;
	}

	public String getPNAME(){
		return pNAME;
	}

	public void setGRP(String gRP){
		this.gRP = gRP;
	}

	public String getGRP(){
		return gRP;
	}

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

	public void setDETFLAG(String dETFLAG){
		this.dETFLAG = dETFLAG;
	}

	public String getDETFLAG(){
		return dETFLAG;
	}

	public void setRxQTY(String rxQTY){
		this.rxQTY = rxQTY;
	}

	public String getRxQTY(){
		return rxQTY;
	}

	public void setDEMO(String dEMO){
		this.dEMO = dEMO;
	}

	public String getDEMO(){
		return dEMO;
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
			"DcrproductlistItem{" + 
			"pNAME = '" + pNAME + '\'' + 
			",gRP = '" + gRP + '\'' + 
			",aBV = '" + aBV + '\'' + 
			",qTY = '" + qTY + '\'' + 
			",dETFLAG = '" + dETFLAG + '\'' + 
			",rxQTY = '" + rxQTY + '\'' + 
			",dEMO = '" + dEMO + '\'' + 
			",pRODID = '" + pRODID + '\'' + 
			",BAL = '" + BAL + '\'' +
			"}";
		}
}