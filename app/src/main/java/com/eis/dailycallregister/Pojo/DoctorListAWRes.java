package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class DoctorListAWRes{

	@SerializedName("error")
	private boolean error;

	@SerializedName("docinaw")
	private List<DocinawItem> docinaw;

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setDocinaw(List<DocinawItem> docinaw){
		this.docinaw = docinaw;
	}

	public List<DocinawItem> getDocinaw(){
		return docinaw;
	}

	@Override
 	public String toString(){
		return 
			"DoctorListAWRes{" + 
			"error = '" + error + '\'' + 
			",docinaw = '" + docinaw + '\'' + 
			"}";
		}
}