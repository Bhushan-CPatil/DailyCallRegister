package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class DCRGiftListRes{

	@SerializedName("dcrgiftslist")
	private List<DcrgiftslistItem> dcrgiftslist;

	public void setDcrgiftslist(List<DcrgiftslistItem> dcrgiftslist){
		this.dcrgiftslist = dcrgiftslist;
	}

	public List<DcrgiftslistItem> getDcrgiftslist(){
		return dcrgiftslist;
	}

	@Override
 	public String toString(){
		return 
			"DCRGiftListRes{" + 
			"dcrgiftslist = '" + dcrgiftslist + '\'' + 
			"}";
		}
}