package com.eis.dailycallregister.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.DCRGiftListRes;
import com.eis.dailycallregister.Pojo.DCRProdListRes;
import com.eis.dailycallregister.Pojo.DcrddrlstItem;
import com.eis.dailycallregister.Pojo.DcrproductlistItem;
import com.eis.dailycallregister.Pojo.DefaultResponse;
import com.eis.dailycallregister.Pojo.GetPopupQuesRes;
import com.eis.dailycallregister.Pojo.QuestionslistItem;
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
    ViewDialog progressDialoge;
    MaterialButton submitbtn,cancelbtn;
    ConstraintLayout nsv;
    TextView docname;
    //NestedScrollView nsv;
    RecyclerView productnameslist;
    public String serial,serialwp,d1d2,iscompcall,finyr,field,cntcd,drclass;
    Spinner spn;
    int position;
    public List<DcrproductlistItem> dcrprodlst = new ArrayList<>();
    public List<QuestionslistItem> questionslist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_dcrproduct);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>Product Entry</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        String[] arraySpinner = new String[] {
                "NO", "YES"
        };
        finyr = Global.getFinancialYr(Global.dcrdatemonth,Global.dcrdateyear);
        iscompcall = getIntent().getStringExtra("compcall");
        drclass = getIntent().getStringExtra("drclass");
        position = Integer.parseInt(getIntent().getStringExtra("position"));
        field = Global.getFieldName(Integer.parseInt(Global.dcrdatemonth));

        progressDialoge=new ViewDialog(DocDCRProduct.this);

        serial = getIntent().getStringExtra("serial");
        serialwp = getIntent().getStringExtra("oserial");
        cntcd = getIntent().getStringExtra("cntcd");
        if(Global.hname.contains("(A)")){
            d1d2 = "A";
        }else if(Global.hname.contains("(B)")){
            d1d2 = "B";
        }else if(Global.hname.contains("(C)")){
            d1d2 = "C";
        }else if(Global.hname.contains("(D)")){
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
        if(iscompcall.equalsIgnoreCase("Y")){
            spn.setSelection(1);
        }else{
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
                productnameslist.clearFocus();
                AlertDialog.Builder builder = new AlertDialog.Builder(DocDCRProduct.this);
                builder.setCancelable(true);
                builder.setTitle("SUBMIT ?");
                builder.setMessage("Are you sure want to submit ?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Gson gson = new GsonBuilder().create();
                                JsonArray myCustomArray = gson.toJsonTree(dcrprodlst).getAsJsonArray();
                                String text = spn.getSelectedItem().toString();
                                String compcall = "N";
                                if(text.equalsIgnoreCase("YES")){
                                    compcall = "Y";
                                    iscompcall = "Y";
                                }else{
                                    iscompcall = "N";
                                }
                                //Toast.makeText(DocDCRProduct.this, myCustomArray.toString(), Toast.LENGTH_LONG).show();
                                new DocDCRProduct.addProductEntry().execute(Global.ecode,Global.netid,serialwp,Global.dcrno,finyr,d1d2,field,
                                        myCustomArray.toString(),"",Global.dbprefix,cntcd,Global.dcrdate,compcall,"X","");
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

    private void apicall1() {
        progressDialoge.show();

        retrofit2.Call<DCRProdListRes> call1 = RetrofitClient
                .getInstance().getApi().DCRProdApi(serial,Global.netid, Global.dcrno, d1d2,Global.ecode,finyr,Global.dbprefix);
        call1.enqueue(new Callback<DCRProdListRes>() {
            @Override
            public void onResponse(retrofit2.Call<DCRProdListRes> call1, Response<DCRProdListRes> response) {
                DCRProdListRes res = response.body();
                //progressDialoge.dismiss();
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
                .getInstance().getApi().yesNoQuestionPopup(Global.ecode,Global.netid, drclass, d1d2, Global.dcrdatemonth, Global.dcrdateyear, cntcd,Global.dbprefix);
        call1.enqueue(new Callback<GetPopupQuesRes>() {
            @Override
            public void onResponse(retrofit2.Call<GetPopupQuesRes> call1, Response<GetPopupQuesRes> response) {
                progressDialoge.dismiss();
                GetPopupQuesRes res = response.body();
                if(!res.isError()){
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
                View view= LayoutInflater.from(DocDCRProduct.this).inflate(R.layout.doc_product_adapter, viewGroup,false);
                Holder holder=new Holder(view);
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                final Holder myHolder= (Holder) viewHolder;
                final DcrproductlistItem model = dcrprodlst.get(i);
                myHolder.prodname.setText(model.getPNAME());
                if(!model.getQTY().equalsIgnoreCase("")){
                    myHolder.qty.setText(model.getQTY());
                }else{
                    myHolder.qty.setText("");
                }

                if(model.getDETFLAG().equalsIgnoreCase("Y")){
                    myHolder.detailing.setChecked(true);
                }else {
                    myHolder.detailing.setChecked(false);
                }

                if(!model.getRxQTY().equalsIgnoreCase("")){
                    myHolder.rx.setText(model.getRxQTY());
                }else{
                    myHolder.rx.setText("");
                }
                myHolder.bal.setText("Bal : "+model.getBAL());
                myHolder.qty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (!hasFocus) {

                            if(myHolder.qty.getText().toString().equalsIgnoreCase("")){
                                model.setQTY("");
                                model.setDETFLAG("");
                                myHolder.detailing.setChecked(false);
                            }else{
                                if(Integer.parseInt(myHolder.qty.getText().toString())>=0){
                                    model.setQTY(Integer.toString(Integer.parseInt(myHolder.qty.getText().toString())));
                                    model.setDETFLAG("Y");
                                    myHolder.detailing.setChecked(true);
                                }
                            }
                            //Toast.makeText(DocDCRGift.this, "Focus Lose", Toast.LENGTH_SHORT).show();
                            InputMethodManager imm =  (InputMethodManager) getSystemService(DocDCRProduct.this.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(nsv.getWindowToken(), 0);
                        }

                    }
                });
                myHolder.rx.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (!hasFocus) {

                            if(myHolder.detailing.isChecked() && myHolder.rx.getText().toString().equalsIgnoreCase("")){
                                model.setRxQTY("");
                                model.setQTY("");
                                model.setDETFLAG("");
                                myHolder.detailing.setChecked(false);
                            }else{
                                if(!myHolder.rx.getText().toString().equalsIgnoreCase("") && Integer.parseInt(myHolder.rx.getText().toString())>=0) {
                                    model.setRxQTY(Integer.toString(Integer.parseInt(myHolder.rx.getText().toString())));
                                }else if(myHolder.rx.getText().toString().equalsIgnoreCase("")){
                                    model.setRxQTY("");
                                }
                            }
                            //Toast.makeText(DocDCRGift.this, "Focus Lose", Toast.LENGTH_SHORT).show();
                            InputMethodManager imm =  (InputMethodManager) getSystemService(DocDCRProduct.this.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(nsv.getWindowToken(), 0);
                        }

                    }
                });

                myHolder.detailing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            model.setDETFLAG("Y");
                            if(myHolder.qty.getText().toString().equalsIgnoreCase("")){
                                model.setQTY("0");
                                myHolder.qty.setText(model.getQTY());
                            }

                            if(myHolder.rx.getText().toString().equalsIgnoreCase("")){
                                model.setRxQTY("0");
                                myHolder.rx.setText(model.getRxQTY());
                            }
                        }else{
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
                TextView prodname,bal;
                EditText qty,rx;
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
                url = new URL(RetrofitClient.BASE_URL+"addDcrProductEntry.php");

            } catch (MalformedURLException e) {

                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("ecode",params[0])
                        .appendQueryParameter("netid",params[1])
                        .appendQueryParameter("serial",params[2])
                        .appendQueryParameter("dcrno",params[3])
                        .appendQueryParameter("financialyear",params[4])
                        .appendQueryParameter("d1d2",params[5])
                        .appendQueryParameter("field",params[6])
                        .appendQueryParameter("jsonarray",params[7])
                        .appendQueryParameter("qgen",params[8])
                        .appendQueryParameter("DBPrefix",params[9])
                        .appendQueryParameter("cntcd",params[10])
                        .appendQueryParameter("dcrdate",params[11])
                        .appendQueryParameter("COMPLETECALL",params[12])
                        .appendQueryParameter("spflag",params[13])
                        .appendQueryParameter("pflag",params[14]);

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
                    return(result.toString());

                }else{

                    return("unsuccessful");
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

                if(!jobj.getBoolean("error"))
                {
                    DcrddrlstItem modelx = DoctorsData.dcrdlst.get(position);
                    modelx.setCompletecall(iscompcall);
                    onBackPressed();
                    DoctorsData.doctorslist.getAdapter().notifyDataSetChanged();
                    Toast.makeText(DocDCRProduct.this, jobj.getString("errormsg"),Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void showQuesPopup(){
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
                View view= LayoutInflater.from(DocDCRProduct.this).inflate(R.layout.yes_no_questions_popup_adapter, viewGroup,false);
                Holder holder=new Holder(view);
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final Holder myHolder= (Holder) viewHolder;
                final QuestionslistItem model = questionslist.get(i);
                myHolder.donewith.setVisibility(View.GONE);
                myHolder.question.setText(model.getQdescrpn());
                String andesc = "Select option~"+model.getAnsdesc();
                String[] answ = andesc.split("~");
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(DocDCRProduct.this,
                        android.R.layout.simple_spinner_item, answ);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                myHolder.ans.setAdapter(adapter1);
                if(!model.getAns().equalsIgnoreCase("")){
                    myHolder.ans.setSelection(adapter1.getPosition(model.getAns()));
                }


                if(model.getSubansdesc().length() > 0) {
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
                        if(text.equalsIgnoreCase("Select option")){
                            model.setAns("");
                            if(model.getSubansdesc().length() > 0){
                            myHolder.subans.setSelection(0);
                            model.setSubans("");
                            myHolder.donewith.setVisibility(View.GONE);
                            }
                        }else{
                            if(text.equalsIgnoreCase("YES")){
                                model.setAns(text);
                                if(model.getSubansdesc().length() > 0){
                                    myHolder.donewith.setVisibility(View.VISIBLE);
                                }
                            }else {
                                model.setAns(text);
                                if(model.getSubansdesc().length() > 0){
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
                        if(text.equalsIgnoreCase("Select option")){
                            model.setSubans("");
                        }else{
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
                Spinner ans,subans;
                LinearLayout donewith;
                public Holder(@NonNull View itemView) {
                    super(itemView);
                    question = itemView.findViewById(R.id.question);
                    ans = itemView.findViewById(R.id.ans);
                    subans = itemView.findViewById(R.id.subans);
                    donewith = itemView.findViewById(R.id.donewith);
                }
            } }
        );

        rv_list_popup.getAdapter().notifyDataSetChanged();
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                Toast.makeText(DocDCRProduct.this, "Can not Cancel !", Toast.LENGTH_SHORT).show();
            }
        });
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isansgiven = true;
                boolean issubansgiven = true;
                for(int m=0;m<questionslist.size();m++){
                    if(questionslist.get(m).getAns().equalsIgnoreCase("")){
                        isansgiven = false;
                    }
                    if(questionslist.get(m).getSubansdesc().length()>0){
                        if(questionslist.get(m).getAns().equalsIgnoreCase("YES") && questionslist.get(m).getSubans().equalsIgnoreCase("")){
                            issubansgiven = false;
                        }
                    }
                }

                if(isansgiven){
                    if(issubansgiven){
                        dialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        JsonArray myCustomArray = gson.toJsonTree(questionslist).getAsJsonArray();
                        //Toast.makeText(DocDCRProduct.this, myCustomArray.toString(), Toast.LENGTH_SHORT).show();
                        storePopupQuesAnsInDB(myCustomArray.toString());

                    }else{
                        Toast.makeText(DocDCRProduct.this, "First answer all sub question !", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(DocDCRProduct.this, "First answer all question !", Toast.LENGTH_SHORT).show();
                    //todo save in db
                }
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void storePopupQuesAnsInDB(String json) {
        progressDialoge.show();
        //Log.d("drclass//d1d2//cntcd",drclass+"//"+d1d2+"//"+cntcd);
        retrofit2.Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().submitPopupQuesAns(Global.ecode,Global.netid, Global.dcrdate, json,Global.dcrdatemonth, Global.dcrdateyear, cntcd,Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                progressDialoge.dismiss();
                DefaultResponse res = response.body();
                if(!res.isError()){
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
        DocDCRProduct.this.overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
    }
}
