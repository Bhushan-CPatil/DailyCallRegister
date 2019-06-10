package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MissCallDocsRes{

	@SerializedName("misscalldrs")
	private List<MisscalldrsItem> misscalldrs;

	@SerializedName("error")
	private boolean error;

	public void setMisscalldrs(List<MisscalldrsItem> misscalldrs){
		this.misscalldrs = misscalldrs;
	}

	public List<MisscalldrsItem> getMisscalldrs(){
		return misscalldrs;
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
			"MissCallDocsRes{" + 
			"misscalldrs = '" + misscalldrs + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}