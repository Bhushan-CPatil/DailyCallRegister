package com.eis.dailycallregister.Pojo;


import com.google.gson.annotations.SerializedName;

public class DocinawItem{

	@SerializedName("TEMPTCP")
	private String tEMPTCP;

	@SerializedName("DRCD")
	private String dRCD;

	@SerializedName("DRNAME")
	private String dRNAME;

	@SerializedName("CNTCD")
	private String cNTCD;

	public void setTEMPTCP(String tEMPTCP){
		this.tEMPTCP = tEMPTCP;
	}

	public String getTEMPTCP(){
		return tEMPTCP;
	}

	public void setDRCD(String dRCD){
		this.dRCD = dRCD;
	}

	public String getDRCD(){
		return dRCD;
	}

	public void setDRNAME(String dRNAME){
		this.dRNAME = dRNAME;
	}

	public String getDRNAME(){
		return dRNAME;
	}

	public void setCNTCD(String cNTCD){
		this.cNTCD = cNTCD;
	}

	public String getCNTCD(){
		return cNTCD;
	}

	@Override
 	public String toString(){
		return 
			"DocinawItem{" + 
			"tEMPTCP = '" + tEMPTCP + '\'' +
			",dRCD = '" + dRCD + '\'' +
			",dRNAME = '" + dRNAME + '\'' +
			",cNTCD = '" + cNTCD + '\'' + 
			"}";
		}
}