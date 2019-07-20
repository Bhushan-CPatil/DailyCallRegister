package com.eis.dailycallregister.Fragment;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.cleveroad.adaptivetablelayout.OnItemClickListener;
import com.cleveroad.adaptivetablelayout.OnItemLongClickListener;
import com.eis.dailycallregister.Activity.HomeActivity;
import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.SampleLinkedTableAdapter;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.MissCallDocsRes;
import com.eis.dailycallregister.Pojo.MisscalldrsItem;
import com.eis.dailycallregister.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Options extends Fragment {

    MaterialButton dcr, mtp, uploadcard, vps;
    ViewDialog progressDialoge;
    List<MisscalldrsItem> misscall = new ArrayList<>();
    LinearLayout menuoptions;
    AdaptiveTableLayout mTableLayout;
    String[][] misseddr;
    View view;
    String checkmtp = "";
    ArrayList<String> empacc = new ArrayList<>();

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
        vps = view.findViewById(R.id.vps);
        menuoptions = view.findViewById(R.id.menuoptions);
        progressDialoge = new ViewDialog(getActivity());
        mTableLayout = view.findViewById(R.id.tableLayout);
        uploadcard = view.findViewById(R.id.uploadcard);
        empacc.clear();
        //CD
        empacc.add("02680");
        empacc.add("02684");
        empacc.add("02957");
        empacc.add("03069");
        empacc.add("01804");
        empacc.add("02274");
        empacc.add("02681");
        empacc.add("02956");
        empacc.add("01652");
        empacc.add("02706");
        empacc.add("02944");
        empacc.add("03196");
        empacc.add("03340");
        empacc.add("03358");
        empacc.add("01973");
        empacc.add("02901");
        empacc.add("03239");
        empacc.add("03260");
        empacc.add("03339");
        empacc.add("03402");

        //AB
        empacc.add("02654");
        empacc.add("02663");
        empacc.add("02672");
        empacc.add("02782");
        empacc.add("03286");
        empacc.add("01637");
        empacc.add("00475");
        empacc.add("01941");
        empacc.add("02364");
        empacc.add("02366");
        empacc.add("03293");
        empacc.add("00431");
        empacc.add("01575");
        empacc.add("02985");
        empacc.add("03118");
        empacc.add("03151");
        empacc.add("03202");
        empacc.add("03303");

        //kol team
        empacc.add("01349");
        empacc.add("01511");
        empacc.add("01723");
        empacc.add("01809");
        empacc.add("02042");
        empacc.add("02712");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            // Do something for lollipop and above versions
            if (empacc.contains(Global.ecode)) {

            }else{
                dcr.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.textcolorgray));
                mtp.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.textcolorgray));
            }
        } else{
            // do something for phones running an SDK before lollipop
            if (empacc.contains(Global.ecode)) {

            }else{
                dcr.setRippleColor(getActivity().getResources().getColorStateList(R.color.textcolorgray));
                mtp.setRippleColor(getActivity().getResources().getColorStateList(R.color.textcolorgray));
            }
        }
        Global.whichmth = null;
        dcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new Global().notAllowed(getActivity());
                if (empacc.contains(Global.ecode)) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra("ecode", Global.ecode);
                    intent.putExtra("date", Global.date);
                    intent.putExtra("dbprefix", Global.dbprefix);
                    intent.putExtra("openfrag", "dcr");
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                    startActivity(intent, bndlanimation);
                    getActivity().finish();
                } else {
                    new Global().notAllowed(getActivity());
                }
            }
        });

        uploadcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("ecode", Global.ecode);
                intent.putExtra("date", Global.date);
                intent.putExtra("dbprefix", Global.dbprefix);
                intent.putExtra("openfrag", "visitingcard");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(intent, bndlanimation);
                getActivity().finish();
            }
        });

        vps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.putExtra("ecode", Global.ecode);
                intent.putExtra("date", Global.date);
                intent.putExtra("dbprefix", Global.dbprefix);
                intent.putExtra("openfrag", "visitplansum");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(intent, bndlanimation);
                getActivity().finish();
            }
        });

        mtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Alert ?");
                builder.setMessage("Which month of MTP you wants to view ?");
                builder.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Global.whichmth = "NEXT";
                        Intent intent = new Intent(getActivity(),HomeActivity.class);
                        intent.putExtra("ecode", Global.ecode);
                        intent.putExtra("date",Global.date);
                        intent.putExtra("dbprefix",Global.dbprefix);
                        intent.putExtra("openfrag","mtp");
                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                        startActivity(intent,bndlanimation);
                        getActivity().finish();
                    }
                });
                builder.setNeutralButton("CURRENT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //new Global().notAllowed(getActivity());
                        Global.whichmth = "CURRENT";
                        Intent intent = new Intent(getActivity(),HomeActivity.class);
                        intent.putExtra("ecode", Global.ecode);
                        intent.putExtra("date",Global.date);
                        intent.putExtra("dbprefix",Global.dbprefix);
                        intent.putExtra("openfrag","mtp");
                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                        startActivity(intent,bndlanimation);
                        getActivity().finish();
                    }
                });
                AlertDialog dialog2 = builder.create();
                dialog2.show();*/
                if (empacc.contains(Global.ecode)) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.putExtra("ecode", Global.ecode);
                    intent.putExtra("date", Global.date);
                    intent.putExtra("dbprefix", Global.dbprefix);
                    intent.putExtra("openfrag", "mtp");
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                    startActivity(intent, bndlanimation);
                    getActivity().finish();
                } else {
                    new Global().notAllowed(getActivity());
                }
            }
        });

        if (Global.misscallpopup == 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            //String datex = new SimpleDateFormat("yyyy-MM-15", Locale.getDefault()).format(new Date());
            String datex = new SimpleDateFormat("yyyy-MM-15", Locale.getDefault()).format(new Date());
            Calendar calendar1 = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();

            Date date1 = null;
            Date date2 = null;
            try {
                date1 = dateFormat.parse(datex);
                date2 = dateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            calendar1.setTime(date1);
            calendar2.setTime(date2);
            //Toast.makeText(getActivity(),datex +"///"+ date , Toast.LENGTH_LONG).show();
            if (calendar2.compareTo(calendar1) < 0) {
                checkmtp = "N";
                //Toast.makeText(getActivity(), "Do not show", Toast.LENGTH_LONG).show();
            } else {
                checkmtp = "Y";
                //Toast.makeText(getActivity(), "show MTP", Toast.LENGTH_LONG).show();
                /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setMessage("Next month MTP is ready to view.");
                builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Global.whichmth = "NEXT";
                        Intent intent = new Intent(getActivity(),HomeActivity.class);
                        intent.putExtra("ecode", Global.ecode);
                        intent.putExtra("date",Global.date);
                        intent.putExtra("dbprefix",Global.dbprefix);
                        intent.putExtra("openfrag","mtp");
                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                        startActivity(intent,bndlanimation);
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                AlertDialog dialog2 = builder.create();
                dialog2.show();*/
            }
            getMissCalls();
        }

        /*String valid_until = "19";
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date strDate = null;
        try {
            strDate = sdf.parse(valid_until);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (new Date().after(strDate)) {
            Toast.makeText(getActivity(), "Show mtp", Toast.LENGTH_LONG).show();
        }*/


        return view;

    }

    private void getMissCalls() {
        String[] newdate = Global.date.split("-");
        progressDialoge.show();
        Call<MissCallDocsRes> call = RetrofitClient.getInstance()
                .getApi().DrMissCallAlert(Global.ecode, Global.netid, newdate[0], newdate[1], checkmtp, Global.dbprefix);
        call.enqueue(new Callback<MissCallDocsRes>() {
            @Override
            public void onResponse(Call<MissCallDocsRes> call, Response<MissCallDocsRes> response) {
                MissCallDocsRes res = response.body();
                Global.misscallpopup = 1;
                // && (Global.ecode.equalsIgnoreCase("01349") || Global.ecode.equalsIgnoreCase("01511") || Global.ecode.equalsIgnoreCase("01723") || Global.ecode.equalsIgnoreCase("01809") || Global.ecode.equalsIgnoreCase("02042") || Global.ecode.equalsIgnoreCase("02712"))
                if (res.isMtpflg() && (empacc.contains(Global.ecode))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(true);
                    builder.setMessage("Next month MTP is ready to view.");
                    builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Global.whichmth = "NEXT";
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.putExtra("ecode", Global.ecode);
                            intent.putExtra("date", Global.date);
                            intent.putExtra("dbprefix", Global.dbprefix);
                            intent.putExtra("openfrag", "mtp");
                            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                            startActivity(intent, bndlanimation);
                            getActivity().finish();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    });
                    AlertDialog dialog2 = builder.create();
                    dialog2.show();
                }

                if (!res.isError()) {
                    misscall = res.getMisscalldrs();
                    misseddr = new String[misscall.size()][3];
                    for (int i = 0; i < misscall.size(); i++) {
                        MisscalldrsItem temp = misscall.get(i);
                        misseddr[i][0] = temp.getTOWN();
                        misseddr[i][1] = temp.getDRNAMES();
                        misseddr[i][2] = temp.getTOTAL();
                    }

                    if (misscall.size() > 1)
                        detailedTablePopup(getActivity(), "MISSED CALL DOCTORS", misseddr);
                    /*else{
                        Snackbar.make(menuoptions, "Doctors not missed yet.", Snackbar.LENGTH_LONG).show();
                    }*/

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

