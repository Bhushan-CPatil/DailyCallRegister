package com.eis.dailycallregister.Api;


import com.eis.dailycallregister.Pojo.AreaJntWrkRes;
import com.eis.dailycallregister.Pojo.ChemistListAWRes;
import com.eis.dailycallregister.Pojo.DBList;
import com.eis.dailycallregister.Pojo.DCRDChemListRes;
import com.eis.dailycallregister.Pojo.DCRDDocListRes;
import com.eis.dailycallregister.Pojo.DCRExpenseListRes;
import com.eis.dailycallregister.Pojo.DCRGiftListRes;
import com.eis.dailycallregister.Pojo.DCRProdListRes;
import com.eis.dailycallregister.Pojo.DefaultResponse;
import com.eis.dailycallregister.Pojo.DoctorListAWRes;
import com.eis.dailycallregister.Pojo.EpidermPopUpRes;
import com.eis.dailycallregister.Pojo.ErrorBooleanResponce;
import com.eis.dailycallregister.Pojo.FetchExpdtRes;
import com.eis.dailycallregister.Pojo.GetDcrDateRes;
import com.eis.dailycallregister.Pojo.GetPopupQuesRes;
import com.eis.dailycallregister.Pojo.NonFieldWrkRes;
import com.eis.dailycallregister.Pojo.QseraPopUpRes;
import com.eis.dailycallregister.Pojo.RedicnePopUpRes;
import com.eis.dailycallregister.Pojo.VstCardDrLstRes;

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
    @POST("checkstatus")
    Call<ErrorBooleanResponce> islogincontinue(
            @Field("ecode") String ecode,
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
    @POST("getdcrdchem.php")
    Call<DCRDChemListRes> getDCRDChem(
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
    @POST("DCRGift.php")
    Call<DCRGiftListRes> DCRGiftApi(
            @Field("serial") String serial,
            @Field("netid") String netid,
            @Field("dcrno") String dcrno,
            @Field("d1d2") String d1d2,
            @Field("ecode") String ecode,
            @Field("financialyear") String financialyear,
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


}
