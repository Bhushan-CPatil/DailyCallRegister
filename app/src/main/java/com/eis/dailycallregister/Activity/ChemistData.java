package com.eis.dailycallregister.Activity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.JointWRKArrayList;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.AreaJntWrkRes;
import com.eis.dailycallregister.Pojo.ArealistItem;
import com.eis.dailycallregister.Pojo.CheminawItem;
import com.eis.dailycallregister.Pojo.ChemistListAWRes;
import com.eis.dailycallregister.Pojo.DCRDChemListRes;
import com.eis.dailycallregister.Pojo.DcrdchlstItem;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChemistData extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 60000;
    public static final int READ_TIMEOUT = 90000;
    Spinner area;
    AppCompatCheckBox indchkbox;
    MaterialButton jointwrkbtn, chementry;
    RadioGroup rdgrp;
    RecyclerView jointwrklist;
    public static RecyclerView chemistlist;
    ViewDialog progressDialoge;
    NestedScrollView sv;
    NestedScrollView nestedsv;
    public String field = "", finyear = "";
    int areanameid = 0;
    public List<JointWRKArrayList> seljntwrklst = new ArrayList<>();
    public List<JointwrkItem> jntwrklist = new ArrayList<>();
    public LinkedHashMap<String, String> workingarea = new LinkedHashMap<String, String>();
    public List<ArealistItem> arealist = new ArrayList<>();
    public List<String> arrayList = new ArrayList<>();
    public List<CheminawItem> chlstawlist = new ArrayList<>();
    public ArrayList<String> selchaw = new ArrayList();
    public static List<DcrdchlstItem> dcrdlst = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chemist_data);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>Chemist Data</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        progressDialoge = new ViewDialog(ChemistData.this);
        indchkbox = findViewById(R.id.indchkbox);
        jointwrkbtn = findViewById(R.id.jointwrkbtn);
        area = findViewById(R.id.areaspinn);
        rdgrp = findViewById(R.id.rdgrp);
        chementry = findViewById(R.id.chementry);
        chemistlist = findViewById(R.id.chemistlist);
        jointwrklist = findViewById(R.id.jointwrklist);
        nestedsv = findViewById(R.id.nestedsv);
        jointwrklist.setNestedScrollingEnabled(false);
        jointwrklist.setLayoutManager(new LinearLayoutManager(ChemistData.this));
        sv = findViewById(R.id.sv);
        field = Global.getFieldName(Integer.parseInt(Global.dcrdatemonth));
        finyear = Global.getFinancialYr(Global.dcrdatemonth, Global.dcrdateyear);
        dcrdlst.clear();
        independentCkbCode();
        apicall1();
        jointWorkingSetAdaper();
        jointwrkbtnOnClickListner();
        chemEntryOnClickListner();
        setDcrdChemAdapter();
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
                                        View view = LayoutInflater.from(ChemistData.this).inflate(R.layout.jointwrkemplist, viewGroup, false);
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

    private void jointwrkbtnOnClickListner() {
        jointwrkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedsv.setVisibility(View.GONE);
                seljntwrklst.clear();
                final Dialog dialog = new Dialog(ChemistData.this);
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
                rv_list_popup.setLayoutManager(new LinearLayoutManager(ChemistData.this));
                rv_list_popup.setAdapter(new RecyclerView.Adapter() {
                                             @NonNull
                                             @Override
                                             public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                                 View view = LayoutInflater.from(ChemistData.this).inflate(R.layout.jointwrkpopuplst_adapter, viewGroup, false);
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChemistData.this, android.R.layout.simple_spinner_item, arrayList);
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

    private void apicall3() {
        progressDialoge.show();

        retrofit2.Call<DCRDChemListRes> call1 = RetrofitClient
                .getInstance().getApi().getDCRDChem(Global.dcrno, Global.dbprefix);
        call1.enqueue(new Callback<DCRDChemListRes>() {
            @Override
            public void onResponse(retrofit2.Call<DCRDChemListRes> call1, Response<DCRDChemListRes> response) {
                DCRDChemListRes res = response.body();
                progressDialoge.dismiss();
                dcrdlst.clear();
                if (!res.isError()) {
                    selchaw.clear();

                    dcrdlst = res.getDcrdchlst();
                    for (int z = 0; z < dcrdlst.size(); z++) {
                        selchaw.add(dcrdlst.get(z).getCntCD());
                    }
                    chemistlist.setVisibility(View.VISIBLE);

                    chemistlist.getAdapter().notifyDataSetChanged();

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
                    Snackbar snackbar = Snackbar.make(sv, "Chemist not addded !", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<DCRDChemListRes> call1, Throwable t) {
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

    private void chemEntryOnClickListner() {
        chementry.setOnClickListener(new View.OnClickListener() {
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

    private void apicall2() {
        int selectedId = rdgrp.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String stype = "";
        if (radioButton.getText().toString().equalsIgnoreCase("Area Wise")) {
            stype = "aw";
        } else if (radioButton.getText().toString().equalsIgnoreCase("Wildcard Search")) {
            stype = "wc";
        } else {
            stype = "al";
        }
        progressDialoge.show();
        String selitem = area.getSelectedItem().toString().trim();
        String valuefrmhm = workingarea.get(selitem);
        String[] valspt = valuefrmhm.split(":");
        Global.tcpid = valspt[0];
        Global.wrktype = valspt[1];

        retrofit2.Call<ChemistListAWRes> call1 = RetrofitClient
                .getInstance().getApi().getChemistDataList(Global.ecode, Global.netid, Global.tcpid, Global.dcrdate, Global.dcrdatemonth, Global.dcrdateyear, stype, Global.dbprefix);
        call1.enqueue(new Callback<ChemistListAWRes>() {
            @Override
            public void onResponse(retrofit2.Call<ChemistListAWRes> call1, Response<ChemistListAWRes> response) {
                progressDialoge.dismiss();
                ChemistListAWRes res = response.body();
                if (!res.isError()) {
                    chlstawlist = res.getCheminaw();
                    //Log.d("list size-->", Integer.toString(dclstawlist.size()));
                    showPopup();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChemistData.this);
                    builder.setCancelable(true);
                    //builder.setTitle("LOGOUT ?");
                    builder.setMessage(res.getErrormsg());
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
            public void onFailure(Call<ChemistListAWRes> call1, Throwable t) {
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
        final Dialog dialog = new Dialog(ChemistData.this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.jointwrklstpopup);
        TextView heading = dialog.findViewById(R.id.heading);
        heading.setText("Select Chemist");
        CardView cancelbtn = dialog.findViewById(R.id.no);
        CardView submitbtn = dialog.findViewById(R.id.yes);
        RecyclerView rv_list_popup = dialog.findViewById(R.id.jointwrkpopuplist);
        rv_list_popup.setNestedScrollingEnabled(false);
        rv_list_popup.setLayoutManager(new LinearLayoutManager(ChemistData.this));
        rv_list_popup.setAdapter(new RecyclerView.Adapter() {
                                     @NonNull
                                     @Override
                                     public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                         View view = LayoutInflater.from(ChemistData.this).inflate(R.layout.jointwrkpopuplst_adapter, viewGroup, false);
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
                                         final CheminawItem model = chlstawlist.get(i);
                                         myHolder.drname.setText(model.getSTCD() + " - " + model.getSTNAME());
                                         if (selchaw.size() > 0 && selchaw.contains(model.getCNTCD())) {
                                             myHolder.ckb.setChecked(true);
                                         }
                                         myHolder.ckb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                             @Override
                                             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                 if (isChecked) {
                                                     selchaw.add(model.getCNTCD());
                                                 } else {
                                                     selchaw.remove(model.getCNTCD());
                                                 }
                                             }
                                         });

                                     }

                                     @Override
                                     public int getItemCount() {
                                         return chlstawlist.size();
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
                saveSelChemInDB();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    public void setDcrdChemAdapter() {
        chemistlist.setNestedScrollingEnabled(false);
        chemistlist.setLayoutManager(new LinearLayoutManager(ChemistData.this));
        chemistlist.setAdapter(new RecyclerView.Adapter() {
                                   @NonNull
                                   @Override
                                   public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                       View view = LayoutInflater.from(ChemistData.this).inflate(R.layout.chem_det_chem_list_adapter, viewGroup, false);
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
                                       final DcrdchlstItem model = dcrdlst.get(i);
                                       myHolder.stname.setText(model.getStname());

                                       myHolder.deletechem.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               showconfirmationdialog(model.getSerial());
                                           }
                                       });

                                       myHolder.giftentry.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(ChemistData.this, ChemDCRGift.class);
                                               intent.putExtra("serial", "CH" + model.getSerial());
                                               intent.putExtra("oserial", model.getSerial());
                                               intent.putExtra("cntcd", model.getCntCD());
                                               intent.putExtra("wnetid", model.getWNetID());
                                               intent.putExtra("pob", model.getPOB());
                                               intent.putExtra("position", Integer.toString(i));
                                               intent.putExtra("chname", "Name - " + model.getStname());
                                               Bundle bndlanimation = ActivityOptions.makeCustomAnimation(ChemistData.this, R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                                               startActivity(intent, bndlanimation);
                                           }
                                       });

                                   }

                                   @Override
                                   public int getItemCount() {
                                       return dcrdlst.size();
                                   }

                                   class Holder extends RecyclerView.ViewHolder {
                                       TextView stname;
                                       ImageButton giftentry, deletechem;

                                       public Holder(@NonNull View itemView) {
                                           super(itemView);
                                           stname = itemView.findViewById(R.id.stname);
                                           giftentry = itemView.findViewById(R.id.giftentry);
                                           deletechem = itemView.findViewById(R.id.deletechem);
                                       }
                                   }
                               }
        );
    }

    private void saveSelChemInDB() {
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

        String tempdcrno = "";
        if (Global.dcrno == null)
            tempdcrno = "";
        else
            tempdcrno = Global.dcrno;

        new ChemistData.addSelChemInDB().execute(Global.ecode, Global.netid, Global.tcpid, Global.dcrdate, Global.dcrdatemonth,
                Global.dcrdateyear, tempdcrno, Global.wrktype, ww0, ww1, ww2, ww3, selchaw.toString(), Global.dbprefix);

    }

    public class addSelChemInDB extends AsyncTask<String, String, String> {
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
                url = new URL(RetrofitClient.BASE_URL + "chemistdataaddsel.php");

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
                    Snackbar snackbar = Snackbar.make(sv, "Chemist's added successfully.", Snackbar.LENGTH_LONG);
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

    private void showconfirmationdialog(final String drserial) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete ?");
        builder.setMessage("Are you sure you want to delete ?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ChemistData.deleteDRfromDCR().execute(Global.ecode, Global.netid, Global.tcpid, Global.dcrno, drserial, finyear, Global.dcrdate, Global.emplevel, field, Global.dbprefix);
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

                /*if(!jobj.getBoolean("error") && jobj.getString("errormsg").equalsIgnoreCase("deleted")) {
                    Toast.makeText(ChemistData.this,"Chemist deleted successfully",Toast.LENGTH_LONG).show();
                    apicall3();
                }*/
                if (!jobj.getBoolean("error") && jobj.getString("errormsg").equalsIgnoreCase("Y")) {
                    Toast.makeText(ChemistData.this, "Chemist deleted successfully", Toast.LENGTH_LONG).show();
                    dcrdlst.clear();
                    selchaw.clear();
                    chemistlist.getAdapter().notifyDataSetChanged();
                    Global.dcrno = null;
                    //recreate();
                } else if (!jobj.getBoolean("error") && jobj.getString("errormsg").equalsIgnoreCase("N")) {
                    Toast.makeText(ChemistData.this, "Chemist deleted successfully", Toast.LENGTH_LONG).show();
                    dcrdlst.clear();
                    selchaw.clear();
                    chemistlist.getAdapter().notifyDataSetChanged();
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
        ChemistData.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
