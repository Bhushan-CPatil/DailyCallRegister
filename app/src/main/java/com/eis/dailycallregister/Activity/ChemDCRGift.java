package com.eis.dailycallregister.Activity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.DCRGiftListRes;
import com.eis.dailycallregister.Pojo.DcrdchlstItem;
import com.eis.dailycallregister.Pojo.DcrddrlstItem;
import com.eis.dailycallregister.Pojo.DcrgiftslistItem;
import com.eis.dailycallregister.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChemDCRGift extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 60000;
    public static final int READ_TIMEOUT = 90000;
    ViewDialog progressDialoge;
    MaterialButton submitbtn, cancelbtn;
    ConstraintLayout nsv;
    TextView chname;
    public AppCompatEditText pob;
    //NestedScrollView nsv;
    int position;
    String param = "";
    RecyclerView giftnameslist;
    public String serial, d1d2, finyr, field;
    public List<DcrgiftslistItem> dcrplst = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chem_dcrgift);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>Gift/POB Entry</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        progressDialoge = new ViewDialog(ChemDCRGift.this);

        serial = getIntent().getStringExtra("serial");

        if (Global.hname.contains("(A)")) {
            d1d2 = "A";
        } else if (Global.hname.contains("(B)")) {
            d1d2 = "B";
        } else if (Global.hname.contains("(C)")) {
            d1d2 = "C";
        } else if (Global.hname.contains("(D)")) {
            d1d2 = "D";
        }
        finyr = Global.getFinancialYr(Global.dcrdatemonth, Global.dcrdateyear);
        field = Global.getFieldName(Integer.parseInt(Global.dcrdatemonth));
        position = Integer.parseInt(getIntent().getStringExtra("position"));
        //Log.d("finyr ",finyr);
        submitbtn = findViewById(R.id.submit);
        cancelbtn = findViewById(R.id.cancel);
        nsv = findViewById(R.id.nsv);
        giftnameslist = findViewById(R.id.giftlist);
        chname = findViewById(R.id.chname);
        pob = findViewById(R.id.pob);
        chname.setText(getIntent().getStringExtra("chname"));
        if (!getIntent().getStringExtra("pob").equalsIgnoreCase("") && Integer.parseInt(getIntent().getStringExtra("pob")) > 0) {
            pob.setText(getIntent().getStringExtra("pob"));
        }
        setGiftAdapter();

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                param = "SUBMIT";
                //onBackPressed();
                //giftnameslist.clearFocus();
                AlertDialog.Builder builder = new AlertDialog.Builder(ChemDCRGift.this);
                builder.setCancelable(true);
                builder.setTitle("SUBMIT ?");
                builder.setMessage("Are you sure you want to submit ?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                submitentry();
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
                //Toast.makeText(DocDCRGift.this, myCustomArray.toString(), Toast.LENGTH_LONG).show();
            }
        });

        apicall1();
    }

    private void apicall1() {
        String[] wrkdate = Global.date.split("-");
        String lyr = wrkdate[0];
        String lmth = wrkdate[1];
        progressDialoge.show();

        retrofit2.Call<DCRGiftListRes> call1 = RetrofitClient
                .getInstance().getApi().DCRGiftApi(serial, Global.netid, Global.dcrno, d1d2, Global.ecode, finyr, lmth, lyr, Global.dbprefix);
        call1.enqueue(new Callback<DCRGiftListRes>() {
            @Override
            public void onResponse(retrofit2.Call<DCRGiftListRes> call1, Response<DCRGiftListRes> response) {
                DCRGiftListRes res = response.body();
                progressDialoge.dismiss();
                dcrplst = res.getDcrgiftslist();
                giftnameslist.setVisibility(View.VISIBLE);
                giftnameslist.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<DCRGiftListRes> call1, Throwable t) {
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

    public void setGiftAdapter() {
        giftnameslist.setNestedScrollingEnabled(false);
        giftnameslist.setLayoutManager(new LinearLayoutManager(ChemDCRGift.this));
        giftnameslist.setAdapter(new RecyclerView.Adapter() {
                                     @NonNull
                                     @Override
                                     public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                         View view = LayoutInflater.from(ChemDCRGift.this).inflate(R.layout.doc_gift_adapter, viewGroup, false);
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
                                         final DcrgiftslistItem model = dcrplst.get(i);
                                         myHolder.giftname.setText(model.getPNAME());
                                         if (!model.getQTY().equalsIgnoreCase("")) {
                                             myHolder.qty.setText(model.getQTY());
                                         } else {
                                             myHolder.qty.setText("");
                                         }
                                         myHolder.bal.setText("Bal : " + model.getBAL());
                                         myHolder.qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                             @Override
                                             public void onFocusChange(View view, boolean hasFocus) {
                                                 if (!hasFocus) {
                                                     if (myHolder.qty.getText().toString().equalsIgnoreCase("")) {
                                                         model.setQTY("");
                                                     } else {
                                                         if (Integer.parseInt(myHolder.qty.getText().toString()) >= 0) {
                                                             model.setQTY(Integer.toString(Integer.parseInt(myHolder.qty.getText().toString())));
                                                         }
                                                     }
                                                     //Toast.makeText(DocDCRGift.this, "Focus Lose", Toast.LENGTH_SHORT).show();
                                                     InputMethodManager imm = (InputMethodManager) getSystemService(ChemDCRGift.this.INPUT_METHOD_SERVICE);
                                                     imm.hideSoftInputFromWindow(nsv.getWindowToken(), 0);
                                                 }

                                             }
                                         });
                                     }

                                     @Override
                                     public int getItemCount() {
                                         return dcrplst.size();
                                     }

                                     class Holder extends RecyclerView.ViewHolder {
                                         TextView giftname, bal;
                                         EditText qty;

                                         public Holder(@NonNull View itemView) {
                                             super(itemView);
                                             giftname = itemView.findViewById(R.id.giftname);
                                             bal = itemView.findViewById(R.id.bal);
                                             qty = itemView.findViewById(R.id.qty);
                                         }
                                     }
                                 }
        );
    }

    public class addGiftEntry extends AsyncTask<String, String, String> {
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
                url = new URL(RetrofitClient.BASE_URL + "addDcrGiftEntry.php");

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
                        .appendQueryParameter("pobamt", params[8])
                        .appendQueryParameter("DBPrefix", params[9]);

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
                    DcrdchlstItem modelx = ChemistData.dcrdlst.get(position);
                    modelx.setPOB(pob.getText().toString());
                    //onBackPressed();
                    menuOperation(param);
                    ChemistData.chemistlist.getAdapter().notifyDataSetChanged();
                    Toast.makeText(ChemDCRGift.this, jobj.getString("errormsg"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dcr_ch_gift_menu, menu);
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
        if (id == R.id.chnextgift) {
            param = "NEXT";
            submitentry();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void submitentry() {
        giftnameslist.clearFocus();
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(dcrplst).getAsJsonArray();
        //Toast.makeText(ChemDCRGift.this, myCustomArray.toString(), Toast.LENGTH_LONG).show();
        new ChemDCRGift.addGiftEntry().execute(Global.ecode, Global.netid, serial, Global.dcrno, finyr, d1d2, field, myCustomArray.toString(), pob.getText().toString(), Global.dbprefix);
    }

    private void menuOperation(String mode) {
        int nextposition = position + 1;
        if (mode.equalsIgnoreCase("NEXT")) {
            if (nextposition < ChemistData.dcrdlst.size()) {
                DcrdchlstItem model = ChemistData.dcrdlst.get(nextposition);
                Intent intent = new Intent(ChemDCRGift.this, ChemDCRGift.class);
                intent.putExtra("serial", "CH" + model.getSerial());
                intent.putExtra("oserial", model.getSerial());
                intent.putExtra("cntcd", model.getCntCD());
                intent.putExtra("wnetid", model.getWNetID());
                intent.putExtra("pob", model.getPOB());
                intent.putExtra("position", Integer.toString(nextposition));
                intent.putExtra("chname", "Name - " + model.getStname());
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(ChemDCRGift.this, R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
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
        ChemDCRGift.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
