package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class MtppendlistItem{

	@SerializedName("TOWN")
	private String tOWN;

	@SerializedName("OBJECTIVE")
	private String oBJECTIVE;

	@SerializedName("JOINTWORKING")
	private String jOINTWORKING;

	@SerializedName("MRNETID")
	private String mRNETID;

	@SerializedName("WORKDATE")
	private String wORKDATE;

	public void setTOWN(String tOWN){
		this.tOWN = tOWN;
	}

	public String getTOWN(){
		return tOWN;
	}

	public void setOBJECTIVE(String oBJECTIVE){
		this.oBJECTIVE = oBJECTIVE;
	}

	public String getOBJECTIVE(){
		return oBJECTIVE;
	}

	public void setJOINTWORKING(String jOINTWORKING){
		this.jOINTWORKING = jOINTWORKING;
	}

	public String getJOINTWORKING(){
		return jOINTWORKING;
	}

	public void setMRNETID(String mRNETID){
		this.mRNETID = mRNETID;
	}

	public String getMRNETID(){
		return mRNETID;
	}

	public void setWORKDATE(String wORKDATE){
		this.wORKDATE = wORKDATE;
	}

	public String getWORKDATE(){
		return wORKDATE;
	}

	@Override
 	public String toString(){
		return 
			"MtppendlistItem{" + 
			"tOWN = '" + tOWN + '\'' + 
			",oBJECTIVE = '" + oBJECTIVE + '\'' + 
			",jOINTWORKING = '" + jOINTWORKING + '\'' + 
			",mRNETID = '" + mRNETID + '\'' + 
			",wORKDATE = '" + wORKDATE + '\'' + 
			"}";
		}
}