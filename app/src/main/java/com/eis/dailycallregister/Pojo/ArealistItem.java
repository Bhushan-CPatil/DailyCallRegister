package com.eis.dailycallregister.Pojo;


import com.google.gson.annotations.SerializedName;

public class ArealistItem{

	@SerializedName("WTYPE")
	private String wTYPE;

	@SerializedName("TCPID")
	private String tCPID;

	@SerializedName("TOWNID")
	private String tOWNID;

	@SerializedName("TOWN")
	private String tOWN;

	@SerializedName("status")
	private String status;

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

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ArealistItem{" + 
			"wTYPE = '" + wTYPE + '\'' + 
			",tCPID = '" + tCPID + '\'' + 
			",tOWNID = '" + tOWNID + '\'' + 
			",tOWN = '" + tOWN + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}