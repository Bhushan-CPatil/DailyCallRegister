package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetRCPAPulseChemist{

	@SerializedName("rcpapulsechemist")
	private List<RcpapulsechemistItem> rcpapulsechemist;

	@SerializedName("error")
	private boolean error;

	public void setRcpapulsechemist(List<RcpapulsechemistItem> rcpapulsechemist){
		this.rcpapulsechemist = rcpapulsechemist;
	}

	public List<RcpapulsechemistItem> getRcpapulsechemist(){
		return rcpapulsechemist;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	@Override
 	public String toString(){
		return 
			"GetRCPAPulseChemist{" + 
			"rcpapulsechemist = '" + rcpapulsechemist + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}