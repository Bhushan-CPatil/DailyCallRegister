package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GetDCRSummaryMainRes{

	@SerializedName("nopob")
	private String nopob;

	@SerializedName("totexp")
	private String totexp;

	@SerializedName("chemnsvl")
	private String chemnsvl;

	@SerializedName("deduction")
	private String deduction;

	@SerializedName("expensedetSummary")
	private List<ExpensedetSummaryItem> expensedetSummary;

	@SerializedName("giftSummary")
	private List<GiftSummaryItem> giftSummary;

	@SerializedName("productSummary")
	private List<ProductSummaryItem> productSummary;

	@SerializedName("docnsvl")
	private String docnsvl;

	@SerializedName("prodgiftdetSummary")
	private List<ProdgiftdetSummaryItem> prodgiftdetSummary;

	@SerializedName("docsvl")
	private String docsvl;

	@SerializedName("totpob")
	private String totpob;

	@SerializedName("chemsvl")
	private String chemsvl;

	public void setNopob(String nopob){
		this.nopob = nopob;
	}

	public String getNopob(){
		return nopob;
	}

	public void setTotexp(String totexp){
		this.totexp = totexp;
	}

	public String getTotexp(){
		return totexp;
	}

	public void setChemnsvl(String chemnsvl){
		this.chemnsvl = chemnsvl;
	}

	public String getChemnsvl(){
		return chemnsvl;
	}

	public void setDeduction(String deduction){
		this.deduction = deduction;
	}

	public String getDeduction(){
		return deduction;
	}

	public void setExpensedetSummary(List<ExpensedetSummaryItem> expensedetSummary){
		this.expensedetSummary = expensedetSummary;
	}

	public List<ExpensedetSummaryItem> getExpensedetSummary(){
		return expensedetSummary;
	}

	public void setGiftSummary(List<GiftSummaryItem> giftSummary){
		this.giftSummary = giftSummary;
	}

	public List<GiftSummaryItem> getGiftSummary(){
		return giftSummary;
	}

	public void setProductSummary(List<ProductSummaryItem> productSummary){
		this.productSummary = productSummary;
	}

	public List<ProductSummaryItem> getProductSummary(){
		return productSummary;
	}

	public void setDocnsvl(String docnsvl){
		this.docnsvl = docnsvl;
	}

	public String getDocnsvl(){
		return docnsvl;
	}

	public void setProdgiftdetSummary(List<ProdgiftdetSummaryItem> prodgiftdetSummary){
		this.prodgiftdetSummary = prodgiftdetSummary;
	}

	public List<ProdgiftdetSummaryItem> getProdgiftdetSummary(){
		return prodgiftdetSummary;
	}

	public void setDocsvl(String docsvl){
		this.docsvl = docsvl;
	}

	public String getDocsvl(){
		return docsvl;
	}

	public void setTotpob(String totpob){
		this.totpob = totpob;
	}

	public String getTotpob(){
		return totpob;
	}

	public void setChemsvl(String chemsvl){
		this.chemsvl = chemsvl;
	}

	public String getChemsvl(){
		return chemsvl;
	}

	@Override
 	public String toString(){
		return 
			"GetDCRSummaryMainRes{" + 
			"nopob = '" + nopob + '\'' + 
			",totexp = '" + totexp + '\'' + 
			",chemnsvl = '" + chemnsvl + '\'' + 
			",deduction = '" + deduction + '\'' + 
			",expensedetSummary = '" + expensedetSummary + '\'' + 
			",giftSummary = '" + giftSummary + '\'' + 
			",productSummary = '" + productSummary + '\'' + 
			",docnsvl = '" + docnsvl + '\'' + 
			",prodgiftdetSummary = '" + prodgiftdetSummary + '\'' + 
			",docsvl = '" + docsvl + '\'' + 
			",totpob = '" + totpob + '\'' + 
			",chemsvl = '" + chemsvl + '\'' + 
			"}";
		}
}