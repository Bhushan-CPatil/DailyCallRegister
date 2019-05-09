package com.eis.dailycallregister.Pojo;


import com.google.gson.annotations.SerializedName;


public class DcrgiftslistItem{

	@SerializedName("PNAME")
	private String pNAME;

	@SerializedName("GRP")
	private String gRP;

	@SerializedName("ABV")
	private String aBV;

	@SerializedName("QTY")
	private String qTY;

	@SerializedName("DEMO")
	private String dEMO;

	@SerializedName("PRODID")
	private String pRODID;

	@SerializedName("CAMPFLG")
	private String cAMPFLG;

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

	public void setCAMPFLG(String cAMPFLG){
		this.cAMPFLG = cAMPFLG;
	}

	public String getCAMPFLG(){
		return cAMPFLG;
	}

	@Override
 	public String toString(){
		return 
			"DcrgiftslistItem{" + 
			"pNAME = '" + pNAME + '\'' + 
			",gRP = '" + gRP + '\'' + 
			",aBV = '" + aBV + '\'' + 
			",qTY = '" + qTY + '\'' + 
			",dEMO = '" + dEMO + '\'' + 
			",pRODID = '" + pRODID + '\'' + 
			",cAMPFLG = '" + cAMPFLG + '\'' + 
			",BAL = '" + BAL + '\'' +
			"}";
		}
}