package com.eis.dailycallregister.Others;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.button.MaterialButton;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.eis.dailycallregister.R;

public class Global {
    public static String ecode = null;
    public static String ename = null;
    public static String password = null;
    public static String date = null;
    public static String d1d2 = null;
    public static String dbprefix = null;
    public static String netid = null;
    public static String hname = null;
    public static String dcrdate = null;
    public static String dcrdateday = null;
    public static String dcrdatemonth = null;
    public static String dcrdateyear = null;
    public static String SampleGiftRecOrNot = null;
    public static String workingareaid = null;
    public static String tcpid = null;
    public static String wrktype = null;
    public static String dcrno = null;
    public static boolean dcrdatestatus;
    public static boolean executedcrchecks;
    public static String finyear = null;
    public static String emplevel = "1";
    public static int misscallpopup = 0;
    public static String whichmth = null;

    public void clearGlobal(String mode) {
        if (mode.equalsIgnoreCase("All")) {
            ecode = null;
            ename = null;
            password = null;
            date = null;
            d1d2 = null;
            dbprefix = null;
            netid = null;
            hname = null;
            dcrdate = null;
            dcrdateday = null;
            dcrdatemonth = null;
            dcrdateyear = null;
            SampleGiftRecOrNot = null;
            workingareaid = null;
            tcpid = null;
            wrktype = null;
            dcrno = null;
            finyear = null;
            emplevel = "1";
            misscallpopup = 0;
            whichmth = null;
        } else if (mode.equalsIgnoreCase("DCR")) {
            dcrdate = null;
            dcrdateday = null;
            dcrdatemonth = null;
            dcrdateyear = null;
            SampleGiftRecOrNot = null;
            workingareaid = null;
            tcpid = null;
            wrktype = null;
            dcrno = null;
            finyear = null;
            emplevel = "1";
            whichmth = null;
        }
    }


    public static void successDilogue(final Context context, final String result) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.success_dilogue);
        MaterialButton button = dialog.findViewById(R.id.btnsucces);
        AppCompatTextView textView = dialog.findViewById(R.id.successtext);
        textView.setText(result);
        button.setOnClickListener(new View.OnClickListener() {
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

    public void notAllowed(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Coming soon....");
        builder.setMessage("This feature is currently under construction !");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static String getFinancialYr(String logMth, String logYr) {
        String finStrtMth = "04";
        //String logMth = "01";
        //String logYr = "2019";

        int endMth = Integer.parseInt(finStrtMth) - 1;
        int strtYr = getFinStrtYr(Integer.parseInt(finStrtMth), Integer.parseInt(logMth), Integer.parseInt(logYr));
        int endYr = 0;

        if (endMth == 0) {
            endMth = 12;
            endYr = strtYr;
        } else {
            endYr = strtYr + 1;
        }
        //System.out.println("Financial Year is :"+finStrtMth+strtYr+" to "+endMth+""+endYr);

        //return strtYr+finStrtMth+"-"+endYr+""+((endMth+"").length()<2 ? "0"+endMth : endMth);
        String syr = Integer.toString(strtYr).substring(2);
        String eyr = Integer.toString(endYr).substring(2);
        return syr + "" + eyr;
    }

    public static String getFullFinancialYr(String logMth, String logYr) {
        String finStrtMth = "04";

        int endMth = Integer.parseInt(finStrtMth) - 1;
        int strtYr = getFinStrtYr(Integer.parseInt(finStrtMth), Integer.parseInt(logMth), Integer.parseInt(logYr));
        int endYr = 0;

        if (endMth == 0) {
            endMth = 12;
            endYr = strtYr;
        } else {
            endYr = strtYr + 1;
        }
        return strtYr + "-" + endYr;
    }


    public static int getFinStrtYr(int strtMth, int logMth, int logYr) {
        if (logMth < strtMth) { //login date=012017 -> (logmth)01 <= (endMth)0
            return (logYr - 1); //2017
        } else { //login date=042017 -> 04>03
            return logYr; //2016
        }
    }

    public static String getFieldName(int mth) {
        String field = "";

        switch (mth) {
            case 1:
                field = "JANCON";
                break;
            case 2:
                field = "FEBCON";
                break;
            case 3:
                field = "MARCON";
                break;
            case 4:
                field = "APRCON";
                break;
            case 5:
                field = "MAYCON";
                break;
            case 6:
                field = "JUNCON";
                break;
            case 7:
                field = "JULCON";
                break;
            case 8:
                field = "AUGCON";
                break;
            case 9:
                field = "SEPCON";
                break;
            case 10:
                field = "OCTCON";
                break;
            case 11:
                field = "NOVCON";
                break;
            case 12:
                field = "DECCON";
                break;
        }

        return field;
    }
}
