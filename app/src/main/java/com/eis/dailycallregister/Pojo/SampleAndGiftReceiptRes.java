package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SampleAndGiftReceiptRes{

	@SerializedName("SampleAndGiftReceipt")
	private List<SampleAndGiftReceiptItem> sampleAndGiftReceipt;

	public void setSampleAndGiftReceipt(List<SampleAndGiftReceiptItem> sampleAndGiftReceipt){
		this.sampleAndGiftReceipt = sampleAndGiftReceipt;
	}

	public List<SampleAndGiftReceiptItem> getSampleAndGiftReceipt(){
		return sampleAndGiftReceipt;
	}

	@Override
 	public String toString(){
		return 
			"SampleAndGiftReceiptRes{" + 
			"sampleAndGiftReceipt = '" + sampleAndGiftReceipt + '\'' + 
			"}";
		}
}