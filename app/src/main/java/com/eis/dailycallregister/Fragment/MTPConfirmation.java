package com.eis.dailycallregister.Fragment;

import android.app.ActivityOptions;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.cleveroad.adaptivetablelayout.OnItemClickListener;
import com.cleveroad.adaptivetablelayout.OnItemLongClickListener;
import com.eis.dailycallregister.Activity.HomeActivity;
import com.eis.dailycallregister.Activity.LoginScreen;
import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.SampleLinkedTableAdapter;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.DefaultResponse;
import com.eis.dailycallregister.Pojo.MtppendlistItem;
import com.eis.dailycallregister.Pojo.NextMTPListRes;
import com.eis.dailycallregister.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MTPConfirmation extends Fragment {

    View view;
    public RelativeLayout rtl;
    public TextView confirm,alconf;
    ViewDialog progressDialoge;
    AdaptiveTableLayout mTableLayout;
    public List<MtppendlistItem> mtplst = new ArrayList();
    public LinearLayout llt;
    String[][] mtp;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("MTP");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_mtpconfirmation, container, false);
        rtl = view.findViewById(R.id.rtl);
        llt = view.findViewById(R.id.llt);
        confirm = view.findViewById(R.id.confirm);
        alconf = view.findViewById(R.id.alconf);
        progressDialoge = new ViewDialog(getActivity());
        mTableLayout = view.findViewById(R.id.mtptable);
        confirm.setVisibility(View.GONE);
        confirm.setEnabled(false);
        alconf.setVisibility(View.GONE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Confirm ?");
                builder.setMessage("Are you sure want to confirm ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confmtp();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        callapi();
        return view;
    }

    private void confmtp() {
        String[] date = mtplst.get(2).getWORKDATE().split("/");
        progressDialoge.show();
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().confirmMTP(Global.ecode,Global.netid,date[2],date[1],Global.dbprefix);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                progressDialoge.dismiss();
                DefaultResponse res =response.body();
                if(!res.isError()){
                    if(res.getErrormsg().equalsIgnoreCase("Successfully Confirmed.")){
                        confirm.setEnabled(false);
                        confirm.setVisibility(View.GONE);
                        alconf.setText(res.getErrormsg());
                        alconf.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressDialoge.dismiss();
                Snackbar.make(rtl,"Failed confirm MTP !", Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confmtp();
                    }
                }).show();
            }
        });
    }

    private void callapi() {
        String[] logdate = Global.date.split("-");
        progressDialoge.show();
        Call<NextMTPListRes> call = RetrofitClient.getInstance().getApi().nextMthMTPConf(Global.ecode,Global.netid,logdate[0],logdate[1],Global.dbprefix);
        call.enqueue(new Callback<NextMTPListRes>() {
            @Override
            public void onResponse(Call<NextMTPListRes> call, Response<NextMTPListRes> response) {
                progressDialoge.dismiss();
                NextMTPListRes res = response.body();
                if(!res.isError()){
                    if(res.isConfirmed()){
                        alconf.setText(res.getErrormsg());
                        alconf.setVisibility(View.VISIBLE);
                        mtplst = res.getMtppendlist();
                        mtp = new String[mtplst.size()][5];
                        for (int i = 0; i < mtplst.size(); i++) {
                            MtppendlistItem temp = mtplst.get(i);
                            mtp[i][0] = temp.getTOWN();
                            mtp[i][1] = temp.getWORKDATE();
                            mtp[i][2] = temp.getOBJECTIVE();
                            mtp[i][3] = temp.getMRNETID();
                            mtp[i][4] = temp.getJOINTWORKING();
                        }

                        if(mtplst.size()>1){
                            LinkedAdaptiveTableAdapter mTableAdapter = new SampleLinkedTableAdapter(getActivity(), mtp, "3");
                            mTableAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(int row, int column) {
                                    dialogCloseType(getActivity(), mtp[row][column]);
                                }

                                @Override
                                public void onRowHeaderClick(int row) {
                                    dialogCloseType(getActivity(), mtp[row][0]);
                                }

                                @Override
                                public void onColumnHeaderClick(int column) {
                                    dialogCloseType(getActivity(), mtp[0][column]);
                                }

                                @Override
                                public void onLeftTopHeaderClick() {
                                    dialogCloseType(getActivity(), mtp[0][0]);
                                }
                            });
                            mTableAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                                @Override
                                public void onItemLongClick(int row, int column) {

                                }

                                @Override
                                public void onLeftTopHeaderLongClick() {

                                }
                            });
                            mTableLayout.setAdapter(mTableAdapter);
                            mTableAdapter.notifyDataSetChanged();
                        }else{
                            llt.setVisibility(View.GONE);
                        }
                    }else{
                        confirm.setText(res.getErrormsg());
                        confirm.setVisibility(View.VISIBLE);
                        confirm.setEnabled(true);
                        mtplst = res.getMtppendlist();
                        mtplst = res.getMtppendlist();
                        mtp = new String[mtplst.size()][5];
                        for (int i = 0; i < mtplst.size(); i++) {
                            MtppendlistItem temp = mtplst.get(i);
                            mtp[i][0] = temp.getTOWN();
                            mtp[i][1] = temp.getWORKDATE();
                            mtp[i][2] = temp.getOBJECTIVE();
                            mtp[i][3] = temp.getMRNETID();
                            mtp[i][4] = temp.getJOINTWORKING();
                        }

                        if(mtplst.size()>1){
                            LinkedAdaptiveTableAdapter mTableAdapter = new SampleLinkedTableAdapter(getActivity(), mtp, "3");
                            mTableAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(int row, int column) {
                                    dialogCloseType(getActivity(), mtp[row][column]);
                                }

                                @Override
                                public void onRowHeaderClick(int row) {
                                    dialogCloseType(getActivity(), mtp[row][0]);
                                }

                                @Override
                                public void onColumnHeaderClick(int column) {
                                    dialogCloseType(getActivity(), mtp[0][column]);
                                }

                                @Override
                                public void onLeftTopHeaderClick() {
                                    dialogCloseType(getActivity(), mtp[0][0]);
                                }
                            });
                            mTableAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                                @Override
                                public void onItemLongClick(int row, int column) {

                                }

                                @Override
                                public void onLeftTopHeaderLongClick() {

                                }
                            });
                            mTableLayout.setAdapter(mTableAdapter);
                            mTableAdapter.notifyDataSetChanged();
                        }else{
                            llt.setVisibility(View.GONE);
                        }
                    }
                }else{
                    alconf.setText(res.getErrormsg());
                    alconf.setVisibility(View.VISIBLE);
                    llt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<NextMTPListRes> call, Throwable t) {
                progressDialoge.dismiss();
                Snackbar.make(rtl,"Failed to get MTP details !", Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callapi();
                    }
                }).show();
            }
        });
    }

    public void dialogCloseType(final Context context, String stringmsg) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_edit_item);
        TextView textView = dialog.findViewById(R.id.tvTitle);
        AppCompatButton bPositive = dialog.findViewById(R.id.bPositive);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(stringmsg);
        bPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

}
