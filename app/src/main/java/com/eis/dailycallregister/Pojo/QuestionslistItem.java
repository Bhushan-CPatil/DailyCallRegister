package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class QuestionslistItem{

	@SerializedName("ansdesc")
	private String ansdesc;

	@SerializedName("entdate")
	private String entdate;

	@SerializedName("ans")
	private String ans;

	@SerializedName("subans")
	private String subans;

	@SerializedName("subansdesc")
	private String subansdesc;

	@SerializedName("qid")
	private String qid;

	@SerializedName("qdescrpn")
	private String qdescrpn;

	public void setAnsdesc(String ansdesc){
		this.ansdesc = ansdesc;
	}

	public String getAnsdesc(){
		return ansdesc;
	}

	public void setEntdate(String entdate){
		this.entdate = entdate;
	}

	public String getEntdate(){
		return entdate;
	}

	public void setAns(String ans){
		this.ans = ans;
	}

	public String getAns(){
		return ans;
	}

	public void setSubans(String subans){
		this.subans = subans;
	}

	public String getSubans(){
		return subans;
	}

	public void setSubansdesc(String subansdesc){
		this.subansdesc = subansdesc;
	}

	public String getSubansdesc(){
		return subansdesc;
	}

	public void setQid(String qid){
		this.qid = qid;
	}

	public String getQid(){
		return qid;
	}

	public void setQdescrpn(String qdescrpn){
		this.qdescrpn = qdescrpn;
	}

	public String getQdescrpn(){
		return qdescrpn;
	}

	@Override
 	public String toString(){
		return 
			"QuestionslistItem{" + 
			"ansdesc = '" + ansdesc + '\'' + 
			",entdate = '" + entdate + '\'' + 
			",ans = '" + ans + '\'' + 
			",subans = '" + subans + '\'' + 
			",subansdesc = '" + subansdesc + '\'' + 
			",qid = '" + qid + '\'' + 
			",qdescrpn = '" + qdescrpn + '\'' + 
			"}";
		}
}