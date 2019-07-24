package com.eis.dailycallregister.Activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.QuizMainRes;
import com.eis.dailycallregister.Pojo.TestlstItem;
import com.eis.dailycallregister.Pojo.TestqueslstItem;
import com.eis.dailycallregister.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Quiz extends AppCompatActivity {

    //private static final long START_TIME_IN_MILLIS=0;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private CountDownTimer mCountDownTimer;
    TextView mTextViewCountDown,tvtestname,ttltime,question,prev,next,quescount;
    AppCompatRadioButton op1,op2,op3,op4;
    RadioGroup ansrbgrp;
    ViewDialog progressDialoge;
    Button submit;
    List<TestqueslstItem> testqueslst = new ArrayList<>();
    String testid,testname,totques;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>TEST</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        progressDialoge = new ViewDialog(Quiz.this);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        tvtestname = findViewById(R.id.testname);
        ttltime = findViewById(R.id.ttltime);
        question = findViewById(R.id.question);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        op1 = findViewById(R.id.op1);
        op2 = findViewById(R.id.op2);
        op3 = findViewById(R.id.op3);
        op4 = findViewById(R.id.op4);
        ansrbgrp = findViewById(R.id.ansrbgrp);
        quescount = findViewById(R.id.quescount);
        submit = findViewById(R.id.submit);
        mTimeLeftInMillis = Integer.parseInt(getIntent().getStringExtra("time")) * 60 * 1000;
        testid = getIntent().getStringExtra("testid");
        testname = getIntent().getStringExtra("testname");
        totques = getIntent().getStringExtra("totques");
        tvtestname.setText(testname+" ("+totques+" Questions)");
        ttltime.setText(getIntent().getStringExtra("time"));

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = ansrbgrp.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                int id = radioButton.getId();
                switch(id){
                    case R.id.op1:
                        testqueslst.get(count).setAnsgiven("1");
                        break;
                    case R.id.op2:
                        testqueslst.get(count).setAnsgiven("2");
                        break;
                    case R.id.op3:
                        testqueslst.get(count).setAnsgiven("3");
                        break;
                    case R.id.op4:
                        testqueslst.get(count).setAnsgiven("4");
                        break;
                }


                if(count==0){
                    Toast.makeText(Quiz.this, "This is first question.", Toast.LENGTH_SHORT).show();
                }else{
                    count--;
                    showQuestion();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = ansrbgrp.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                int id = radioButton.getId();
                switch(id){
                    case R.id.op1:
                        testqueslst.get(count).setAnsgiven("1");
                        break;
                    case R.id.op2:
                        testqueslst.get(count).setAnsgiven("2");
                        break;
                    case R.id.op3:
                        testqueslst.get(count).setAnsgiven("3");
                        break;
                    case R.id.op4:
                        testqueslst.get(count).setAnsgiven("4");
                        break;
                }

                if(testqueslst.size()>count+1){
                    count++;
                    showQuestion();
                }else{
                    Toast.makeText(Quiz.this, "This is last question.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEntry();
            }
        });

        getQues();
    }

    private void submitEntry(){
        int selectedId = ansrbgrp.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        int id = radioButton.getId();
        switch(id){
            case R.id.op1:
                testqueslst.get(count).setAnsgiven("1");
                break;
            case R.id.op2:
                testqueslst.get(count).setAnsgiven("2");
                break;
            case R.id.op3:
                testqueslst.get(count).setAnsgiven("3");
                break;
            case R.id.op4:
                testqueslst.get(count).setAnsgiven("4");
                break;
        }
        double totalmarks = 0.0;
        double totalachived = 0.0;
        int totalques = testqueslst.size();
        String timeremain = Long.toString(mTimeLeftInMillis);
        mCountDownTimer.cancel();
        mTimerRunning = false;
        int timetaken = Integer.parseInt(getIntent().getStringExtra("time")) * 60 * 1000 - Integer.parseInt(timeremain);
        int minutes = (int) (timetaken / 1000) / 60;
        int seconds = (int) (timetaken / 1000) % 60;
        String exacttime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        Log.d("time---->",exacttime);
        for(int i=0;i<testqueslst.size();i++){
            Log.d("given ans---->",testqueslst.get(i).getAnsgiven());
            totalmarks+=Integer.parseInt(testqueslst.get(i).getMarks());
            if(testqueslst.get(i).getAnswer().equalsIgnoreCase(testqueslst.get(i).getAnsgiven())){
                totalachived+=Integer.parseInt(testqueslst.get(i).getMarks());
            }
        }

        double percent = totalachived / totalmarks * 100 ;
        Toast.makeText(Quiz.this, Double.toString(percent), Toast.LENGTH_LONG).show();
    }

    private void getQues() {
        progressDialoge.show();
        retrofit2.Call<QuizMainRes> call1 = RetrofitClient
                .getInstance().getApi().getQuesData(testid, Global.dbprefix);
        call1.enqueue(new Callback<QuizMainRes>() {
            @Override
            public void onResponse(retrofit2.Call<QuizMainRes> call1, Response<QuizMainRes> response) {
                QuizMainRes res = response.body();
                progressDialoge.dismiss();
                assert res != null;
                testqueslst = res.getTestqueslst();
                if(testqueslst.size()>0){
                    startTimer();
                    showQuestion();
                }
            }

            @Override
            public void onFailure(Call<QuizMainRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Toast.makeText(Quiz.this, "Failed to fetch data !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showQuestion() {
        /*op1.setChecked(false);
        op2.setChecked(false);
        op3.setChecked(false);
        op4.setChecked(false);*/
        TestqueslstItem model = testqueslst.get(count);

        quescount.setText("Question "+(count+1));//+(count+1))
        question.setText(model.getQuestion());
        op1.setText(model.getOption1());
        op2.setText(model.getOption2());
        op3.setText(model.getOption3());
        op4.setText(model.getOption4());


        if(!model.getAnsgiven().equalsIgnoreCase("")){
            if(model.getAnsgiven().equalsIgnoreCase("1")){
                op1.setChecked(true);
            }else if(model.getAnsgiven().equalsIgnoreCase("2")){
                op2.setChecked(true);
            }else if(model.getAnsgiven().equalsIgnoreCase("3")){
                op3.setChecked(true);
            }else if(model.getAnsgiven().equalsIgnoreCase("4")){
                op4.setChecked(true);
            }
        }
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                Toast.makeText(Quiz.this, "time finished", Toast.LENGTH_LONG).show();
            }
        }.start();

        mTimerRunning = true;

    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
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
        mCountDownTimer.cancel();
        mTimerRunning = false;
        finish();
        Quiz.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
