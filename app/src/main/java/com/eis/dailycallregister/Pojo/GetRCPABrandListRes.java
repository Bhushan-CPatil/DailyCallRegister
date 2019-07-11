package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetRCPABrandListRes{

	@SerializedName("rcpabrandlist")
	private List<RcpabrandlistItem> rcpabrandlist;

	@SerializedName("error")
	private boolean error;

	public void setRcpabrandlist(List<RcpabrandlistItem> rcpabrandlist){
		this.rcpabrandlist = rcpabrandlist;
	}

	public List<RcpabrandlistItem> getRcpabrandlist(){
		return rcpabrandlist;
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
			"GetRCPABrandListRes{" + 
			"rcpabrandlist = '" + rcpabrandlist + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}