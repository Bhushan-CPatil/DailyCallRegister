package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class ConfirmDCRRes{

	@SerializedName("PURL")
	private String pURL;

	@SerializedName("popuphrconnect")
	private boolean popuphrconnect;

	@SerializedName("error")
	private boolean error;

	@SerializedName("errormsg")
	private String errormsg;

	public void setPURL(String pURL){
		this.pURL = pURL;
	}

	public String getPURL(){
		return pURL;
	}

	public void setPopuphrconnect(boolean popuphrconnect){
		this.popuphrconnect = popuphrconnect;
	}

	public boolean isPopuphrconnect(){
		return popuphrconnect;
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
			"ConfirmDCRRes{" + 
			"pURL = '" + pURL + '\'' + 
			",popuphrconnect = '" + popuphrconnect + '\'' + 
			",error = '" + error + '\'' + 
			",errormsg = '" + errormsg + '\'' + 
			"}";
		}
}