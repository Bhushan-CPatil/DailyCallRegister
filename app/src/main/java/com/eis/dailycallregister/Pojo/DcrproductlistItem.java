package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DcrproductlistItem{

	@SerializedName("taggedflag")
	private boolean taggedflag;

	@SerializedName("GRP")
	private String gRP;

	@SerializedName("flag1176")
	private boolean flag1176;

	@SerializedName("flag1187")
	private boolean flag1187;

	@SerializedName("RxQTY")
	private String rxQTY;

	@SerializedName("BAL")
	private String bAL;

	@SerializedName("flag1177")
	private boolean flag1177;

	@SerializedName("PNAME")
	private String pNAME;

	@SerializedName("flag3009")
	private boolean flag3009;

	@SerializedName("spldrflag")
	private boolean spldrflag;

	@SerializedName("ABV")
	private String aBV;

	@SerializedName("QTY")
	private String qTY;

	@SerializedName("DETFLAG")
	private String dETFLAG;

	@SerializedName("dateflag")
	private boolean dateflag;

	@SerializedName("DEMO")
	private String dEMO;

	@SerializedName("PRODID")
	private String pRODID;

	@SerializedName("lastmodifydate")
	private String lastmodifydate;

	@SerializedName("hsbrandid")
	private List<String> hsbrandid;

	public void setTaggedflag(boolean taggedflag){
		this.taggedflag = taggedflag;
	}

	public boolean isTaggedflag(){
		return taggedflag;
	}

	public void setGRP(String gRP){
		this.gRP = gRP;
	}

	public String getGRP(){
		return gRP;
	}

	public void setFlag1176(boolean flag1176){
		this.flag1176 = flag1176;
	}

	public boolean isFlag1176(){
		return flag1176;
	}

	public void setFlag1187(boolean flag1187){
		this.flag1187 = flag1187;
	}

	public boolean isFlag1187(){
		return flag1187;
	}

	public void setRxQTY(String rxQTY){
		this.rxQTY = rxQTY;
	}

	public String getRxQTY(){
		return rxQTY;
	}

	public void setBAL(String bAL){
		this.bAL = bAL;
	}

	public String getBAL(){
		return bAL;
	}

	public void setFlag1177(boolean flag1177){
		this.flag1177 = flag1177;
	}

	public boolean isFlag1177(){
		return flag1177;
	}

	public void setPNAME(String pNAME){
		this.pNAME = pNAME;
	}

	public String getPNAME(){
		return pNAME;
	}

	public void setFlag3009(boolean flag3009){
		this.flag3009 = flag3009;
	}

	public boolean isFlag3009(){
		return flag3009;
	}

	public void setSpldrflag(boolean spldrflag){
		this.spldrflag = spldrflag;
	}

	public boolean isSpldrflag(){
		return spldrflag;
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

	public void setDateflag(boolean dateflag){
		this.dateflag = dateflag;
	}

	public boolean isDateflag(){
		return dateflag;
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

	public void setLastmodifydate(String lastmodifydate){
		this.lastmodifydate = lastmodifydate;
	}

	public String getLastmodifydate(){
		return lastmodifydate;
	}

	public void setHsbrandid(List<String> hsbrandid){
		this.hsbrandid = hsbrandid;
	}

	public List<String> getHsbrandid(){
		return hsbrandid;
	}

	@Override
 	public String toString(){
		return 
			"DcrproductlistItem{" + 
			"taggedflag = '" + taggedflag + '\'' + 
			",gRP = '" + gRP + '\'' + 
			",flag1176 = '" + flag1176 + '\'' + 
			",flag1187 = '" + flag1187 + '\'' + 
			",rxQTY = '" + rxQTY + '\'' + 
			",bAL = '" + bAL + '\'' + 
			",flag1177 = '" + flag1177 + '\'' + 
			",pNAME = '" + pNAME + '\'' + 
			",flag3009 = '" + flag3009 + '\'' + 
			",spldrflag = '" + spldrflag + '\'' + 
			",aBV = '" + aBV + '\'' + 
			",qTY = '" + qTY + '\'' + 
			",dETFLAG = '" + dETFLAG + '\'' + 
			",dateflag = '" + dateflag + '\'' + 
			",dEMO = '" + dEMO + '\'' + 
			",pRODID = '" + pRODID + '\'' + 
			",lastmodifydate = '" + lastmodifydate + '\'' + 
			",hsbrandid = '" + hsbrandid + '\'' + 
			"}";
		}
}