package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class NonwrkareaItem{

	@SerializedName("WTYPE")
	private String wTYPE;

	@SerializedName("TCPID")
	private String tCPID;

	@SerializedName("TOWNID")
	private String tOWNID;

	@SerializedName("TOWN")
	private String tOWN;

	public void setWTYPE(String wTYPE){
		this.wTYPE = wTYPE;
	}

	public String getWTYPE(){
		return wTYPE;
	}

	public void setTCPID(String tCPID){
		this.tCPID = tCPID;
	}

	public String getTCPID(){
		return tCPID;
	}

	public void setTOWNID(String tOWNID){
		this.tOWNID = tOWNID;
	}

	public String getTOWNID(){
		return tOWNID;
	}

	public void setTOWN(String tOWN){
		this.tOWN = tOWN;
	}

	public String getTOWN(){
		return tOWN;
	}

	@Override
 	public String toString(){
		return 
			"NonwrkareaItem{" + 
			"wTYPE = '" + wTYPE + '\'' + 
			",tCPID = '" + tCPID + '\'' + 
			",tOWNID = '" + tOWNID + '\'' + 
			",tOWN = '" + tOWN + '\'' + 
			"}";
		}
}