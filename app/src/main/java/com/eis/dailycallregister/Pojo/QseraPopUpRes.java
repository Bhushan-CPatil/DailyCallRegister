package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class QseraPopUpRes{

	@SerializedName("DCRGiftSplDrDet")
	private List<DCRGiftSplDrDetItem> dCRGiftSplDrDet;

	public void setDCRGiftSplDrDet(List<DCRGiftSplDrDetItem> dCRGiftSplDrDet){
		this.dCRGiftSplDrDet = dCRGiftSplDrDet;
	}

	public List<DCRGiftSplDrDetItem> getDCRGiftSplDrDet(){
		return dCRGiftSplDrDet;
	}

	@Override
 	public String toString(){
		return 
			"QseraPopUpRes{" + 
			"dCRGiftSplDrDet = '" + dCRGiftSplDrDet + '\'' + 
			"}";
		}
}