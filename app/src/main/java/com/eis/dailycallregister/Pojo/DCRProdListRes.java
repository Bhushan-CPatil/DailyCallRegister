package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DCRProdListRes{

	@SerializedName("dropgenQ")
	private String dropgenQ;

	@SerializedName("dcrproductlist")
	private List<DcrproductlistItem> dcrproductlist;

	@SerializedName("dropspldrflag")
	private String dropspldrflag;

	@SerializedName("label")
	private String label;

	@SerializedName("dropprodid")
	private String dropprodid;

	public void setDropgenQ(String dropgenQ){
		this.dropgenQ = dropgenQ;
	}

	public String getDropgenQ(){
		return dropgenQ;
	}

	public void setDcrproductlist(List<DcrproductlistItem> dcrproductlist){
		this.dcrproductlist = dcrproductlist;
	}

	public List<DcrproductlistItem> getDcrproductlist(){
		return dcrproductlist;
	}

	public void setDropspldrflag(String dropspldrflag){
		this.dropspldrflag = dropspldrflag;
	}

	public String getDropspldrflag(){
		return dropspldrflag;
	}

	public void setLabel(String label){
		this.label = label;
	}

	public String getLabel(){
		return label;
	}

	public void setDropprodid(String dropprodid){
		this.dropprodid = dropprodid;
	}

	public String getDropprodid(){
		return dropprodid;
	}

	@Override
 	public String toString(){
		return 
			"DCRProdListRes{" + 
			"dropgenQ = '" + dropgenQ + '\'' + 
			",dcrproductlist = '" + dcrproductlist + '\'' + 
			",dropspldrflag = '" + dropspldrflag + '\'' + 
			",label = '" + label + '\'' + 
			",dropprodid = '" + dropprodid + '\'' + 
			"}";
		}
}