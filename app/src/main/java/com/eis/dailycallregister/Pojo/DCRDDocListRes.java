package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DCRDDocListRes{

	@SerializedName("error")
	private boolean error;

	@SerializedName("dcrddrlst")
	private List<DcrddrlstItem> dcrddrlst;

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setDcrddrlst(List<DcrddrlstItem> dcrddrlst){
		this.dcrddrlst = dcrddrlst;
	}

	public List<DcrddrlstItem> getDcrddrlst(){
		return dcrddrlst;
	}

	@Override
 	public String toString(){
		return 
			"DCRDDocListRes{" + 
			"error = '" + error + '\'' + 
			",dcrddrlst = '" + dcrddrlst + '\'' + 
			"}";
		}
}