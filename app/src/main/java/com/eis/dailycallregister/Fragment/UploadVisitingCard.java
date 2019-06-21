package com.eis.dailycallregister.Fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eis.dailycallregister.Activity.CapNUpVstCard;
import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.VisitingCardDRLstItem;
import com.eis.dailycallregister.Pojo.VstCardDrLstRes;
import com.eis.dailycallregister.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadVisitingCard extends Fragment {

    View view;
    ViewDialog progressDialoge;
    RelativeLayout sv;
    RecyclerView doctorslist;
    public List<VisitingCardDRLstItem> vstdrlst = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Visiting Card Upload");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload_visiting_card, container, false);

        progressDialoge = new ViewDialog(getActivity());
        sv = view.findViewById(R.id.sv);
        doctorslist = view.findViewById(R.id.visitcarddrlst);
        setDocLstAdapter();
        callApi();
        return view;
    }

    private void callApi() {
        progressDialoge.show();

        retrofit2.Call<VstCardDrLstRes> call1 = RetrofitClient
                .getInstance().getApi().getVstDrLstFormDB(Global.netid, Global.dbprefix);
        call1.enqueue(new Callback<VstCardDrLstRes>() {
            @Override
            public void onResponse(retrofit2.Call<VstCardDrLstRes> call1, Response<VstCardDrLstRes> response) {
                VstCardDrLstRes res = response.body();
                progressDialoge.dismiss();
                vstdrlst = res.getVisitingCardDRLst();
                doctorslist.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<VstCardDrLstRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(view, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callApi();
                            }
                        });
                snackbar.show();
                //Toast.makeText(getActivity(),"Failed !" ,Toast.LENGTH_LONG).show();
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
                                       View view = LayoutInflater.from(getActivity()).inflate(R.layout.visitingdr_adapter, viewGroup, false);
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
                                       final VisitingCardDRLstItem model = vstdrlst.get(i);
                                       myHolder.drcdndrname.setText(model.getDrcd() + " - " + model.getDrname().toUpperCase());
                                       if (model.getStatus().equalsIgnoreCase("A")) {
                                           myHolder.clickimage.setImageResource(R.drawable.ic_check_circle);
                                       } else {
                                           myHolder.clickimage.setImageResource(R.drawable.ic_linked_camera);
                                       }
                                       myHolder.itemView.setTag(i);
                                       myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(getActivity(), CapNUpVstCard.class);
                                               intent.putExtra("cntcd", model.getCntcd());
                                               intent.putExtra("drcd", model.getDrcd());
                                               intent.putExtra("status", model.getStatus());
                                               Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                                               startActivity(intent, bndlanimation);
                                           }
                                       });
                                       myHolder.clickimage.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(getActivity(), CapNUpVstCard.class);
                                               intent.putExtra("cntcd", model.getCntcd());
                                               intent.putExtra("drcd", model.getDrcd());
                                               intent.putExtra("status", model.getStatus());
                                               Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                                               startActivity(intent, bndlanimation);
                                           }
                                       });
                                   }

                                   @Override
                                   public int getItemCount() {
                                       return vstdrlst.size();
                                   }

                                   class Holder extends RecyclerView.ViewHolder {
                                       TextView drcdndrname;
                                       ImageButton clickimage;

                                       public Holder(@NonNull View itemView) {
                                           super(itemView);
                                           drcdndrname = itemView.findViewById(R.id.drcdndrname);
                                           clickimage = itemView.findViewById(R.id.clickimage);
                                       }
                                   }
                               }
        );
    }

}
