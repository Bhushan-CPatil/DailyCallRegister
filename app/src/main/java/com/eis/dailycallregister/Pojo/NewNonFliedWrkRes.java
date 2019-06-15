package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class NewNonFliedWrkRes{

	@SerializedName("nonjointwrk")
	private List<NonjointwrkItem> nonjointwrk;

	@SerializedName("wwith")
	private String wwith;

	@SerializedName("ListOfTown")
	private List<ListOfTownItem> listOfTown;

	@SerializedName("drchshow")
	private String drchshow;

	@SerializedName("nonwrkarea")
	private List<NonwrkareaItem> nonwrkarea;

	@SerializedName("NoRecordsExists")
	private boolean noRecordsExists;

	public void setNonjointwrk(List<NonjointwrkItem> nonjointwrk){
		this.nonjointwrk = nonjointwrk;
	}

	public List<NonjointwrkItem> getNonjointwrk(){
		return nonjointwrk;
	}

	public void setWwith(String wwith){
		this.wwith = wwith;
	}

	public String getWwith(){
		return wwith;
	}

	public void setListOfTown(List<ListOfTownItem> listOfTown){
		this.listOfTown = listOfTown;
	}

	public List<ListOfTownItem> getListOfTown(){
		return listOfTown;
	}

	public void setDrchshow(String drchshow){
		this.drchshow = drchshow;
	}

	public String getDrchshow(){
		return drchshow;
	}

	public void setNonwrkarea(List<NonwrkareaItem> nonwrkarea){
		this.nonwrkarea = nonwrkarea;
	}

	public List<NonwrkareaItem> getNonwrkarea(){
		return nonwrkarea;
	}

	public void setNoRecordsExists(boolean noRecordsExists){
		this.noRecordsExists = noRecordsExists;
	}

	public boolean isNoRecordsExists(){
		return noRecordsExists;
	}

	@Override
 	public String toString(){
		return 
			"NewNonFliedWrkRes{" + 
			"nonjointwrk = '" + nonjointwrk + '\'' + 
			",wwith = '" + wwith + '\'' + 
			",listOfTown = '" + listOfTown + '\'' + 
			",drchshow = '" + drchshow + '\'' + 
			",nonwrkarea = '" + nonwrkarea + '\'' + 
			",noRecordsExists = '" + noRecordsExists + '\'' + 
			"}";
		}
}