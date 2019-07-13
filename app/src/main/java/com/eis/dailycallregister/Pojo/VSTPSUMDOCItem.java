package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class VSTPSUMDOCItem{

	@SerializedName("drcd")
	private String drcd;

	@SerializedName("netid")
	private String netid;

	@SerializedName("drname")
	private String drname;

	@SerializedName("cntcd")
	private String cntcd;

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

	@Override
 	public String toString(){
		return 
			"VSTPSUMDOCItem{" + 
			"drcd = '" + drcd + '\'' + 
			",netid = '" + netid + '\'' + 
			",drname = '" + drname + '\'' + 
			",cntcd = '" + cntcd + '\'' + 
			"}";
		}
}