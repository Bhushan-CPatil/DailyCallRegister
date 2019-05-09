package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;


public class DBList{

	@SerializedName("dbnames")
	private String dbnames;

	public void setDbnames(String dbnames){
		this.dbnames = dbnames;
	}

	public String getDbnames(){
		return dbnames;
	}

	@Override
 	public String toString(){
		return 
			"DBList{" + 
			"dbnames = '" + dbnames + '\'' + 
			"}";
		}
}