package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class VstCardDrLstRes{

	@SerializedName("visitingCardDRLst")
	private List<VisitingCardDRLstItem> visitingCardDRLst;

	public void setVisitingCardDRLst(List<VisitingCardDRLstItem> visitingCardDRLst){
		this.visitingCardDRLst = visitingCardDRLst;
	}

	public List<VisitingCardDRLstItem> getVisitingCardDRLst(){
		return visitingCardDRLst;
	}

	@Override
 	public String toString(){
		return 
			"VstCardDrLstRes{" + 
			"visitingCardDRLst = '" + visitingCardDRLst + '\'' + 
			"}";
		}
}