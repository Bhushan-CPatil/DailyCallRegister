package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class ChemistListAWRes{

	@SerializedName("error")
	private boolean error;

	@SerializedName("errormsg")
	private String errormsg;

	@SerializedName("cheminaw")
	private List<CheminawItem> cheminaw;

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

	public void setCheminaw(List<CheminawItem> cheminaw){
		this.cheminaw = cheminaw;
	}

	public List<CheminawItem> getCheminaw(){
		return cheminaw;
	}

	@Override
 	public String toString(){
		return 
			"ChemistListAWRes{" + 
			"error = '" + error + '\'' + 
			",errormsg = '" + errormsg + '\'' + 
			",cheminaw = '" + cheminaw + '\'' + 
			"}";
		}
}