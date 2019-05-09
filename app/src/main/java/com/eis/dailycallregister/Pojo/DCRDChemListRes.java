package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class DCRDChemListRes{

	@SerializedName("dcrdchlst")
	private List<DcrdchlstItem> dcrdchlst;

	@SerializedName("error")
	private boolean error;

	public void setDcrdchlst(List<DcrdchlstItem> dcrdchlst){
		this.dcrdchlst = dcrdchlst;
	}

	public List<DcrdchlstItem> getDcrdchlst(){
		return dcrdchlst;
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
			"DCRDChemListRes{" + 
			"dcrdchlst = '" + dcrdchlst + '\'' + 
			",error = '" + error + '\'' + 
			"}";
		}
}