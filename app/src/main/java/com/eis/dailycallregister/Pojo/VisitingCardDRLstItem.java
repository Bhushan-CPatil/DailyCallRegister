package com.eis.dailycallregister.Pojo;


import com.google.gson.annotations.SerializedName;


public class VisitingCardDRLstItem{

	@SerializedName("drcd")
	private String drcd;

	@SerializedName("netid")
	private String netid;

	@SerializedName("drname")
	private String drname;

	@SerializedName("cntcd")
	private String cntcd;

	@SerializedName("status")
	private String status;

	public void setDrcd(String drcd){
		this.drcd = drcd;
	}

	public String getDrcd(){
		return drcd;
	}

	public void setNetid(String netid){
		this.netid = netid;
	}

	public String getNetid(){
		return netid;
	}

	public void setDrname(String drname){
		this.drname = drname;
	}

	public String getDrname(){
		return drname;
	}

	public void setCntcd(String cntcd){
		this.cntcd = cntcd;
	}

	public String getCntcd(){
		return cntcd;
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
			"VisitingCardDRLstItem{" + 
			"drcd = '" + drcd + '\'' + 
			",netid = '" + netid + '\'' + 
			",drname = '" + drname + '\'' + 
			",cntcd = '" + cntcd + '\'' + 
			",status = '" + status + '\'' +
			"}";
		}
}