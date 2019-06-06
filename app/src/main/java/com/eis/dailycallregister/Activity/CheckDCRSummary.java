package com.eis.dailycallregister.Activity;

import android.app.Dialog;
import android.content.Context;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.cleveroad.adaptivetablelayout.OnItemClickListener;
import com.cleveroad.adaptivetablelayout.OnItemLongClickListener;
import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.SampleLinkedTableAdapter;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.ExpensedetSummaryItem;
import com.eis.dailycallregister.Pojo.GetDCRSummaryMainRes;
import com.eis.dailycallregister.Pojo.GiftSummaryItem;
import com.eis.dailycallregister.Pojo.ProdgiftdetSummaryItem;
import com.eis.dailycallregister.Pojo.ProductSummaryItem;
import com.eis.dailycallregister.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckDCRSummary extends AppCompatActivity {

    ViewDialog progressDialoge;
    TextView viewprod,dsvl,dnsvl,csvl,cnsvl,npob,tpob,texp,ded,remark;
    List<ExpensedetSummaryItem> expsum = new ArrayList<>();
    List<ProductSummaryItem> prodsum = new ArrayList<>();
    List<GiftSummaryItem> giftsum = new ArrayList<>();
    List<ProdgiftdetSummaryItem> pgdetsum = new ArrayList<>();
    RelativeLayout rtl;
    AdaptiveTableLayout mTableLayout,mTableLayout2;
    String[][] prodgiftdetsum;
    String[][] arrgiftsum;
    String[][] arrprodsum;
    ScrollView parentScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>DCR SUMMARY</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        setContentView(R.layout.activity_check_dcrsummary);
        progressDialoge=new ViewDialog(CheckDCRSummary.this);
        dsvl = findViewById(R.id.dsvl);
        dnsvl = findViewById(R.id.dnsvl);
        csvl = findViewById(R.id.csvl);
        cnsvl = findViewById(R.id.cnsvl);
        npob = findViewById(R.id.npob);
        tpob = findViewById(R.id.tpob);
        texp = findViewById(R.id.texp);
        ded = findViewById(R.id.ded);
        remark = findViewById(R.id.remark);
        viewprod = findViewById(R.id.viewprod);
        rtl = findViewById(R.id.rtl);
        viewprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailedTablePopup(CheckDCRSummary.this,"DETAILS",prodgiftdetsum);
            }
        });
        TextView viewgift = findViewById(R.id.viewgift);
        viewgift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailedTablePopup(CheckDCRSummary.this,"DETAILS",prodgiftdetsum);
            }
        });

        callApi1();
        mTableLayout = findViewById(R.id.tableLayout);
        mTableLayout2 = findViewById(R.id.tableLayout2);
        parentScroll = findViewById(R.id.parentScroll);
        MaterialButton goback = findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void callApi1() {
        progressDialoge.show();
        Call<GetDCRSummaryMainRes> call = RetrofitClient.getInstance().getApi().getDCRSummary(Global.ecode,Global.netid,"",Global.dcrno,Global.dbprefix);
        call.enqueue(new Callback<GetDCRSummaryMainRes>() {
            @Override
            public void onResponse(Call<GetDCRSummaryMainRes> call, Response<GetDCRSummaryMainRes> response) {
                GetDCRSummaryMainRes res = response.body();
                dsvl.setText(res.getDocsvl());
                dnsvl.setText(res.getDocnsvl());
                csvl.setText(res.getChemsvl());
                cnsvl.setText(res.getChemnsvl());
                npob.setText(res.getNopob());
                tpob.setText(res.getTotpob());
                texp.setText(res.getTotexp());
                ded.setText(res.getDeduction());
                remark.setText("");
                expsum = res.getExpensedetSummary();
                prodsum = res.getProductSummary();
                giftsum = res.getGiftSummary();
                pgdetsum = res.getProdgiftdetSummary();

                prodgiftdetsum = new String[pgdetsum.size()][10];
                for(int i=0;i < pgdetsum.size();i++){
                    ProdgiftdetSummaryItem temp = pgdetsum.get(i);
                    prodgiftdetsum[i][0] = temp.getDRSTNAME();
                    prodgiftdetsum[i][1] = temp.getDRCD();
                    prodgiftdetsum[i][2] = temp.getVSTTM();
                    prodgiftdetsum[i][3] = temp.getTOWN();
                    prodgiftdetsum[i][4] = temp.getWWITH();
                    prodgiftdetsum[i][5] = temp.getPOB();
                    prodgiftdetsum[i][6] = temp.getPRODQTY();
                    prodgiftdetsum[i][7] = temp.getGIFTQTY();
                    prodgiftdetsum[i][8] = temp.getRX();
                    prodgiftdetsum[i][9] = temp.getPRODRQTY();
                }

                arrgiftsum = new String[giftsum.size()+1][3];
                for(int i=0;i < giftsum.size();i++){
                    GiftSummaryItem temp = giftsum.get(i);
                    if(i==0){
                        arrgiftsum[i][0] = "PRODUCT NAME";
                        arrgiftsum[i][1] = "COUNT";
                        arrgiftsum[i][2] = "QTY";
                    }
                    arrgiftsum[i+1][0] = temp.getABV();
                    arrgiftsum[i+1][1] = temp.getCOUNT();
                    arrgiftsum[i+1][2] = temp.getQTY();
                }

                arrprodsum = new String[prodsum.size()+1][3];
                for(int i=0;i < prodsum.size();i++){
                    ProductSummaryItem temp = prodsum.get(i);
                    if(i==0){
                        arrprodsum[i][0] = "PRODUCT NAME";
                        arrprodsum[i][1] = "COUNT";
                        arrprodsum[i][2] = "QTY";
                    }
                    arrprodsum[i+1][0] = temp.getABV();
                    arrprodsum[i+1][1] = temp.getCOUNT();
                    arrprodsum[i+1][2] = temp.getQTY();
                }

                LinkedAdaptiveTableAdapter mTableAdapter = new SampleLinkedTableAdapter(CheckDCRSummary.this, arrprodsum);
                mTableAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int row, int column) {
                        dialogCloseType(CheckDCRSummary.this, arrprodsum[row][column]);
                    }

                    @Override
                    public void onRowHeaderClick(int row) {

                        dialogCloseType(CheckDCRSummary.this, arrprodsum[row][0]);
                    }

                    @Override
                    public void onColumnHeaderClick(int column) {
                        dialogCloseType(CheckDCRSummary.this, arrprodsum[0][column]);
                    }

                    @Override
                    public void onLeftTopHeaderClick() {
                        dialogCloseType(CheckDCRSummary.this, arrprodsum[0][0]);
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

                LinkedAdaptiveTableAdapter mTableAdapter2 = new SampleLinkedTableAdapter(CheckDCRSummary.this, arrgiftsum);
                mTableAdapter2.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int row, int column) {
                        dialogCloseType(CheckDCRSummary.this, arrgiftsum[row][column]);
                    }

                    @Override
                    public void onRowHeaderClick(int row) {
                        dialogCloseType(CheckDCRSummary.this, arrgiftsum[row][0]);
                    }

                    @Override
                    public void onColumnHeaderClick(int column) {
                        dialogCloseType(CheckDCRSummary.this, arrgiftsum[0][column]);
                    }

                    @Override
                    public void onLeftTopHeaderClick() {
                        dialogCloseType(CheckDCRSummary.this, arrgiftsum[0][0]);
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

                progressDialoge.dismiss();
            }

            @Override
            public void onFailure(Call<GetDCRSummaryMainRes> call, Throwable t) {
                progressDialoge.dismiss();
                Snackbar.make(rtl, "Failed to fetch data !",Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callApi1();
                            }
                        }).show();
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

    public void detailedTablePopup(final Context context, String stringmsg, String[][] aary){
        final String[][] dataarray;
        dataarray =new String[aary.length][];
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
        LinkedAdaptiveTableAdapter mTableAdapter3 = new SampleLinkedTableAdapter(context, dataarray);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        } return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        CheckDCRSummary.this.overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
    }
}
