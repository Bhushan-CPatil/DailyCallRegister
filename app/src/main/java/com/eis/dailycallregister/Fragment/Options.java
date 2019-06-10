package com.eis.dailycallregister.Fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eis.dailycallregister.Activity.HomeActivity;
import com.eis.dailycallregister.Activity.LoginScreen;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.R;


public class Options extends  Fragment {

    MaterialButton dcr,mtp,uploadcard;
    View view;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Home");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_options, container, false);

        dcr = view.findViewById(R.id.dcr);
        mtp = view.findViewById(R.id.mtp);
        uploadcard = view.findViewById(R.id.uploadcard);

        dcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new Global().notAllowed(getActivity());
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                intent.putExtra("ecode", Global.ecode);
                intent.putExtra("date",Global.date);
                intent.putExtra("dbprefix",Global.dbprefix);
                intent.putExtra("openfrag","dcr");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(intent,bndlanimation);
                getActivity().finish();
            }
        });

        uploadcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                intent.putExtra("ecode", Global.ecode);
                intent.putExtra("date",Global.date);
                intent.putExtra("dbprefix",Global.dbprefix);
                intent.putExtra("openfrag","visitingcard");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.trans_left_in,R.anim.trans_left_out).toBundle();
                startActivity(intent,bndlanimation);
                getActivity().finish();
            }
        });

        mtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Global().notAllowed(getActivity());

            }
        });
        return view;

    }
}

