package com.eis.dailycallregister.Pojo;

import com.google.gson.annotations.SerializedName;

public class DCRGiftSplDrDetItem{

	@SerializedName("DRNAME")
	private String dRNAME;

	@SerializedName("NoQSeraHairSerumRx")
	private String noQSeraHairSerumRx;

	@SerializedName("AvailabilityDate")
	private String availabilityDate;

	@SerializedName("DemoSessionGiven")
	private String demoSessionGiven;

	@SerializedName("prescriptiondate")
	private String prescriptiondate;

	@SerializedName("Noofunitsold")
	private String noofunitsold;

	@SerializedName("TradeDis")
	private String tradeDis;

	@SerializedName("qty")
	private String qty;

	@SerializedName("Doctorsfeedback")
	private String doctorsfeedback;

	@SerializedName("launchdate")
	private String launchdate;

	@SerializedName("Chemistname")
	private String chemistname;

	@SerializedName("currentdate")
	private String currentdate;

	@SerializedName("DemoSessionGivenDate")
	private String demoSessionGivenDate;

	@SerializedName("CNTCD")
	private String cNTCD;

	@SerializedName("madeAvailableAtPulseChem")
	private String madeAvailableAtPulseChem;

	public void setDRNAME(String dRNAME){
		this.dRNAME = dRNAME;
	}

	public String getDRNAME(){
		return dRNAME;
	}

	public void setNoQSeraHairSerumRx(String noQSeraHairSerumRx){
		this.noQSeraHairSerumRx = noQSeraHairSerumRx;
	}

	public String getNoQSeraHairSerumRx(){
		return noQSeraHairSerumRx;
	}

	public void setAvailabilityDate(String availabilityDate){
		this.availabilityDate = availabilityDate;
	}

	public String getAvailabilityDate(){
		return availabilityDate;
	}

	public void setDemoSessionGiven(String demoSessionGiven){
		this.demoSessionGiven = demoSessionGiven;
	}

	public String getDemoSessionGiven(){
		return demoSessionGiven;
	}

	public void setPrescriptiondate(String prescriptiondate){
		this.prescriptiondate = prescriptiondate;
	}

	public String getPrescriptiondate(){
		return prescriptiondate;
	}

	public void setNoofunitsold(String noofunitsold){
		this.noofunitsold = noofunitsold;
	}

	public String getNoofunitsold(){
		return noofunitsold;
	}

	public void setTradeDis(String tradeDis){
		this.tradeDis = tradeDis;
	}

	public String getTradeDis(){
		return tradeDis;
	}

	public void setQty(String qty){
		this.qty = qty;
	}

	public String getQty(){
		return qty;
	}

	public void setDoctorsfeedback(String doctorsfeedback){
		this.doctorsfeedback = doctorsfeedback;
	}

	public String getDoctorsfeedback(){
		return doctorsfeedback;
	}

	public void setLaunchdate(String launchdate){
		this.launchdate = launchdate;
	}

	public String getLaunchdate(){
		return launchdate;
	}

	public void setChemistname(String chemistname){
		this.chemistname = chemistname;
	}

	public String getChemistname(){
		return chemistname;
	}

	public void setCurrentdate(String currentdate){
		this.currentdate = currentdate;
	}

	public String getCurrentdate(){
		return currentdate;
	}

	public void setDemoSessionGivenDate(String demoSessionGivenDate){
		this.demoSessionGivenDate = demoSessionGivenDate;
	}

	public String getDemoSessionGivenDate(){
		return demoSessionGivenDate;
	}

	public void setCNTCD(String cNTCD){
		this.cNTCD = cNTCD;
	}

	public String getCNTCD(){
		return cNTCD;
	}

	public void setMadeAvailableAtPulseChem(String madeAvailableAtPulseChem){
		this.madeAvailableAtPulseChem = madeAvailableAtPulseChem;
	}

	public String getMadeAvailableAtPulseChem(){
		return madeAvailableAtPulseChem;
	}

	@Override
 	public String toString(){
		return 
			"DCRGiftSplDrDetItem{" + 
			"dRNAME = '" + dRNAME + '\'' + 
			",noQSeraHairSerumRx = '" + noQSeraHairSerumRx + '\'' + 
			",availabilityDate = '" + availabilityDate + '\'' + 
			",demoSessionGiven = '" + demoSessionGiven + '\'' + 
			",prescriptiondate = '" + prescriptiondate + '\'' + 
			",noofunitsold = '" + noofunitsold + '\'' + 
			",tradeDis = '" + tradeDis + '\'' + 
			",qty = '" + qty + '\'' + 
			",doctorsfeedback = '" + doctorsfeedback + '\'' + 
			",launchdate = '" + launchdate + '\'' + 
			",chemistname = '" + chemistname + '\'' + 
			",currentdate = '" + currentdate + '\'' + 
			",demoSessionGivenDate = '" + demoSessionGivenDate + '\'' + 
			",cNTCD = '" + cNTCD + '\'' + 
			",madeAvailableAtPulseChem = '" + madeAvailableAtPulseChem + '\'' + 
			"}";
		}
}