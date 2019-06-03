package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class SampleAndGiftReceiptItem{

	@SerializedName("tcname")
	private String tcname;

	@SerializedName("cocode")
	private String cocode;

	@SerializedName("lrnodocno")
	private String lrnodocno;

	@SerializedName("RecQtySE")
	private String recQtySE;

	@SerializedName("ptype")
	private String ptype;

	@SerializedName("prodid")
	private String prodid;

	@SerializedName("RecQtyD")
	private String recQtyD;

	@SerializedName("TRecQty")
	private String tRecQty;

	@SerializedName("DispatchQty")
	private String dispatchQty;

	@SerializedName("PNAME")
	private String pNAME;

	@SerializedName("lrdatedocdate")
	private String lrdatedocdate;

	@SerializedName("sdid")
	private String sdid;

	@SerializedName("RecQtyDate")
	private String recQtyDate;

	@SerializedName("RecQtyS")
	private String recQtyS;

	public void setTcname(String tcname){
		this.tcname = tcname;
	}

	public String getTcname(){
		return tcname;
	}

	public void setCocode(String cocode){
		this.cocode = cocode;
	}

	public String getCocode(){
		return cocode;
	}

	public void setLrnodocno(String lrnodocno){
		this.lrnodocno = lrnodocno;
	}

	public String getLrnodocno(){
		return lrnodocno;
	}

	public void setRecQtySE(String recQtySE){
		this.recQtySE = recQtySE;
	}

	public String getRecQtySE(){
		return recQtySE;
	}

	public void setPtype(String ptype){
		this.ptype = ptype;
	}

	public String getPtype(){
		return ptype;
	}

	public void setProdid(String prodid){
		this.prodid = prodid;
	}

	public String getProdid(){
		return prodid;
	}

	public void setRecQtyD(String recQtyD){
		this.recQtyD = recQtyD;
	}

	public String getRecQtyD(){
		return recQtyD;
	}

	public void setTRecQty(String tRecQty){
		this.tRecQty = tRecQty;
	}

	public String getTRecQty(){
		return tRecQty;
	}

	public void setDispatchQty(String dispatchQty){
		this.dispatchQty = dispatchQty;
	}

	public String getDispatchQty(){
		return dispatchQty;
	}

	public void setPNAME(String pNAME){
		this.pNAME = pNAME;
	}

	public String getPNAME(){
		return pNAME;
	}

	public void setLrdatedocdate(String lrdatedocdate){
		this.lrdatedocdate = lrdatedocdate;
	}

	public String getLrdatedocdate(){
		return lrdatedocdate;
	}

	public void setSdid(String sdid){
		this.sdid = sdid;
	}

	public String getSdid(){
		return sdid;
	}

	public void setRecQtyDate(String recQtyDate){
		this.recQtyDate = recQtyDate;
	}

	public String getRecQtyDate(){
		return recQtyDate;
	}

	public void setRecQtyS(String recQtyS){
		this.recQtyS = recQtyS;
	}

	public String getRecQtyS(){
		return recQtyS;
	}

	@Override
 	public String toString(){
		return 
			"SampleAndGiftReceiptItem{" + 
			"tcname = '" + tcname + '\'' + 
			",cocode = '" + cocode + '\'' + 
			",lrnodocno = '" + lrnodocno + '\'' + 
			",recQtySE = '" + recQtySE + '\'' + 
			",ptype = '" + ptype + '\'' + 
			",prodid = '" + prodid + '\'' + 
			",recQtyD = '" + recQtyD + '\'' + 
			",tRecQty = '" + tRecQty + '\'' + 
			",dispatchQty = '" + dispatchQty + '\'' + 
			",pNAME = '" + pNAME + '\'' + 
			",lrdatedocdate = '" + lrdatedocdate + '\'' + 
			",sdid = '" + sdid + '\'' + 
			",recQtyDate = '" + recQtyDate + '\'' + 
			",recQtyS = '" + recQtyS + '\'' + 
			"}";
		}
}