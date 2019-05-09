package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class DCRProdListRes{

	@SerializedName("dcrproductlist")
	private List<DcrproductlistItem> dcrproductlist;

	public void setDcrproductlist(List<DcrproductlistItem> dcrproductlist){
		this.dcrproductlist = dcrproductlist;
	}

	public List<DcrproductlistItem> getDcrproductlist(){
		return dcrproductlist;
	}

	@Override
 	public String toString(){
		return 
			"DCRProdListRes{" + 
			"dcrproductlist = '" + dcrproductlist + '\'' + 
			"}";
		}
}