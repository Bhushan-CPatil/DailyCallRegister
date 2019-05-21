package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class RedicnePopUpRes{

	@SerializedName("DCRRidacneDet")
	private List<DCRRidacneDetItem> dCRRidacneDet;

	public void setDCRRidacneDet(List<DCRRidacneDetItem> dCRRidacneDet){
		this.dCRRidacneDet = dCRRidacneDet;
	}

	public List<DCRRidacneDetItem> getDCRRidacneDet(){
		return dCRRidacneDet;
	}

	@Override
 	public String toString(){
		return 
			"RedicnePopUpRes{" + 
			"dCRRidacneDet = '" + dCRRidacneDet + '\'' + 
			"}";
		}
}