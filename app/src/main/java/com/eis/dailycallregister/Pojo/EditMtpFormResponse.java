package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class EditMtpFormResponse{

	@SerializedName("EDITMTPTCPIDNTOWNS")
	private List<EDITMTPTCPIDNTOWNSItem> eDITMTPTCPIDNTOWNS;

	@SerializedName("error")
	private boolean error;

	@SerializedName("errormsg")
	private String errormsg;

	public void setEDITMTPTCPIDNTOWNS(List<EDITMTPTCPIDNTOWNSItem> eDITMTPTCPIDNTOWNS){
		this.eDITMTPTCPIDNTOWNS = eDITMTPTCPIDNTOWNS;
	}

	public List<EDITMTPTCPIDNTOWNSItem> getEDITMTPTCPIDNTOWNS(){
		return eDITMTPTCPIDNTOWNS;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setErrormsg(String errormsg){
		this.errormsg = errormsg;
	}

	public String getErrormsg(){
		return errormsg;
	}

	@Override
 	public String toString(){
		return 
			"EditMtpFormResponse{" + 
			"eDITMTPTCPIDNTOWNS = '" + eDITMTPTCPIDNTOWNS + '\'' + 
			",error = '" + error + '\'' + 
			",errormsg = '" + errormsg + '\'' + 
			"}";
		}
}