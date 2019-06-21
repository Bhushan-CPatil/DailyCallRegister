package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class EDITMTPTCPIDNTOWNSItem{

	@SerializedName("TCPID")
	private String tCPID;

	@SerializedName("TOWN")
	private String tOWN;

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

	@Override
 	public String toString(){
		return 
			"EDITMTPTCPIDNTOWNSItem{" + 
			"tCPID = '" + tCPID + '\'' + 
			",tOWN = '" + tOWN + '\'' + 
			"}";
		}
}