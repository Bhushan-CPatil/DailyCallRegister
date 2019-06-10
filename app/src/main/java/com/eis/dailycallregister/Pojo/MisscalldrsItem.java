package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class MisscalldrsItem{

	@SerializedName("TOTAL")
	private String tOTAL;

	@SerializedName("DRNAMES")
	private String dRNAMES;

	@SerializedName("TOWN")
	private String tOWN;

	public void setTOTAL(String tOTAL){
		this.tOTAL = tOTAL;
	}

	public String getTOTAL(){
		return tOTAL;
	}

	public void setDRNAMES(String dRNAMES){
		this.dRNAMES = dRNAMES;
	}

	public String getDRNAMES(){
		return dRNAMES;
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
			"MisscalldrsItem{" + 
			"tOTAL = '" + tOTAL + '\'' + 
			",dRNAMES = '" + dRNAMES + '\'' + 
			",tOWN = '" + tOWN + '\'' + 
			"}";
		}
}