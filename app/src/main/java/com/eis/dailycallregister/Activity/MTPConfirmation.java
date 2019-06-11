package com.eis.dailycallregister.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eis.dailycallregister.R;

public class MTPConfirmation extends AppCompatActivity {

    public RelativeLayout rtl;
    public TextView confirm,alconf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtpconfirmation);

        rtl = findViewById(R.id.rtl);
        confirm = findViewById(R.id.confirm);
        alconf = findViewById(R.id.alconf);

    }
}
