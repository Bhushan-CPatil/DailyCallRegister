package com.eis.dailycallregister.Fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.eis.dailycallregister.Activity.VisitPlanSummary;
import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.VSTPSUMDOCItem;
import com.eis.dailycallregister.Pojo.VstPlnDocLstRes;
import com.eis.dailycallregister.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitPlanDocLst extends Fragment {

    TextView all, mthyr;
    AppCompatSpinner day;
    RecyclerView doctorslist;
    View view;
    ViewDialog progressDialoge;
    List<VSTPSUMDOCItem> fulldoclst = new ArrayList<>();
    String mdate;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Visit Plan Summary");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_visit_plan_doc_lst, container, false);
        all = view.findViewById(R.id.all);
        day = view.findViewById(R.id.day);
        mthyr = view.findViewById(R.id.mthyr);
        doctorslist = view.findViewById(R.id.doctorslist);
        progressDialoge = new ViewDialog(getActivity());
        setDocLstAdapter();
        final String[] xyz = Global.date.split("-");
        Calendar mycal = new GregorianCalendar(Integer.parseInt(xyz[0]), Integer.parseInt(xyz[1]) - 1, 1);
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
        List<String> days = new ArrayList<>();
        for (int z = 1; z <= daysInMonth; z++) {
            days.add(Integer.toString(z));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        day.setAdapter(adapter);
        day.setSelection(adapter.getPosition(Integer.toString(Integer.parseInt(xyz[2]))));
        mthyr.setText(" / " + xyz[1] + " / " + xyz[0]);
        /*String newday = day.getSelectedItem().toString().trim().length() != 2 ? "0" + day.getSelectedItem().toString().trim() : day.getSelectedItem().toString().trim();
        mdate = xyz[0] + "/" + xyz[1] + "/" + newday;
        getDRList(mdate);*/

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fulldoclst.size()>0) {
                    Intent intent = new Intent(getActivity(), VisitPlanSummary.class);
                    intent.putExtra("mode", "ALL");
                    intent.putExtra("cntcd", "");
                    intent.putExtra("date", mdate);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                    startActivity(intent, bndlanimation);
                }
                else{
                    Toast.makeText(getActivity(), "Doctors not Found !", Toast.LENGTH_LONG).show();
                }
            }
        });

        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //if(chemonchangeacc){
                String newday = day.getSelectedItem().toString().trim().length() != 2 ? "0" + day.getSelectedItem().toString().trim() : day.getSelectedItem().toString().trim();
                mdate = xyz[0] + "/" + xyz[1] + "/" + newday;
                getDRList(mdate);
                //}
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        return view;
    }

    private void getDRList(String newdate) {
        doctorslist.setVisibility(View.GONE);
        fulldoclst.clear();
        progressDialoge.show();
        retrofit2.Call<VstPlnDocLstRes> call = RetrofitClient.getInstance().getApi().getVisitPlanDocList(Global.netid, newdate, Global.dbprefix);
        call.enqueue(new Callback<VstPlnDocLstRes>() {
            @Override
            public void onResponse(Call<VstPlnDocLstRes> call, Response<VstPlnDocLstRes> response) {
                progressDialoge.dismiss();
                VstPlnDocLstRes res = response.body();
                if (!res.isError()) {
                    fulldoclst = res.getVSTPSUMDOC();
                    doctorslist.setVisibility(View.VISIBLE);
                    doctorslist.getAdapter().notifyDataSetChanged();
                    doctorslist.smoothScrollToPosition(0);
                } else {
                    Toast.makeText(getActivity(), res.getErrormsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VstPlnDocLstRes> call, Throwable t) {
                progressDialoge.dismiss();
                Toast.makeText(getActivity(), "Failed to process your request !", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setDocLstAdapter() {
        doctorslist.setNestedScrollingEnabled(false);
        doctorslist.setLayoutManager(new LinearLayoutManager(getActivity()));
        doctorslist.setAdapter(new RecyclerView.Adapter() {
                                   @NonNull
                                   @Override
                                   public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                       View view = LayoutInflater.from(getActivity()).inflate(R.layout.vst_dr_adapter, viewGroup, false);
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
                                       final VSTPSUMDOCItem model = fulldoclst.get(i);
                                       myHolder.drcdndrname.setText(model.getDrcd() + " - " + model.getDrname().toUpperCase());
                                       myHolder.itemView.setTag(i);
                                       myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(getActivity(), VisitPlanSummary.class);
                                               intent.putExtra("mode", "SEL");
                                               intent.putExtra("cntcd", model.getCntcd());
                                               intent.putExtra("date", mdate);
                                               Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                                               startActivity(intent, bndlanimation);
                                           }
                                       });
                                   }

                                   @Override
                                   public int getItemCount() {
                                       return fulldoclst.size();
                                   }

                                   class Holder extends RecyclerView.ViewHolder {
                                       TextView drcdndrname;

                                       public Holder(@NonNull View itemView) {
                                           super(itemView);
                                           drcdndrname = itemView.findViewById(R.id.drcdndrname);
                                       }
                                   }
                               }
        );
    }
}
