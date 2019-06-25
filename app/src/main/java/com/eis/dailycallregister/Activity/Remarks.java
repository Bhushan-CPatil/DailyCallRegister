package com.eis.dailycallregister.Activity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.DefaultResponse;
import com.eis.dailycallregister.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Remarks extends AppCompatActivity {

    ViewDialog progressDialoge;
    MaterialButton submitbtn;
    AppCompatEditText edtremk;
    LinearLayout llt;
    public String old_remark = "";
    public String serial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remarks);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>Remarks</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);

        progressDialoge = new ViewDialog(Remarks.this);
        edtremk = findViewById(R.id.remark);
        submitbtn = findViewById(R.id.submitrem);
        llt = findViewById(R.id.llt);
        serial = getIntent().getStringExtra("serial");
        getRemarkFromDB();
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Remarks.this);
                builder.setCancelable(true);
                builder.setTitle("Save ?");
                builder.setMessage("Are you sure you want to save remark ?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveRemarkInDB();
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
    }

    private void getRemarkFromDB() {
        progressDialoge.show();
        retrofit2.Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().getSubRemark(Global.dcrno, serial, Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                progressDialoge.dismiss();
                if (!res.isError()) {
                    old_remark = res.getErrormsg();
                    edtremk.setText(old_remark);
                } else {
                    Snackbar snackbar = Snackbar.make(llt, res.getErrormsg(), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(llt, "Failed to fetch remark !", Snackbar.LENGTH_LONG)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getRemarkFromDB();
                            }
                        });
                snackbar.show();
            }
        });
    }

    private void saveRemarkInDB() {
        if (edtremk.getText().toString().trim().equalsIgnoreCase(old_remark)) {
            Snackbar snackbar = Snackbar.make(llt, "Saved Successfully", Snackbar.LENGTH_LONG);
            snackbar.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    finish();
                    Remarks.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                }
            }, 1200);

        } else {
            progressDialoge.show();
            retrofit2.Call<DefaultResponse> call1 = RetrofitClient
                    .getInstance().getApi().saveRemark(Global.dcrno, serial, edtremk.getText().toString().trim(), Global.dbprefix);
            call1.enqueue(new Callback<DefaultResponse>() {
                @Override
                public void onResponse(retrofit2.Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                    DefaultResponse res = response.body();
                    progressDialoge.dismiss();
                    if (!res.isError()) {
                        Snackbar snackbar = Snackbar.make(llt, res.getErrormsg(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                finish();
                                Remarks.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                            }
                        }, 1200);
                    } else {
                        Snackbar snackbar = Snackbar.make(llt, res.getErrormsg(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }

                @Override
                public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                    progressDialoge.dismiss();
                    Snackbar snackbar = Snackbar.make(llt, "Failed to fetch data !", Snackbar.LENGTH_LONG)
                            .setAction("Re-try", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    saveRemarkInDB();
                                }
                            });
                    snackbar.show();
                }
            });
        }
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
        Remarks.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
