package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class EpidermPopUpRes{

	@SerializedName("DCRProdDemoDet")
	private List<DCRProdDemoDetItem> dCRProdDemoDet;

	public void setDCRProdDemoDet(List<DCRProdDemoDetItem> dCRProdDemoDet){
		this.dCRProdDemoDet = dCRProdDemoDet;
	}

	public List<DCRProdDemoDetItem> getDCRProdDemoDet(){
		return dCRProdDemoDet;
	}

	@Override
 	public String toString(){
		return 
			"EpidermPopUpRes{" + 
			"dCRProdDemoDet = '" + dCRProdDemoDet + '\'' + 
			"}";
		}
}