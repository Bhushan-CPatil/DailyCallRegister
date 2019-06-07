package com.eis.dailycallregister.Pojo;


import com.google.gson.annotations.SerializedName;

public class GetDcrDateRes{

	@SerializedName("dcrdate")
	private String dcrdate;

	@SerializedName("newflg")
	private boolean newflg;

	@SerializedName("error")
	private boolean error;

	@SerializedName("errormsg")
	private String errormsg;

	@SerializedName("remark")
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setDcrdate(String dcrdate){
		this.dcrdate = dcrdate;
	}

	public String getDcrdate(){
		return dcrdate;
	}

	public void setNewflg(boolean newflg){
		this.newflg = newflg;
	}

	public boolean isNewflg(){
		return newflg;
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setErrormsg(String errormsg){
		this.errormsg = errormsg;
	}

	public String getErrormsg(){
		return errormsg;
	}

	@Override
 	public String toString(){
		return 
			"GetDcrDateRes{" + 
			"dcrdate = '" + dcrdate + '\'' + 
			",newflg = '" + newflg + '\'' + 
			",error = '" + error + '\'' + 
			",errormsg = '" + errormsg + '\'' + 
			",remark = '" + remark + '\'' +
			"}";
		}
}