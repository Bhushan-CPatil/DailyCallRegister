package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class ProdgiftdetSummaryItem{

	@SerializedName("VSTTM")
	private String vSTTM;

	@SerializedName("POB")
	private String pOB;

	@SerializedName("TOWN")
	private String tOWN;

	@SerializedName("PRODQTY")
	private String pRODQTY;

	@SerializedName("RX")
	private String rX;

	@SerializedName("GIFTQTY")
	private String gIFTQTY;

	@SerializedName("PRODRQTY")
	private String pRODRQTY;

	@SerializedName("DRSTNAME")
	private String dRSTNAME;

	@SerializedName("DRCD")
	private String dRCD;

	@SerializedName("WWITH")
	private String wWITH;

	public void setVSTTM(String vSTTM){
		this.vSTTM = vSTTM;
	}

	public String getVSTTM(){
		return vSTTM;
	}

	public void setPOB(String pOB){
		this.pOB = pOB;
	}

	public String getPOB(){
		return pOB;
	}

	public void setTOWN(String tOWN){
		this.tOWN = tOWN;
	}

	public String getTOWN(){
		return tOWN;
	}

	public void setPRODQTY(String pRODQTY){
		this.pRODQTY = pRODQTY;
	}

	public String getPRODQTY(){
		return pRODQTY;
	}

	public void setRX(String rX){
		this.rX = rX;
	}

	public String getRX(){
		return rX;
	}

	public void setGIFTQTY(String gIFTQTY){
		this.gIFTQTY = gIFTQTY;
	}

	public String getGIFTQTY(){
		return gIFTQTY;
	}

	public void setPRODRQTY(String pRODRQTY){
		this.pRODRQTY = pRODRQTY;
	}

	public String getPRODRQTY(){
		return pRODRQTY;
	}

	public void setDRSTNAME(String dRSTNAME){
		this.dRSTNAME = dRSTNAME;
	}

	public String getDRSTNAME(){
		return dRSTNAME;
	}

	public void setDRCD(String dRCD){
		this.dRCD = dRCD;
	}

	public String getDRCD(){
		return dRCD;
	}

	public void setWWITH(String wWITH){
		this.wWITH = wWITH;
	}

	public String getWWITH(){
		return wWITH;
	}

	@Override
 	public String toString(){
		return 
			"ProdgiftdetSummaryItem{" + 
			"vSTTM = '" + vSTTM + '\'' + 
			",pOB = '" + pOB + '\'' + 
			",tOWN = '" + tOWN + '\'' + 
			",pRODQTY = '" + pRODQTY + '\'' + 
			",rX = '" + rX + '\'' + 
			",gIFTQTY = '" + gIFTQTY + '\'' + 
			",pRODRQTY = '" + pRODRQTY + '\'' + 
			",dRSTNAME = '" + dRSTNAME + '\'' + 
			",dRCD = '" + dRCD + '\'' + 
			",wWITH = '" + wWITH + '\'' + 
			"}";
		}
}