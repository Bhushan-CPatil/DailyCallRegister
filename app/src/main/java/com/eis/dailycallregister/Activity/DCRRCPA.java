package com.eis.dailycallregister.Activity;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.DcrddrlstItem;
import com.eis.dailycallregister.Pojo.GetRCPABrandListRes;
import com.eis.dailycallregister.Pojo.GetRCPACompProdLstRes;
import com.eis.dailycallregister.Pojo.GetRCPAPulseChemist;
import com.eis.dailycallregister.Pojo.RcpabrandlistItem;
import com.eis.dailycallregister.Pojo.RcpacomplistItem;
import com.eis.dailycallregister.Pojo.RcpapulsechemistItem;
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
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DCRRCPA extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 60000;
    public static final int READ_TIMEOUT = 90000;
    TextView docname;
    AppCompatSpinner pulsechemist,brandlst;
    EditText brandrx;
    RecyclerView complist;
    MaterialButton submit,cancel;
    LinearLayout ll1,ll2,ll3;
    String doccntcd,doctorname;
    ViewDialog progressDialoge;
    List<RcpapulsechemistItem> pulsechemlist = new ArrayList();
    List<RcpabrandlistItem> brandlist = new ArrayList();
    List<RcpacomplistItem> complst = new ArrayList();
    public LinkedHashMap<String, String> pulsechemmap = new LinkedHashMap<String, String>();
    public List<String> arrayList = new ArrayList<>();
    public List<String> arrayListB = new ArrayList<>();
    boolean chemonchangeacc = false, brandonchangeacc = false;
    String scntcd="",d1d2="";
    ConstraintLayout contl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcrrcp);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>RCPA Entry</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        doccntcd = getIntent().getStringExtra("cntcd");
        doctorname = getIntent().getStringExtra("drname");
        if (Global.hname.contains("(A)")) {
            d1d2 = "A";
        } else if (Global.hname.contains("(B)")) {
            d1d2 = "B";
        } else if (Global.hname.contains("(C)")) {
            d1d2 = "C";
        } else if (Global.hname.contains("(D)")) {
            d1d2 = "D";
        }
        progressDialoge = new ViewDialog(DCRRCPA.this);
        docname = findViewById(R.id.docname);
        pulsechemist = findViewById(R.id.pulsechemist);
        brandlst = findViewById(R.id.brandlst);
        brandrx = findViewById(R.id.brandrx);
        complist = findViewById(R.id.complist);
        submit = findViewById(R.id.submit);
        cancel = findViewById(R.id.cancel);
        contl = findViewById(R.id.contl);
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.GONE);
        ll3.setVisibility(View.GONE);
        submit.setEnabled(false);
        setCompAdapter();
        if(!doctorname.equalsIgnoreCase("")){
            docname.setText(doctorname);
            ll1.setVisibility(View.VISIBLE);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complist.clearFocus();
                AlertDialog.Builder builder = new AlertDialog.Builder(DCRRCPA.this);
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
                                    String brx = brandrx.getText().toString();
                                    if(brx.equalsIgnoreCase("")){
                                        brandrx.setError("Please enter RX QTY of selected brand. \n0 is also accepted.");
                                        brandrx.requestFocus();
                                    }else{
                                        //call save api
                                        //Toast.makeText(DCRRCPA.this, myCustomArray.toString(),Toast.LENGTH_LONG).show();
                                        Gson gson = new GsonBuilder().create();
                                        JsonArray myCustomArray = gson.toJsonTree(complst).getAsJsonArray();
                                        String selprodname = brandlst.getSelectedItem().toString();
                                        String prodid = "";
                                        for(int x=0;x<brandlist.size();x++){
                                            if(brandlist.get(x).getPname().equalsIgnoreCase(selprodname)){
                                                prodid = brandlist.get(x).getProdid();
                                            }
                                        }
                                        String yrmth = Global.dcrdateyear+""+Global.dcrdatemonth;
                                        new DCRRCPA.addEntry().execute(Global.netid, scntcd, yrmth, prodid, myCustomArray.toString(), brx, Global.dbprefix);
                                    }

                                }else{
                                    Toast.makeText(DCRRCPA.this, "Records not saved !\nPlease go back and re submit the entry." ,Toast.LENGTH_LONG).show();
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
        });


        pulsechemist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(chemonchangeacc){
                    complist.setVisibility(View.GONE);
                    ll3.setVisibility(View.GONE);
                    submit.setEnabled(false);
                    String hkey = pulsechemist.getSelectedItem().toString();
                    scntcd = pulsechemmap.get(hkey);
                    getBrandsApi();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        brandlst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(brandonchangeacc){
                    String hkey = brandlst.getSelectedItem().toString().trim();
                    String RX,prodid="";
                    for(int j=0;j<brandlist.size();j++){
                        RcpabrandlistItem temp = brandlist.get(j);
                        if(temp.getPname().equalsIgnoreCase(hkey)){
                            RX = temp.getRX();
                            prodid = temp.getProdid();
                            brandrx.setText(RX);
                        }
                    }
                    getCompetitorApi(prodid);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        getChemistApi();
    }

    private boolean checkIfAllOkOrNot() {
        for (int i = 0; i < complst.size(); i++) {
            RcpacomplistItem temp = complst.get(i);
            if (temp.getRX().equalsIgnoreCase("")) {
                makeAlert("Rx QTY", temp.getCOMPNAME());
                return false;
            }
        }
        return true;
    }

    private void makeAlert(String ofthe, String pname) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DCRRCPA.this);
        builder.setCancelable(true);
        builder.setTitle("Alert !");
        builder.setMessage("PLEASE ENTER " + ofthe + " OF " + pname + "\n0 is also accepted");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setCompAdapter() {
        complist.setNestedScrollingEnabled(false);
        complist.setLayoutManager(new LinearLayoutManager(DCRRCPA.this));
        complist.setAdapter(new RecyclerView.Adapter() {
                                   @NonNull
                                   @Override
                                   public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                       View view = LayoutInflater.from(DCRRCPA.this).inflate(R.layout.competotor_rx_entry_popup, viewGroup, false);
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
                                       final RcpacomplistItem model = complst.get(i);
                                       myHolder.compname.setText(model.getCOMPNAME());
                                       myHolder.comprxqty.setText(model.getRX());
                                       myHolder.comprxqty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                           @Override
                                           public void onFocusChange(View view, boolean hasFocus) {
                                               if (!hasFocus) {
                                                   String xyz = myHolder.comprxqty.getText().toString().equalsIgnoreCase("") ? "" : myHolder.comprxqty.getText().toString();
                                                   model.setRX(xyz);
                                                   //Toast.makeText(DocDCRGift.this, "Focus Lose", Toast.LENGTH_SHORT).show();
                                                   InputMethodManager imm = (InputMethodManager) getSystemService(DCRRCPA.this.INPUT_METHOD_SERVICE);
                                                   imm.hideSoftInputFromWindow(contl.getWindowToken(), 0);
                                               }

                                           }
                                       });
                                   }

                                   @Override
                                   public int getItemCount() {
                                       return complst.size();
                                   }

                                   class Holder extends RecyclerView.ViewHolder {
                                       TextView compname;
                                       EditText comprxqty;

                                       public Holder(@NonNull View itemView) {
                                           super(itemView);
                                           compname = itemView.findViewById(R.id.compname);
                                           comprxqty = itemView.findViewById(R.id.comprxqty);
                                       }
                                   }
                               }
        );
    }

    private void getCompetitorApi(String prodid) {
        complst.clear();
        progressDialoge.show();
        retrofit2.Call<GetRCPACompProdLstRes> call = RetrofitClient.getInstance().getApi().rcpa_comp_prod(scntcd, Global.netid,
                Global.dcrdateyear+""+Global.dcrdatemonth, prodid, Global.dbprefix);
        call.enqueue(new Callback<GetRCPACompProdLstRes>() {
            @Override
            public void onResponse(Call<GetRCPACompProdLstRes> call, Response<GetRCPACompProdLstRes> response) {
                progressDialoge.dismiss();
                GetRCPACompProdLstRes res = response.body();
                if(!res.isError()){
                    complst = res.getRcpacomplist();
                    complist.setVisibility(View.VISIBLE);
                    submit.setEnabled(true);
                    complist.getAdapter().notifyDataSetChanged();
                }else{
                    Toast.makeText(DCRRCPA.this, "Competitor brands not present !", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<GetRCPACompProdLstRes> call, Throwable t) {
                progressDialoge.dismiss();
                Toast.makeText(DCRRCPA.this, "Failed to get brand list !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getBrandsApi() {
        arrayListB.clear();
        progressDialoge.show();
        retrofit2.Call<GetRCPABrandListRes> call = RetrofitClient.getInstance().getApi().rcpa_brands(scntcd, Global.netid,
                Global.dcrdateyear+""+Global.dcrdatemonth, d1d2, Global.dbprefix);
        call.enqueue(new Callback<GetRCPABrandListRes>() {
            @Override
            public void onResponse(Call<GetRCPABrandListRes> call, Response<GetRCPABrandListRes> response) {
                progressDialoge.dismiss();
                GetRCPABrandListRes res = response.body();
                if(!res.isError()){
                    brandlist = res.getRcpabrandlist();
                    for (int i = 0; i < brandlist.size(); i++) {
                        arrayListB.add(brandlist.get(i).getPname());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DCRRCPA.this, android.R.layout.simple_spinner_item, arrayListB);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    brandlst.setAdapter(adapter);
                    brandlst.setPrompt("Select Brand");
                    ll3.setVisibility(View.VISIBLE);
                    brandonchangeacc = true;
                }else{
                    Toast.makeText(DCRRCPA.this, "Brands not present !", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<GetRCPABrandListRes> call, Throwable t) {
                progressDialoge.dismiss();
                Toast.makeText(DCRRCPA.this, "Failed to get brand list !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getChemistApi() {
        progressDialoge.show();
        retrofit2.Call<GetRCPAPulseChemist> call = RetrofitClient.getInstance().getApi().rcpa_chemist(doccntcd, Global.netid, Global.dbprefix);
        call.enqueue(new Callback<GetRCPAPulseChemist>() {
            @Override
            public void onResponse(Call<GetRCPAPulseChemist> call, Response<GetRCPAPulseChemist> response) {
                progressDialoge.dismiss();
                GetRCPAPulseChemist res = response.body();
                if(!res.isError()){
                    pulsechemlist = res.getRcpapulsechemist();
                    for (int i = 0; i < pulsechemlist.size(); i++) {
                        String key = pulsechemlist.get(i).getStname();
                        arrayList.add(key);
                        String value = pulsechemlist.get(i).getCntcd();
                        pulsechemmap.put(key, value);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(DCRRCPA.this, android.R.layout.simple_spinner_item, arrayList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    pulsechemist.setAdapter(adapter);
                    pulsechemist.setPrompt("Select Chemist");
                    ll2.setVisibility(View.VISIBLE);
                    chemonchangeacc = true;
                }else{
                    Toast.makeText(DCRRCPA.this, "Pulse Chemists not present !", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetRCPAPulseChemist> call, Throwable t) {
                progressDialoge.dismiss();
                Toast.makeText(DCRRCPA.this, "Failed to get pulse chemists list !", Toast.LENGTH_LONG).show();
            }
        });
    }

    public class addEntry extends AsyncTask<String, String, String> {
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
                url = new URL(RetrofitClient.BASE_URL + "addRCPAEntry.php");

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
                        .appendQueryParameter("netid", params[0])
                        .appendQueryParameter("cntcd", params[1])
                        .appendQueryParameter("yrmth", params[2])
                        .appendQueryParameter("prodid", params[3])
                        .appendQueryParameter("jsonarray", params[4])
                        .appendQueryParameter("brdrx", params[5])
                        .appendQueryParameter("DBPrefix", params[6]);

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
                    //onBackPressed();
                    String hkey = brandlst.getSelectedItem().toString().trim();
                    String RX;
                    for(int j=0;j<brandlist.size();j++){
                        RcpabrandlistItem temp = brandlist.get(j);
                        if(temp.getPname().equalsIgnoreCase(hkey)){
                            RX = brandrx.getText().toString().trim();
                            temp.setRX(Integer.toString(Integer.parseInt(RX)));
                        }
                    }
                    Toast.makeText(DCRRCPA.this, jobj.getString("errormsg"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                //todo handle exception
                e.printStackTrace();
            }
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
        DCRRCPA.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
