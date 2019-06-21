package com.eis.dailycallregister.Fragment;

import android.app.ActivityOptions;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.adaptivetablelayout.AdaptiveTableLayout;
import com.cleveroad.adaptivetablelayout.LinkedAdaptiveTableAdapter;
import com.cleveroad.adaptivetablelayout.OnItemClickListener;
import com.cleveroad.adaptivetablelayout.OnItemLongClickListener;
import com.eis.dailycallregister.Activity.DoctorsData;
import com.eis.dailycallregister.Activity.HomeActivity;
import com.eis.dailycallregister.Activity.LoginScreen;
import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.SampleLinkedTableAdapter;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.DefaultResponse;
import com.eis.dailycallregister.Pojo.EDITMTPTCPIDNTOWNSItem;
import com.eis.dailycallregister.Pojo.EditMtpFormResponse;
import com.eis.dailycallregister.Pojo.MtppendlistItem;
import com.eis.dailycallregister.Pojo.MtptownlistItem;
import com.eis.dailycallregister.Pojo.NewMTPListOfMTHRes;
import com.eis.dailycallregister.Pojo.NextMTPListRes;
import com.eis.dailycallregister.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MTPConfirmation extends Fragment {

    View view;
    public RelativeLayout rtl;
    public TextView confirm,alconf;
    ViewDialog progressDialoge;
    //AdaptiveTableLayout mTableLayout;
    RecyclerView mtptownlist;
    public List<MtptownlistItem> mtplst = new ArrayList();
    public List<EDITMTPTCPIDNTOWNSItem> alltownlist = new ArrayList();
    //public List<MtppendlistItem> mtplst = new ArrayList();
    public LinearLayout llt;
    public ArrayList<String> towns = new ArrayList<>();
    public HashMap<String,String> towntcpid = new HashMap<>();
    //String[][] mtp;
    boolean mtpconfirmed;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("MTP");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Global.whichmth = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_mtpconfirmation, container, false);
        rtl = view.findViewById(R.id.rtl);
        llt = view.findViewById(R.id.llt);
        confirm = view.findViewById(R.id.confirm);
        alconf = view.findViewById(R.id.alconf);
        mtptownlist = view.findViewById(R.id.mtptownlist);
        progressDialoge = new ViewDialog(getActivity());
        //mTableLayout = view.findViewById(R.id.mtptable);
        setMtpLstAdapter();
        confirm.setVisibility(View.GONE);
        confirm.setEnabled(false);
        alconf.setVisibility(View.GONE);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Confirm ?");
                builder.setMessage("Are you sure want to confirm ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confmtp();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //callapi();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String datex = new SimpleDateFormat("yyyy-MM-24", Locale.getDefault()).format(new Date());
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = dateFormat.parse(datex);
            date2 = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        calendar1.setTime(date1);
        calendar2.setTime(date2);
        //Toast.makeText(getActivity(),datex +"///"+ date , Toast.LENGTH_LONG).show();
        if(calendar2.compareTo(calendar1) < 0){
            Global.whichmth = "CURRENT";
            callnewapi();
        }else{
            //Toast.makeText(getActivity(), "show MTP", Toast.LENGTH_LONG).show();
            if(Global.whichmth == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Alert ?");
                builder.setMessage("Which month of MTP you wants to view ?");
                builder.setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Global.whichmth = "NEXT";
                        callnewapi();
                    }
                });
                builder.setNeutralButton("CURRENT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //new Global().notAllowed(getActivity());
                        Global.whichmth = "CURRENT";
                        callnewapi();
                    }
                });
                AlertDialog dialog2 = builder.create();
                dialog2.show();
            }else{
                callnewapi();
            }
        }
        return view;
    }

    private void confmtp() {
        String[] date = mtplst.get(2).getWORKDATE().split("/");
        progressDialoge.show();
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().confirmMTP(Global.ecode,Global.netid,date[2],date[1],Global.dbprefix);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                progressDialoge.dismiss();
                DefaultResponse res =response.body();
                if(!res.isError()){
                    if(res.getErrormsg().equalsIgnoreCase("Successfully Confirmed.")){
                        confirm.setEnabled(false);
                        confirm.setVisibility(View.GONE);
                        alconf.setText(res.getErrormsg());
                        alconf.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressDialoge.dismiss();
                Snackbar.make(rtl,"Failed confirm MTP !", Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confmtp();
                    }
                }).show();
            }
        });
    }

    /*private void callapi() {
        String[] logdate = Global.date.split("-");
        progressDialoge.show();
        Call<NextMTPListRes> call = RetrofitClient.getInstance().getApi().nextMthMTPConf(Global.ecode,Global.netid,logdate[0],logdate[1],Global.dbprefix);
        call.enqueue(new Callback<NextMTPListRes>() {
            @Override
            public void onResponse(Call<NextMTPListRes> call, Response<NextMTPListRes> response) {
                progressDialoge.dismiss();
                NextMTPListRes res = response.body();
                if(!res.isError()){
                    if(res.isConfirmed()){
                        alconf.setText(res.getErrormsg());
                        alconf.setVisibility(View.VISIBLE);
                        mtplst = res.getMtppendlist();
                        mtp = new String[mtplst.size()][5];
                        for (int i = 0; i < mtplst.size(); i++) {
                            MtppendlistItem temp = mtplst.get(i);
                            mtp[i][0] = temp.getTOWN();
                            mtp[i][1] = temp.getWORKDATE();
                            mtp[i][2] = temp.getOBJECTIVE();
                            mtp[i][3] = temp.getMRNETID();
                            mtp[i][4] = temp.getJOINTWORKING();
                        }

                        if(mtplst.size()>1){
                            LinkedAdaptiveTableAdapter mTableAdapter = new SampleLinkedTableAdapter(getActivity(), mtp, "3");
                            mTableAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(int row, int column) {
                                    dialogCloseType(getActivity(), mtp[row][column]);
                                }

                                @Override
                                public void onRowHeaderClick(int row) {
                                    dialogCloseType(getActivity(), mtp[row][0]);
                                }

                                @Override
                                public void onColumnHeaderClick(int column) {
                                    dialogCloseType(getActivity(), mtp[0][column]);
                                }

                                @Override
                                public void onLeftTopHeaderClick() {
                                    dialogCloseType(getActivity(), mtp[0][0]);
                                }
                            });
                            mTableAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                                @Override
                                public void onItemLongClick(int row, int column) {

                                }

                                @Override
                                public void onLeftTopHeaderLongClick() {

                                }
                            });
                            mTableLayout.setAdapter(mTableAdapter);
                            mTableAdapter.notifyDataSetChanged();
                        }else{
                            llt.setVisibility(View.GONE);
                        }
                    }else{
                        confirm.setText(res.getErrormsg());
                        confirm.setVisibility(View.VISIBLE);
                        confirm.setEnabled(true);
                        mtplst = res.getMtppendlist();
                        mtplst = res.getMtppendlist();
                        mtp = new String[mtplst.size()][5];
                        for (int i = 0; i < mtplst.size(); i++) {
                            MtppendlistItem temp = mtplst.get(i);
                            mtp[i][0] = temp.getTOWN();
                            mtp[i][1] = temp.getWORKDATE();
                            mtp[i][2] = temp.getOBJECTIVE();
                            mtp[i][3] = temp.getMRNETID();
                            mtp[i][4] = temp.getJOINTWORKING();
                        }

                        if(mtplst.size()>1){
                            LinkedAdaptiveTableAdapter mTableAdapter = new SampleLinkedTableAdapter(getActivity(), mtp, "3");
                            mTableAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(int row, int column) {
                                    dialogCloseType(getActivity(), mtp[row][column]);
                                }

                                @Override
                                public void onRowHeaderClick(int row) {
                                    dialogCloseType(getActivity(), mtp[row][0]);
                                }

                                @Override
                                public void onColumnHeaderClick(int column) {
                                    dialogCloseType(getActivity(), mtp[0][column]);
                                }

                                @Override
                                public void onLeftTopHeaderClick() {
                                    dialogCloseType(getActivity(), mtp[0][0]);
                                }
                            });
                            mTableAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                                @Override
                                public void onItemLongClick(int row, int column) {

                                }

                                @Override
                                public void onLeftTopHeaderLongClick() {

                                }
                            });
                            mTableLayout.setAdapter(mTableAdapter);
                            mTableAdapter.notifyDataSetChanged();
                        }else{
                            llt.setVisibility(View.GONE);
                        }
                    }
                }else{
                    alconf.setText(res.getErrormsg());
                    alconf.setVisibility(View.VISIBLE);
                    llt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<NextMTPListRes> call, Throwable t) {
                progressDialoge.dismiss();
                Snackbar.make(rtl,"Failed to get MTP details !", Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callapi();
                    }
                }).show();
            }
        });
    }*/

    /*public void dialogCloseType(final Context context, String stringmsg) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_edit_item);
        TextView textView = dialog.findViewById(R.id.tvTitle);
        AppCompatButton bPositive = dialog.findViewById(R.id.bPositive);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(stringmsg);
        bPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }*/

    private void callnewapi() {
        String[] logdate = Global.date.split("-");
        progressDialoge.show();
        Call<NewMTPListOfMTHRes> call = RetrofitClient.getInstance().getApi().getMTPListOfMth(Global.ecode,Global.netid,logdate[0],logdate[1], Global.whichmth,Global.dbprefix);
        call.enqueue(new Callback<NewMTPListOfMTHRes>() {
            @Override
            public void onResponse(Call<NewMTPListOfMTHRes> call, Response<NewMTPListOfMTHRes> response) {
                progressDialoge.dismiss();
                NewMTPListOfMTHRes res = response.body();
                if(!res.isError()){
                    if(res.isConfirmed()){
                        mtpconfirmed = res.isConfirmed();
                        alconf.setText(res.getErrormsg());
                        alconf.setVisibility(View.VISIBLE);
                        mtplst = res.getMtptownlist();

                        if(mtplst.size()>1){
                            mtptownlist.getAdapter().notifyDataSetChanged();
                        }else{
                            llt.setVisibility(View.GONE);
                        }
                    }else{
                        mtpconfirmed = res.isConfirmed();
                        confirm.setText(res.getErrormsg());
                        confirm.setVisibility(View.VISIBLE);
                        confirm.setEnabled(true);
                        mtplst = res.getMtptownlist();

                        if(mtplst.size()>1){
                            mtptownlist.getAdapter().notifyDataSetChanged();
                        }else{
                            llt.setVisibility(View.GONE);
                        }
                    }
                }else{
                    alconf.setText(res.getErrormsg());
                    alconf.setVisibility(View.VISIBLE);
                    llt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<NewMTPListOfMTHRes> call, Throwable t) {
                progressDialoge.dismiss();
                Snackbar.make(rtl,"Failed to get MTP details !", Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callnewapi();
                    }
                }).show();
            }
        });
    }

    public void setMtpLstAdapter(){
        mtptownlist.setNestedScrollingEnabled(true);
        mtptownlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        mtptownlist.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view=LayoutInflater.from(getActivity()).inflate(R.layout.mtp_list_adapter, viewGroup,false);
                Holder holder=new Holder(view);
                return holder;
            }
            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                final Holder myHolder= (Holder) viewHolder;
                final MtptownlistItem model = mtplst.get(i);
                if(model.getAprvcode().length()>0) {
                    myHolder.town.setText(model.getTOWN());
                    myHolder.town.setTextColor(Color.parseColor("#4D4D4D"));
                }else {
                    myHolder.town.setText(model.getTOWN()+" *");
                    myHolder.town.setTextColor(Color.parseColor("#FF5555"));
                }

                myHolder.wdate.setText(model.getWORKDATE());
                myHolder.objective.setText(model.getOBJECTIVE());
                myHolder.jointwrk.setText(model.getJOINTWORKING());
                myHolder.orgtown.setText(model.getORGTOWN());
                if(!mtpconfirmed){
                    myHolder.operation.setVisibility(View.VISIBLE);
                }else{
                    myHolder.operation.setVisibility(View.GONE);
                }

                myHolder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(true);
                        builder.setTitle("Alert ?");
                        builder.setMessage("Are you sure you want to Edit MTP of date "+model.getWORKDATE()+" ?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getAllMtpTowns(model.getTCPID(),model.getWORKDATE(),i, model.getTOWN(),model.getOBJECTIVE(), model.getJOINTWORKING());
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });

                myHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(true);
                        builder.setTitle("Alert ?");
                        builder.setMessage("Are you sure you want to Delete MTP of date "+model.getWORKDATE()+" ?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteEntry(model.getTCPID(),model.getWORKDATE(),i);
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return mtplst.size();
            }
            class Holder extends RecyclerView.ViewHolder {
                TextView objective,town,wdate,jointwrk,orgtown;
                ImageButton delete,edit;
                LinearLayout operation;
                public Holder(@NonNull View itemView) {
                    super(itemView);
                    operation = itemView.findViewById(R.id.operation);
                    objective = itemView.findViewById(R.id.objective);
                    town = itemView.findViewById(R.id.town);
                    wdate = itemView.findViewById(R.id.wdate);
                    jointwrk = itemView.findViewById(R.id.jointwrk);
                    orgtown = itemView.findViewById(R.id.orgtown);
                    delete = itemView.findViewById(R.id.delete);
                    edit = itemView.findViewById(R.id.edit);
                }
            }
        }
        );
    }

    private void getAllMtpTowns(final String tcpid, final String workdate, final int position, final String townname, final String objective, final String jointwrk) {
        String[] date = workdate.split("/");
        String newdate = date[2]+"-"+date[1]+"-"+date[0];
        progressDialoge.show();
        Call<EditMtpFormResponse> call = RetrofitClient.getInstance().getApi().editMTPEntry(Global.ecode,Global.netid,tcpid,newdate,Global.dbprefix);
        call.enqueue(new Callback<EditMtpFormResponse>() {
            @Override
            public void onResponse(Call<EditMtpFormResponse> call, Response<EditMtpFormResponse> response) {
                progressDialoge.dismiss();
                EditMtpFormResponse res =response.body();
                if(!res.isError()){
                    if(res.getErrormsg().equalsIgnoreCase("Success")){
                        alltownlist = res.getEDITMTPTCPIDNTOWNS();
                        if(alltownlist.size()>0){
                            for(int j=0;j<alltownlist.size();j++){
                                towns.add(alltownlist.get(j).getTOWN());
                                towntcpid.put(alltownlist.get(j).getTOWN(), alltownlist.get(j).getTCPID());
                            }
                            editMTPPopup(townname, workdate, objective, jointwrk, tcpid, position);
                        }
                    }
                }else{
                    Toast.makeText(getActivity(), res.getErrormsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<EditMtpFormResponse> call, Throwable t) {
                progressDialoge.dismiss();
                Snackbar.make(rtl,"Failed to delete MTP !", Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteEntry(tcpid,workdate,position);
                    }
                }).show();
            }
        });
    }

    private void deleteEntry(final String tcpid, final String workdate, final int position) {
        String[] date = workdate.split("/");
        String newdate = date[2]+"-"+date[1]+"-"+date[0];
        progressDialoge.show();
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().deleteMTPEntry(Global.ecode,Global.netid,tcpid,newdate,Global.dbprefix);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                progressDialoge.dismiss();
                DefaultResponse res =response.body();
                if(!res.isError()){
                    if(res.getErrormsg().equalsIgnoreCase("Success")){
                        mtplst.get(position).setAprvcode("");
                        mtptownlist.getAdapter().notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(getActivity(), res.getErrormsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressDialoge.dismiss();
                Snackbar.make(rtl,"Failed to delete MTP !", Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteEntry(tcpid,workdate,position);
                    }
                }).show();
            }
        });
    }

    private void editMTPPopup(String townname, final String workdate, String objective, String jointwrk, final String prevtcpid, final int position) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.edit_mtp_popup);

        CardView buttonNo = dialog.findViewById(R.id.cancel);
        CardView buttonupdate = dialog.findViewById(R.id.update);
        final Spinner townslist = dialog.findViewById(R.id.townslist);
        TextView wdate = dialog.findViewById(R.id.wdate);
        final EditText wwith = dialog.findViewById(R.id.wwith);
        final EditText objectives = dialog.findViewById(R.id.objectives);

        wdate.setText(workdate);
        objectives.setText(objective);
        wwith.setText(jointwrk);

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo save data
                String[] date = workdate.split("/");
                String newdate = date[2]+"-"+date[1]+"-"+date[0];
                String selitem = townslist.getSelectedItem().toString().trim();
                String valuefrmhm = towntcpid.get(selitem);
                String seltcpid = valuefrmhm;
                String selobj = objectives.getText().toString().trim();
                String selwwith = wwith.getText().toString().trim();

                updateMTPEntry(seltcpid,selobj,selwwith,newdate,prevtcpid,position,dialog,selitem);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, towns);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);


        townslist.setAdapter(adapter);
        townslist.setSelection(adapter.getPosition(townname));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }

    private void updateMTPEntry(final String seltcpid, final String selobj, final String selwwith, final String newdate, final String prevtcpid, final int position, final Dialog dialog, final String townname) {
        progressDialoge.show();
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().updateMTPEntry(Global.ecode,Global.netid,seltcpid,newdate,selobj,selwwith,prevtcpid,Global.dbprefix);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                progressDialoge.dismiss();
                DefaultResponse res = response.body();
                if(!res.isError()){
                    mtplst.get(position).setAprvcode("");
                    mtplst.get(position).setTCPID(seltcpid);
                    mtplst.get(position).setOBJECTIVE(selobj);
                    mtplst.get(position).setJOINTWORKING(selwwith);
                    mtplst.get(position).setTOWN(townname);
                    mtptownlist.getAdapter().notifyDataSetChanged();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressDialoge.dismiss();
                Snackbar.make(rtl,"Failed to Update MTP !", Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateMTPEntry(seltcpid,selobj,selwwith,newdate,prevtcpid,position,dialog,townname);
                    }
                }).show();
            }
        });
    }
}
