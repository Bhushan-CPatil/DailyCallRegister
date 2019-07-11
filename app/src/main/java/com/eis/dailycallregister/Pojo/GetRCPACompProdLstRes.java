package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetRCPACompProdLstRes{

	@SerializedName("error")
	private boolean error;

	@SerializedName("rcpacomplist")
	private List<RcpacomplistItem> rcpacomplist;

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setRcpacomplist(List<RcpacomplistItem> rcpacomplist){
		this.rcpacomplist = rcpacomplist;
	}

	public List<RcpacomplistItem> getRcpacomplist(){
		return rcpacomplist;
	}

	@Override
 	public String toString(){
		return 
			"GetRCPACompProdLstRes{" + 
			"error = '" + error + '\'' + 
			",rcpacomplist = '" + rcpacomplist + '\'' + 
			"}";
		}
}