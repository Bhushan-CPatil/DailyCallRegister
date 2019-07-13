package com.eis.dailycallregister.Api;


import com.eis.dailycallregister.Pojo.AreaJntWrkRes;
import com.eis.dailycallregister.Pojo.ChemistListAWRes;
import com.eis.dailycallregister.Pojo.ConfirmDCRRes;
import com.eis.dailycallregister.Pojo.DBList;
import com.eis.dailycallregister.Pojo.DCRDChemListRes;
import com.eis.dailycallregister.Pojo.DCRDDocListRes;
import com.eis.dailycallregister.Pojo.DCRExpenseListRes;
import com.eis.dailycallregister.Pojo.DCRGiftListRes;
import com.eis.dailycallregister.Pojo.DCRProdListRes;
import com.eis.dailycallregister.Pojo.DefaultResponse;
import com.eis.dailycallregister.Pojo.DoctorListAWRes;
import com.eis.dailycallregister.Pojo.EditMtpFormResponse;
import com.eis.dailycallregister.Pojo.EpidermPopUpRes;
import com.eis.dailycallregister.Pojo.ErrorBooleanResponce;
import com.eis.dailycallregister.Pojo.FetchExpdtRes;
import com.eis.dailycallregister.Pojo.GetDCRSummaryMainRes;
import com.eis.dailycallregister.Pojo.GetDcrDateRes;
import com.eis.dailycallregister.Pojo.GetPopupQuesRes;
import com.eis.dailycallregister.Pojo.GetRCPABrandListRes;
import com.eis.dailycallregister.Pojo.GetRCPACompProdLstRes;
import com.eis.dailycallregister.Pojo.GetRCPAPulseChemist;
import com.eis.dailycallregister.Pojo.IsDCRCorrectRes;
import com.eis.dailycallregister.Pojo.MissCallDocsRes;
import com.eis.dailycallregister.Pojo.NewMTPListOfMTHRes;
import com.eis.dailycallregister.Pojo.NewNonFliedWrkRes;
import com.eis.dailycallregister.Pojo.NextMTPListRes;
import com.eis.dailycallregister.Pojo.NonFieldWrkRes;
import com.eis.dailycallregister.Pojo.QseraPopUpRes;
import com.eis.dailycallregister.Pojo.RedicnePopUpRes;
import com.eis.dailycallregister.Pojo.SampleAndGiftReceiptRes;
import com.eis.dailycallregister.Pojo.VstCardDrLstRes;
import com.eis.dailycallregister.Pojo.VstPlnDocLstRes;
import com.eis.dailycallregister.Pojo.VstPlnSumRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    //to get division names
    @GET("getdbnames.php")
    Call<DBList> getdblist();

    //to login
    @FormUrlEncoded
    @POST("userloginnew.php")
    Call<DefaultResponse> login(
            @Field("ecode") String ecode,
            @Field("password") String password,
            @Field("date") String date,
            @Field("DBPrefix") String DBPrefix
    );

    //to check weather user is resigned or not
    @FormUrlEncoded
    @POST("changeDCRDate.php")
    Call<DefaultResponse> changeDCRDate(
            @Field("empcode") String empcode,
            @Field("netid") String netid,
            @Field("newdcrdate") String newdcrdate,
            @Field("dcrno") String dcrno,
            @Field("DBPrefix") String DBPrefix
    );

    //to get dcrdate and also checks weather it is greater than current date or not
    @FormUrlEncoded
    @POST("checkdcrdate.php")
    Call<GetDcrDateRes> getDcrdate(
            @Field("empcode") String ecode,
            @Field("netid") String netid,
            @Field("DBPrefix") String DBPrefix
    );

    //to check MTP. It checks weather the MTP is filled or not of current date and also check MTP of next month on 24 of each month
    @FormUrlEncoded
    @POST("checkcurmthmtp.php")
    Call<DefaultResponse> checkMTP(
            @Field("empcode") String ecode,
            @Field("netid") String netid,
            @Field("dcrmth") String dcrmth,
            @Field("dcryr") String dcryr,
            @Field("DBPrefix") String DBPrefix
    );

    //to check sample & gift entries
    @FormUrlEncoded
    @POST("checksamplegift.php")
    Call<DefaultResponse> checkSampleGift(
            @Field("empcode") String ecode,
            @Field("DBPrefix") String DBPrefix
    );

    //to check dcr is blocked or not
    @FormUrlEncoded
    @POST("dcrblockcheck.php")
    Call<DefaultResponse> DCRBlockCheck(
            @Field("empcode") String ecode,
            @Field("netid") String netid,
            @Field("dcrdate") String dcrdate,
            @Field("DBPrefix") String DBPrefix
    );

    //to get holiday in between last confirm dcr and current dcrdate
    @FormUrlEncoded
    @POST("getholdcrdates.php")
    Call<DefaultResponse> getHolidayDcrdates(
            @Field("empcode") String ecode,
            @Field("netid") String netid,
            @Field("dcrdate") String dcrdate,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("jnwrkandarea.php")
    Call<AreaJntWrkRes> getAreaJntWrk(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("hname") String hname,
            @Field("dcrdate") String dcrdate,
            @Field("dcrmth") String dcrmth,
            @Field("dcryr") String dcryr,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("getdoctordatalist.php")
    Call<DoctorListAWRes> getDoctorDataList(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("tcpid") String tcpid,
            @Field("dcrdate") String dcrdate,
            @Field("dcrmth") String dcrmth,
            @Field("dcryr") String dcryr,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("getchemistdatalist.php")
    Call<ChemistListAWRes> getChemistDataList(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("tcpid") String tcpid,
            @Field("dcrdate") String dcrdate,
            @Field("dcrmth") String dcrmth,
            @Field("dcryr") String dcryr,
            @Field("stype") String stype,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("getdcrddoc.php")
    Call<DCRDDocListRes> getDCRDDrs(
            @Field("dcrno") String dcrno,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("SampleAndGiftReceipt.php")
    Call<SampleAndGiftReceiptRes> SampleAndGiftReceipt(
            @Field("empcode") String empcode,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("getdcrdchem.php")
    Call<DCRDChemListRes> getDCRDChem(
            @Field("dcrno") String dcrno,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("accessSummary.php")
    Call<IsDCRCorrectRes> isDCRCorrectlyFilled(
            @Field("dcrno") String dcrno,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("DCRExpense.php")
    Call<DCRExpenseListRes> DCRExpenseReq(
            @Field("dcrno") String dcrno,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("fetchexpensedata.php")
    Call<FetchExpdtRes> fetchExpData(
            @Field("dcrno") String dcrno,
            @Field("DBPrefix") String DBPrefix
    );


    @FormUrlEncoded
    @POST("deleteExpenseEntry.php")
    Call<DefaultResponse> deleteExpenseEntry(
            @Field("dcrno") String dcrno,
            @Field("serial") String serial,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("deleteNonFieldWrk.php")
    Call<DefaultResponse> deleteNonFieldWrkEntry(
            @Field("dcrno") String dcrno,
            @Field("serial") String serial,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("updateremark.php")
    Call<DefaultResponse> saveRemark(
            @Field("dcrno") String dcrno,
            @Field("serial") String serial,
            @Field("remark") String remark,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("getremark.php")
    Call<DefaultResponse> getSubRemark(
            @Field("dcrno") String dcrno,
            @Field("serial") String serial,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("getNonFieldWorkList2.php")
    Call<NewNonFliedWrkRes> getNonFieldWorkList2(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("dcrno") String dcrno,
            @Field("dcrmth") String dcrmth,
            @Field("dcryr") String dcryr,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("visitingcardupload.php")
    Call<VstCardDrLstRes> getVstDrLstFormDB(
            @Field("netid") String netid,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("getAlreadtExistImg.php")
    Call<DefaultResponse> getAlreadtExistImg(
            @Field("netid") String netid,
            @Field("cntcd") String cntcd,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("deleteDRfromDCR.php")
    Call<DefaultResponse> deleteDRfromDCR(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("dcrhtcpid") String tcpid,
            @Field("dcrno") String dcrno,
            @Field("serial") String serial,
            @Field("finyear") String finyear,
            @Field("dcrdate") String dcrdate,
            @Field("emplvl") String emplvl,
            @Field("field") String field,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("getDeleteExistingImg.php")
    Call<DefaultResponse> getDeleteExistingImg(
            @Field("netid") String netid,
            @Field("cntcd") String cntcd,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("checkSalesEntryNotFilled.php")
    Call<DefaultResponse> checkSalesEntryNotFilled(
            @Field("netid") String netid,
            @Field("d1d2") String d1d2,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("DCRGift.php")
    Call<DCRGiftListRes> DCRGiftApi(
            @Field("serial") String serial,
            @Field("netid") String netid,
            @Field("dcrno") String dcrno,
            @Field("d1d2") String d1d2,
            @Field("ecode") String ecode,
            @Field("financialyear") String financialyear,
            @Field("logmth") String logmth,
            @Field("logyr") String logyr,
            @Field("DBPrefix") String DBPrefix
    );

    /*@FormUrlEncoded
    @POST("DCRProduct.php")
    Call<DCRProdListRes> DCRProdApi(
            @Field("serial") String serial,
            @Field("netid") String netid,
            @Field("dcrno") String dcrno,
            @Field("d1d2") String d1d2,
            @Field("ecode") String ecode,
            @Field("financialyear") String financialyear,
            @Field("DBPrefix") String DBPrefix
    );*/

    @FormUrlEncoded
    @POST("demo.php")
    Call<DCRProdListRes> DCRProdApi(
            @Field("serial") String serial,
            @Field("netid") String netid,
            @Field("dcrno") String dcrno,
            @Field("d1d2") String d1d2,
            @Field("ecode") String ecode,
            @Field("financialyear") String financialyear,
            @Field("dcrdate") String dcrdate,
            @Field("mth") String mth,
            @Field("yr") String yr,
            @Field("cntcd") String cntcd,
            @Field("logmth") String logmth,
            @Field("logyr") String logyr,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("NonFieldWorkApi.php")
    Call<NonFieldWrkRes> getNonFieldWrk(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("dcrno") String dcrno,
            @Field("dcrmth") String dcrmth,
            @Field("dcryr") String dcryr,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("yesnoquestionpopup.php")
    Call<GetPopupQuesRes> yesNoQuestionPopup(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("drclass") String drclass,
            @Field("d1d2") String d1d2,
            @Field("dcrmth") String dcrmth,
            @Field("dcryr") String dcryr,
            @Field("cntcd") String cntcd,
            @Field("DBPrefix") String DBPrefix
    );

    @FormUrlEncoded
    @POST("submitPopupQuesAns.php")
    Call<DefaultResponse> submitPopupQuesAns(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("dcrdate") String dcrdate,
            @Field("jsonstr") String jsonstr,
            @Field("dcrmth") String dcrmth,
            @Field("dcryr") String dcryr,
            @Field("cntcd") String cntcd,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("UpdateSampleGiftAcceptance.php")
    Call<DefaultResponse> UpdateSampleGiftAcceptance(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("financialyear") String financialyear,
            @Field("jsonarray") String jsonstr,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("get117611771187.php")
    Call<EpidermPopUpRes> get117611771187(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("dcrdate") String dcrdate,
            @Field("cntcd") String cntcd,
            @Field("prodid") String prodid,
            @Field("dcrno") String dcrno,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("get1098.php")
    Call<QseraPopUpRes> get1098(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("dcrdate") String dcrdate,
            @Field("cntcd") String cntcd,
            @Field("prodid") String prodid,
            @Field("dcrno") String dcrno,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("get3009.php")
    Call<RedicnePopUpRes> get3009(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("dcrdate") String dcrdate,
            @Field("cntcd") String cntcd,
            @Field("prodid") String prodid,
            @Field("dcrno") String dcrno,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("submit11761177.php")
    Call<DefaultResponse> submit11761177(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("dcrdate") String dcrdate,
            @Field("cntcd") String cntcd,
            @Field("prodid") String prodid,
            @Field("dcrno") String dcrno,
            @Field("startpresc") String startpresc,
            @Field("madeavail") String madeavail,
            @Field("ddorderqty") String ddorderqty,
            @Field("triopackgiven") String triopackgiven,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("submit1098.php")
    Call<DefaultResponse> submit1098(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("dcrdate") String dcrdate,
            @Field("cntcd") String cntcd,
            @Field("prodid") String prodid,
            @Field("dcrno") String dcrno,
            @Field("madeavail") String madeavail,
            @Field("NoQSeraHairSerumRx") String NoQSeraHairSerumRx,
            @Field("Noofunitsold") String Noofunitsold,
            @Field("Doctorsfeedback") String Doctorsfeedback,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("submit1187.php")
    Call<DefaultResponse> submit1187(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("dcrdate") String dcrdate,
            @Field("cntcd") String cntcd,
            @Field("prodid") String prodid,
            @Field("dcrno") String dcrno,
            @Field("epidermlaunched") String epidermlaunched,
            @Field("epidermsamplegiven") String epidermsamplegiven,
            @Field("epidermprscReceived") String epidermprscReceived,
            @Field("epidermpresno") String epidermpresno,
            @Field("madeavail") String madeavail,
            @Field("epidermnoofunitsavail") String epidermnoofunitsavail,
            @Field("epidermnoofunitsoldafterlaunch") String epidermnoofunitsoldafterlaunch,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("submit3009.php")
    Call<DefaultResponse> submit3009(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("dcrdate") String dcrdate,
            @Field("cntcd") String cntcd,
            @Field("prodid") String prodid,
            @Field("dcrno") String dcrno,
            @Field("KMACBriefednConsentRcvd") String KMACBriefednConsentRcvd,
            @Field("NoofRidacneRxWeek1520jul") String NoofRidacneRxWeek1520jul,
            @Field("KMACUploadMaterailRcvdFromDr") String KMACUploadMaterailRcvdFromDr,
            @Field("NoofRidacneRxWeek2431jul") String NoofRidacneRxWeek2431jul,
            @Field("DrAgreedWiththeKMACUploadedMaterial") String DrAgreedWiththeKMACUploadedMaterial,
            @Field("NoofRidacneRxWeek0107aug") String NoofRidacneRxWeek0107aug,
            @Field("HandedOverKMACInstrumentToTheDr") String HandedOverKMACInstrumentToTheDr,
            @Field("NoofRidacneRxWeek0814aug") String NoofRidacneRxWeek0814aug,
            @Field("KMACRelatedallMaterialPlacedDspatientWaitingarena") String KMACRelatedallMaterialPlacedDspatientWaitingarena,
            @Field("NoofRidacneRxWeek1622aug") String NoofRidacneRxWeek1622aug,
            @Field("KMACRunningWellCheckednFdbkUpdatedDr") String KMACRunningWellCheckednFdbkUpdatedDr,
            @Field("NoofRidacneRxWeek2431aug") String NoofRidacneRxWeek2431aug,
            @Field("KMACFdbkTakenFromDr") String KMACFdbkTakenFromDr,
            @Field("NoofRidacneRxWeek0105sep") String NoofRidacneRxWeek0105sep,
            @Field("sectimeKMACRunningWellcheckednFdbkUpdatedDr") String sectimeKMACRunningWellcheckednFdbkUpdatedDr,
            @Field("NoofRidacneRxWeek1630sep") String NoofRidacneRxWeek1630sep,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("checkDCRSummary.php")
    Call<GetDCRSummaryMainRes> getDCRSummary(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("remark") String remark,
            @Field("dcrno") String dcrno,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("confirmDCREntry.php")
    Call<ConfirmDCRRes> confirmDCREntry(
            @Field("Dsvlcalls") String Dsvlcalls,
            @Field("DNsvlcalls") String DNsvlcalls,
            @Field("Csvlcalls") String Csvlcalls,
            @Field("CNsvlcalls") String CNsvlcalls,
            @Field("NoPOB") String NoPOB,
            @Field("TotPOB") String TotPOB,
            @Field("Deduction") String Deduction,
            @Field("ecode") String ecode,
            @Field("DCRDate") String DCRDate,
            @Field("netid") String netid,
            @Field("dcrno") String dcrno,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("DrMissCallAlert.php")
    Call<MissCallDocsRes> DrMissCallAlert(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("logyr") String logyr,
            @Field("logmth") String logmth,
            @Field("checkmtp") String checkmtp,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("nextMthMTPConf.php")
    Call<NextMTPListRes> nextMthMTPConf(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("wyr") String wyr,
            @Field("wmonth") String wmonth,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("getMTPListOfMth.php")
    Call<NewMTPListOfMTHRes> getMTPListOfMth(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("wyr") String wyr,
            @Field("wmonth") String wmonth,
            @Field("whichmth") String whichmth,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("confirmMTP.php")
    Call<DefaultResponse> confirmMTP(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("yr") String yr,
            @Field("mth") String mth,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("deleteMTPEntry.php")
    Call<DefaultResponse> deleteMTPEntry(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("tcpid") String tcpid,
            @Field("wdate") String wdate,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("getVisitPlanSummary.php")
    Call<VstPlnSumRes> getVisitPlanSummary(
            @Field("netid") String netid,
            @Field("prevfinyr") String prevfinyr,
            @Field("finyr") String finyr,
            @Field("mtpdate") String mtpdate,
            @Field("mode") String mode,
            @Field("cntcd") String cntcd,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("getVisitPlanDocList.php")
    Call<VstPlnDocLstRes> getVisitPlanDocList(
            @Field("netid") String netid,
            @Field("mtpdate") String mtpdate,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("editMTPEntry.php")
    Call<EditMtpFormResponse> editMTPEntry(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("tcpid") String tcpid,
            @Field("wdate") String wdate,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("updateMTPEntry.php")
    Call<DefaultResponse> updateMTPEntry(
            @Field("ecode") String ecode,
            @Field("netid") String netid,
            @Field("tcpid") String tcpid,
            @Field("wdate") String wdate,
            @Field("objctv") String objctv,
            @Field("mngrjtwrk") String mngrjtwrk,
            @Field("prevtcpid") String prevtcpid,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("rcpa_chemist.php")
    Call<GetRCPAPulseChemist> rcpa_chemist(
            @Field("cntcd") String cntcd,
            @Field("netid") String netid,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("rcpa_brands.php")
    Call<GetRCPABrandListRes> rcpa_brands(
            @Field("cntcd") String cntcd,
            @Field("netid") String netid,
            @Field("yrmth") String yrmth,
            @Field("d1d2") String d1d2,
            @Field("DBPrefix") String dbprefix
    );

    @FormUrlEncoded
    @POST("rcpa_comp_prod.php")
    Call<GetRCPACompProdLstRes> rcpa_comp_prod(
            @Field("cntcd") String cntcd,
            @Field("netid") String netid,
            @Field("yrmth") String yrmth,
            @Field("prodid") String prodid,
            @Field("DBPrefix") String dbprefix
    );


}
