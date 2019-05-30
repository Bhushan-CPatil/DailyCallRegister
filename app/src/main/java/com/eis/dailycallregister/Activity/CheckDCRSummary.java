package com.eis.dailycallregister.Activity;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.cleveroad.adaptivetablelayout.OnItemClickListener;
import com.cleveroad.adaptivetablelayout.OnItemLongClickListener;
import com.eis.dailycallregister.Others.SampleLinkedTableAdapter;
import com.eis.dailycallregister.R;

public class CheckDCRSummary extends AppCompatActivity {

    public String[][] arr = {
            {"DR/ST NAME", "CODE", "VST TIME","TOWN", "WORK WITH NAME", "POB","PRODUCT/QTY", "GIFT/QTY", "PRODUCT/RX/RXQTY"},
            {"10", "20", "M/S BIJOY MEDICAL","SCH-17 JORHAT", "20", "0","NVLBAR125/0\n" +
                    "AF-TER L20/3\n" +
                    "AF-TER TAB/0\n" +
                    "HYSY/5\n" +
                    "NVL LOTN60/1", "NLCN/1", "NVLBAR125/4\n" +
                    "AF-TER L20/3\n" +
                    "AF-TER TAB/6\n" +
                    "HYSY/6\n" +
                    "NVL LOTN60/2"},
            {"10", "20", "M/S BIJOY MEDICAL","SCH-17 JORHAT", "20", "0","NVLBAR125/0\n" +
                    "AF-TER L20/3\n" +
                    "AF-TER TAB/0\n" +
                    "HYSY/5\n" +
                    "NVL LOTN60/1", "NLCN/1", "NVLBAR125/4\n" +
                    "AF-TER L20/3\n" +
                    "AF-TER TAB/6\n" +
                    "HYSY/6\n" +
                    "NVL LOTN60/2"},

            {"10", "20", "M/S BIJOY MEDICAL","SCH-17 JORHAT", "20", "0","NVLBAR125/0\n" +
                    "AF-TER L20/3\n" +
                    "AF-TER TAB/0\n" +
                    "HYSY/5\n" +
                    "NVL LOTN60/1", "NLCN/1", "NVLBAR125/4\n" +
                    "AF-TER L20/3\n" +
                    "AF-TER TAB/6\n" +
                    "HYSY/6\n" +
                    "NVL LOTN60/2"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>DCR SUMMARY</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        setContentView(R.layout.activity_check_dcrsummary);
        TextView viewprod = findViewById(R.id.viewprod);
        viewprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailedTablePopup(CheckDCRSummary.this,"DETAILS");
            }
        });
        TextView viewgift = findViewById(R.id.viewgift);
        viewgift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailedTablePopup(CheckDCRSummary.this,"PRODUCT DETAILS");
            }
        });

        AdaptiveTableLayout mTableLayout = findViewById(R.id.tableLayout);
        ScrollView parentScroll = findViewById(R.id.parentScroll);

        LinkedAdaptiveTableAdapter mTableAdapter = new SampleLinkedTableAdapter(CheckDCRSummary.this, arr);
        mTableAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int row, int column) {
                dialogCloseType(CheckDCRSummary.this, arr[row][column]);
            }

            @Override
            public void onRowHeaderClick(int row) {

                dialogCloseType(CheckDCRSummary.this, arr[row][0]);
            }

            @Override
            public void onColumnHeaderClick(int column) {
                dialogCloseType(CheckDCRSummary.this, arr[0][column]);
            }

            @Override
            public void onLeftTopHeaderClick() {
                dialogCloseType(CheckDCRSummary.this, arr[0][0]);
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
        parentScroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                Log.v("PARENT", "PARENT TOUCH");
                findViewById(R.id.tableLayout).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        mTableLayout.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                Log.v("CHILD", "CHILD TOUCH");
                // Disallow the touch request for parent scroll on touch of
                // child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        //2nd table
        AdaptiveTableLayout mTableLayout2 = findViewById(R.id.tableLayout2);
        LinkedAdaptiveTableAdapter mTableAdapter2 = new SampleLinkedTableAdapter(CheckDCRSummary.this, arr);
        mTableAdapter2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int row, int column) {
                dialogCloseType(CheckDCRSummary.this, arr[row][column]);
            }

            @Override
            public void onRowHeaderClick(int row) {
                dialogCloseType(CheckDCRSummary.this, arr[row][0]);
            }

            @Override
            public void onColumnHeaderClick(int column) {
                dialogCloseType(CheckDCRSummary.this, arr[0][column]);
            }

            @Override
            public void onLeftTopHeaderClick() {
                dialogCloseType(CheckDCRSummary.this, arr[0][0]);
            }
        });
        mTableAdapter2.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int row, int column) {

            }

            @Override
            public void onLeftTopHeaderLongClick() {

            }
        });
        mTableLayout2.setAdapter(mTableAdapter2);
        mTableAdapter2.notifyDataSetChanged();
        mTableLayout2.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                Log.v("CHILD", "CHILD TOUCH");
                // Disallow the touch request for parent scroll on touch of
                // child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    public void dialogCloseType(final Context context, String stringmsg){
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

    public void detailedTablePopup(final Context context, String stringmsg){
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.detailed_table_popup);
        TextView textView = dialog.findViewById(R.id.title);
        textView.setText(stringmsg);
        ImageButton goback = dialog.findViewById(R.id.goback);
        AdaptiveTableLayout mTableLayout3 = dialog.findViewById(R.id.dettablelayout);
        LinkedAdaptiveTableAdapter mTableAdapter3 = new SampleLinkedTableAdapter(context, arr);
        mTableAdapter3.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int row, int column) {
                dialogCloseType(context, arr[row][column]);
            }

            @Override
            public void onRowHeaderClick(int row) {
                dialogCloseType(context, arr[row][0]);
            }

            @Override
            public void onColumnHeaderClick(int column) {
                dialogCloseType(context, arr[0][column]);
            }

            @Override
            public void onLeftTopHeaderClick() {
                dialogCloseType(context, arr[0][0]);
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
}
