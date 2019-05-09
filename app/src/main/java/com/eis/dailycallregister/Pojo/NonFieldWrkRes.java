package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NonFieldWrkRes{

	@SerializedName("nonworking")
	private List<NonworkingItem> nonworking;

	@SerializedName("nonjointwrk")
	private List<NonjointwrkItem> nonjointwrk;

	@SerializedName("working")
	private List<WorkingItem> working;

	@SerializedName("nonwrkarea")
	private List<NonwrkareaItem> nonwrkarea;

	@SerializedName("errornonwrk")
	private boolean errornonwrk;

	@SerializedName("errorwrk")
	private boolean errorwrk;

	public void setNonworking(List<NonworkingItem> nonworking){
		this.nonworking = nonworking;
	}

	public List<NonworkingItem> getNonworking(){
		return nonworking;
	}

	public void setNonjointwrk(List<NonjointwrkItem> nonjointwrk){
		this.nonjointwrk = nonjointwrk;
	}

	public List<NonjointwrkItem> getNonjointwrk(){
		return nonjointwrk;
	}

	public void setWorking(List<WorkingItem> working){
		this.working = working;
	}

	public List<WorkingItem> getWorking(){
		return working;
	}

	public void setNonwrkarea(List<NonwrkareaItem> nonwrkarea){
		this.nonwrkarea = nonwrkarea;
	}

	public List<NonwrkareaItem> getNonwrkarea(){
		return nonwrkarea;
	}

	public void setErrornonwrk(boolean errornonwrk){
		this.errornonwrk = errornonwrk;
	}

	public boolean isErrornonwrk(){
		return errornonwrk;
	}

	public void setErrorwrk(boolean errorwrk){
		this.errorwrk = errorwrk;
	}

	public boolean isErrorwrk(){
		return errorwrk;
	}

	@Override
 	public String toString(){
		return 
			"NonFieldWrkRes{" + 
			"nonworking = '" + nonworking + '\'' + 
			",nonjointwrk = '" + nonjointwrk + '\'' + 
			",working = '" + working + '\'' + 
			",nonwrkarea = '" + nonwrkarea + '\'' + 
			",errornonwrk = '" + errornonwrk + '\'' + 
			",errorwrk = '" + errorwrk + '\'' + 
			"}";
		}
}