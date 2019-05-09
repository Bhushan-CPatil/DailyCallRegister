package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class WorkingItem{

	@SerializedName("TCPID")
	private String tCPID;

	@SerializedName("TOWN")
	private String tOWN;

	@SerializedName("DRCHCALLSFLG")
	private String dRCHCALLSFLG;

	@SerializedName("WWITH")
	private String wWITH;

	public void setTCPID(String tCPID){
		this.tCPID = tCPID;
	}

	public String getTCPID(){
		return tCPID;
	}

	public void setTOWN(String tOWN){
		this.tOWN = tOWN;
	}

	public String getTOWN(){
		return tOWN;
	}

	public void setDRCHCALLSFLG(String dRCHCALLSFLG){
		this.dRCHCALLSFLG = dRCHCALLSFLG;
	}

	public String getDRCHCALLSFLG(){
		return dRCHCALLSFLG;
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
			"WorkingItem{" + 
			"tCPID = '" + tCPID + '\'' + 
			",tOWN = '" + tOWN + '\'' + 
			",dRCHCALLSFLG = '" + dRCHCALLSFLG + '\'' + 
			",wWITH = '" + wWITH + '\'' + 
			"}";
		}
}