package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class ListOfTownItem{

	@SerializedName("Delete")
	private String delete;

	@SerializedName("Area")
	private String area;

	@SerializedName("Type")
	private String type;

	@SerializedName("Selected")
	private String selected;

	@SerializedName("tcpid")
	private String tcpid;

	public void setDelete(String delete){
		this.delete = delete;
	}

	public String getDelete(){
		return delete;
	}

	public void setArea(String area){
		this.area = area;
	}

	public String getArea(){
		return area;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setSelected(String selected){
		this.selected = selected;
	}

	public String getSelected(){
		return selected;
	}

	public void setTcpid(String tcpid){
		this.tcpid = tcpid;
	}

	public String getTcpid(){
		return tcpid;
	}

	@Override
 	public String toString(){
		return 
			"ListOfTownItem{" + 
			"delete = '" + delete + '\'' + 
			",area = '" + area + '\'' + 
			",type = '" + type + '\'' + 
			",selected = '" + selected + '\'' + 
			",tcpid = '" + tcpid + '\'' + 
			"}";
		}
}