package com.eis.dailycallregister.Fragment;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eis.dailycallregister.Activity.ChemistData;
import com.eis.dailycallregister.Activity.DoctorsData;
import com.eis.dailycallregister.Activity.ExpenseData;
import com.eis.dailycallregister.Activity.HomeActivity;
import com.eis.dailycallregister.Activity.LoginScreen;
import com.eis.dailycallregister.Activity.NonFieldWork;
import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.DefaultResponse;
import com.eis.dailycallregister.Pojo.GetDcrDateRes;
import com.eis.dailycallregister.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DCREntry extends Fragment {

    View view;
    ViewDialog progressDialoge;
    ScrollView sv ;
    public MaterialCardView m1,m2,m3;
    public CardView dd,cd,ed,nond;
    LinearLayout l1,l2,l3;
    TextView dcrdate;
    Spinner spinnerHolDates;
    MaterialButton submitdcrdate;
    List<String> arrayList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("DCR Entry");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dcrentry, container, false);
        progressDialoge=new ViewDialog(getActivity());
        sv = view.findViewById(R.id.sv);
        m1 = view.findViewById(R.id.carddcrdate);
        m2 = view.findViewById(R.id.dcrdatesdropdown);
        m3 = view.findViewById(R.id.cardremark);
        l1 = view.findViewById(R.id.l1);
        l2 = view.findViewById(R.id.l2);
        l3 = view.findViewById(R.id.l3);
        dd = view.findViewById(R.id.doctorsdata);
        cd = view.findViewById(R.id.chemdata);
        ed = view.findViewById(R.id.expensedata);
        nond = view.findViewById(R.id.nonfieldwrk);
        dcrdate = view.findViewById(R.id.dcrdate);
        spinnerHolDates = view.findViewById(R.id.holdate);
        submitdcrdate = view.findViewById(R.id.submitdcrdate);
        m1.setVisibility(View.GONE);
        m2.setVisibility(View.GONE);
        m3.setVisibility(View.GONE);
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);
        l3.setVisibility(View.GONE);
        getdcrdate();

        dd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoctorsData.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(intent,bndlanimation);
            }
        });

        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChemistData.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(intent,bndlanimation);
            }
        });

        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExpenseData.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(intent,bndlanimation);
            }
        });

        nond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NonFieldWork.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(intent,bndlanimation);
            }
        });

        submitdcrdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("DCR ENTRY");
                builder.setMessage("Are you sure want to fill DCR of date - "+spinnerHolDates.getSelectedItem().toString().trim());
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //it stores selected dcrdate in global variables
                                if(Global.dcrno == null) {
                                    String[] detsplt = spinnerHolDates.getSelectedItem().toString().trim().split("-");
                                    Global.dcrdate = detsplt[2] + "-" + detsplt[1] + "-" + detsplt[0];
                                    Global.dcrdateday = detsplt[0];
                                    Global.dcrdatemonth = detsplt[1];
                                    Global.dcrdateyear = detsplt[2];
                                    Global.dcrdatestatus = true;
                                    dcrdate.setText("DCR DATE : " + Global.dcrdateday + "-" + Global.dcrdatemonth + "-" + Global.dcrdateyear);
                                    arrayList.remove(spinnerHolDates.getSelectedItem().toString().trim());
                                    Global.dcrno = null;

                                    Snackbar.make(sv, "DCR date changed successfully.", Snackbar.LENGTH_LONG).show();
                                    m2.setVisibility(View.GONE);
                                }else{
                                    Snackbar.make(sv, "Please wait.....", Snackbar.LENGTH_LONG).show();
                                    final String[] detsplt = spinnerHolDates.getSelectedItem().toString().trim().split("-");
                                    final String newdcrdate = detsplt[2] + "-" + detsplt[1] + "-" + detsplt[0];
                                    progressDialoge.show();
                                    Call<DefaultResponse> call1 = RetrofitClient
                                            .getInstance().getApi().changeDCRDate(Global.ecode,Global.netid, newdcrdate,Global.dcrno,Global.dbprefix);
                                    call1.enqueue(new Callback<DefaultResponse>() {
                                        @Override
                                        public void onResponse(Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                                            DefaultResponse res = response.body();
                                            progressDialoge.dismiss();
                                            if (!res.isError()) {
                                                Global.dcrdateday = detsplt[0];
                                                Global.dcrdatemonth = detsplt[1];
                                                Global.dcrdateyear = detsplt[2];
                                                Global.dcrdatestatus = true;
                                                dcrdate.setText("DCR DATE : " + Global.dcrdateday + "-" + Global.dcrdatemonth + "-" + Global.dcrdateyear);
                                                arrayList.remove(spinnerHolDates.getSelectedItem().toString().trim());
                                                m2.setVisibility(View.GONE);
                                                Snackbar.make(sv, res.getErrormsg(), Snackbar.LENGTH_LONG).show();
                                            }else{
                                                Snackbar.make(sv, res.getErrormsg(), Snackbar.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                                            progressDialoge.dismiss();
                                            Snackbar snackbar = Snackbar.make(sv, "Failed to update dcrdate !", Snackbar.LENGTH_LONG);
                                            snackbar.show();
                                        }
                                    });
                                }

                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }

    public void getdcrdate(){
        progressDialoge.show();
        //Log.d("progress 4-->",ecode);
        Call<GetDcrDateRes> call1 = RetrofitClient
                .getInstance().getApi().getDcrdate(Global.ecode,Global.netid,Global.dbprefix);
        call1.enqueue(new Callback<GetDcrDateRes>() {
            @Override
            public void onResponse(Call<GetDcrDateRes> call1, Response<GetDcrDateRes> response) {
                GetDcrDateRes res = response.body();

                //Log.d("progress 5-->",ecode);
                if (res.isError()) {
                    progressDialoge.dismiss();
                    dialogCloseTypeError(getContext(),res.getErrormsg());
                }else{
                    Global.dcrdate = res.getDcrdate();
                    String[] datespt = res.getDcrdate().split("-");
                    Global.dcrdateday = datespt[2];
                    Global.dcrdatemonth = datespt[1];
                    Global.dcrdateyear = datespt[0];
                    Global.dcrdatestatus = res.isNewflg();

                    if(!Global.dcrdatestatus){
                        Global.dcrno = res.getErrormsg();
                    }
                    dcrdate.setText("DCR DATE : "+Global.dcrdateday+"-"+Global.dcrdatemonth+"-"+Global.dcrdateyear);
                    m1.setVisibility(View.VISIBLE);
                    checkmtp();
                }
            }

            @Override
            public void onFailure(Call<GetDcrDateRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getdcrdate();
                            }
                        });
                snackbar.show();
            }
        });
    }

    public  void checkmtp(){
        Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().checkMTP(Global.ecode,Global.netid,Global.dcrdatemonth,Global.dcrdateyear,Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();

                //Log.d("progress 5-->",ecode);
                if (res.isError()) {
                    progressDialoge.dismiss();
                    dialogCloseTypeError(getContext(),res.getErrormsg());
                }else{
                    //todo add ckeckblock here
                    checkblock();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getdcrdate();
                            }
                        });
                snackbar.show();
            }
        });
    }

    public void checkblock(){
        Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().DCRBlockCheck(Global.ecode,Global.netid,Global.dcrdate,Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                //progressDialoge.dismiss();
                //Log.d("progress 5-->",ecode);
                if (res.isError()) {
                    progressDialoge.dismiss();
                    dialogCloseTypeError(getContext(),res.getErrormsg());
                }else{
                    checksamplegift();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getdcrdate();
                            }
                        });
                snackbar.show();
            }
        });
    }

    public void checksamplegift(){
        Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().checkSampleGift(Global.ecode,Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                //progressDialoge.dismiss();
                //Log.d("progress 5-->",ecode);
                if (res.isError()) {
                    checkholidays();
                    m3.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);
                    l3.setVisibility(View.VISIBLE);
                    dialogYesNoTypeQuestion(getContext(),res.getErrormsg());
                }else{
                    m3.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);
                    l3.setVisibility(View.VISIBLE);
                    checkholidays();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getdcrdate();
                            }
                        });
                snackbar.show();
            }
        });
    }

    public void checkholidays(){
        Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().getHolidayDcrdates(Global.ecode,Global.netid,Global.dcrdate,Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                //progressDialoge.dismiss();
                //Log.d("progress 5-->",ecode);
                if (res.isError()) {
                    arrayList =new ArrayList<>();
                    if(res.getErrormsg().length() > 10){
                        String[] datelist = res.getErrormsg().split(",");
                        for (String s: datelist) {

                            arrayList.add(s);
                        }
                    }else{
                        arrayList.add(res.getErrormsg());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, arrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerHolDates.setAdapter(adapter);
                    //if(Global.dcrno == null) {
                        m2.setVisibility(View.VISIBLE);
                    /*}else{
                        m2.setVisibility(View.GONE);
                    }*/
                }

                checkPendingStockandSalesEntry();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getdcrdate();
                            }
                        });
                snackbar.show();
            }
        });
    }

    private void checkPendingStockandSalesEntry() {
        String d1d2="";
        if(Global.hname.contains("(A)")){
            d1d2 = "A";
        }else if(Global.hname.contains("(B)")){
            d1d2 = "B";
        }else if(Global.hname.contains("(C)")){
            d1d2 = "C";
        }else if(Global.hname.contains("(D)")){
            d1d2 = "D";
        }
        Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().checkSalesEntryNotFilled(Global.netid,d1d2,Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                progressDialoge.dismiss();
                //Log.d("progress 5-->",ecode);
                if (!res.isError()) {
                    Log.d("error msg-->",res.getErrormsg());
                    salesEntryRemainingAlert(res.getErrormsg());
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getdcrdate();
                            }
                        });
                snackbar.show();
            }
        });
    }

    public void dialogCloseTypeError(final Context context,String errormsg){
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.mtperror);
        CardView button = dialog.findViewById(R.id.close);
        TextView textView = dialog.findViewById(R.id.questiontxt);
        textView.setText(errormsg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("openfrag","home");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_right_in,R.anim.trans_right_out).toBundle();
                startActivity(intent,bndlanimation);
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public static void dialogYesNoTypeQuestion(final Context context , String result) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.samplegiftpopup);
        CardView buttonNo = dialog.findViewById(R.id.no);
        CardView buttonYes = dialog.findViewById(R.id.yes);
        TextView textView = dialog.findViewById(R.id.questiontxt);
        textView.setText(result);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.SampleGiftRecOrNot = "Y";
                Toast.makeText(context,"Ans "+Global.SampleGiftRecOrNot,Toast.LENGTH_LONG);
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void salesEntryRemainingAlert(String response){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("Alert");
        builder.setMessage(response);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
