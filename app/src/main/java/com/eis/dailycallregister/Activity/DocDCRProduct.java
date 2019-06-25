package com.eis.dailycallregister.Activity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.DCRGiftListRes;
import com.eis.dailycallregister.Pojo.DCRGiftSplDrDetItem;
import com.eis.dailycallregister.Pojo.DCRProdDemoDetItem;
import com.eis.dailycallregister.Pojo.DCRProdListRes;
import com.eis.dailycallregister.Pojo.DCRRidacneDetItem;
import com.eis.dailycallregister.Pojo.DcrddrlstItem;
import com.eis.dailycallregister.Pojo.DcrproductlistItem;
import com.eis.dailycallregister.Pojo.DefaultResponse;
import com.eis.dailycallregister.Pojo.EpidermPopUpRes;
import com.eis.dailycallregister.Pojo.GetPopupQuesRes;
import com.eis.dailycallregister.Pojo.QseraPopUpRes;
import com.eis.dailycallregister.Pojo.QuestionslistItem;
import com.eis.dailycallregister.Pojo.RedicnePopUpRes;
import com.eis.dailycallregister.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocDCRProduct extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 60000;
    public static final int READ_TIMEOUT = 90000;
    String qgen = "", spflag = "", pflag = "", label = "";
    boolean showDropdownAlert = false;
    ViewDialog progressDialoge;
    MaterialButton submitbtn, cancelbtn;
    ConstraintLayout nsv;
    TextView docname;
    //NestedScrollView nsv;
    RecyclerView productnameslist;
    public String serial, serialwp, d1d2, iscompcall, finyr, field, cntcd, drclass;
    Spinner spn;
    int position;
    String param = "";
    public List<DcrproductlistItem> dcrprodlst = new ArrayList<>();
    public List<QuestionslistItem> questionslist = new ArrayList<>();
    public List<DCRProdDemoDetItem> pop1data = new ArrayList<>();
    public List<DCRGiftSplDrDetItem> pop2data = new ArrayList<>();
    public List<DCRRidacneDetItem> pop3data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_dcrproduct);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>Product Entry</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        String[] arraySpinner = new String[]{
                "NO", "YES"
        };
        finyr = Global.getFinancialYr(Global.dcrdatemonth, Global.dcrdateyear);
        iscompcall = getIntent().getStringExtra("compcall");
        drclass = getIntent().getStringExtra("drclass");
        position = Integer.parseInt(getIntent().getStringExtra("position"));
        field = Global.getFieldName(Integer.parseInt(Global.dcrdatemonth));

        progressDialoge = new ViewDialog(DocDCRProduct.this);

        serial = getIntent().getStringExtra("serial");
        serialwp = getIntent().getStringExtra("oserial");
        cntcd = getIntent().getStringExtra("cntcd");
        if (Global.hname.contains("(A)")) {
            d1d2 = "A";
        } else if (Global.hname.contains("(B)")) {
            d1d2 = "B";
        } else if (Global.hname.contains("(C)")) {
            d1d2 = "C";
        } else if (Global.hname.contains("(D)")) {
            d1d2 = "D";
        }

        submitbtn = findViewById(R.id.submit);
        cancelbtn = findViewById(R.id.cancel);
        nsv = findViewById(R.id.nsv);
        productnameslist = findViewById(R.id.productlist);
        docname = findViewById(R.id.docname);
        docname.setText(getIntent().getStringExtra("drname"));
        spn = findViewById(R.id.iscompcall);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);
        if (iscompcall.equalsIgnoreCase("Y")) {
            spn.setSelection(1);
        } else {
            spn.setSelection(0);
        }
        setProductAdapter();

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                param = "SUBMIT";
                //productnameslist.clearFocus();
                if (showDropdownAlert) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DocDCRProduct.this);
                    builder.setCancelable(true);
                    builder.setTitle(Html.fromHtml("<font color='#FF5555'>" + label + "</font>"));
                    //builder.setMessage("\nNote : If answer is already given then the answer is marked with RED colour.\nIf you don't want to change answer click on CANCEL button.");
                    builder.setMessage(Html.fromHtml("<b>NOTE :</b> If answer is already given then the answer is marked with <b>RED</b> colour.<br>If you don't want to change the answer click on <b>CANCEL</b> button."));
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            qgen = "Y";
                            submitentry();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            qgen = "N";
                            submitentry();
                        }
                    });
                    builder.setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            qgen = "";
                            submitentry();
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            if (qgen.equalsIgnoreCase("N")) {
                                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.textcolorred));
                            } else if (qgen.equalsIgnoreCase("Y")) {
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.textcolorred));
                            }
                        }
                    });
                    dialog.show();
                } else {
                    submitentry();
                }


                //Toast.makeText(DocDCRGift.this, myCustomArray.toString(), Toast.LENGTH_LONG).show();
            }
        });

        /*submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                productnameslist.clearFocus();
                Gson gson = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree(dcrprodlst).getAsJsonArray();
                //Toast.makeText(DocDCRProduct.this, myCustomArray.toString(), Toast.LENGTH_LONG).show();
            }
        });*/

        /*submitbtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //todo add confirmation box (yes/no type)
                    Gson gson = new GsonBuilder().create();
                    JsonArray myCustomArray = gson.toJsonTree(dcrprodlst).getAsJsonArray();
                    Toast.makeText(DocDCRProduct.this, myCustomArray.toString(), Toast.LENGTH_LONG).show();

                    *//*Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {

                            Gson gson = new GsonBuilder().create();
                            JsonArray myCustomArray = gson.toJsonTree(dcrprodlst).getAsJsonArray();
                            Toast.makeText(DocDCRProduct.this, myCustomArray.toString(), Toast.LENGTH_LONG).show();
                        }
                    }, 900);*//*
                }
            }
        });*/

        apicall1();

    }

    public void submitentry() {
        productnameslist.clearFocus();
        AlertDialog.Builder builder = new AlertDialog.Builder(DocDCRProduct.this);
        builder.setCancelable(true);
        builder.setTitle("SUBMIT ?");
        builder.setMessage("Are you sure you want to submit ?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        boolean allok = true;
                        allok = checkIfAllOkOrNot();
                        if (allok) {
                            Gson gson = new GsonBuilder().create();
                            JsonArray myCustomArray = gson.toJsonTree(dcrprodlst).getAsJsonArray();
                            String text = spn.getSelectedItem().toString();
                            String compcall = "N";
                            if (text.equalsIgnoreCase("YES")) {
                                compcall = "Y";
                                iscompcall = "Y";
                            } else {
                                iscompcall = "N";
                            }


                            new DocDCRProduct.addProductEntry().execute(Global.ecode, Global.netid, serialwp, Global.dcrno, finyr, d1d2, field,
                                    myCustomArray.toString(), qgen, Global.dbprefix, cntcd, Global.dcrdate, compcall, spflag, pflag);
                        }else{
                            Toast.makeText(DocDCRProduct.this, "Records not saved !\nPlease go back and re submit the entry." ,Toast.LENGTH_LONG).show();
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

    private boolean checkIfAllOkOrNot() {
        for (int i = 0; i < dcrprodlst.size(); i++) {
            DcrproductlistItem temp = dcrprodlst.get(i);
            if (temp.getDETFLAG().equalsIgnoreCase("Y")) {
                if (temp.getRxQTY().equalsIgnoreCase("")) {
                    makeAlert("Rx QTY", temp.getPNAME());
                    return false;
                }
                if (temp.getQTY().equalsIgnoreCase("")) {
                    makeAlert("QTY", temp.getPNAME());
                    return false;
                }

            }
        }
        return true;
    }

    private void makeAlert(String ofthe, String pname) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DocDCRProduct.this);
        builder.setCancelable(true);
        builder.setTitle("Alert !");
        builder.setMessage("PLEASE ENTER " + ofthe + " OF " + pname);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void apicall1() {
        String[] wrkdate = Global.date.split("-");
        String lyr = wrkdate[0];
        String lmth = wrkdate[1];
        progressDialoge.show();

        retrofit2.Call<DCRProdListRes> call1 = RetrofitClient
                .getInstance().getApi().DCRProdApi(serial, Global.netid, Global.dcrno, d1d2, Global.ecode, finyr, Global.dcrdate, Global.dcrdatemonth, Global.dcrdateyear, cntcd, lmth, lyr, Global.dbprefix);
        call1.enqueue(new Callback<DCRProdListRes>() {
            @Override
            public void onResponse(retrofit2.Call<DCRProdListRes> call1, Response<DCRProdListRes> response) {
                DCRProdListRes res = response.body();
                //progressDialoge.dismiss();
                if (!res.getDropprodid().equalsIgnoreCase("") && !res.getLabel().equalsIgnoreCase("") && !res.getDropspldrflag().equalsIgnoreCase("")) {
                    showDropdownAlert = true;
                    qgen = res.getDropgenQ();
                    spflag = res.getDropspldrflag();
                    pflag = res.getDropprodid();
                    label = res.getLabel();
                }
                dcrprodlst = res.getDcrproductlist();
                productnameslist.setVisibility(View.VISIBLE);
                productnameslist.getAdapter().notifyDataSetChanged();
                checkPopupQuestion();
            }

            @Override
            public void onFailure(Call<DCRProdListRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(nsv, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                apicall1();
                            }
                        });
                snackbar.show();
            }
        });
    }

    private void checkPopupQuestion() {
        //progressDialoge.show();
        //Log.d("drclass//d1d2//cntcd",drclass+"//"+d1d2+"//"+cntcd);
        questionslist.clear();
        retrofit2.Call<GetPopupQuesRes> call1 = RetrofitClient
                .getInstance().getApi().yesNoQuestionPopup(Global.ecode, Global.netid, drclass, d1d2, Global.dcrdatemonth, Global.dcrdateyear, cntcd, Global.dbprefix);
        call1.enqueue(new Callback<GetPopupQuesRes>() {
            @Override
            public void onResponse(retrofit2.Call<GetPopupQuesRes> call1, Response<GetPopupQuesRes> response) {
                progressDialoge.dismiss();
                GetPopupQuesRes res = response.body();
                if (!res.isError()) {
                    questionslist = res.getQuestionslist();
                    showQuesPopup();
                    //Toast.makeText(DocDCRProduct.this, "questions present", Toast.LENGTH_LONG).show();
                }/*else{
                    Toast.makeText(DocDCRProduct.this, "No question to ask", Toast.LENGTH_LONG).show();
                }*/
            }

            @Override
            public void onFailure(Call<GetPopupQuesRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(nsv, "Failed to check questionnaire !", Snackbar.LENGTH_LONG)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                checkPopupQuestion();
                            }
                        });
                snackbar.show();
            }
        });
    }

    private void setProductAdapter() {
        productnameslist.setNestedScrollingEnabled(false);
        productnameslist.setLayoutManager(new LinearLayoutManager(DocDCRProduct.this));
        productnameslist.setAdapter(new RecyclerView.Adapter() {
                                        @NonNull
                                        @Override
                                        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                            View view = LayoutInflater.from(DocDCRProduct.this).inflate(R.layout.doc_product_adapter, viewGroup, false);
                                            Holder holder = new Holder(view);
                                            return holder;
                                        }

                                        @Override
                                        public long getItemId(int position) {
                                            return position;
                                        }

                                        @Override
                                        public int getItemViewType(int position) {
                                            return position;
                                        }

                                        @Override
                                        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                                            final Holder myHolder = (Holder) viewHolder;
                                            final DcrproductlistItem model = dcrprodlst.get(i);
                                            myHolder.prodname.setText(model.getPNAME());

                                            if (model.getQTY().equalsIgnoreCase("") || (model.getQTY().equalsIgnoreCase("0") && !model.getDETFLAG().equalsIgnoreCase("Y"))) {
                                                model.setQTY("");
                                                myHolder.qty.setText("");
                                            } else {
                                                myHolder.qty.setText(model.getQTY());
                                            }

                                            if (model.getDETFLAG().equalsIgnoreCase("Y")) {
                                                myHolder.detailing.setChecked(true);
                                            } else {
                                                myHolder.detailing.setChecked(false);
                                            }

                                            if (!model.getRxQTY().equalsIgnoreCase("")) {
                                                myHolder.rx.setText(model.getRxQTY());
                                            } else {
                                                myHolder.rx.setText("");
                                            }
                                            myHolder.bal.setText("Bal : " + model.getBAL());
                                            myHolder.qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                @Override
                                                public void onFocusChange(View view, boolean hasFocus) {
                                                    if (!hasFocus) {

                                                        if (myHolder.qty.getText().toString().equalsIgnoreCase("")) {
                                                            model.setQTY("");
                                                            model.setDETFLAG("");
                                                            myHolder.detailing.setChecked(false);
                                                        } else {
                                                            if (Integer.parseInt(myHolder.qty.getText().toString()) >= 0) {
                                                                model.setQTY(Integer.toString(Integer.parseInt(myHolder.qty.getText().toString())));
                                                                model.setDETFLAG("Y");
                                                                myHolder.detailing.setChecked(true);
                                                            }
                                                        }
                                                        //Toast.makeText(DocDCRGift.this, "Focus Lose", Toast.LENGTH_SHORT).show();
                                                        InputMethodManager imm = (InputMethodManager) getSystemService(DocDCRProduct.this.INPUT_METHOD_SERVICE);
                                                        imm.hideSoftInputFromWindow(nsv.getWindowToken(), 0);
                                                    }

                                                }
                                            });
                                            myHolder.rx.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                @Override
                                                public void onFocusChange(View view, boolean hasFocus) {
                                                    if (!hasFocus) {

                                                        if (myHolder.detailing.isChecked() && myHolder.rx.getText().toString().equalsIgnoreCase("")) {
                                                            model.setRxQTY("");
                                                            model.setQTY("");
                                                            model.setDETFLAG("");
                                                            myHolder.detailing.setChecked(false);
                                                        } else {
                                                            if (!myHolder.rx.getText().toString().equalsIgnoreCase("") && Integer.parseInt(myHolder.rx.getText().toString()) >= 0) {
                                                                model.setRxQTY(Integer.toString(Integer.parseInt(myHolder.rx.getText().toString())));
                                                            } else if (myHolder.rx.getText().toString().equalsIgnoreCase("")) {
                                                                model.setRxQTY("");
                                                            }
                                                        }
                                                        //Toast.makeText(DocDCRGift.this, "Focus Lose", Toast.LENGTH_SHORT).show();
                                                        InputMethodManager imm = (InputMethodManager) getSystemService(DocDCRProduct.this.INPUT_METHOD_SERVICE);
                                                        imm.hideSoftInputFromWindow(nsv.getWindowToken(), 0);
                                                    }

                                                }
                                            });

                                            myHolder.detailing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if (isChecked) {
                                                        model.setDETFLAG("Y");
                                                        /*if (myHolder.qty.getText().toString().equalsIgnoreCase("")) {
                                                            model.setQTY("0");
                                                            myHolder.qty.setText(model.getQTY());
                                                        }

                                                        if (myHolder.rx.getText().toString().equalsIgnoreCase("")) {
                                                            model.setRxQTY("0");
                                                            myHolder.rx.setText(model.getRxQTY());
                                                        }*/

                                                        if (model.isTaggedflag() && model.getHsbrandid().contains(model.getGRP()) && model.getDEMO().equalsIgnoreCase("Y") && (model.isSpldrflag() || model.getLastmodifydate().equalsIgnoreCase(Global.dcrdate)) && model.isDateflag()) {
                                                            popupSelection(model.getPRODID(), cntcd, model.isFlag1176(), model.isFlag1177(), model.isFlag3009(), model.isTaggedflag(), model.isFlag1187(), "popupTag", model.getPNAME());
                                                        } else {
                                                            popupSelection(model.getPRODID(), cntcd, model.isFlag1176(), model.isFlag1177(), model.isFlag3009(), model.isTaggedflag(), model.isFlag1187(), "popup", model.getPNAME());
                                                        }
                                                    } else {
                                                        model.setDETFLAG("");
                                                        model.setQTY("");
                                                        myHolder.qty.setText(model.getQTY());
                                                    }
                                                }
                                            });

                                        }

                                        @Override
                                        public int getItemCount() {
                                            return dcrprodlst.size();
                                        }

                                        class Holder extends RecyclerView.ViewHolder {
                                            TextView prodname, bal;
                                            EditText qty, rx;
                                            AppCompatCheckBox detailing;

                                            public Holder(@NonNull View itemView) {
                                                super(itemView);
                                                prodname = itemView.findViewById(R.id.productname);
                                                bal = itemView.findViewById(R.id.bal);
                                                qty = itemView.findViewById(R.id.qty);
                                                rx = itemView.findViewById(R.id.rxqty);
                                                detailing = itemView.findViewById(R.id.detailing);
                                            }
                                        }
                                    }
        );
    }

    private void popupSelection(String prodid, String cntcd, boolean flag1176, boolean flag1177, boolean flag3009, boolean taggedflag, boolean flag1187, String popupType, String pname) {
        if (popupType.equalsIgnoreCase("popupTag")) {
            if (Global.dbprefix.equalsIgnoreCase("Aqua-Basale") && prodid.equalsIgnoreCase("1098")) {
                Toast.makeText(DocDCRProduct.this, "Please wait....", Toast.LENGTH_SHORT).show();
                getPopup2data(prodid, pname);
            }
        } else {
            if (prodid.equalsIgnoreCase("1176") && flag1176) {
                Toast.makeText(DocDCRProduct.this, "Please wait....", Toast.LENGTH_SHORT).show();
                getPopup1data(prodid, pname);
            } else if (prodid.equalsIgnoreCase("1177") && flag1177) {
                Toast.makeText(DocDCRProduct.this, "Please wait....", Toast.LENGTH_SHORT).show();
                getPopup1data(prodid, pname);
            } else if (prodid.equalsIgnoreCase("1187") && flag1187 && Global.dbprefix.equalsIgnoreCase("Aqua-Basale")) {
                Toast.makeText(DocDCRProduct.this, "Please wait....", Toast.LENGTH_SHORT).show();
                getPopup1data(prodid, pname);
            } else if (prodid.equalsIgnoreCase("1180") && flag3009 && taggedflag) {
                Toast.makeText(DocDCRProduct.this, "Please wait....", Toast.LENGTH_SHORT).show();
                getPopup3data(prodid, pname);
            }
        }
    }

    public class addProductEntry extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialoge.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL(RetrofitClient.BASE_URL + "addDcrProductEntry.php");

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("ecode", params[0])
                        .appendQueryParameter("netid", params[1])
                        .appendQueryParameter("serial", params[2])
                        .appendQueryParameter("dcrno", params[3])
                        .appendQueryParameter("financialyear", params[4])
                        .appendQueryParameter("d1d2", params[5])
                        .appendQueryParameter("field", params[6])
                        .appendQueryParameter("jsonarray", params[7])
                        .appendQueryParameter("qgen", params[8])
                        .appendQueryParameter("DBPrefix", params[9])
                        .appendQueryParameter("cntcd", params[10])
                        .appendQueryParameter("dcrdate", params[11])
                        .appendQueryParameter("COMPLETECALL", params[12])
                        .appendQueryParameter("spflag", params[13])
                        .appendQueryParameter("pflag", params[14]);

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {

                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            progressDialoge.dismiss();
            try {
                JSONObject jobj = new JSONObject(result);

                if (!jobj.getBoolean("error")) {
                    DcrddrlstItem modelx = DoctorsData.dcrdlst.get(position);
                    modelx.setCompletecall(iscompcall);
                    //onBackPressed();
                    menuOperation(param);
                    DoctorsData.doctorslist.getAdapter().notifyDataSetChanged();
                    Toast.makeText(DocDCRProduct.this, jobj.getString("errormsg"), Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                //todo handle exception
                e.printStackTrace();
            }
        }
    }

    public void showQuesPopup() {
        final Dialog dialog = new Dialog(DocDCRProduct.this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.jointwrklstpopup);
        TextView heading = dialog.findViewById(R.id.heading);
        heading.setText("Below questions are mandatory");
        CardView cancelbtn = dialog.findViewById(R.id.no);
        CardView submitbtn = dialog.findViewById(R.id.yes);
        RecyclerView rv_list_popup = dialog.findViewById(R.id.jointwrkpopuplist);
        rv_list_popup.setNestedScrollingEnabled(false);
        rv_list_popup.setLayoutManager(new LinearLayoutManager(DocDCRProduct.this));
        rv_list_popup.setAdapter(new RecyclerView.Adapter() {
                                     @NonNull
                                     @Override
                                     public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                         View view = LayoutInflater.from(DocDCRProduct.this).inflate(R.layout.yes_no_questions_popup_adapter, viewGroup, false);
                                         Holder holder = new Holder(view);
                                         return holder;
                                     }

                                     @Override
                                     public long getItemId(int position) {
                                         return position;
                                     }

                                     @Override
                                     public int getItemViewType(int position) {
                                         return position;
                                     }

                                     @Override
                                     public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                                         final Holder myHolder = (Holder) viewHolder;
                                         final QuestionslistItem model = questionslist.get(i);
                                         myHolder.donewith.setVisibility(View.GONE);
                                         myHolder.question.setText(model.getQdescrpn());
                                         String andesc = "Select option~" + model.getAnsdesc();
                                         String[] answ = andesc.split("~");
                                         ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(DocDCRProduct.this,
                                                 android.R.layout.simple_spinner_item, answ);
                                         adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                         myHolder.ans.setAdapter(adapter1);
                                         if (!model.getAns().equalsIgnoreCase("")) {
                                             myHolder.ans.setSelection(adapter1.getPosition(model.getAns()));
                                         }


                                         if (model.getSubansdesc().length() > 0) {
                                             String subandesc = "Select option~" + model.getSubansdesc();
                                             String[] subansw = subandesc.split("~");
                                             ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(DocDCRProduct.this,
                                                     android.R.layout.simple_spinner_item, subansw);
                                             adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                             myHolder.subans.setAdapter(adapter2);
                                         }

                                         myHolder.ans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                             @Override
                                             public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                                 String text = myHolder.ans.getSelectedItem().toString();
                                                 if (text.equalsIgnoreCase("Select option")) {
                                                     model.setAns("");
                                                     if (model.getSubansdesc().length() > 0) {
                                                         myHolder.subans.setSelection(0);
                                                         model.setSubans("");
                                                         myHolder.donewith.setVisibility(View.GONE);
                                                     }
                                                 } else {
                                                     if (text.equalsIgnoreCase("YES")) {
                                                         model.setAns(text);
                                                         if (model.getSubansdesc().length() > 0) {
                                                             myHolder.donewith.setVisibility(View.VISIBLE);
                                                         }
                                                     } else {
                                                         model.setAns(text);
                                                         if (model.getSubansdesc().length() > 0) {
                                                             myHolder.subans.setSelection(0);
                                                             model.setSubans("");
                                                             myHolder.donewith.setVisibility(View.GONE);
                                                         }
                                                     }
                                                 }
                                             }

                                             @Override
                                             public void onNothingSelected(AdapterView<?> parentView) {
                                                 // your code here
                                             }

                                         });

                                         myHolder.subans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                             @Override
                                             public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                                 String text = myHolder.subans.getSelectedItem().toString();
                                                 if (text.equalsIgnoreCase("Select option")) {
                                                     model.setSubans("");
                                                 } else {
                                                     model.setSubans(text);
                                                 }
                                             }

                                             @Override
                                             public void onNothingSelected(AdapterView<?> parentView) {
                                                 // your code here
                                             }

                                         });

                                     }

                                     @Override
                                     public int getItemCount() {
                                         return questionslist.size();
                                     }

                                     class Holder extends RecyclerView.ViewHolder {
                                         TextView question;
                                         Spinner ans, subans;
                                         LinearLayout donewith;

                                         public Holder(@NonNull View itemView) {
                                             super(itemView);
                                             question = itemView.findViewById(R.id.question);
                                             ans = itemView.findViewById(R.id.ans);
                                             subans = itemView.findViewById(R.id.subans);
                                             donewith = itemView.findViewById(R.id.donewith);
                                         }
                                     }
                                 }
        );

        rv_list_popup.getAdapter().notifyDataSetChanged();
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                Toast.makeText(DocDCRProduct.this, "Answer all questions and submit !", Toast.LENGTH_SHORT).show();
            }
        });
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isansgiven = true;
                boolean issubansgiven = true;
                for (int m = 0; m < questionslist.size(); m++) {
                    if (questionslist.get(m).getAns().equalsIgnoreCase("")) {
                        isansgiven = false;
                    }
                    if (questionslist.get(m).getSubansdesc().length() > 0) {
                        if (questionslist.get(m).getAns().equalsIgnoreCase("YES") && questionslist.get(m).getSubans().equalsIgnoreCase("")) {
                            issubansgiven = false;
                        }
                    }
                }

                if (isansgiven) {
                    if (issubansgiven) {
                        dialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        JsonArray myCustomArray = gson.toJsonTree(questionslist).getAsJsonArray();
                        //Toast.makeText(DocDCRProduct.this, myCustomArray.toString(), Toast.LENGTH_SHORT).show();
                        storePopupQuesAnsInDB(myCustomArray.toString());
                    } else {
                        Toast.makeText(DocDCRProduct.this, "First answer all sub question !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DocDCRProduct.this, "First answer all question !", Toast.LENGTH_SHORT).show();
                    //todo save in db
                }
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void storePopupQuesAnsInDB(String json) {
        progressDialoge.show();
        retrofit2.Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().submitPopupQuesAns(Global.ecode, Global.netid, Global.dcrdate, json, Global.dcrdatemonth, Global.dcrdateyear, cntcd, Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                progressDialoge.dismiss();
                DefaultResponse res = response.body();
                if (!res.isError()) {
                    Toast.makeText(DocDCRProduct.this, res.getErrormsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(nsv, "Failed to submit questionnaire !", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void getPopup1data(final String prodid, final String pname) {
        pop1data.clear();
        progressDialoge.show();
        retrofit2.Call<EpidermPopUpRes> call1 = RetrofitClient
                .getInstance().getApi().get117611771187(Global.ecode, Global.netid, Global.dcrdate, cntcd, prodid, Global.dcrno, Global.dbprefix);
        call1.enqueue(new Callback<EpidermPopUpRes>() {
            @Override
            public void onResponse(retrofit2.Call<EpidermPopUpRes> call1, Response<EpidermPopUpRes> response) {
                progressDialoge.dismiss();
                EpidermPopUpRes res = response.body();
                pop1data = res.getDCRProdDemoDet();
                if (pop1data.size() > 0) {
                    if (prodid.equalsIgnoreCase("1187"))
                        showPopup1187(prodid, pname);
                    else
                        showPopupOth(prodid, pname);
                } else {
                    showNoDocDataAlert();
                }
            }

            @Override
            public void onFailure(Call<EpidermPopUpRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(nsv, "Failed to get requested data !", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void showNoDocDataAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("NO DOCTORS DATA !");
        builder.setPositiveButton("GO BACK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPopupOth(final String prodid, String pname) {
        final Dialog dialog = new Dialog(DocDCRProduct.this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.croma_popup);

        CardView buttonNo = dialog.findViewById(R.id.cancel);
        CardView buttonYes = dialog.findViewById(R.id.submit);
        TextView drname = dialog.findViewById(R.id.drname);
        TextView drtype = dialog.findViewById(R.id.drtype);
        TextView chemname = dialog.findViewById(R.id.chemname);
        final RadioGroup rbgrp1 = dialog.findViewById(R.id.rbgrp1);
        final RadioGroup rbgrp2 = dialog.findViewById(R.id.rbgrp2);
        final RadioGroup rbgrp3 = dialog.findViewById(R.id.rbgrp3);
        RadioButton no1 = dialog.findViewById(R.id.no1);
        RadioButton no2 = dialog.findViewById(R.id.no2);
        RadioButton no3 = dialog.findViewById(R.id.no3);
        final EditText edt1 = dialog.findViewById(R.id.odrqty);

        TextView prdname = dialog.findViewById(R.id.prdname);
        prdname.setText("PRODUCT NAME \n" + pname);
        drname.setText(pop1data.get(0).getDRNAME());
        drtype.setText(pop1data.get(0).getTradeDis());
        chemname.setText("AVAILABLE AT PULSE CHEMIST ?\n" + pop1data.get(0).getChemistname());

        String strpres = "";
        if (!pop1data.get(0).getPrescriptiondate().equalsIgnoreCase("0000-00-00"))
            strpres = "Y";

        if (strpres.equalsIgnoreCase("Y")) {
            rbgrp1.check(R.id.yes1);
            no1.setEnabled(false);
        }

        if (pop1data.get(0).getMadeAvailableAtChem().equalsIgnoreCase("Y")) {
            rbgrp2.check(R.id.yes2);
            no2.setEnabled(false);
        } else if (pop1data.get(0).getMadeAvailableAtChem().equalsIgnoreCase("N")) {
            rbgrp2.check(R.id.no2);
        }

        if (!pop1data.get(0).getDdorderqty().equalsIgnoreCase("")) {
            edt1.setText(pop1data.get(0).getDdorderqty());
            edt1.setEnabled(false);
        }

        if (pop1data.get(0).getTriopackgiven().equalsIgnoreCase("Y")) {
            rbgrp3.check(R.id.yes3);
            no3.setEnabled(false);
        } else if (pop1data.get(0).getTriopackgiven().equalsIgnoreCase("N")) {
            rbgrp3.check(R.id.no3);
        }

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startedpres = "", madeavail = "", triopack = "";
                if (rbgrp1.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp1.getCheckedRadioButtonId());
                    startedpres = radioButton.getText().toString();
                }
                //Log.d("rbgrp2-->",Integer.toString(rbgrp2.getCheckedRadioButtonId())+"  id-->"+R.id.yes2);
                if (rbgrp2.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp2.getCheckedRadioButtonId());
                    madeavail = radioButton.getText().toString();
                    //Log.d("madeavail-->",madeavail);
                }
                if (rbgrp3.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp3.getCheckedRadioButtonId());
                    triopack = radioButton.getText().toString();
                }

                save11761177(prodid, startedpres, madeavail, triopack, edt1.getText().toString());
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

    private void save11761177(String prodid, String startedpres, String madeavail, String triopack, String odrqty) {
        progressDialoge.show();
        retrofit2.Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().submit11761177(Global.ecode, Global.netid, Global.dcrdate, cntcd, prodid, Global.dcrno, startedpres, madeavail, odrqty, triopack, Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                progressDialoge.dismiss();
                DefaultResponse res = response.body();
                if (!res.isError()) {
                    Toast.makeText(DocDCRProduct.this, res.getErrormsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(nsv, "Failed to get requested data !", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void showPopup1187(final String prodid, String pname) {
        final Dialog dialog = new Dialog(DocDCRProduct.this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.eberderm_popup);

        CardView buttonNo = dialog.findViewById(R.id.cancel);
        CardView buttonYes = dialog.findViewById(R.id.submit);
        TextView drname = dialog.findViewById(R.id.drname);
        TextView drtype = dialog.findViewById(R.id.drtype);
        TextView chemname = dialog.findViewById(R.id.chemname);
        final RadioGroup rbgrp1 = dialog.findViewById(R.id.rbgrp1);
        final RadioGroup rbgrp2 = dialog.findViewById(R.id.rbgrp2);
        final RadioGroup rbgrp3 = dialog.findViewById(R.id.rbgrp3);
        final RadioGroup rbgrp4 = dialog.findViewById(R.id.rbgrp4);
        RadioButton no1 = dialog.findViewById(R.id.no1);
        RadioButton no2 = dialog.findViewById(R.id.no2);
        RadioButton no3 = dialog.findViewById(R.id.no3);
        RadioButton no4 = dialog.findViewById(R.id.no4);
        final EditText edt1 = dialog.findViewById(R.id.presval);
        final EditText edt2 = dialog.findViewById(R.id.unitavail);
        final EditText edt3 = dialog.findViewById(R.id.unitsold);

        TextView prdname = dialog.findViewById(R.id.prdname);
        prdname.setText("PRODUCT NAME \n" + pname);
        drname.setText(pop1data.get(0).getDRNAME());
        drtype.setText(pop1data.get(0).getTradeDis());
        chemname.setText("AVAILABLE AT PULSE CHEMIST ?\n" + pop1data.get(0).getChemistname());

        if (pop1data.get(0).getEpidermlaunched().equalsIgnoreCase("Y") && !pop1data.get(0).getLaunchdate().equalsIgnoreCase(Global.dcrdate)) {
            rbgrp1.check(R.id.yes1);
            no1.setEnabled(false);
        } else {
            if (pop1data.get(0).getEpidermlaunched().equalsIgnoreCase("Y")) {
                rbgrp1.check(R.id.yes1);
            } else if (pop1data.get(0).getEpidermlaunched().equalsIgnoreCase("N")) {
                rbgrp1.check(R.id.no1);
            }
        }

        if (pop1data.get(0).getEpidermsamplegiven().equalsIgnoreCase("Y") && !pop1data.get(0).getEpidermsamplegivedate().equalsIgnoreCase(Global.dcrdate)) {
            rbgrp2.check(R.id.yes2);
            no2.setEnabled(false);
        } else {
            if (pop1data.get(0).getEpidermsamplegiven().equalsIgnoreCase("Y")) {
                rbgrp2.check(R.id.yes2);
            } else if (pop1data.get(0).getEpidermsamplegiven().equalsIgnoreCase("N")) {
                rbgrp2.check(R.id.no2);
            }
        }

        if (pop1data.get(0).getEpidermprscReceived().equalsIgnoreCase("Y") && !pop1data.get(0).getEpidermprscReceiveddate().equalsIgnoreCase(Global.dcrdate)) {
            rbgrp3.check(R.id.yes3);
            no3.setEnabled(false);
        } else {
            if (pop1data.get(0).getEpidermprscReceived().equalsIgnoreCase("Y")) {
                rbgrp3.check(R.id.yes3);
            } else if (pop1data.get(0).getEpidermprscReceived().equalsIgnoreCase("N")) {
                rbgrp3.check(R.id.no3);
            }
        }

        if (!pop1data.get(0).getEpidermpresno().equalsIgnoreCase("0") && !pop1data.get(0).getEpidermpresnodate().equalsIgnoreCase(Global.dcrdate)) {
            edt1.setText(pop1data.get(0).getEpidermpresno());
            edt1.setEnabled(false);
        } else {
            edt1.setText(pop1data.get(0).getEpidermpresno());
        }

        if (pop1data.get(0).getMadeAvailableAtChem().equalsIgnoreCase("Y") && !pop1data.get(0).getAvailabilityDate().equalsIgnoreCase(Global.dcrdate)) {
            rbgrp4.check(R.id.yes4);
            no4.setEnabled(false);
        } else {
            if (pop1data.get(0).getMadeAvailableAtChem().equalsIgnoreCase("Y")) {
                rbgrp4.check(R.id.yes4);
            } else if (pop1data.get(0).getMadeAvailableAtChem().equalsIgnoreCase("N")) {
                rbgrp4.check(R.id.no4);
            }
        }

        if (!pop1data.get(0).getEpidermnoofunitsavail().equalsIgnoreCase("0") && !pop1data.get(0).getEpidermnoofunitsavaildate().equalsIgnoreCase(Global.dcrdate)) {
            edt2.setText(pop1data.get(0).getEpidermnoofunitsavail());
            edt2.setEnabled(false);
        } else {
            edt2.setText(pop1data.get(0).getEpidermnoofunitsavail());
        }

        if (!pop1data.get(0).getEpidermnoofunitsoldafterlaunch().equalsIgnoreCase("0") && !pop1data.get(0).getEpidermnoofunitsoldafterlaunchdate().equalsIgnoreCase(Global.dcrdate)) {
            edt3.setText(pop1data.get(0).getEpidermnoofunitsoldafterlaunch());
            edt3.setEnabled(false);
        } else {
            edt3.setText(pop1data.get(0).getEpidermnoofunitsoldafterlaunch());
        }

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo save data
                String launched = "", madeavail = "", samplegiven = "", prescreceived = "";
                if (rbgrp1.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp1.getCheckedRadioButtonId());
                    launched = radioButton.getText().toString();
                }
                if (rbgrp2.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp2.getCheckedRadioButtonId());
                    samplegiven = radioButton.getText().toString();
                }
                if (rbgrp3.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp3.getCheckedRadioButtonId());
                    prescreceived = radioButton.getText().toString();
                }
                if (rbgrp4.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp4.getCheckedRadioButtonId());
                    madeavail = radioButton.getText().toString();
                }

                save1187(prodid, launched, samplegiven, prescreceived, madeavail, edt1.getText().toString(), edt2.getText().toString(), edt3.getText().toString());
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

    private void save1187(String prodid, String launched, String samplegiven, String prescreceived, String madeavail, String presno, String unitavail, String unitsold) {
        progressDialoge.show();
        retrofit2.Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().submit1187(Global.ecode, Global.netid, Global.dcrdate, cntcd, prodid, Global.dcrno, launched, samplegiven, prescreceived, presno, madeavail, unitavail, unitsold, Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                progressDialoge.dismiss();
                DefaultResponse res = response.body();
                if (!res.isError()) {
                    Toast.makeText(DocDCRProduct.this, res.getErrormsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(nsv, "Failed to get requested data !", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void getPopup2data(final String prodid, final String pname) {
        pop2data.clear();
        progressDialoge.show();
        retrofit2.Call<QseraPopUpRes> call1 = RetrofitClient
                .getInstance().getApi().get1098(Global.ecode, Global.netid, Global.dcrdate, cntcd, prodid, Global.dcrno, Global.dbprefix);
        call1.enqueue(new Callback<QseraPopUpRes>() {
            @Override
            public void onResponse(retrofit2.Call<QseraPopUpRes> call1, Response<QseraPopUpRes> response) {
                progressDialoge.dismiss();
                QseraPopUpRes res = response.body();
                pop2data = res.getDCRGiftSplDrDet();
                if (pop2data.size() > 0) {
                    showPopupQSera(prodid, pname);
                } else {
                    showNoDocDataAlert();
                }
            }

            @Override
            public void onFailure(Call<QseraPopUpRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(nsv, "Failed to get requested data !", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void showPopupQSera(final String prodid, String pname) {
        final Dialog dialog = new Dialog(DocDCRProduct.this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.qserahair_popup);

        CardView buttonNo = dialog.findViewById(R.id.cancel);
        CardView buttonYes = dialog.findViewById(R.id.submit);
        TextView drname = dialog.findViewById(R.id.drname);
        TextView drtype = dialog.findViewById(R.id.drtype);
        TextView chemname = dialog.findViewById(R.id.chemname);
        final RadioGroup rbgrp1 = dialog.findViewById(R.id.rbgrp1);
        RadioButton no1 = dialog.findViewById(R.id.no1);
        final EditText edt1 = dialog.findViewById(R.id.rxgen);
        final EditText edt2 = dialog.findViewById(R.id.unitsold);
        final EditText edt3 = dialog.findViewById(R.id.drfeedbk);
        LinearLayout ll1 = dialog.findViewById(R.id.ll1);
        LinearLayout ll2 = dialog.findViewById(R.id.ll2);
        LinearLayout ll3 = dialog.findViewById(R.id.ll3);
        View v1 = dialog.findViewById(R.id.v1);
        View v2 = dialog.findViewById(R.id.v2);
        View v3 = dialog.findViewById(R.id.v3);

        TextView prdname = dialog.findViewById(R.id.prdname);
        prdname.setText("PRODUCT NAME \n" + pname);
        drname.setText(pop2data.get(0).getDRNAME());
        drtype.setText(pop2data.get(0).getTradeDis());
        chemname.setText("AVAILABLE AT PULSE CHEMIST ?\n" + pop2data.get(0).getChemistname());

        if (pop2data.get(0).getMadeAvailableAtPulseChem().equalsIgnoreCase("Y") && !pop2data.get(0).getAvailabilityDate().equalsIgnoreCase(Global.dcrdate)) {
            rbgrp1.check(R.id.yes1);
            no1.setEnabled(false);
        } else {
            if (pop2data.get(0).getMadeAvailableAtPulseChem().equalsIgnoreCase("Y")) {
                rbgrp1.check(R.id.yes1);
            } else if (pop2data.get(0).getMadeAvailableAtPulseChem().equalsIgnoreCase("N")) {
                rbgrp1.check(R.id.no1);
            }
        }

        if (prodid.equalsIgnoreCase("2024") || prodid.equalsIgnoreCase("1098")) {
            edt1.setText(pop2data.get(0).getNoQSeraHairSerumRx());
            edt2.setText(pop2data.get(0).getNoofunitsold());
            edt3.setText(pop2data.get(0).getDoctorsfeedback());
        } else {
            v1.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            ll1.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            ll3.setVisibility(View.GONE);
        }

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String madeavail = "";
                if (rbgrp1.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp1.getCheckedRadioButtonId());
                    madeavail = radioButton.getText().toString();
                }

                save1098(prodid, madeavail, edt1.getText().toString(), edt2.getText().toString(), edt3.getText().toString());
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

    private void save1098(String prodid, String madeavail, String rxgen, String unitsold, String drfeedbk) {
        progressDialoge.show();
        retrofit2.Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().submit1098(Global.ecode, Global.netid, Global.dcrdate, cntcd, prodid, Global.dcrno, madeavail, rxgen, unitsold, drfeedbk, Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                progressDialoge.dismiss();
                DefaultResponse res = response.body();
                if (!res.isError()) {
                    Toast.makeText(DocDCRProduct.this, res.getErrormsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(nsv, "Failed to get requested data !", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void getPopup3data(final String prodid, final String pname) {
        pop3data.clear();
        progressDialoge.show();
        retrofit2.Call<RedicnePopUpRes> call1 = RetrofitClient
                .getInstance().getApi().get3009(Global.ecode, Global.netid, Global.dcrdate, cntcd, "3009", Global.dcrno, Global.dbprefix);
        call1.enqueue(new Callback<RedicnePopUpRes>() {
            @Override
            public void onResponse(retrofit2.Call<RedicnePopUpRes> call1, Response<RedicnePopUpRes> response) {
                progressDialoge.dismiss();
                RedicnePopUpRes res = response.body();
                pop3data = res.getDCRRidacneDet();
                if (pop3data.size() > 0) {
                    showPopupRedicne(prodid, pname);
                } else {
                    showNoDocDataAlert();
                }
            }

            @Override
            public void onFailure(Call<RedicnePopUpRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(nsv, "Failed to get requested data !", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void showPopupRedicne(final String prodid, String pname) {
        final Dialog dialog = new Dialog(DocDCRProduct.this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.redicne_popup);

        CardView buttonNo = dialog.findViewById(R.id.cancel);
        CardView buttonYes = dialog.findViewById(R.id.submit);
        TextView drname = dialog.findViewById(R.id.drname);
        TextView drtype = dialog.findViewById(R.id.drtype);
        final RadioGroup rbgrp1 = dialog.findViewById(R.id.rbgrp1);
        final RadioGroup rbgrp2 = dialog.findViewById(R.id.rbgrp2);
        final RadioGroup rbgrp3 = dialog.findViewById(R.id.rbgrp3);
        final RadioGroup rbgrp4 = dialog.findViewById(R.id.rbgrp4);
        final RadioGroup rbgrp5 = dialog.findViewById(R.id.rbgrp5);
        final RadioGroup rbgrp6 = dialog.findViewById(R.id.rbgrp6);
        final RadioGroup rbgrp7 = dialog.findViewById(R.id.rbgrp7);
        final RadioGroup rbgrp8 = dialog.findViewById(R.id.rbgrp8);
        RadioButton no1 = dialog.findViewById(R.id.no1);
        RadioButton no2 = dialog.findViewById(R.id.no2);
        RadioButton no3 = dialog.findViewById(R.id.no3);
        RadioButton no4 = dialog.findViewById(R.id.no4);
        RadioButton no5 = dialog.findViewById(R.id.no5);
        RadioButton no6 = dialog.findViewById(R.id.no6);
        RadioButton no7 = dialog.findViewById(R.id.no7);
        RadioButton no8 = dialog.findViewById(R.id.no8);
        final EditText edt1 = dialog.findViewById(R.id.rxgen1);
        final EditText edt2 = dialog.findViewById(R.id.rxgen2);
        final EditText edt3 = dialog.findViewById(R.id.rxgen3);
        final EditText edt4 = dialog.findViewById(R.id.rxgen4);
        final EditText edt5 = dialog.findViewById(R.id.rxgen5);
        final EditText edt6 = dialog.findViewById(R.id.rxgen6);
        final EditText edt7 = dialog.findViewById(R.id.rxgen7);
        final EditText edt8 = dialog.findViewById(R.id.rxgen8);

        TextView prdname = dialog.findViewById(R.id.prdname);
        prdname.setText("PRODUCT NAME \n" + pname);
        drname.setText(pop3data.get(0).getDRNAME());
        drtype.setText(pop3data.get(0).getTradeDis());

        // 1
        if (pop3data.get(0).getKMACBriefednConsentRcvd().equalsIgnoreCase("Y")) {
            rbgrp1.check(R.id.done1);
            no1.setEnabled(false);
        } else {
            if (pop3data.get(0).getKMACBriefednConsentRcvd().equalsIgnoreCase("Y")) {
                rbgrp1.check(R.id.done1);
            } else if (pop3data.get(0).getKMACBriefednConsentRcvd().equalsIgnoreCase("N")) {
                rbgrp1.check(R.id.no1);
            }
        }

        if (!pop3data.get(0).getNoofRidacneRxWeek1520jul().equalsIgnoreCase("0")) {
            edt1.setText(pop3data.get(0).getNoofRidacneRxWeek1520jul());
            edt1.setEnabled(false);
        } else {
            edt1.setText(pop3data.get(0).getNoofRidacneRxWeek1520jul());
        }
        //1 ends here
        //2
        if (pop3data.get(0).getKMACUploadMaterailRcvdFromDr().equalsIgnoreCase("Y")) {
            rbgrp2.check(R.id.done2);
            no2.setEnabled(false);
        } else {
            if (pop3data.get(0).getKMACUploadMaterailRcvdFromDr().equalsIgnoreCase("Y")) {
                rbgrp2.check(R.id.done2);
            } else if (pop3data.get(0).getKMACUploadMaterailRcvdFromDr().equalsIgnoreCase("N")) {
                rbgrp2.check(R.id.no2);
            }
        }

        if (!pop3data.get(0).getNoofRidacneRxWeek2431jul().equalsIgnoreCase("0")) {
            edt2.setText(pop3data.get(0).getNoofRidacneRxWeek2431jul());
            edt2.setEnabled(false);
        } else {
            edt2.setText(pop3data.get(0).getNoofRidacneRxWeek2431jul());
        }
        //2 ends here
        //3
        if (pop3data.get(0).getDrAgreedWiththeKMACUploadedMaterial().equalsIgnoreCase("Y")) {
            rbgrp3.check(R.id.done3);
            no3.setEnabled(false);
        } else {
            if (pop3data.get(0).getDrAgreedWiththeKMACUploadedMaterial().equalsIgnoreCase("Y")) {
                rbgrp3.check(R.id.done3);
            } else if (pop3data.get(0).getDrAgreedWiththeKMACUploadedMaterial().equalsIgnoreCase("N")) {
                rbgrp3.check(R.id.no3);
            }
        }

        if (!pop3data.get(0).getNoofRidacneRxWeek0107aug().equalsIgnoreCase("0")) {
            edt3.setText(pop3data.get(0).getNoofRidacneRxWeek0107aug());
            edt3.setEnabled(false);
        } else {
            edt3.setText(pop3data.get(0).getNoofRidacneRxWeek0107aug());
        }
        //3 ends here
        //4
        if (pop3data.get(0).getHandedOverKMACInstrumentToTheDr().equalsIgnoreCase("Y")) {
            rbgrp4.check(R.id.done4);
            no4.setEnabled(false);
        } else {
            if (pop3data.get(0).getHandedOverKMACInstrumentToTheDr().equalsIgnoreCase("Y")) {
                rbgrp4.check(R.id.done4);
            } else if (pop3data.get(0).getHandedOverKMACInstrumentToTheDr().equalsIgnoreCase("N")) {
                rbgrp4.check(R.id.no4);
            }
        }

        if (!pop3data.get(0).getNoofRidacneRxWeek0814aug().equalsIgnoreCase("0")) {
            edt4.setText(pop3data.get(0).getNoofRidacneRxWeek0814aug());
            edt4.setEnabled(false);
        } else {
            edt4.setText(pop3data.get(0).getNoofRidacneRxWeek0814aug());
        }
        //4 ends here
        //5
        if (pop3data.get(0).getKMACRelatedallMaterialPlacedDspatientWaitingarena().equalsIgnoreCase("Y")) {
            rbgrp5.check(R.id.done5);
            no5.setEnabled(false);
        } else {
            if (pop3data.get(0).getKMACRelatedallMaterialPlacedDspatientWaitingarena().equalsIgnoreCase("Y")) {
                rbgrp5.check(R.id.done5);
            } else if (pop3data.get(0).getKMACRelatedallMaterialPlacedDspatientWaitingarena().equalsIgnoreCase("N")) {
                rbgrp5.check(R.id.no5);
            }
        }

        if (!pop3data.get(0).getNoofRidacneRxWeek1622aug().equalsIgnoreCase("0")) {
            edt5.setText(pop3data.get(0).getNoofRidacneRxWeek1622aug());
            edt5.setEnabled(false);
        } else {
            edt5.setText(pop3data.get(0).getNoofRidacneRxWeek1622aug());
        }
        //5 ends here
        //6
        if (pop3data.get(0).getKMACRunningWellCheckednFdbkUpdatedDr().equalsIgnoreCase("Y")) {
            rbgrp6.check(R.id.done6);
            no6.setEnabled(false);
        } else {
            if (pop3data.get(0).getKMACRunningWellCheckednFdbkUpdatedDr().equalsIgnoreCase("Y")) {
                rbgrp6.check(R.id.done6);
            } else if (pop3data.get(0).getKMACRunningWellCheckednFdbkUpdatedDr().equalsIgnoreCase("N")) {
                rbgrp6.check(R.id.no6);
            }
        }

        if (!pop3data.get(0).getNoofRidacneRxWeek2431aug().equalsIgnoreCase("0")) {
            edt6.setText(pop3data.get(0).getNoofRidacneRxWeek2431aug());
            edt6.setEnabled(false);
        } else {
            edt6.setText(pop3data.get(0).getNoofRidacneRxWeek2431aug());
        }
        //6 ends here
        //7
        if (pop3data.get(0).getKMACFdbkTakenFromDr().equalsIgnoreCase("Y")) {
            rbgrp7.check(R.id.done7);
            no7.setEnabled(false);
        } else {
            if (pop3data.get(0).getKMACFdbkTakenFromDr().equalsIgnoreCase("Y")) {
                rbgrp7.check(R.id.done7);
            } else if (pop3data.get(0).getKMACFdbkTakenFromDr().equalsIgnoreCase("N")) {
                rbgrp7.check(R.id.no7);
            }
        }

        if (!pop3data.get(0).getNoofRidacneRxWeek0105sep().equalsIgnoreCase("0")) {
            edt7.setText(pop3data.get(0).getNoofRidacneRxWeek0105sep());
            edt7.setEnabled(false);
        } else {
            edt7.setText(pop3data.get(0).getNoofRidacneRxWeek0105sep());
        }
        //7 ends here
        //8
        if (pop3data.get(0).getSectimeKMACRunningWellcheckednFdbkUpdatedDr().equalsIgnoreCase("Y")) {
            rbgrp8.check(R.id.done8);
            no8.setEnabled(false);
        } else {
            if (pop3data.get(0).getSectimeKMACRunningWellcheckednFdbkUpdatedDr().equalsIgnoreCase("Y")) {
                rbgrp8.check(R.id.done8);
            } else if (pop3data.get(0).getSectimeKMACRunningWellcheckednFdbkUpdatedDr().equalsIgnoreCase("N")) {
                rbgrp8.check(R.id.no8);
            }
        }

        if (!pop3data.get(0).getNoofRidacneRxWeek1630sep().equalsIgnoreCase("0")) {
            edt8.setText(pop3data.get(0).getNoofRidacneRxWeek1630sep());
            edt8.setEnabled(false);
        } else {
            edt8.setText(pop3data.get(0).getNoofRidacneRxWeek1630sep());
        }
        //8 ends here

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo save data
                String KMACBriefednConsentRcvd = "", KMACUploadMaterailRcvdFromDr = "", DrAgreedWiththeKMACUploadedMaterial = "", HandedOverKMACInstrumentToTheDr = "";
                String KMACRelatedallMaterialPlacedDspatientWaitingarena = "", KMACRunningWellCheckednFdbkUpdatedDr = "", KMACFdbkTakenFromDr = "", sectimeKMACRunningWellcheckednFdbkUpdatedDr = "";
                if (rbgrp1.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp1.getCheckedRadioButtonId());
                    String txt = radioButton.getText().toString();
                    if (txt.equalsIgnoreCase("DONE"))
                        KMACBriefednConsentRcvd = "Y";
                    else if (txt.equalsIgnoreCase("Could Not Do"))
                        KMACBriefednConsentRcvd = "N";
                }
                if (rbgrp2.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp2.getCheckedRadioButtonId());
                    String txt = radioButton.getText().toString();
                    if (txt.equalsIgnoreCase("DONE"))
                        KMACUploadMaterailRcvdFromDr = "Y";
                    else if (txt.equalsIgnoreCase("Could Not Do"))
                        KMACUploadMaterailRcvdFromDr = "N";
                }
                if (rbgrp3.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp3.getCheckedRadioButtonId());
                    String txt = radioButton.getText().toString();
                    if (txt.equalsIgnoreCase("DONE"))
                        DrAgreedWiththeKMACUploadedMaterial = "Y";
                    else if (txt.equalsIgnoreCase("Could Not Do"))
                        DrAgreedWiththeKMACUploadedMaterial = "N";
                }
                if (rbgrp4.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp4.getCheckedRadioButtonId());
                    String txt = radioButton.getText().toString();
                    if (txt.equalsIgnoreCase("DONE"))
                        HandedOverKMACInstrumentToTheDr = "Y";
                    else if (txt.equalsIgnoreCase("Could Not Do"))
                        HandedOverKMACInstrumentToTheDr = "N";
                }
                if (rbgrp5.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp5.getCheckedRadioButtonId());
                    String txt = radioButton.getText().toString();
                    if (txt.equalsIgnoreCase("DONE"))
                        KMACRelatedallMaterialPlacedDspatientWaitingarena = "Y";
                    else if (txt.equalsIgnoreCase("Could Not Do"))
                        KMACRelatedallMaterialPlacedDspatientWaitingarena = "N";
                }
                if (rbgrp6.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp6.getCheckedRadioButtonId());
                    String txt = radioButton.getText().toString();
                    if (txt.equalsIgnoreCase("DONE"))
                        KMACRunningWellCheckednFdbkUpdatedDr = "Y";
                    else if (txt.equalsIgnoreCase("Could Not Do"))
                        KMACRunningWellCheckednFdbkUpdatedDr = "N";
                }
                if (rbgrp7.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp7.getCheckedRadioButtonId());
                    String txt = radioButton.getText().toString();
                    if (txt.equalsIgnoreCase("DONE"))
                        KMACFdbkTakenFromDr = "Y";
                    else if (txt.equalsIgnoreCase("Could Not Do"))
                        KMACFdbkTakenFromDr = "N";
                }
                if (rbgrp8.getCheckedRadioButtonId() != -1) {
                    AppCompatRadioButton radioButton = dialog.findViewById(rbgrp8.getCheckedRadioButtonId());
                    String txt = radioButton.getText().toString();
                    if (txt.equalsIgnoreCase("DONE"))
                        sectimeKMACRunningWellcheckednFdbkUpdatedDr = "Y";
                    else if (txt.equalsIgnoreCase("Could Not Do"))
                        sectimeKMACRunningWellcheckednFdbkUpdatedDr = "N";
                }

                save3009(prodid, KMACBriefednConsentRcvd, edt1.getText().toString(), KMACUploadMaterailRcvdFromDr, edt2.getText().toString(),
                        DrAgreedWiththeKMACUploadedMaterial, edt3.getText().toString(), HandedOverKMACInstrumentToTheDr, edt4.getText().toString(),
                        KMACRelatedallMaterialPlacedDspatientWaitingarena, edt5.getText().toString(), KMACRunningWellCheckednFdbkUpdatedDr, edt6.getText().toString(),
                        KMACFdbkTakenFromDr, edt7.getText().toString(), sectimeKMACRunningWellcheckednFdbkUpdatedDr, edt8.getText().toString());
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

    private void save3009(String prodid, String kmacBriefednConsentRcvd, String rx, String kmacUploadMaterailRcvdFromDr, String rx1,
                          String drAgreedWiththeKMACUploadedMaterial, String rx2, String handedOverKMACInstrumentToTheDr, String rx3,
                          String kmacRelatedallMaterialPlacedDspatientWaitingarena, String rx4, String kmacRunningWellCheckednFdbkUpdatedDr,
                          String rx5, String kmacFdbkTakenFromDr, String rx6, String sectimeKMACRunningWellcheckednFdbkUpdatedDr, String rx7) {
        progressDialoge.show();
        retrofit2.Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().submit3009(Global.ecode, Global.netid, Global.dcrdate, cntcd, "3009", Global.dcrno, kmacBriefednConsentRcvd, rx,
                        kmacUploadMaterailRcvdFromDr, rx1, drAgreedWiththeKMACUploadedMaterial, rx2, handedOverKMACInstrumentToTheDr, rx3,
                        kmacRelatedallMaterialPlacedDspatientWaitingarena, rx4, kmacRunningWellCheckednFdbkUpdatedDr,
                        rx5, kmacFdbkTakenFromDr, rx6, sectimeKMACRunningWellcheckednFdbkUpdatedDr, rx7, Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                progressDialoge.dismiss();
                DefaultResponse res = response.body();
                if (!res.isError()) {
                    Toast.makeText(DocDCRProduct.this, res.getErrormsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(nsv, "Failed to get requested data !", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dcr_product_entry_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;*/
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nextprod) {
            param = "NEXT";
            submitentry();
            return true;
        } else if (id == R.id.samegift) {
            param = "SAME";
            submitentry();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void menuOperation(String mode) {
        int nextposition = position + 1;
        if (mode.equalsIgnoreCase("NEXT")) {
            if (nextposition < DoctorsData.dcrdlst.size()) {
                DcrddrlstItem model = DoctorsData.dcrdlst.get(nextposition);
                Intent intent = new Intent(DocDCRProduct.this, DocDCRProduct.class);
                intent.putExtra("serial", "DR" + model.getSerial());
                intent.putExtra("oserial", model.getSerial());
                intent.putExtra("cntcd", model.getCntCD());
                intent.putExtra("wnetid", model.getWNetID());
                intent.putExtra("drname", "Doctor Name - " + model.getDrname());
                intent.putExtra("compcall", model.getCompletecall());
                intent.putExtra("position", Integer.toString(nextposition));
                intent.putExtra("drclass", model.getJsonMemberClass());
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(DocDCRProduct.this, R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(intent, bndlanimation);
                finish();
            } else {
                onBackPressed();
            }
        } else if (mode.equalsIgnoreCase("SAME")) {
            if (position < DoctorsData.dcrdlst.size()) {
                DcrddrlstItem model = DoctorsData.dcrdlst.get(position);
                Intent intent = new Intent(DocDCRProduct.this, DocDCRGift.class);
                intent.putExtra("serial", "DR" + model.getSerial());
                intent.putExtra("oserial", model.getSerial());
                intent.putExtra("cntcd", model.getCntCD());
                intent.putExtra("wnetid", model.getWNetID());
                intent.putExtra("drname", "Doctor Name - " + model.getDrname());
                intent.putExtra("compcall", model.getCompletecall());
                intent.putExtra("position", Integer.toString(position));
                intent.putExtra("drclass", model.getJsonMemberClass());
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(DocDCRProduct.this, R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                startActivity(intent, bndlanimation);
                finish();
            } else {
                onBackPressed();
            }
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        DocDCRProduct.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
