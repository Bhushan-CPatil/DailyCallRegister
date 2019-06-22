package com.eis.dailycallregister.Activity;

import android.app.ActivityOptions;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
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
import android.widget.Toast;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.cleveroad.adaptivetablelayout.OnItemClickListener;
import com.cleveroad.adaptivetablelayout.OnItemLongClickListener;
import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Fragment.Options;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.SampleLinkedTableAdapter;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.ConfirmDCRRes;
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
    TextView viewprod, viewgift, viewexp, dsvl, dnsvl, csvl, cnsvl, npob, tpob, texp, ded, remark;
    List<ExpensedetSummaryItem> expsum = new ArrayList<>();
    List<ProductSummaryItem> prodsum = new ArrayList<>();
    List<GiftSummaryItem> giftsum = new ArrayList<>();
    List<ProdgiftdetSummaryItem> pgdetsum = new ArrayList<>();
    RelativeLayout rtl;
    String intentremark = "";
    AdaptiveTableLayout mTableLayout, mTableLayout2;
    String[][] prodgiftdetsum;
    String[][] arrgiftsum;
    String[][] arrprodsum;
    String[][] arrexpsum;
    ScrollView parentScroll;
    CardView prodcard, giftcard;
    MaterialButton goback, confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>DCR SUMMARY</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        setContentView(R.layout.activity_check_dcrsummary);
        progressDialoge = new ViewDialog(CheckDCRSummary.this);
        intentremark = getIntent().getStringExtra("remark");
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
        viewgift = findViewById(R.id.viewgift);
        viewexp = findViewById(R.id.viewexp);
        prodcard = findViewById(R.id.prodsummcard);
        giftcard = findViewById(R.id.giftsummcard);
        rtl = findViewById(R.id.rtl);

        viewprod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pgdetsum.size() > 1)
                    detailedTablePopup(CheckDCRSummary.this, "DETAILS", prodgiftdetsum);
            }
        });

        viewgift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pgdetsum.size() > 1)
                    detailedTablePopup(CheckDCRSummary.this, "DETAILS", prodgiftdetsum);
            }
        });

        viewexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expsum.size() > 0) {
                    detailedTablePopup(CheckDCRSummary.this, "DETAILS", arrexpsum);
                } else {
                    //Toast.makeText(CheckDCRSummary.this, "Expense not filled", Toast.LENGTH_LONG).show();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CheckDCRSummary.this);
                    builder.setCancelable(true);
                    builder.setTitle("Alert !");
                    builder.setMessage("Expenses not entered !");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        callApi1();
        mTableLayout = findViewById(R.id.tableLayout);
        mTableLayout2 = findViewById(R.id.tableLayout2);
        parentScroll = findViewById(R.id.parentScroll);
        goback = findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDCREntry();
            }
        });

    }

    private void confirmDCREntry() {

        String a, b, c, d, e, f, g, h, i, j;
        a = dsvl.getText().toString();
        b = dnsvl.getText().toString();
        c = csvl.getText().toString();
        d = cnsvl.getText().toString();
        e = npob.getText().toString();
        f = tpob.getText().toString();
        g = ded.getText().toString();

        progressDialoge.show();
        Call<ConfirmDCRRes> call = RetrofitClient.getInstance()
                .getApi().confirmDCREntry(a.equalsIgnoreCase("") ? "0" : a, b.equalsIgnoreCase("") ? "0" : b,
                        c.equalsIgnoreCase("") ? "0" : c, d.equalsIgnoreCase("") ? "0" : d, e.equalsIgnoreCase("") ? "0" : e,
                        f.equalsIgnoreCase("") ? "0" : f, g.equalsIgnoreCase("") ? "0.00" : g, Global.ecode,
                        Global.dcrdate, Global.netid, Global.dcrno, Global.dbprefix);
        call.enqueue(new Callback<ConfirmDCRRes>() {
            @Override
            public void onResponse(Call<ConfirmDCRRes> call, Response<ConfirmDCRRes> response) {
                progressDialoge.dismiss();
                final ConfirmDCRRes res = response.body();
                if (!res.isError()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckDCRSummary.this);
                    builder.setCancelable(false);
                    builder.setTitle("Success");
                    builder.setMessage(Html.fromHtml(res.getErrormsg()));
                    builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (res.isPopuphrconnect()) {
                                new Global().clearGlobal("DCR");
                                String link = res.getPURL();
                                Intent intent = new Intent(CheckDCRSummary.this, HomeActivity.class);
                                intent.putExtra("openfrag", "home");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(CheckDCRSummary.this, R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                                startActivity(intent, bndlanimation);
                                finish();
                                finish();
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(res.getPURL()));
                                startActivity(browserIntent);
                            } else {
                                new Global().clearGlobal("DCR");
                                Intent intent = new Intent(CheckDCRSummary.this, HomeActivity.class);
                                intent.putExtra("openfrag", "dcr");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(CheckDCRSummary.this, R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                                startActivity(intent, bndlanimation);
                                finish();
                                finish();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }

            @Override
            public void onFailure(Call<ConfirmDCRRes> call, Throwable t) {
                progressDialoge.dismiss();
                Snackbar.make(rtl, "Something went wrong ! Please try again !", Snackbar.LENGTH_LONG)
                        .setAction("Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                confirmDCREntry();
                            }
                        }).show();
            }
        });
    }

    private void callApi1() {
        progressDialoge.show();
        Call<GetDCRSummaryMainRes> call = RetrofitClient.getInstance().getApi().getDCRSummary(Global.ecode, Global.netid, intentremark, Global.dcrno, Global.dbprefix);
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
                remark.setText(intentremark);
                expsum = res.getExpensedetSummary();
                prodsum = res.getProductSummary();
                giftsum = res.getGiftSummary();
                pgdetsum = res.getProdgiftdetSummary();

                prodgiftdetsum = new String[pgdetsum.size()][10];
                for (int i = 0; i < pgdetsum.size(); i++) {
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

                arrgiftsum = new String[giftsum.size() + 1][3];
                for (int i = 0; i < giftsum.size(); i++) {
                    GiftSummaryItem temp = giftsum.get(i);
                    if (i == 0) {
                        arrgiftsum[i][0] = "PRODUCT NAME";
                        arrgiftsum[i][1] = "COUNT";
                        arrgiftsum[i][2] = "QTY";
                    }
                    arrgiftsum[i + 1][0] = temp.getABV();
                    arrgiftsum[i + 1][1] = temp.getCOUNT();
                    arrgiftsum[i + 1][2] = temp.getQTY();
                }

                arrprodsum = new String[prodsum.size() + 1][3];
                for (int i = 0; i < prodsum.size(); i++) {
                    ProductSummaryItem temp = prodsum.get(i);
                    if (i == 0) {
                        arrprodsum[i][0] = "PRODUCT NAME";
                        arrprodsum[i][1] = "COUNT";
                        arrprodsum[i][2] = "QTY";
                    }
                    arrprodsum[i + 1][0] = temp.getABV();
                    arrprodsum[i + 1][1] = temp.getCOUNT();
                    arrprodsum[i + 1][2] = temp.getQTY();
                }

                if (!res.getTotexp().equalsIgnoreCase("")) {
                    arrexpsum = new String[expsum.size() + 2][2];
                    double amt = 0.00;
                    for (int i = 0; i < expsum.size(); i++) {
                        ExpensedetSummaryItem temp = expsum.get(i);
                        if (i == 0) {
                            arrexpsum[i][0] = "EXP.DESC";
                            arrexpsum[i][1] = "AMOUNT";
                        }
                        arrexpsum[i + 1][0] = temp.getEDESC();
                        arrexpsum[i + 1][1] = temp.getAMOUNT();
                        amt += Double.parseDouble(temp.getAMOUNT());

                        if (i == expsum.size() - 1) {
                            arrexpsum[i + 2][0] = "TOTAL";
                            arrexpsum[i + 2][1] = Double.toString(amt);
                        }
                    }
                }

                if (prodsum.size() > 0) {
                    prodcard.setVisibility(View.VISIBLE);
                    LinkedAdaptiveTableAdapter mTableAdapter = new SampleLinkedTableAdapter(CheckDCRSummary.this, arrprodsum, "1");
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
                }
                //2nd table
                if (giftsum.size() > 0) {
                    giftcard.setVisibility(View.VISIBLE);
                    LinkedAdaptiveTableAdapter mTableAdapter2 = new SampleLinkedTableAdapter(CheckDCRSummary.this, arrgiftsum, "1");
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
                }
                progressDialoge.dismiss();
            }

            @Override
            public void onFailure(Call<GetDCRSummaryMainRes> call, Throwable t) {
                progressDialoge.dismiss();
                Snackbar.make(rtl, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callApi1();
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
        LinkedAdaptiveTableAdapter mTableAdapter3 = new SampleLinkedTableAdapter(context, dataarray, "1");
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
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        CheckDCRSummary.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
