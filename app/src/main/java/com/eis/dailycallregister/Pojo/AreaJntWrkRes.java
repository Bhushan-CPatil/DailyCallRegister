package com.eis.dailycallregister.Pojo;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class AreaJntWrkRes{

	@SerializedName("arealist")
	private List<ArealistItem> arealist;

	@SerializedName("jointwrk")
	private List<JointwrkItem> jointwrk;

	public void setArealist(List<ArealistItem> arealist){
		this.arealist = arealist;
	}

	public List<ArealistItem> getArealist(){
		return arealist;
	}

	public void setJointwrk(List<JointwrkItem> jointwrk){
		this.jointwrk = jointwrk;
	}

	public List<JointwrkItem> getJointwrk(){
		return jointwrk;
	}

	@Override
 	public String toString(){
		return 
			"AreaJntWrkRes{" + 
			"arealist = '" + arealist + '\'' + 
			",jointwrk = '" + jointwrk + '\'' + 
			"}";
		}
}