package com.eis.dailycallregister.Pojo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class EleaningMainRes{

	@SerializedName("showforthtest")
	private boolean showforthtest;

	@SerializedName("showtest")
	private boolean showtest;

	@SerializedName("forthtestlst")
	private List<ForthtestlstItem> forthtestlst;

	@SerializedName("showresult")
	private boolean showresult;

	@SerializedName("testresultlst")
	private List<TestresultlstItem> testresultlst;

	@SerializedName("testlst")
	private List<TestlstItem> testlst;

	public void setShowforthtest(boolean showforthtest){
		this.showforthtest = showforthtest;
	}

	public boolean isShowforthtest(){
		return showforthtest;
	}

	public void setShowtest(boolean showtest){
		this.showtest = showtest;
	}

	public boolean isShowtest(){
		return showtest;
	}

	public void setForthtestlst(List<ForthtestlstItem> forthtestlst){
		this.forthtestlst = forthtestlst;
	}

	public List<ForthtestlstItem> getForthtestlst(){
		return forthtestlst;
	}

	public void setShowresult(boolean showresult){
		this.showresult = showresult;
	}

	public boolean isShowresult(){
		return showresult;
	}

	public void setTestresultlst(List<TestresultlstItem> testresultlst){
		this.testresultlst = testresultlst;
	}

	public List<TestresultlstItem> getTestresultlst(){
		return testresultlst;
	}

	public void setTestlst(List<TestlstItem> testlst){
		this.testlst = testlst;
	}

	public List<TestlstItem> getTestlst(){
		return testlst;
	}

	@Override
 	public String toString(){
		return 
			"EleaningMainRes{" + 
			"showforthtest = '" + showforthtest + '\'' + 
			",showtest = '" + showtest + '\'' + 
			",forthtestlst = '" + forthtestlst + '\'' + 
			",showresult = '" + showresult + '\'' + 
			",testresultlst = '" + testresultlst + '\'' + 
			",testlst = '" + testlst + '\'' + 
			"}";
		}
}