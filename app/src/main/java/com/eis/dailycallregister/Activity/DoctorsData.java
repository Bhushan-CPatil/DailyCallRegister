package com.eis.dailycallregister.Activity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.JointWRKArrayList;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.AreaJntWrkRes;
import com.eis.dailycallregister.Pojo.ArealistItem;
import com.eis.dailycallregister.Pojo.DCRDDocListRes;
import com.eis.dailycallregister.Pojo.DcrddrlstItem;
import com.eis.dailycallregister.Pojo.DefaultResponse;
import com.eis.dailycallregister.Pojo.DocinawItem;
import com.eis.dailycallregister.Pojo.DoctorListAWRes;
import com.eis.dailycallregister.Pojo.JointwrkItem;
import com.eis.dailycallregister.R;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorsData extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 60000;
    public static final int READ_TIMEOUT = 90000;
    Spinner area;
    AppCompatCheckBox indchkbox;
    MaterialButton jointwrkbtn, docentry;
    RadioGroup rdgrp;
    RecyclerView jointwrklist;
    public static RecyclerView doctorslist;
    ViewDialog progressDialoge;
    NestedScrollView sv;
    NestedScrollView nestedsv;
    public List<String> arrayList = new ArrayList<>();
    public List<ArealistItem> arealist = new ArrayList<>();
    public List<DocinawItem> dclstawlist = new ArrayList<>();
    public List<JointwrkItem> jntwrklist = new ArrayList<>();
    public List<JointWRKArrayList> seljntwrklst = new ArrayList<>();
    public static List<DcrddrlstItem> dcrdlst = new ArrayList<>();
    boolean isapi1success = false;
    int areanameid = 0;
    public String field = "", finyear = "";
    public LinkedHashMap<String, String> workingarea = new LinkedHashMap<String, String>();
    public LinkedHashMap<String, String> seljnwrkmap = new LinkedHashMap<String, String>();
    public ArrayList<String> seldraw = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_data);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>Doctors Data</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        progressDialoge = new ViewDialog(DoctorsData.this);
        indchkbox = findViewById(R.id.indchkbox);
        jointwrkbtn = findViewById(R.id.jointwrkbtn);
        area = findViewById(R.id.areaspinn);
        rdgrp = findViewById(R.id.rdgrp);
        docentry = findViewById(R.id.docentry);
        doctorslist = findViewById(R.id.doctorslist);
        jointwrklist = findViewById(R.id.jointwrklist);
        nestedsv = findViewById(R.id.nestedsv);
        jointwrklist.setNestedScrollingEnabled(false);
        jointwrklist.setLayoutManager(new LinearLayoutManager(DoctorsData.this));
        sv = findViewById(R.id.sv);
        field = Global.getFieldName(Integer.parseInt(Global.dcrdatemonth));
        finyear = Global.getFinancialYr(Global.dcrdatemonth, Global.dcrdateyear);
        dcrdlst.clear();
        independentCkbCode();
        jointWorkingSetAdaper();
        apicall1();
        docentryOnClickListner();
        jointwrkbtnOnClickListner();
        setDcrddocAdapter();

    }

    private void independentCkbCode() {
        indchkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    seljntwrklst.clear();
                    JointWRKArrayList jk = new JointWRKArrayList("Indep", "INDEPENDENT");
                    seljntwrklst.add(jk);
                    jointwrklist.getAdapter().notifyDataSetChanged();
                    nestedsv.setVisibility(View.VISIBLE);
                    jointwrkbtn.setEnabled(false);
                } else {
                    nestedsv.setVisibility(View.GONE);
                    seljntwrklst.clear();
                    jointwrkbtn.setEnabled(true);
                }
            }
        });
    }


    private void jointwrkbtnOnClickListner() {
        jointwrkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedsv.setVisibility(View.GONE);
                seljntwrklst.clear();
                final Dialog dialog = new Dialog(DoctorsData.this);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.jointwrklstpopup);
                TextView heading = dialog.findViewById(R.id.heading);
                heading.setText("Select Joint Working");
                CardView cancelbtn = dialog.findViewById(R.id.no);
                CardView submitbtn = dialog.findViewById(R.id.yes);
                RecyclerView rv_list_popup = dialog.findViewById(R.id.jointwrkpopuplist);
                rv_list_popup.setNestedScrollingEnabled(false);
                rv_list_popup.setLayoutManager(new LinearLayoutManager(DoctorsData.this));
                rv_list_popup.setAdapter(new RecyclerView.Adapter() {
                                             @NonNull
                                             @Override
                                             public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                                 View view = LayoutInflater.from(DoctorsData.this).inflate(R.layout.jointwrkpopuplst_adapter, viewGroup, false);
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
                                                 final JointwrkItem model = jntwrklist.get(i);
                                                 myHolder.drname.setText(model.getENAME());
                                                 myHolder.ckb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                         if (isChecked) {
                                                             JointWRKArrayList jk = new JointWRKArrayList(model.getECODE(), model.getENAME());
                                                             seljntwrklst.add(jk);
                                                         } else {
                                                             for (int k = 0; k < seljntwrklst.size(); k++) {
                                                                 String ecode = seljntwrklst.get(k).getEcode();
                                                                 if (ecode.equals(model.getECODE())) {
                                                                     seljntwrklst.remove(k);
                                                                 }
                                                             }
                                                         }
                                                     }
                                                 });

                                             }

                                             @Override
                                             public int getItemCount() {
                                                 return jntwrklist.size();
                                             }

                                             class Holder extends RecyclerView.ViewHolder {
                                                 TextView drname;
                                                 AppCompatCheckBox ckb;

                                                 public Holder(@NonNull View itemView) {
                                                     super(itemView);
                                                     drname = itemView.findViewById(R.id.jnempname);
                                                     ckb = itemView.findViewById(R.id.jnwrkchkbx);
                                                 }
                                             }
                                         }
                );

                rv_list_popup.getAdapter().notifyDataSetChanged();
                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                submitbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (seljntwrklst.size() > 4) {
                            Snackbar snackbar = Snackbar.make(sv, "Maximum 4 can be selected at a time !", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else if (seljntwrklst.size() == 0) {
                            Snackbar snackbar = Snackbar.make(sv, "Please select at list 1 name !", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            //todo notify dataset changed an visible it
                            dialog.dismiss();
                            nestedsv.setVisibility(View.VISIBLE);
                            jointwrklist.getAdapter().notifyDataSetChanged();
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
        });
    }

    private void docentryOnClickListner() {
        docentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seljntwrklst.size() > 0) {
                    apicall2();
                } else {
                    Snackbar snackbar = Snackbar.make(sv, "Please select independent or joint working !", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    private void jointWorkingSetAdaper() {
        jointwrklist.setAdapter(new RecyclerView.Adapter() {
                                    @NonNull
                                    @Override
                                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                        View view = LayoutInflater.from(DoctorsData.this).inflate(R.layout.jointwrkemplist, viewGroup, false);
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
                                        final JointWRKArrayList model = seljntwrklst.get(i);
                                        myHolder.seljntwrk.setText(model.getEname());
                                    }

                                    @Override
                                    public int getItemCount() {
                                        return seljntwrklst.size();
                                    }

                                    class Holder extends RecyclerView.ViewHolder {
                                        TextView seljntwrk;

                                        public Holder(@NonNull View itemView) {
                                            super(itemView);
                                            seljntwrk = itemView.findViewById(R.id.seljntwrk);
                                        }
                                    }
                                }
        );
    }


    private void apicall1() {
        progressDialoge.show();

        retrofit2.Call<AreaJntWrkRes> call1 = RetrofitClient
                .getInstance().getApi().getAreaJntWrk(Global.ecode, Global.netid, Global.hname, Global.dcrdate, Global.dcrdatemonth, Global.dcrdateyear, Global.dbprefix);
        call1.enqueue(new Callback<AreaJntWrkRes>() {
            @Override
            public void onResponse(retrofit2.Call<AreaJntWrkRes> call1, Response<AreaJntWrkRes> response) {
                AreaJntWrkRes res = response.body();
                workingarea.clear();
                progressDialoge.dismiss();
                arealist = res.getArealist();
                jntwrklist = res.getJointwrk();
                String areaname = "";


                for (int i = 0; i < arealist.size(); i++) {
                    if (arealist.get(i).getStatus().equalsIgnoreCase("active")) {
                        areaname = arealist.get(i).getTOWN() + " / " + arealist.get(i).getTOWNID();
                        Global.workingareaid = arealist.get(i).getTCPID() + ":" + arealist.get(i).getWTYPE();
                        areanameid = i;
                    }

                    String key = arealist.get(i).getTOWN() + " / " + arealist.get(i).getTOWNID();
                    arrayList.add(key);
                    String value = arealist.get(i).getTCPID() + ":" + arealist.get(i).getWTYPE();
                    workingarea.put(key, value);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DoctorsData.this, android.R.layout.simple_spinner_item, arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                area.setAdapter(adapter);
                area.setSelection(adapter.getPosition(areaname));

                if (Global.dcrno != null) {
                    apicall3();
                }
            }

            @Override
            public void onFailure(Call<AreaJntWrkRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
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

    private void apicall2() {
        progressDialoge.show();
        String selitem = area.getSelectedItem().toString().trim();
        String valuefrmhm = workingarea.get(selitem);
        String[] valspt = valuefrmhm.split(":");
        Global.tcpid = valspt[0];
        Global.wrktype = valspt[1];

        retrofit2.Call<DoctorListAWRes> call1 = RetrofitClient
                .getInstance().getApi().getDoctorDataList(Global.ecode, Global.netid, Global.tcpid, Global.dcrdate, Global.dcrdatemonth, Global.dcrdateyear, Global.dbprefix);
        call1.enqueue(new Callback<DoctorListAWRes>() {
            @Override
            public void onResponse(retrofit2.Call<DoctorListAWRes> call1, Response<DoctorListAWRes> response) {
                progressDialoge.dismiss();
                DoctorListAWRes res = response.body();
                if (!res.isError()) {
                    dclstawlist = res.getDocinaw();
                    //Log.d("list size-->", Integer.toString(dclstawlist.size()));
                    showPopup();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DoctorsData.this);
                    builder.setCancelable(true);
                    //builder.setTitle("LOGOUT ?");
                    builder.setMessage("No doctors available to show for selected criteria !");
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<DoctorListAWRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
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

    public void showPopup() {
        final Dialog dialog = new Dialog(DoctorsData.this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.jointwrklstpopup);
        TextView heading = dialog.findViewById(R.id.heading);
        heading.setText("Select Doctor");
        CardView cancelbtn = dialog.findViewById(R.id.no);
        CardView submitbtn = dialog.findViewById(R.id.yes);
        RecyclerView rv_list_popup = dialog.findViewById(R.id.jointwrkpopuplist);
        rv_list_popup.setNestedScrollingEnabled(false);
        rv_list_popup.setLayoutManager(new LinearLayoutManager(DoctorsData.this));
        rv_list_popup.setAdapter(new RecyclerView.Adapter() {
                                     @NonNull
                                     @Override
                                     public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                         View view = LayoutInflater.from(DoctorsData.this).inflate(R.layout.jointwrkpopuplst_adapter, viewGroup, false);
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
                                         final DocinawItem model = dclstawlist.get(i);
                                         myHolder.drname.setText(model.getDRCD() + " - " + model.getDRNAME());
                                         if (seldraw.size() > 0 && seldraw.contains(model.getCNTCD())) {
                                             myHolder.ckb.setChecked(true);
                                         }
                                         myHolder.ckb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                             @Override
                                             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                 if (isChecked) {
                                                     seldraw.add(model.getCNTCD());
                                                 } else {
                                                     seldraw.remove(model.getCNTCD());
                                                 }
                                             }
                                         });

                                     }

                                     @Override
                                     public int getItemCount() {
                                         return dclstawlist.size();
                                     }

                                     class Holder extends RecyclerView.ViewHolder {
                                         TextView drname;
                                         AppCompatCheckBox ckb;

                                         public Holder(@NonNull View itemView) {
                                             super(itemView);
                                             drname = itemView.findViewById(R.id.jnempname);
                                             ckb = itemView.findViewById(R.id.jnwrkchkbx);
                                         }
                                     }
                                 }
        );

        rv_list_popup.getAdapter().notifyDataSetChanged();
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                saveSelDrsInDB();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    private void saveSelDrsInDB() {
        //progressDialoge.show();
        String ww0 = "", ww1 = "", ww2 = "", ww3 = "";
        for (int i = 0; i < seljntwrklst.size(); i++) {
            if (i == 0) {
                ww0 = seljntwrklst.get(i).getEcode();
            } else if (i == 1) {
                ww1 = seljntwrklst.get(i).getEcode();
            } else if (i == 2) {
                ww2 = seljntwrklst.get(i).getEcode();
            } else if (i == 3) {
                ww3 = seljntwrklst.get(i).getEcode();
            }
        }
        //Log.d("joint wrk sel ",seldraw.toString());
        /*retrofit2.Call<DefaultResponse> call3 = RetrofitClient
                .getInstance().getApi().*/
        //Log.d("dcrno ",Global.dcrno+"<--");
        String tempdcrno = "";
        if (Global.dcrno == null)
            tempdcrno = "";
        else
            tempdcrno = Global.dcrno;

        new DoctorsData.addSelDrsInDB().execute(Global.ecode, Global.netid, Global.tcpid, Global.dcrdate, Global.dcrdatemonth,
                Global.dcrdateyear, tempdcrno, Global.wrktype, ww0, ww1, ww2, ww3, seldraw.toString(), Global.dbprefix);
        /*call3.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DefaultResponse> call3, Response<DefaultResponse> response) {
                progressDialoge.dismiss();
                DefaultResponse res = response.body();
                //if(!res.isError()) {
                    Global.dcrno = res.getErrormsg();
                Snackbar snackbar = Snackbar.make(sv, Global.dcrno, Snackbar.LENGTH_LONG);
                snackbar.show();
                //}
            }

            @Override
            public void onFailure(Call<DefaultResponse> call3, Throwable t) {
                Log.d("error ",t.toString());
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Retry", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });*/
    }

    public class addSelDrsInDB extends AsyncTask<String, String, String> {
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
                url = new URL(RetrofitClient.BASE_URL + "doctordataaddsel.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
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
                        .appendQueryParameter("emp", params[0])
                        .appendQueryParameter("netid", params[1])
                        .appendQueryParameter("tcpid", params[2])
                        .appendQueryParameter("dcrdate", params[3])
                        .appendQueryParameter("dcrmth", params[4])
                        .appendQueryParameter("dcryr", params[5])
                        .appendQueryParameter("dcrno", params[6])
                        .appendQueryParameter("wrkflg", params[7])
                        .appendQueryParameter("WWITH", params[8])
                        .appendQueryParameter("WWITH2", params[9])
                        .appendQueryParameter("WWITH3", params[10])
                        .appendQueryParameter("WWITH4", params[11])
                        .appendQueryParameter("selcntcd", params[12])
                        .appendQueryParameter("DBPrefix", params[13]);

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
                // TODO Auto-generated catch block
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
                    // Log.d("error msg",jobj.getString("errormsg"));
                    Global.dcrno = jobj.getString("errormsg");
                    Snackbar snackbar = Snackbar.make(sv, "Doctors's added successfully !!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    apicall3();
                } else {
                    Snackbar snackbar = Snackbar.make(sv, "Failed to process your request !", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDcrddocAdapter() {
        doctorslist.setNestedScrollingEnabled(false);
        doctorslist.setLayoutManager(new LinearLayoutManager(DoctorsData.this));
        doctorslist.setAdapter(new RecyclerView.Adapter() {
                                   @NonNull
                                   @Override
                                   public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                       View view = LayoutInflater.from(DoctorsData.this).inflate(R.layout.doc_det_doc_list_adapter, viewGroup, false);
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
                                       final DcrddrlstItem model = dcrdlst.get(i);
                                       myHolder.drname.setText(model.getDrname());
                                       myHolder.remarks.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(DoctorsData.this, Remarks.class);
                                               intent.putExtra("serial", "DR" + model.getSerial());
                                               Bundle bndlanimation = ActivityOptions.makeCustomAnimation(DoctorsData.this, R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                                               startActivity(intent, bndlanimation);
                                           }
                                       });

                                       myHolder.deletedoc.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               showconfirmationdialog(model.getSerial());
                                           }
                                       });

                                       myHolder.giftentry.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(DoctorsData.this, DocDCRGift.class);
                                               intent.putExtra("serial", "DR" + model.getSerial());
                                               intent.putExtra("oserial", model.getSerial());
                                               intent.putExtra("cntcd", model.getCntCD());
                                               intent.putExtra("wnetid", model.getWNetID());
                                               intent.putExtra("drname", "Doctor Name - " + model.getDrname());
                                               intent.putExtra("compcall", model.getCompletecall());
                                               intent.putExtra("position", Integer.toString(i));
                                               intent.putExtra("drclass", model.getJsonMemberClass());
                                               Bundle bndlanimation = ActivityOptions.makeCustomAnimation(DoctorsData.this, R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                                               startActivity(intent, bndlanimation);
                                           }
                                       });

                                       myHolder.productentry.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(DoctorsData.this, DocDCRProduct.class);
                                               intent.putExtra("serial", "DR" + model.getSerial());
                                               intent.putExtra("oserial", model.getSerial());
                                               intent.putExtra("cntcd", model.getCntCD());
                                               intent.putExtra("wnetid", model.getWNetID());
                                               intent.putExtra("drname", "Doctor Name - " + model.getDrname());
                                               intent.putExtra("compcall", model.getCompletecall());
                                               intent.putExtra("position", Integer.toString(i));
                                               intent.putExtra("drclass", model.getJsonMemberClass());
                                               Bundle bndlanimation = ActivityOptions.makeCustomAnimation(DoctorsData.this, R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                                               startActivity(intent, bndlanimation);
                                           }
                                       });

                                       myHolder.rcpa.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(DoctorsData.this, DCRRCPA.class);
                                               intent.putExtra("serial", "DR" + model.getSerial());
                                               intent.putExtra("oserial", model.getSerial());
                                               intent.putExtra("cntcd", model.getCntCD());
                                               intent.putExtra("wnetid", model.getWNetID());
                                               intent.putExtra("drname", "Doctor Name - " + model.getDrname());
                                               intent.putExtra("compcall", model.getCompletecall());
                                               intent.putExtra("position", Integer.toString(i));
                                               intent.putExtra("drclass", model.getJsonMemberClass());
                                               Bundle bndlanimation = ActivityOptions.makeCustomAnimation(DoctorsData.this, R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                                               startActivity(intent, bndlanimation);
                                           }
                                       });
                                   }

                                   @Override
                                   public int getItemCount() {
                                       return dcrdlst.size();
                                   }

                                   class Holder extends RecyclerView.ViewHolder {
                                       TextView drname;
                                       ImageButton productentry, giftentry, remarks, deletedoc, rcpa;

                                       public Holder(@NonNull View itemView) {
                                           super(itemView);
                                           drname = itemView.findViewById(R.id.drname);
                                           productentry = itemView.findViewById(R.id.productentry);
                                           giftentry = itemView.findViewById(R.id.giftentry);
                                           remarks = itemView.findViewById(R.id.remarks);
                                           deletedoc = itemView.findViewById(R.id.deletedoc);
                                           rcpa = itemView.findViewById(R.id.rcpa);
                                           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                           try {
                                               Date loggedin = sdf.parse(Global.date);
                                               Date given = sdf.parse("2019-07-31");
                                               if(loggedin.after(given)){
                                                   rcpa.setVisibility(View.VISIBLE);
                                               }else{
                                                   rcpa.setVisibility(View.GONE);
                                               }
                                           } catch (ParseException e) {
                                               e.printStackTrace();
                                           }
                                       }
                                   }
                               }
        );
    }

    private void showconfirmationdialog(final String drserial) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete ?");
        builder.setMessage("Are you sure you want to delete ?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DoctorsData.deleteDRfromDCR().execute(Global.ecode, Global.netid, Global.tcpid, Global.dcrno, drserial, finyear, Global.dcrdate, Global.emplevel, field, Global.dbprefix);
                        //apicall4(drserial);
                    }
                });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void apicall4(final String serial) {

        progressDialoge.show();
        retrofit2.Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().deleteDRfromDCR(Global.ecode, Global.netid, Global.tcpid, Global.dcrno, serial, finyear, Global.dcrdate, Global.emplevel, field, Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                progressDialoge.dismiss();
                Toast.makeText(DoctorsData.this, res.getErrormsg(), Toast.LENGTH_LONG).show();
                if (!res.isError() && res.getErrormsg().equalsIgnoreCase("deleted")) {
                    apicall3();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to fetch data !", Snackbar.LENGTH_LONG)
                        .setAction("Reload", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //apicall4(serial);
                                recreate();
                            }
                        });
                snackbar.show();
            }
        });
    }

    private void apicall3() {
        progressDialoge.show();

        retrofit2.Call<DCRDDocListRes> call1 = RetrofitClient
                .getInstance().getApi().getDCRDDrs(Global.dcrno, Global.dbprefix);
        call1.enqueue(new Callback<DCRDDocListRes>() {
            @Override
            public void onResponse(retrofit2.Call<DCRDDocListRes> call1, Response<DCRDDocListRes> response) {
                DCRDDocListRes res = response.body();
                progressDialoge.dismiss();
                dcrdlst.clear();
                if (!res.isError()) {
                    seldraw.clear();

                    dcrdlst = res.getDcrddrlst();
                    for (int z = 0; z < dcrdlst.size(); z++) {
                        seldraw.add(dcrdlst.get(z).getCntCD());
                    }
                    doctorslist.setVisibility(View.VISIBLE);

                    doctorslist.getAdapter().notifyDataSetChanged();

                    if (dcrdlst.size() > 0) {
                        seljntwrklst.clear();
                        if (dcrdlst.get(0).getWw1() != null && dcrdlst.get(0).getWw1().equalsIgnoreCase("INDEPENDENT")) {
                            JointWRKArrayList jk = new JointWRKArrayList("Indep", "INDEPENDENT");
                            seljntwrklst.add(jk);
                            indchkbox.setChecked(true);
                            jointwrkbtn.setEnabled(false);

                        } else if (dcrdlst.get(0).getWw1() != null && !dcrdlst.get(0).getWw1().equalsIgnoreCase("INDEPENDENT")) {
                            JointWRKArrayList jk = new JointWRKArrayList(dcrdlst.get(0).getWWITH(), dcrdlst.get(0).getWw1());
                            seljntwrklst.add(jk);
                        }

                        if (dcrdlst.get(0).getWw2() != null) {
                            JointWRKArrayList jk = new JointWRKArrayList(dcrdlst.get(0).getWwith2(), dcrdlst.get(0).getWw2());
                            seljntwrklst.add(jk);
                        }

                        if (dcrdlst.get(0).getWw3() != null) {
                            JointWRKArrayList jk = new JointWRKArrayList(dcrdlst.get(0).getWwith3(), dcrdlst.get(0).getWw3());
                            seljntwrklst.add(jk);
                        }

                        if (dcrdlst.get(0).getWw4() != null) {
                            JointWRKArrayList jk = new JointWRKArrayList(dcrdlst.get(0).getWwith4(), dcrdlst.get(0).getWw4());
                            seljntwrklst.add(jk);
                        }
                        jointwrklist.getAdapter().notifyDataSetChanged();
                        nestedsv.setVisibility(View.VISIBLE);
                    }

                } else {
                    Snackbar snackbar = Snackbar.make(sv, "Doctors not addded !", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<DCRDDocListRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Reload", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recreate();
                                //apicall3();
                            }
                        });
                snackbar.show();
            }
        });

    }

    public class deleteDRfromDCR extends AsyncTask<String, String, String> {
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
                url = new URL(RetrofitClient.BASE_URL + "deleteDRCHfromDCR.php");

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
                        .appendQueryParameter("dcrhtcpid", params[2])
                        .appendQueryParameter("dcrno", params[3])
                        .appendQueryParameter("serial", params[4])
                        .appendQueryParameter("finyear", params[5])
                        .appendQueryParameter("dcrdate", params[6])
                        .appendQueryParameter("emplvl", params[7])
                        .appendQueryParameter("field", params[8])
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

                if (!jobj.getBoolean("error") && jobj.getString("errormsg").equalsIgnoreCase("Y")) {
                    Toast.makeText(DoctorsData.this, "Doctor deleted successfully", Toast.LENGTH_LONG).show();
                    dcrdlst.clear();
                    seldraw.clear();
                    doctorslist.getAdapter().notifyDataSetChanged();
                    Global.dcrno = null;
                    //recreate();
                } else if (!jobj.getBoolean("error") && jobj.getString("errormsg").equalsIgnoreCase("N")) {
                    Toast.makeText(DoctorsData.this, "Doctor deleted successfully", Toast.LENGTH_LONG).show();
                    dcrdlst.clear();
                    seldraw.clear();
                    doctorslist.getAdapter().notifyDataSetChanged();
                    apicall3();
                }

            } catch (JSONException e) {
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
        DoctorsData.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
