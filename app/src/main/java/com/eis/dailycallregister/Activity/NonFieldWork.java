package com.eis.dailycallregister.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.JointWRKArrayList;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.DefaultResponse;
import com.eis.dailycallregister.Pojo.ListOfTownItem;
import com.eis.dailycallregister.Pojo.NewNonFliedWrkRes;
import com.eis.dailycallregister.Pojo.NonFieldWrkRes;
import com.eis.dailycallregister.Pojo.NonjointwrkItem;
import com.eis.dailycallregister.Pojo.NonworkingItem;
import com.eis.dailycallregister.Pojo.NonwrkareaItem;
import com.eis.dailycallregister.Pojo.WorkingItem;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NonFieldWork extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 60000;
    public static final int READ_TIMEOUT = 90000;
    AppCompatCheckBox indchkbox;
    MaterialButton jointwrkbtn, addnonworking;
    ViewDialog progressDialoge;
    NestedScrollView nestedsv;
    RecyclerView jointwrklist, nonfieldwrkarealist;
    NestedScrollView sv;
    Spinner area;
    public List<NonjointwrkItem> jntwrklist = new ArrayList<>();
    public List<NonwrkareaItem> arealist = new ArrayList<>();
    public List<String> arrayList = new ArrayList<>();
    int areanameid = 0;
    public List<JointWRKArrayList> seljntwrklst = new ArrayList<>();
    //public List<NonworkingItem> nonworking = new ArrayList<>();
    //public List<WorkingItem> working = new ArrayList<>();
    public List<ListOfTownItem> newnonworktown = new ArrayList<>();
    public LinkedHashMap<String, String> workingarea = new LinkedHashMap<String, String>();

    CardView c1;
    TextView nstat, narea, ntype, wstat, warea, wtype;
    String tcpidofnonwrk = "";
    //ImageButton delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_field_work);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>Non Field Work</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        progressDialoge = new ViewDialog(NonFieldWork.this);
        indchkbox = findViewById(R.id.indchkbox);
        jointwrkbtn = findViewById(R.id.jointwrkbtn);
        nestedsv = findViewById(R.id.nestedsv);
        area = findViewById(R.id.areaspinn);
        jointwrklist = findViewById(R.id.jointwrklist);
        nonfieldwrkarealist = findViewById(R.id.nonfieldwrkarealist);
        jointwrklist.setNestedScrollingEnabled(false);
        jointwrklist.setLayoutManager(new LinearLayoutManager(NonFieldWork.this));
        setNonAdapter();
        sv = findViewById(R.id.sv);
        c1 = findViewById(R.id.card1);
        //c2 = findViewById(R.id.card2);
        //c3 = findViewById(R.id.card3);
        /*nstat = findViewById(R.id.nonwrkstat);
        narea = findViewById(R.id.nonarea);
        ntype = findViewById(R.id.nontype);
        wstat = findViewById(R.id.workingstat);
        warea = findViewById(R.id.wrkarea);
        wtype = findViewById(R.id.wrktype);*/
        //delete = findViewById(R.id.deletenonwrk);
        addnonworking = findViewById(R.id.selnonworking);
        c1.setVisibility(View.VISIBLE);
        //c2.setVisibility(View.GONE);
        //c3.setVisibility(View.GONE);
        //todo include in adapter
        /*delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NonFieldWork.this);
                builder.setCancelable(true);
                builder.setTitle("DELETE ?");
                builder.setMessage("Are you sure wants to Delete ?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteapi(tcpidofnonwrk);
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
        });*/

        addnonworking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seljntwrklst.size() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NonFieldWork.this);
                    builder.setCancelable(true);
                    builder.setTitle("ADD ?");
                    builder.setMessage("Are you sure you want to add?");
                    builder.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String selitem = area.getSelectedItem().toString().trim();
                                    String valuefrmhm = workingarea.get(selitem);
                                    String[] valspt = valuefrmhm.split(":");
                                    Global.tcpid = valspt[0];
                                    Global.wrktype = valspt[1];
                                    String tempdcrno = "";
                                    if (Global.dcrno != null) {
                                        tempdcrno = Global.dcrno;
                                    }
                                    String wwith = seljntwrklst.get(0).getEcode();
                                    new NonFieldWork.addSelNonWrkAreaInDB().execute(Global.ecode, Global.netid, Global.tcpid, Global.dcrdate, tempdcrno, Global.wrktype, wwith, Global.dbprefix);
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
                } else {
                    Snackbar snackbar = Snackbar.make(sv, "Please select independent or joint working !", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        independentCkbCode();
        jointWorkingSetAdaper();
        jointwrkbtnOnClickListner();
        apicall1();
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

    private void jointWorkingSetAdaper() {
        jointwrklist.setAdapter(new RecyclerView.Adapter() {
                                    @NonNull
                                    @Override
                                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                        View view = LayoutInflater.from(NonFieldWork.this).inflate(R.layout.jointwrkemplist, viewGroup, false);
                                        Holder holder = new Holder(view);
                                        return holder;
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

    private void jointwrkbtnOnClickListner() {
        jointwrkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedsv.setVisibility(View.GONE);
                seljntwrklst.clear();
                final Dialog dialog = new Dialog(NonFieldWork.this);
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
                rv_list_popup.setLayoutManager(new LinearLayoutManager(NonFieldWork.this));
                rv_list_popup.setAdapter(new RecyclerView.Adapter() {
                                             @NonNull
                                             @Override
                                             public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                                 View view = LayoutInflater.from(NonFieldWork.this).inflate(R.layout.jointwrkpopuplst_adapter, viewGroup, false);
                                                 Holder holder = new Holder(view);
                                                 return holder;
                                             }

                                             @Override
                                             public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                                                 final Holder myHolder = (Holder) viewHolder;
                                                 final NonjointwrkItem model = jntwrklist.get(i);
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
                        if (seljntwrklst.size() > 1) {
                            Snackbar snackbar = Snackbar.make(sv, "Maximum 1 can be selected at a time !", Snackbar.LENGTH_LONG);
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

    private void apicall1() {
        progressDialoge.show();
        String dcrno = "";
        if (Global.dcrno != null && !Global.dcrno.equalsIgnoreCase("")) {
            dcrno = Global.dcrno;
        }
        Call<NewNonFliedWrkRes> call = RetrofitClient.getInstance().getApi().getNonFieldWorkList2(Global.ecode, Global.netid, dcrno, Global.dcrdatemonth, Global.dcrdateyear, Global.dbprefix);
        call.enqueue(new Callback<NewNonFliedWrkRes>() {
            @Override
            public void onResponse(Call<NewNonFliedWrkRes> call, Response<NewNonFliedWrkRes> response) {
                progressDialoge.dismiss();
                NewNonFliedWrkRes res = response.body();
                String WWITHNAME = "";

                jntwrklist = res.getNonjointwrk();
                arealist = res.getNonwrkarea();
                if (arealist.size() == 0) {
                    addnonworking.setEnabled(false);
                }
                seljntwrklst.clear();
                if (!res.getWwith().equalsIgnoreCase("")) {

                    if (res.getWwith().equalsIgnoreCase("Indep")) {
                        JointWRKArrayList jk = new JointWRKArrayList("Indep", "INDEPENDENT");
                        seljntwrklst.add(jk);
                        indchkbox.setChecked(true);
                        jointwrkbtn.setEnabled(false);
                    } else {
                        for (int k = 0; k < jntwrklist.size(); k++) {
                            if (jntwrklist.get(k).getECODE().equalsIgnoreCase(res.getWwith())) {
                                WWITHNAME = jntwrklist.get(k).getENAME();
                            }
                        }
                        JointWRKArrayList jk = new JointWRKArrayList(res.getWwith(), WWITHNAME);
                        seljntwrklst.add(jk);
                        indchkbox.setChecked(false);
                        jointwrklist.getAdapter().notifyDataSetChanged();
                        nestedsv.setVisibility(View.VISIBLE);
                    }
                }
                for (int i = 0; i < arealist.size(); i++) {
                    String key = arealist.get(i).getTOWN() + " / " + arealist.get(i).getTOWNID();
                    arrayList.add(key);
                    String value = arealist.get(i).getTCPID() + ":" + arealist.get(i).getWTYPE();
                    workingarea.put(key, value);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(NonFieldWork.this, android.R.layout.simple_spinner_item, arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                area.setAdapter(adapter);

                if (!res.isNoRecordsExists()) {
                    newnonworktown = res.getListOfTown();
                    c1.setVisibility(View.GONE);
                    nonfieldwrkarealist.setVisibility(View.VISIBLE);
                    nonfieldwrkarealist.getAdapter().notifyDataSetChanged();
                } else {
                    c1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<NewNonFliedWrkRes> call, Throwable t) {
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

    /*private void apicall1() {
        progressDialoge.show();
        String dcrno = "";
        if(Global.dcrno != null && !Global.dcrno.equalsIgnoreCase("")){
            dcrno = Global.dcrno;
        }
        retrofit2.Call<NonFieldWrkRes> call1 = RetrofitClient
                .getInstance().getApi().getNonFieldWrk(Global.ecode,Global.netid, dcrno,Global.dcrdatemonth,Global.dcrdateyear,Global.dbprefix);
        call1.enqueue(new Callback<NonFieldWrkRes>() {
            @Override
            public void onResponse(retrofit2.Call<NonFieldWrkRes> call1, Response<NonFieldWrkRes> response) {
                NonFieldWrkRes res = response.body();
                progressDialoge.dismiss();
                jntwrklist = res.getNonjointwrk();
                arealist = res.getNonwrkarea();
                if(arealist.size() == 0){
                    addnonworking.setEnabled(false);
                }
                if(!res.isErrornonwrk()){
                    nonworking = res.getNonworking();
                    String TCPID,TOWN,DRCHCALLSFLG,ALLOWMULTIPLEAREA,WWITH,WWITHNAME = "";
                    TCPID = nonworking.get(0).getTCPID();
                    TOWN = nonworking.get(0).getTOWN();
                    DRCHCALLSFLG = nonworking.get(0).getDRCHCALLSFLG();
                    ALLOWMULTIPLEAREA = nonworking.get(0).getALLOWMULTIPLEAREA();
                    WWITH = nonworking.get(0).getWWITH();
                    seljntwrklst.clear();
                    if(!WWITH.equalsIgnoreCase("")) {

                        if (WWITH.equalsIgnoreCase("Indep")) {
                            JointWRKArrayList jk = new JointWRKArrayList("Indep", "INDEPENDENT");
                            seljntwrklst.add(jk);
                            indchkbox.setChecked(true);
                            jointwrkbtn.setEnabled(false);
                        } else {
                            for (int k = 0; k < jntwrklist.size(); k++) {
                                if (jntwrklist.get(k).getECODE().equalsIgnoreCase(WWITH)) {
                                    WWITHNAME = jntwrklist.get(k).getENAME();
                                }
                            }
                            JointWRKArrayList jk = new JointWRKArrayList(WWITH, WWITHNAME);
                            seljntwrklst.add(jk);
                            indchkbox.setChecked(false);
                            jointwrklist.getAdapter().notifyDataSetChanged();
                            nestedsv.setVisibility(View.VISIBLE);
                        }
                    }

                    c1.setVisibility(View.GONE);
                    wstat.setVisibility(View.GONE);
                    c3.setVisibility(View.VISIBLE);
                    tcpidofnonwrk = TCPID;
                    narea.setText(TCPID+"  "+TOWN);
                    ntype.setText("Non-Working");
                    nstat.setText("SELECTED");
                }

                if (!res.isErrorwrk()){
                    working = res.getWorking();
                    String TCPID,TOWN,DRCHCALLSFLG,WWITH,WWITHNAME = "";
                    TCPID = working.get(0).getTCPID();
                    TOWN = working.get(0).getTOWN();
                    DRCHCALLSFLG = working.get(0).getDRCHCALLSFLG();
                    WWITH = working.get(0).getWWITH();
                    seljntwrklst.clear();
                    if(!WWITH.equalsIgnoreCase("")) {

                        if (WWITH.equalsIgnoreCase("Indep")) {
                            JointWRKArrayList jk = new JointWRKArrayList("Indep", "INDEPENDENT");
                            seljntwrklst.add(jk);
                            indchkbox.setChecked(true);
                            jointwrkbtn.setEnabled(false);
                        } else {
                            for (int k = 0; k < jntwrklist.size(); k++) {
                                //Log.d(WWITH, jntwrklist.get(k).getECODE());
                                if (jntwrklist.get(k).getECODE().equalsIgnoreCase(WWITH)) {
                                   // Log.d("wwith name ", jntwrklist.get(k).getENAME());
                                    WWITHNAME = jntwrklist.get(k).getENAME();
                                }
                            }
                            JointWRKArrayList jk = new JointWRKArrayList(WWITH, WWITHNAME);
                            indchkbox.setChecked(false);
                            seljntwrklst.add(jk);
                            //indchkbox.setChecked(false);
                            jointwrklist.getAdapter().notifyDataSetChanged();
                            nestedsv.setVisibility(View.VISIBLE);
                        }
                    }
                    c1.setVisibility(View.GONE);
                    c2.setVisibility(View.VISIBLE);
                    warea.setText("AREA : "+TCPID+"  "+TOWN);
                    wtype.setText("TYPE : Working");
                    wstat.setText("SELECTED");
                }


                for(int i=0;i<arealist.size();i++){
                    String key = arealist.get(i).getTOWN()+" / "+arealist.get(i).getTOWNID();
                    arrayList.add(key);
                    String value = arealist.get(i).getTCPID()+":"+arealist.get(i).getWTYPE();
                    workingarea.put(key,value);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(NonFieldWork.this,  android.R.layout.simple_spinner_item, arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                area.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<NonFieldWrkRes> call1, Throwable t) {
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

    }*/

    private void deleteapi(String serial) {
        progressDialoge.show();

        retrofit2.Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().deleteNonFieldWrkEntry(Global.dcrno, serial, Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                progressDialoge.dismiss();
                if (!res.isError()) {
                    if (res.getErrormsg().equalsIgnoreCase("Updated Successfully.")) {
                        Snackbar snackbar = Snackbar.make(sv, res.getErrormsg(), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        //nonworking.clear();
                        //working.clear();
                        recreate();
                    } else if (res.getErrormsg().equalsIgnoreCase("Deleted Successfully.")) {
                        Snackbar snackbar = Snackbar.make(sv, res.getErrormsg(), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        /*seljntwrklst.clear();
                        indchkbox.setChecked(false);
                        jointwrklist.getAdapter().notifyDataSetChanged();
                        nestedsv.setVisibility(View.GONE);
                        c1.setVisibility(View.VISIBLE);
                        c2.setVisibility(View.GONE);
                        c3.setVisibility(View.GONE);*/
                        recreate();
                    } else {
                        Snackbar snackbar = Snackbar.make(sv, res.getErrormsg(), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(sv, res.getErrormsg(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(sv, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteapi(tcpidofnonwrk);
                            }
                        });
                snackbar.show();
            }
        });
    }

    public class addSelNonWrkAreaInDB extends AsyncTask<String, String, String> {
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
                url = new URL(RetrofitClient.BASE_URL + "addDcrNonFieldWrk.php");

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
                        .appendQueryParameter("ecode", params[0])
                        .appendQueryParameter("netid", params[1])
                        .appendQueryParameter("tcpid", params[2])
                        .appendQueryParameter("dcrdate", params[3])
                        .appendQueryParameter("dcrno", params[4])
                        .appendQueryParameter("wrkflag", params[5])
                        .appendQueryParameter("wwith", params[6])
                        .appendQueryParameter("DBPrefix", params[7]);

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

            progressDialoge.dismiss();
            try {
                JSONObject jobj = new JSONObject(result);

                if (!jobj.getBoolean("error")) {
                    Global.dcrno = jobj.getString("dcrno");
                    Snackbar snackbar = Snackbar.make(sv, jobj.getString("errormsg"), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    recreate();
                } else {
                    Snackbar snackbar = Snackbar.make(sv, jobj.getString("errormsg"), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setNonAdapter() {
        nonfieldwrkarealist.setNestedScrollingEnabled(false);
        nonfieldwrkarealist.setLayoutManager(new LinearLayoutManager(NonFieldWork.this));
        nonfieldwrkarealist.setAdapter(new RecyclerView.Adapter() {
                                           @NonNull
                                           @Override
                                           public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                               View view = LayoutInflater.from(NonFieldWork.this).inflate(R.layout.non_field_wrk_adapter, viewGroup, false);
                                               Holder holder = new Holder(view);
                                               return holder;
                                           }

                                           @Override
                                           public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                                               final Holder myHolder = (Holder) viewHolder;
                                               final ListOfTownItem model = newnonworktown.get(i);
                                               if (model.getSelected().equalsIgnoreCase("Y")) {
                                                   myHolder.wrkstat.setVisibility(View.VISIBLE);
                                               } else {
                                                   myHolder.wrkstat.setVisibility(View.GONE);
                                               }

                                               if (model.getDelete().equalsIgnoreCase("Y")) {
                                                   myHolder.deletenonwrk.setVisibility(View.VISIBLE);
                                               } else {
                                                   myHolder.deletenonwrk.setVisibility(View.GONE);
                                               }

                                               myHolder.area.setText(model.getTcpid() + " " + model.getArea());
                                               myHolder.type.setText(model.getType());

                                               myHolder.deletenonwrk.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       AlertDialog.Builder builder = new AlertDialog.Builder(NonFieldWork.this);
                                                       builder.setCancelable(true);
                                                       builder.setTitle("DELETE ?");
                                                       builder.setMessage("Are you sure you want to Delete ?");
                                                       builder.setPositiveButton("Yes",
                                                               new DialogInterface.OnClickListener() {
                                                                   @Override
                                                                   public void onClick(DialogInterface dialog, int which) {
                                                                       deleteapi(model.getTcpid());
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

                                           @Override
                                           public int getItemCount() {
                                               return newnonworktown.size();
                                           }

                                           class Holder extends RecyclerView.ViewHolder {
                                               TextView wrkstat, area, type;
                                               ImageButton deletenonwrk;

                                               public Holder(@NonNull View itemView) {
                                                   super(itemView);
                                                   wrkstat = itemView.findViewById(R.id.wrkstat);
                                                   deletenonwrk = itemView.findViewById(R.id.deletenonwrk);
                                                   area = itemView.findViewById(R.id.area);
                                                   type = itemView.findViewById(R.id.type);
                                               }
                                           }
                                       }
        );
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
        NonFieldWork.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
