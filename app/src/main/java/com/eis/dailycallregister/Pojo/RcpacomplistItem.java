package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class RcpacomplistItem{

	@SerializedName("RCPA_ID")
	private String rCPAID;

	@SerializedName("COMP_NAME")
	private String cOMPNAME;

	@SerializedName("NETID")
	private String nETID;

	@SerializedName("YRMTH")
	private String yRMTH;

	@SerializedName("RX")
	private String rX;

	@SerializedName("COMP_ID")
	private String cOMPID;

	@SerializedName("RX_TYPE")
	private String rXTYPE;

	@SerializedName("PRODID")
	private String pRODID;

	@SerializedName("CNTCD")
	private String cNTCD;

	@SerializedName("MODIFIED_DATE")
	private String mODIFIEDDATE;

	public void setRCPAID(String rCPAID){
		this.rCPAID = rCPAID;
	}

	public String getRCPAID(){
		return rCPAID;
	}

	public void setCOMPNAME(String cOMPNAME){
		this.cOMPNAME = cOMPNAME;
	}

	public String getCOMPNAME(){
		return cOMPNAME;
	}

	public void setNETID(String nETID){
		this.nETID = nETID;
	}

	public String getNETID(){
		return nETID;
	}

	public void setYRMTH(String yRMTH){
		this.yRMTH = yRMTH;
	}

	public String getYRMTH(){
		return yRMTH;
	}

	public void setRX(String rX){
		this.rX = rX;
	}

	public String getRX(){
		return rX;
	}

	public void setCOMPID(String cOMPID){
		this.cOMPID = cOMPID;
	}

	public String getCOMPID(){
		return cOMPID;
	}

	public void setRXTYPE(String rXTYPE){
		this.rXTYPE = rXTYPE;
	}

	public String getRXTYPE(){
		return rXTYPE;
	}

	public void setPRODID(String pRODID){
		this.pRODID = pRODID;
	}

	public String getPRODID(){
		return pRODID;
	}

	public void setCNTCD(String cNTCD){
		this.cNTCD = cNTCD;
	}

	public String getCNTCD(){
		return cNTCD;
	}

	public void setMODIFIEDDATE(String mODIFIEDDATE){
		this.mODIFIEDDATE = mODIFIEDDATE;
	}

	public String getMODIFIEDDATE(){
		return mODIFIEDDATE;
	}

	@Override
 	public String toString(){
		return 
			"RcpacomplistItem{" + 
			"rCPA_ID = '" + rCPAID + '\'' + 
			",cOMP_NAME = '" + cOMPNAME + '\'' + 
			",nETID = '" + nETID + '\'' + 
			",yRMTH = '" + yRMTH + '\'' + 
			",rX = '" + rX + '\'' + 
			",cOMP_ID = '" + cOMPID + '\'' + 
			",rX_TYPE = '" + rXTYPE + '\'' + 
			",pRODID = '" + pRODID + '\'' + 
			",cNTCD = '" + cNTCD + '\'' + 
			",mODIFIED_DATE = '" + mODIFIEDDATE + '\'' + 
			"}";
		}
}