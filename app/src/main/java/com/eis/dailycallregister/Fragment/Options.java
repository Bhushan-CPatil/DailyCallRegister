package com.eis.dailycallregister.Fragment;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.eis.dailycallregister.Pojo.MissCallDocsRes;
import com.eis.dailycallregister.Pojo.MisscalldrsItem;
import com.eis.dailycallregister.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Options extends  Fragment {

    MaterialButton dcr,mtp,uploadcard;
    ViewDialog progressDialoge;
    List<MisscalldrsItem> misscall = new ArrayList<>();
    LinearLayout menuoptions;
    AdaptiveTableLayout mTableLayout;
    String[][] misseddr;
    View view;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_options, container, false);

        dcr = view.findViewById(R.id.dcr);
        mtp = view.findViewById(R.id.mtp);
        menuoptions = view.findViewById(R.id.menuoptions);
        progressDialoge=new ViewDialog(getActivity());
        mTableLayout = view.findViewById(R.id.tableLayout);
        uploadcard = view.findViewById(R.id.uploadcard);

        dcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new Global().notAllowed(getActivity());
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                intent.putExtra("ecode", Global.ecode);
                intent.putExtra("date",Global.date);
                intent.putExtra("dbprefix",Global.dbprefix);
                intent.putExtra("openfrag","dcr");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(intent,bndlanimation);
                getActivity().finish();
            }
        });

        uploadcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                intent.putExtra("ecode", Global.ecode);
                intent.putExtra("date",Global.date);
                intent.putExtra("dbprefix",Global.dbprefix);
                intent.putExtra("openfrag","visitingcard");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(intent,bndlanimation);
                getActivity().finish();
            }
        });

        mtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Global().notAllowed(getActivity());

            }
        });

        if(Global.misscallpopup == 0) {
            getMissCalls();
        }
        return view;

    }

    private void getMissCalls() {
        String[] newdate = Global.date.split("-");
        progressDialoge.show();
        Call<MissCallDocsRes> call = RetrofitClient.getInstance()
                .getApi().DrMissCallAlert(Global.ecode,Global.netid,newdate[0],newdate[1],Global.dbprefix);
        call.enqueue(new Callback<MissCallDocsRes>() {
            @Override
            public void onResponse(Call<MissCallDocsRes> call, Response<MissCallDocsRes> response) {
                MissCallDocsRes res = response.body();
                Global.misscallpopup = 1;
                if(!res.isError()) {
                    misscall = res.getMisscalldrs();
                    misseddr = new String[misscall.size()][3];
                    for (int i = 0; i < misscall.size(); i++) {
                        MisscalldrsItem temp = misscall.get(i);
                        misseddr[i][0] = temp.getTOWN();
                        misseddr[i][1] = temp.getDRNAMES();
                        misseddr[i][2] = temp.getTOTAL();
                    }

                    if(misscall.size()>1)
                        detailedTablePopup(getActivity(), "MISSED CALL DOCTORS", misseddr);
                    else{
                        Snackbar.make(menuoptions, "Doctors not missed yet.", Snackbar.LENGTH_LONG).show();
                    }

                    progressDialoge.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MissCallDocsRes> call, Throwable t) {
                progressDialoge.dismiss();
                Snackbar.make(menuoptions, "Failed to get miss calls !", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void detailedTablePopup(final Context context, String stringmsg, String[][] aary) {
        final String[][] dataarray;
        dataarray = new String[aary.length][];
        for (int i = 0; i < dataarray.length; ++i) {
            dataarray[i] = new String[aary[i].length];
            for (int j = 0; j < dataarray[i].length; ++j) {
                dataarray[i][j] = aary[i][j];
            }
        }
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.detailed_table_popup);
        TextView textView = dialog.findViewById(R.id.title);
        textView.setText(stringmsg);
        ImageButton goback = dialog.findViewById(R.id.goback);
        AdaptiveTableLayout mTableLayout3 = dialog.findViewById(R.id.dettablelayout);
        LinkedAdaptiveTableAdapter mTableAdapter3 = new SampleLinkedTableAdapter(context, dataarray, "2");
        mTableAdapter3.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int row, int column) {
                dialogCloseType(context, dataarray[row][column]);
            }

            @Override
            public void onRowHeaderClick(int row) {
                dialogCloseType(context, dataarray[row][0]);
            }

            @Override
            public void onColumnHeaderClick(int column) {
                dialogCloseType(context, dataarray[0][column]);
            }

            @Override
            public void onLeftTopHeaderClick() {
                dialogCloseType(context, dataarray[0][0]);
            }
        });
        mTableAdapter3.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int row, int column) {

            }

            @Override
            public void onLeftTopHeaderLongClick() {

            }
        });
        mTableLayout3.setAdapter(mTableAdapter3);
        mTableAdapter3.notifyDataSetChanged();

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
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

