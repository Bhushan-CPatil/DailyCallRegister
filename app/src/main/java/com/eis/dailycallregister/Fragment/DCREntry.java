package com.eis.dailycallregister.Fragment;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eis.dailycallregister.Activity.CheckDCRSummary;
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
import com.eis.dailycallregister.Pojo.IsDCRCorrectRes;
import com.eis.dailycallregister.Pojo.SampleAndGiftReceiptItem;
import com.eis.dailycallregister.Pojo.SampleAndGiftReceiptRes;
import com.eis.dailycallregister.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DCREntry extends Fragment {

    View view;
    ViewDialog progressDialoge;
    ScrollView sv;
    public MaterialCardView m1, m2, m3;
    public CardView dd, cd, ed, nond;
    LinearLayout l1, l2, l3;
    TextView dcrdate;
    Spinner spinnerHolDates;
    EditText remark;
    MaterialButton submitdcrdate, condcrent;
    List<String> arrayList;
    List<SampleAndGiftReceiptItem> samplegift = new ArrayList<>();
    public int mn = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("DCR Entry");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dcrentry, container, false);
        progressDialoge = new ViewDialog(getActivity());
        sv = view.findViewById(R.id.sv);
        m1 = view.findViewById(R.id.carddcrdate);
        remark = view.findViewById(R.id.remark);
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
        condcrent = view.findViewById(R.id.condcrent);
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
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(intent, bndlanimation);
            }
        });

        cd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChemistData.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(intent, bndlanimation);
            }
        });

        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExpenseData.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(intent, bndlanimation);
            }
        });

        nond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NonFieldWork.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(intent, bndlanimation);
            }
        });

        submitdcrdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("DCR ENTRY");
                builder.setMessage("Are you sure you want to fill DCR of date - " + spinnerHolDates.getSelectedItem().toString().trim());
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //it stores selected dcrdate in global variables
                                if (Global.dcrno == null) {
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
                                } else {
                                    Snackbar.make(sv, "Please wait.....", Snackbar.LENGTH_LONG).show();
                                    final String[] detsplt = spinnerHolDates.getSelectedItem().toString().trim().split("-");
                                    final String newdcrdate = detsplt[2] + "-" + detsplt[1] + "-" + detsplt[0];
                                    progressDialoge.show();
                                    Call<DefaultResponse> call1 = RetrofitClient
                                            .getInstance().getApi().changeDCRDate(Global.ecode, Global.netid, newdcrdate, Global.dcrno, Global.dbprefix);
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
                                            } else {
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

        condcrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("CONFIRM DCR ?");
                builder.setMessage("Are you sure you want to confirm DCR ?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //it stores selected dcrdate in global variables
                                if (Global.dcrno == null) {
                                    Snackbar.make(sv, "DCR IS EMPTY !", Snackbar.LENGTH_LONG).show();
                                } else {
                                    checkDCRIsPerfectlyFilledOrNot();
                                   /*Intent intent = new Intent(getActivity(), CheckDCRSummary.class);
                                   intent.putExtra("remark", remark.getText().toString());
                                   Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                                   startActivity(intent,bndlanimation);*/
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

    private void checkDCRIsPerfectlyFilledOrNot() {

        progressDialoge.show();
        Call<IsDCRCorrectRes> call1 = RetrofitClient
                .getInstance().getApi().isDCRCorrectlyFilled(Global.dcrno, Global.dbprefix);
        call1.enqueue(new Callback<IsDCRCorrectRes>() {
            @Override
            public void onResponse(Call<IsDCRCorrectRes> call1, Response<IsDCRCorrectRes> response) {
                progressDialoge.dismiss();
                IsDCRCorrectRes res = response.body();
                String stmsg = "";
                if (!res.isChem()) {
                    stmsg = "Please select at least one chemist !";
                    showAlert(stmsg);
                } else if (!res.isDoc()) {
                    stmsg = "Please select at least one doctor !";
                    showAlert(stmsg);
                } else if (!res.isDocsmp()) {
                    stmsg = "No sample selected for " + res.getDocsmpnames() + ".";
                    showAlert(stmsg);
                } else if (!res.isDocgift()) {
                    stmsg = "No gift selected for " + res.getDocgiftnames() + ".";
                    showAlert(stmsg);
                } else {
                    Intent intent = new Intent(getActivity(), CheckDCRSummary.class);
                    intent.putExtra("remark", remark.getText().toString());
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                    startActivity(intent, bndlanimation);
                }

            }

            @Override
            public void onFailure(Call<IsDCRCorrectRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to fetch DCR date !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkDCRIsPerfectlyFilledOrNot();
                            }
                        });
                snackbar.show();
            }
        });

    }

    private void showAlert(String stmsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle("Alert ?");
        builder.setMessage(stmsg);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //it stores selected dcrdate in global variables

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void getdcrdate() {
        progressDialoge.show();
        //Log.d("progress 4-->",ecode);
        Call<GetDcrDateRes> call1 = RetrofitClient
                .getInstance().getApi().getDcrdate(Global.ecode, Global.netid, Global.dbprefix);
        call1.enqueue(new Callback<GetDcrDateRes>() {
            @Override
            public void onResponse(Call<GetDcrDateRes> call1, Response<GetDcrDateRes> response) {
                GetDcrDateRes res = response.body();

                //Log.d("progress 5-->",ecode);
                if (res.isError()) {
                    progressDialoge.dismiss();
                    dialogCloseTypeError(getContext(), res.getErrormsg());
                } else {
                    Global.dcrdate = res.getDcrdate();
                    String[] datespt = res.getDcrdate().split("-");
                    Global.dcrdateday = datespt[2];
                    Global.dcrdatemonth = datespt[1];
                    Global.dcrdateyear = datespt[0];
                    Global.dcrdatestatus = res.isNewflg();

                    if (!Global.dcrdatestatus) {
                        Global.dcrno = res.getErrormsg();
                    }

                    if (res.getRemark() != null && !res.getRemark().equalsIgnoreCase("")) {
                        remark.setText(res.getRemark());
                    }

                    dcrdate.setText("DCR DATE : " + Global.dcrdateday + "-" + Global.dcrdatemonth + "-" + Global.dcrdateyear);
                    m1.setVisibility(View.VISIBLE);
                    checkmtp();
                }
            }

            @Override
            public void onFailure(Call<GetDcrDateRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to fetch DCR date !", Snackbar.LENGTH_INDEFINITE)
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

    public void checkmtp() {
        Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().checkMTP(Global.ecode, Global.netid, Global.dcrdatemonth, Global.dcrdateyear, Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();

                //Log.d("progress 5-->",ecode);
                if (res.isError()) {
                    progressDialoge.dismiss();
                    dialogCloseTypeError(getContext(), res.getErrormsg());
                } else {
                    //todo add ckeckblock here
                    checkblock();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to check MTP !", Snackbar.LENGTH_INDEFINITE)
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

    public void checkblock() {
        Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().DCRBlockCheck(Global.ecode, Global.netid, Global.dcrdate, Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                //progressDialoge.dismiss();
                //Log.d("progress 5-->",ecode);
                if (res.isError()) {
                    progressDialoge.dismiss();
                    dialogCloseTypeError(getContext(), res.getErrormsg());
                } else {
                    checksamplegift();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to lock data!", Snackbar.LENGTH_INDEFINITE)
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

    public void checksamplegift() {
        Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().checkSampleGift(Global.ecode, Global.dbprefix);
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
                    dialogYesNoTypeQuestion(getContext(), res.getErrormsg());
                } else {
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
                Snackbar snackbar = Snackbar.make(sv, "Failed to check sample !", Snackbar.LENGTH_INDEFINITE)
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

    public void checkholidays() {
        Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().getHolidayDcrdates(Global.ecode, Global.netid, Global.dcrdate, Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                //progressDialoge.dismiss();
                //Log.d("progress 5-->",ecode);
                if (res.isError()) {
                    arrayList = new ArrayList<>();
                    if (res.getErrormsg().length() > 10) {
                        String[] datelist = res.getErrormsg().split(",");
                        for (String s : datelist) {

                            arrayList.add(s);
                        }
                    } else {
                        arrayList.add(res.getErrormsg());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
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
                Snackbar snackbar = Snackbar.make(sv, "Failed to check holidays !", Snackbar.LENGTH_INDEFINITE)
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
        String d1d2 = "";
        if (Global.hname.contains("(A)")) {
            d1d2 = "A";
        } else if (Global.hname.contains("(B)")) {
            d1d2 = "B";
        } else if (Global.hname.contains("(C)")) {
            d1d2 = "C";
        } else if (Global.hname.contains("(D)")) {
            d1d2 = "D";
        }
        Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().checkSalesEntryNotFilled(Global.netid, d1d2, Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                progressDialoge.dismiss();
                //Log.d("progress 5-->",ecode);
                if (!res.isError()) {
                    Log.d("error msg-->", res.getErrormsg());
                    salesEntryRemainingAlert(res.getErrormsg());
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, " pending sales entry Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
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

    public void dialogCloseTypeError(final Context context, final String errormsg) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.mtperror);
        CardView button = dialog.findViewById(R.id.close);
        TextView closebtn = dialog.findViewById(R.id.closebtn);
        if (errormsg.equalsIgnoreCase("Next month MTP is not \nconfirmed or approved.")) {
            closebtn.setText("VIEW MTP");
        }
        TextView textView = dialog.findViewById(R.id.questiontxt);
        textView.setText(errormsg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (errormsg.equalsIgnoreCase("Next month MTP is not \nconfirmed or approved.")) {
                    Global.whichmth = "NEXT";
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra("openfrag", "mtp");
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                    startActivity(intent, bndlanimation);
                    getActivity().finish();
                } else {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra("openfrag", "home");
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                    startActivity(intent, bndlanimation);
                    getActivity().finish();
                }
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void dialogYesNoTypeQuestion(final Context context, String result) {
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
                //Toast.makeText(context,"Ans "+Global.SampleGiftRecOrNot,Toast.LENGTH_LONG);
                fetchSampleGiftForm();
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

    private void fetchSampleGiftForm() {
        progressDialoge.show();
        Call<SampleAndGiftReceiptRes> call1 = RetrofitClient
                .getInstance().getApi().SampleAndGiftReceipt(Global.ecode, Global.dbprefix);
        call1.enqueue(new Callback<SampleAndGiftReceiptRes>() {
            @Override
            public void onResponse(Call<SampleAndGiftReceiptRes> call1, Response<SampleAndGiftReceiptRes> response) {
                SampleAndGiftReceiptRes res = response.body();
                progressDialoge.dismiss();
                samplegift = res.getSampleAndGiftReceipt();
                if (samplegift.size() > 0) {
                    showSampleAndGiftPopup(getActivity());
                }
            }

            @Override
            public void onFailure(Call<SampleAndGiftReceiptRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fetchSampleGiftForm();
                            }
                        });
                snackbar.show();
            }
        });
    }

    private void showSampleAndGiftPopup(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.sample_and_gift_form);
        CardView buttonNo = dialog.findViewById(R.id.cancel);
        CardView buttonYes = dialog.findViewById(R.id.submit);
        final RecyclerView recyclerView = dialog.findViewById(R.id.rectable);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
                                    @NonNull
                                    @Override
                                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                        View view = LayoutInflater.from(context).inflate(R.layout.table_list_item, viewGroup, false);
                                        Holder holder = new Holder(view);
                                        return holder;
                                    }

                                    @Override
                                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                                        final Holder rowViewHolder = (Holder) viewHolder;
                /*int rowPos = rowViewHolder.getAdapterPosition();

                if (rowPos == 0) {

                    // Header Cells. Main Headings appear here
                    rowViewHolder.srno.setBackgroundResource(R.drawable.tableheadbg);
                    rowViewHolder.srno.setTextColor(Color.parseColor("#FFFFFF"));
                    rowViewHolder.pname.setBackgroundResource(R.drawable.tableheadbg);
                    rowViewHolder.pname.setTextColor(Color.parseColor("#FFFFFF"));
                    rowViewHolder.itype.setBackgroundResource(R.drawable.tableheadbg);
                    rowViewHolder.itype.setTextColor(Color.parseColor("#FFFFFF"));
                    rowViewHolder.idate.setBackgroundResource(R.drawable.tableheadbg);
                    rowViewHolder.idate.setTextColor(Color.parseColor("#FFFFFF"));
                    rowViewHolder.cname.setBackgroundResource(R.drawable.tableheadbg);
                    rowViewHolder.cname.setTextColor(Color.parseColor("#FFFFFF"));
                    rowViewHolder.docketno.setBackgroundResource(R.drawable.tableheadbg);
                    rowViewHolder.docketno.setTextColor(Color.parseColor("#FFFFFF"));
                    rowViewHolder.dispatchqty.setBackgroundResource(R.drawable.tableheadbg);
                    rowViewHolder.dispatchqty.setTextColor(Color.parseColor("#FFFFFF"));
                    rowViewHolder.recqtydr.setBackgroundResource(R.drawable.tableheadbg);
                    rowViewHolder.recqtydr.setTextColor(Color.parseColor("#FFFFFF"));
                    rowViewHolder.recqtyself.setBackgroundResource(R.drawable.tableheadbg);
                    rowViewHolder.recqtyself.setTextColor(Color.parseColor("#FFFFFF"));
                    rowViewHolder.total.setBackgroundResource(R.drawable.tableheadbg);
                    rowViewHolder.total.setTextColor(Color.parseColor("#FFFFFF"));
                    rowViewHolder.recqtySE.setBackgroundResource(R.drawable.tableheadbg);
                    rowViewHolder.recqtySE.setTextColor(Color.parseColor("#FFFFFF"));
                    rowViewHolder.recdate.setBackgroundResource(R.drawable.tableheadbg);
                    rowViewHolder.recdate.setTextColor(Color.parseColor("#FFFFFF"));
                } else {

                    SampleAndGiftReceiptItem modal = samplegift.get(rowPos-1);

                    rowViewHolder.srno.setBackgroundResource(R.drawable.tableitembg);
                    rowViewHolder.pname.setBackgroundResource(R.drawable.tableitembg);
                    rowViewHolder.itype.setBackgroundResource(R.drawable.tableitembg);
                    rowViewHolder.idate.setBackgroundResource(R.drawable.tableitembg);
                    rowViewHolder.cname.setBackgroundResource(R.drawable.tableitembg);
                    rowViewHolder.docketno.setBackgroundResource(R.drawable.tableitembg);
                    rowViewHolder.dispatchqty.setBackgroundResource(R.drawable.tableitembg);
                    rowViewHolder.recqtydr.setBackgroundResource(R.drawable.tableitembg);
                    rowViewHolder.recqtyself.setBackgroundResource(R.drawable.tableitembg);
                    rowViewHolder.total.setBackgroundResource(R.drawable.tableitembg);
                    rowViewHolder.recqtySE.setBackgroundResource(R.drawable.tableitembg);
                    rowViewHolder.recdate.setBackgroundResource(R.drawable.tableitembg);

                    rowViewHolder.srno.setText(""+rowPos);
                    rowViewHolder.pname.setText(modal.getPNAME());
                    rowViewHolder.itype.setText(modal.getPtype().equals("1") ? "Sample" : "Gift");
                    rowViewHolder.idate.setText(modal.getLrdatedocdate());
                    rowViewHolder.cname.setText(modal.getTcname());
                    rowViewHolder.docketno.setText(modal.getLrnodocno());
                    rowViewHolder.dispatchqty.setText(modal.getDispatchQty());
                    rowViewHolder.recqtydr.setText("");
                    rowViewHolder.recqtyself.setText("");
                    rowViewHolder.total.setText("");
                    rowViewHolder.recqtySE.setText("");
                    rowViewHolder.recdate.setText("");
                }*/
                                        final SampleAndGiftReceiptItem model = samplegift.get(i);

                /*rowViewHolder.srno.setBackgroundResource(R.drawable.tableitembg);
                rowViewHolder.pname.setBackgroundResource(R.drawable.tableitembg);
                rowViewHolder.itype.setBackgroundResource(R.drawable.tableitembg);
                rowViewHolder.idate.setBackgroundResource(R.drawable.tableitembg);
                rowViewHolder.cname.setBackgroundResource(R.drawable.tableitembg);
                rowViewHolder.docketno.setBackgroundResource(R.drawable.tableitembg);
                rowViewHolder.dispatchqty.setBackgroundResource(R.drawable.tableitembg);
                rowViewHolder.recqtydr.setBackgroundResource(R.drawable.tableitembg);
                rowViewHolder.recqtyself.setBackgroundResource(R.drawable.tableitembg);
                rowViewHolder.total.setBackgroundResource(R.drawable.tableitembg);
                rowViewHolder.recqtySE.setBackgroundResource(R.drawable.tableitembg);
                rowViewHolder.recdate.setBackgroundResource(R.drawable.tableitembg);*/

                                        //rowViewHolder.srno.setText(""+(i+1));
                                        rowViewHolder.pname.setText(model.getPNAME());
                                        rowViewHolder.itype.setText(model.getPtype().equals("1") ? "Sample" : "Gift");
                                        rowViewHolder.idate.setText(model.getLrdatedocdate());
                                        rowViewHolder.cname.setText(model.getTcname());
                                        rowViewHolder.docketno.setText(model.getLrnodocno());
                                        rowViewHolder.dispatchqty.setText(model.getDispatchQty());
                                        rowViewHolder.recqtydr.setText(model.getRecQtyD());
                                        rowViewHolder.recqtyself.setText(model.getRecQtyS());
                                        rowViewHolder.total.setText(model.getTRecQty());
                                        rowViewHolder.recqtySE.setText(model.getRecQtySE());
                                        if (model.getRecQtyDate().equalsIgnoreCase("")) {
                                            rowViewHolder.recdate.setText("Enter date here");
                                        } else {
                                            rowViewHolder.recdate.setText(model.getRecQtyDate());
                                        }
                                        rowViewHolder.recqtydr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                            @Override
                                            public void onFocusChange(View view, boolean hasFocus) {
                                                if (!hasFocus) {

                                                    int D = Integer.parseInt(rowViewHolder.recqtydr.getText().toString().equalsIgnoreCase("") ? "0" : rowViewHolder.recqtydr.getText().toString());
                                                    int S = Integer.parseInt(rowViewHolder.recqtyself.getText().toString().equalsIgnoreCase("") ? "0" : rowViewHolder.recqtyself.getText().toString());
                                                    int T = D + S;
                                                    int M = Integer.parseInt(rowViewHolder.dispatchqty.getText().toString().equalsIgnoreCase("") ? "0" : rowViewHolder.dispatchqty.getText().toString());
                                                    int TM = T - M;
                                                    //Log.d("D/S",D+"/"+S);
                                                    if (S == 0) {
                                                        model.setRecQtyS("");
                                                    } else {
                                                        model.setRecQtyS(Integer.toString(S));
                                                    }
                                                    if (D == 0) {
                                                        model.setRecQtyD("");
                                                    } else {
                                                        model.setRecQtyD(Integer.toString(D));
                                                    }
                                                    String curdate = "";
                                                    if (T == 0) {
                                                        model.setTRecQty("");
                                                        model.setRecQtySE("");
                                                        model.setRecQtyDate(curdate);
                                                    } else {
                                                        model.setTRecQty(Integer.toString(T));
                                                        model.setRecQtySE(Integer.toString(TM));
                                                        curdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                                        model.setRecQtyDate(curdate);
                                                    }
                                                    //Log.d("MD/MS",model.getRecQtyD()+"/"+model.getRecQtyS());
                                                    rowViewHolder.recqtydr.setText(model.getRecQtyD());
                                                    rowViewHolder.recqtyself.setText(model.getRecQtyS());
                                                    rowViewHolder.total.setText(model.getTRecQty());
                                                    rowViewHolder.recqtySE.setText(model.getRecQtySE());
                                                    rowViewHolder.recdate.setText(model.getRecQtyDate());


                                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(context.INPUT_METHOD_SERVICE);
                                                    imm.hideSoftInputFromWindow(sv.getWindowToken(), 0);
                                                    recyclerView.getAdapter().notifyDataSetChanged();
                                                }
                                            }
                                        });

                                        rowViewHolder.recqtyself.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                            @Override
                                            public void onFocusChange(View view, boolean hasFocus) {
                                                if (!hasFocus) {

                                                    int D = Integer.parseInt(rowViewHolder.recqtydr.getText().toString().equalsIgnoreCase("") ? "0" : rowViewHolder.recqtydr.getText().toString());
                                                    int S = Integer.parseInt(rowViewHolder.recqtyself.getText().toString().equalsIgnoreCase("") ? "0" : rowViewHolder.recqtyself.getText().toString());
                                                    int T = D + S;
                                                    int M = Integer.parseInt(rowViewHolder.dispatchqty.getText().toString().equalsIgnoreCase("") ? "0" : rowViewHolder.dispatchqty.getText().toString());
                                                    int TM = T - M;
                                                    //Log.d("D/S",D+"/"+S);
                                                    if (S == 0) {
                                                        model.setRecQtyS("");
                                                    } else {
                                                        model.setRecQtyS(Integer.toString(S));
                                                    }
                                                    if (D == 0) {
                                                        model.setRecQtyD("");
                                                    } else {
                                                        model.setRecQtyD(Integer.toString(D));
                                                    }
                                                    String curdate = "";
                                                    if (T == 0) {
                                                        model.setTRecQty("");
                                                        model.setRecQtySE("");
                                                        model.setRecQtyDate(curdate);
                                                    } else {
                                                        model.setTRecQty(Integer.toString(T));
                                                        model.setRecQtySE(Integer.toString(TM));
                                                        curdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                                        model.setRecQtyDate(curdate);
                                                    }
                                                    //Log.d("MD/MS",model.getRecQtyD()+"/"+model.getRecQtyS());
                                                    rowViewHolder.recqtydr.setText(model.getRecQtyD());
                                                    rowViewHolder.recqtyself.setText(model.getRecQtyS());
                                                    rowViewHolder.total.setText(model.getTRecQty());
                                                    rowViewHolder.recqtySE.setText(model.getRecQtySE());
                                                    rowViewHolder.recdate.setText(model.getRecQtyDate());


                                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(context.INPUT_METHOD_SERVICE);
                                                    imm.hideSoftInputFromWindow(sv.getWindowToken(), 0);
                                                    //recyclerView.getAdapter().notifyDataSetChanged();
                                                }
                                            }
                                        });

                                        rowViewHolder.recdate.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (!model.getRecQtyDate().equalsIgnoreCase("") && !model.getRecQtyDate().equalsIgnoreCase("Enter date here")) {
                                                    final Calendar c = Calendar.getInstance();
                                                    int mYear = c.get(Calendar.YEAR); // current year
                                                    int mMonth = c.get(Calendar.MONTH); // current month
                                                    int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                                                    // date picker dialog
                                                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                                                            new DatePickerDialog.OnDateSetListener() {

                                                                @Override
                                                                public void onDateSet(DatePicker view, int year,
                                                                                      int monthOfYear, int dayOfMonth) {
                                                                    // set day of month , month and year value in the edit text
                                                                    //date.setText(String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + year);
                                                                    model.setRecQtyDate(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
                                                                    rowViewHolder.recdate.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));

                                                                }
                                                            }, mYear, mMonth, mDay);
                                                    datePickerDialog.show();
                                                    recyclerView.getAdapter().notifyDataSetChanged();
                                                }
                                            }
                                        });
                                    }

                                    @Override
                                    public int getItemCount() {
                                        return samplegift.size();
                                    }

                                    class Holder extends RecyclerView.ViewHolder {
                                        public TextView srno, pname, itype, idate, cname, docketno, dispatchqty, total, recqtySE, recdate;
                                        public AppCompatEditText recqtydr, recqtyself;

                                        public Holder(@NonNull View itemView) {
                                            super(itemView);
                                            //srno = itemView.findViewById(R.id.txtsrno);
                                            pname = itemView.findViewById(R.id.txtpname);
                                            itype = itemView.findViewById(R.id.txtit);
                                            idate = itemView.findViewById(R.id.txtdd);
                                            cname = itemView.findViewById(R.id.txtcname);
                                            docketno = itemView.findViewById(R.id.txtdno);
                                            dispatchqty = itemView.findViewById(R.id.txtdqty);
                                            recqtydr = itemView.findViewById(R.id.txtrqtyd);
                                            recqtyself = itemView.findViewById(R.id.txtrqtys);
                                            total = itemView.findViewById(R.id.txttqty);
                                            recqtySE = itemView.findViewById(R.id.txtrqtyse);
                                            recdate = itemView.findViewById(R.id.txtrqtydate);
                                        }
                                    }
                                }
        );

        recyclerView.getAdapter().notifyDataSetChanged();
        /*TableViewAdapter adapter = new TableViewAdapter(samplegift);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);*/
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("openfrag", "home");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                startActivity(intent, bndlanimation);
                getActivity().finish();
                dialog.dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.clearFocus();
                Gson gson = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree(samplegift).getAsJsonArray();
                //Toast.makeText(getActivity(), myCustomArray.toString(), Toast.LENGTH_LONG).show();
                updateSampleGift(myCustomArray.toString());
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void updateSampleGift(final String myCustomArray) {
        String finyr = Global.getFinancialYr(Global.dcrdatemonth, Global.dcrdateyear);
        progressDialoge.show();
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().UpdateSampleGiftAcceptance(Global.ecode, Global.netid, finyr, myCustomArray, Global.dbprefix);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {

                DefaultResponse dres = response.body();
                progressDialoge.dismiss();
                if (!dres.isError()) {
                    Toast.makeText(getActivity(), dres.getErrormsg(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), dres.getErrormsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressDialoge.dismiss();
                Snackbar.make(sv, "Falied to update data !", Snackbar.LENGTH_LONG)
                        .setAction("Re try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateSampleGift(myCustomArray);
                            }
                        }).show();
            }
        });
    }

    public void salesEntryRemainingAlert(String response) {
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
