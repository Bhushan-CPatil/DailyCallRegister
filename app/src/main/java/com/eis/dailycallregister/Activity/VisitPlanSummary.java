package com.eis.dailycallregister.Activity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.eis.dailycallregister.Activity.HomeActivity;
import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.VSTPSUMItem;
import com.eis.dailycallregister.Pojo.VstPlnSumRes;
import com.eis.dailycallregister.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VisitPlanSummary extends AppCompatActivity {

    TextView date,seldate;
    //View view;
    public RelativeLayout rtl;
    ViewDialog progressDialoge;
    RecyclerView tourlist;
    String prevfinyr="",finyr="";
    String[] loggedindate;
    List<VSTPSUMItem> fullsumm = new ArrayList<>();
    String mdate,cntcd="",mode;
    String gbdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_visit_plan_summary);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>Visit Plan Summary</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        mode = getIntent().getStringExtra("mode");
        if(mode.equalsIgnoreCase("SEL")){
            cntcd = getIntent().getStringExtra("cntcd");
        }
        mdate = getIntent().getStringExtra("date");
        rtl = findViewById(R.id.rtl);
        date = findViewById(R.id.date);
        seldate = findViewById(R.id.seldate);
        tourlist = findViewById(R.id.tourlist);
        progressDialoge = new ViewDialog(VisitPlanSummary.this);
        setAdapter();

        loggedindate = mdate.split("/");
        finyr = Global.getFinancialYr(loggedindate[1], loggedindate[0]);
        int prevyr = Integer.parseInt(loggedindate[0]) - 1;
        prevfinyr = Global.getFinancialYr(loggedindate[1], Integer.toString(prevyr));

        gbdate = loggedindate[0]+"/"+loggedindate[1]+"/"+loggedindate[2];
        getSummary(gbdate,finyr,prevfinyr);

        seldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDatePopup(gbdate);
            }
        });
    }

    private void setAdapter() {
        tourlist.setNestedScrollingEnabled(false);
        tourlist.setLayoutManager(new LinearLayoutManager(VisitPlanSummary.this));
        tourlist.setAdapter(new RecyclerView.Adapter() {
                                @NonNull
                                @Override
                                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                    View view = LayoutInflater.from(VisitPlanSummary.this).inflate(R.layout.visit_plan_summ_adapter, viewGroup, false);
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
                                    final VSTPSUMItem model = fullsumm.get(i);
                                    myHolder.drname.setText("DR Name - "+model.getDrname());
                                    myHolder.svlno.setText(model.getDrcd());
                                    String[] xda = model.getLatesprec().split("~");
                                    myHolder.latesprec.setText(Html.fromHtml(xda[0]));
                                    myHolder.drremark.setText(xda[1]);
                                    myHolder.PD1.setText(model.getMth1());
                                    myHolder.PD2.setText(model.getMth2());
                                    myHolder.PD3.setText(model.getMth3());
                                    myHolder.VPD1.setText(model.getPD1());
                                    myHolder.VPD2.setText(model.getPD2());
                                    myHolder.VPD3.setText(model.getPD3());
                                    myHolder.AD1.setText(model.getMth1());
                                    myHolder.AD2.setText(model.getMth2());
                                    myHolder.AD3.setText(model.getMth3());
                                    myHolder.VAD1.setText(model.getAD1());
                                    myHolder.VAD2.setText(model.getAD2());
                                    myHolder.VAD3.setText(model.getAD3());
                                    myHolder.RD1.setText(model.getMth1());
                                    myHolder.RD2.setText(model.getMth2());
                                    myHolder.RD3.setText(model.getMth3());
                                    myHolder.VRD1.setText(model.getRD1());
                                    myHolder.VRD2.setText(model.getRD2());
                                    myHolder.VRD3.setText(model.getRD3());
                                    myHolder.PC1.setText(model.getMth1());
                                    myHolder.PC2.setText(model.getMth2());
                                    myHolder.PC3.setText(model.getMth3());
                                    myHolder.VPC1.setText(model.getPC1());
                                    myHolder.VPC2.setText(model.getPC2());
                                    myHolder.VPC3.setText(model.getPC3());
                                    myHolder.AC1.setText(model.getMth1());
                                    myHolder.AC2.setText(model.getMth2());
                                    myHolder.AC3.setText(model.getMth3());
                                    myHolder.VAC1.setText(model.getAC1());
                                    myHolder.VAC2.setText(model.getAC2());
                                    myHolder.VAC3.setText(model.getAC3());
                                    myHolder.RC1.setText(model.getMth1());
                                    myHolder.RC2.setText(model.getMth2());
                                    myHolder.RC3.setText(model.getMth3());
                                    myHolder.VRC1.setText(model.getRC1());
                                    myHolder.VRC2.setText(model.getRC2());
                                    myHolder.VRC3.setText(model.getRC3());
                                    myHolder.pulsechem.setText(model.getStname());
                                    myHolder.noofpatient.setText(model.getNoofpatient());
                                    myHolder.fee.setText(model.getFees());
                                    myHolder.noofwrk.setText(model.getNoofwrkdays());
                                    myHolder.result.setText(model.getResult());
                                    myHolder.conday.setText(model.getConday());
                                    myHolder.contime.setText(model.getContime());
                                }

                                @Override
                                public int getItemCount() {
                                    return fullsumm.size();
                                }

                                class Holder extends RecyclerView.ViewHolder {
                                    TextView drname,svlno,latesprec,PD1,PD2,PD3,VPD1,VPD2,VPD3,AD1,AD2,AD3,VAD1,VAD2,VAD3,drremark;
                                    TextView RD1,RD2,RD3,VRD1,VRD2,VRD3,PC1,PC2,PC3,VPC1,VPC2,VPC3,AC1,AC2,AC3,VAC1,VAC2,VAC3;
                                    TextView RC1,RC2,RC3,VRC1,VRC2,VRC3,pulsechem,noofpatient,fee,noofwrk,result,conday,contime;

                                    public Holder(@NonNull View itemView) {
                                        super(itemView);
                                        drname = itemView.findViewById(R.id.drname);
                                        svlno = itemView.findViewById(R.id.svlno);
                                        latesprec = itemView.findViewById(R.id.latesprec);
                                        drremark = itemView.findViewById(R.id.drremark);
                                        PD1 = itemView.findViewById(R.id.PD1);
                                        PD2 = itemView.findViewById(R.id.PD2);
                                        PD3 = itemView.findViewById(R.id.PD3);
                                        VPD1 = itemView.findViewById(R.id.VPD1);
                                        VPD2 = itemView.findViewById(R.id.VPD2);
                                        VPD3 = itemView.findViewById(R.id.VPD3);
                                        AD1 = itemView.findViewById(R.id.AD1);
                                        AD2 = itemView.findViewById(R.id.AD2);
                                        AD3 = itemView.findViewById(R.id.AD3);
                                        VAD1 = itemView.findViewById(R.id.VAD1);
                                        VAD2 = itemView.findViewById(R.id.VAD2);
                                        VAD3 = itemView.findViewById(R.id.VAD3);
                                        RD1 = itemView.findViewById(R.id.RD1);
                                        RD2 = itemView.findViewById(R.id.RD2);
                                        RD3 = itemView.findViewById(R.id.RD3);
                                        VRD1 = itemView.findViewById(R.id.VRD1);
                                        VRD2 = itemView.findViewById(R.id.VRD2);
                                        VRD3 = itemView.findViewById(R.id.VRD3);
                                        PC1 = itemView.findViewById(R.id.PC1);
                                        PC2 = itemView.findViewById(R.id.PC2);
                                        PC3 = itemView.findViewById(R.id.PC3);
                                        VPC1 = itemView.findViewById(R.id.VPC1);
                                        VPC2 = itemView.findViewById(R.id.VPC2);
                                        VPC3 = itemView.findViewById(R.id.VPC3);
                                        AC1 = itemView.findViewById(R.id.AC1);
                                        AC2 = itemView.findViewById(R.id.AC2);
                                        AC3 = itemView.findViewById(R.id.AC3);
                                        VAC1 = itemView.findViewById(R.id.VAC1);
                                        VAC2 = itemView.findViewById(R.id.VAC2);
                                        VAC3 = itemView.findViewById(R.id.VAC3);
                                        RC1 = itemView.findViewById(R.id.RC1);
                                        RC2 = itemView.findViewById(R.id.RC2);
                                        RC3 = itemView.findViewById(R.id.RC3);
                                        VRC1 = itemView.findViewById(R.id.VRC1);
                                        VRC2 = itemView.findViewById(R.id.VRC2);
                                        VRC3 = itemView.findViewById(R.id.VRC3);
                                        pulsechem = itemView.findViewById(R.id.pulsechem);
                                        noofpatient = itemView.findViewById(R.id.noofpatient);
                                        fee = itemView.findViewById(R.id.fee);
                                        noofwrk = itemView.findViewById(R.id.noofwrk);
                                        result = itemView.findViewById(R.id.result);
                                        conday = itemView.findViewById(R.id.conday);
                                        contime = itemView.findViewById(R.id.contime);
                                    }
                                }
                            }
        );
    }

    private void showSelectDatePopup(String gbldate) {
        final String[] xyz = gbldate.split("/");
        Calendar mycal = new GregorianCalendar(Integer.parseInt(xyz[0]), Integer.parseInt(xyz[1])-1, 1);
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<String> days = new ArrayList<>();
        for(int z=1;z<=daysInMonth;z++){
            days.add(Integer.toString(z));
        }

        final Dialog dialog = new Dialog(VisitPlanSummary.this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.date_selection_popup);

        CardView home = dialog.findViewById(R.id.home);
        CardView next = dialog.findViewById(R.id.next);
        final AppCompatSpinner wday = dialog.findViewById(R.id.day);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(VisitPlanSummary.this, android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wday.setAdapter(adapter);

        TextView wmthyr = dialog.findViewById(R.id.mthyr);
        wmthyr.setText(" / "+xyz[1]+" / "+xyz[0]);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(VisitPlanSummary.this, HomeActivity.class);
                intent.putExtra("openfrag", "home");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(VisitPlanSummary.this, R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                startActivity(intent, bndlanimation);
                finish();*/
                onBackPressed();
                dialog.dismiss();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newday = wday.getSelectedItem().toString().trim().length() != 2 ? "0"+wday.getSelectedItem().toString().trim() : wday.getSelectedItem().toString().trim();
                String newdate = xyz[0]+"/"+xyz[1]+"/"+newday;
                getSummary(newdate,finyr,prevfinyr);
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

    private void getSummary(String newdate, String finyr, String prevfinyr) {
        date.setText("Tour Date - "+newdate);
        gbdate = newdate;
        tourlist.setVisibility(View.GONE);
        fullsumm.clear();
        progressDialoge.show();
        retrofit2.Call<VstPlnSumRes> call = RetrofitClient.getInstance().getApi().getVisitPlanSummary(Global.netid,prevfinyr,finyr,newdate, mode, cntcd,Global.dbprefix);
        call.enqueue(new Callback<VstPlnSumRes>() {
            @Override
            public void onResponse(Call<VstPlnSumRes> call, Response<VstPlnSumRes> response) {
                progressDialoge.dismiss();
                VstPlnSumRes res = response.body();
                if(!res.isError()){
                    fullsumm = res.getVSTPSUM();
                    tourlist.setVisibility(View.VISIBLE);
                    tourlist.getAdapter().notifyDataSetChanged();
                    tourlist.smoothScrollToPosition(0);
                }else{
                    Toast.makeText(VisitPlanSummary.this, res.getErrormsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VstPlnSumRes> call, Throwable t) {
                progressDialoge.dismiss();
                Toast.makeText(VisitPlanSummary.this, "Failed to process your request !", Toast.LENGTH_LONG).show();
            }
        });
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
        VisitPlanSummary.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
