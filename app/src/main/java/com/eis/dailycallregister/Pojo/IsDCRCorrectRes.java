package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class IsDCRCorrectRes{

	@SerializedName("docsmp")
	private boolean docsmp;

	@SerializedName("docgiftnames")
	private String docgiftnames;

	@SerializedName("chem")
	private boolean chem;

	@SerializedName("docgift")
	private boolean docgift;

	@SerializedName("docsmpnames")
	private String docsmpnames;

	@SerializedName("doc")
	private boolean doc;

	public void setDocsmp(boolean docsmp){
		this.docsmp = docsmp;
	}

	public boolean isDocsmp(){
		return docsmp;
	}

	public void setDocgiftnames(String docgiftnames){
		this.docgiftnames = docgiftnames;
	}

	public String getDocgiftnames(){
		return docgiftnames;
	}

	public void setChem(boolean chem){
		this.chem = chem;
	}

	public boolean isChem(){
		return chem;
	}

	public void setDocgift(boolean docgift){
		this.docgift = docgift;
	}

	public boolean isDocgift(){
		return docgift;
	}

	public void setDocsmpnames(String docsmpnames){
		this.docsmpnames = docsmpnames;
	}

	public String getDocsmpnames(){
		return docsmpnames;
	}

	public void setDoc(boolean doc){
		this.doc = doc;
	}

	public boolean isDoc(){
		return doc;
	}

	@Override
 	public String toString(){
		return 
			"IsDCRCorrectRes{" + 
			"docsmp = '" + docsmp + '\'' + 
			",docgiftnames = '" + docgiftnames + '\'' + 
			",chem = '" + chem + '\'' + 
			",docgift = '" + docgift + '\'' + 
			",docsmpnames = '" + docsmpnames + '\'' + 
			",doc = '" + doc + '\'' + 
			"}";
		}
}