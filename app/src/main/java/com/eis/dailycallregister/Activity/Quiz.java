package com.eis.dailycallregister.Activity;

import android.app.ActivityOptions;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.DefaultResponse;
import com.eis.dailycallregister.Pojo.QuizMainRes;
import com.eis.dailycallregister.Pojo.TestlstItem;
import com.eis.dailycallregister.Pojo.TestqueslstItem;
import com.eis.dailycallregister.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

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
    TextView mTextViewCountDown,tvtestname,ttltime,question,prev,next,quescount,msg;
    AppCompatRadioButton op1,op2,op3,op4;
    RadioGroup ansrbgrp;
    ViewDialog progressDialoge;
    Button submit;
    List<TestqueslstItem> testqueslst = new ArrayList<>();
    String testid,testname,totques;
    int count = 0;
    LinearLayout beforesubmit,aftersubmit;
    RecyclerView resultsummary;
    AlertDialog subdialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>E-LEARNING</font>"));
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
        beforesubmit = findViewById(R.id.beforesubmit);
        aftersubmit = findViewById(R.id.aftersubmit);
        resultsummary = findViewById(R.id.resultsummary);
        msg = findViewById(R.id.msg);
        beforesubmit.setVisibility(View.VISIBLE);
        aftersubmit.setVisibility(View.GONE);
        mTimeLeftInMillis = Integer.parseInt(getIntent().getStringExtra("time")) * 60 * 1000;
        testid = getIntent().getStringExtra("testid");
        testname = getIntent().getStringExtra("testname");
        totques = getIntent().getStringExtra("totques");
        tvtestname.setText(testname+" ("+totques+" Questions)");
        ttltime.setText(getIntent().getStringExtra("time"));
        setRecyAdapter();

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = ansrbgrp.getCheckedRadioButtonId();
                if(selectedId != -1) {
                    RadioButton radioButton = (RadioButton) findViewById(selectedId);
                    int id = radioButton.getId();
                    switch (id) {
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
                if(selectedId != -1) {
                    RadioButton radioButton = (RadioButton) findViewById(selectedId);
                    int id = radioButton.getId();
                    switch (id) {
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
                submitEntry("M");
            }
        });

        getQues();
    }

    private void setRecyAdapter() {
        resultsummary.setNestedScrollingEnabled(false);
        resultsummary.setLayoutManager(new LinearLayoutManager(Quiz.this));
        resultsummary.setAdapter(new RecyclerView.Adapter() {
                                    @NonNull
                                    @Override
                                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                        View view = LayoutInflater.from(Quiz.this).inflate(R.layout.result_summary, viewGroup, false);
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
                                        final TestqueslstItem model = testqueslst.get(i);
                                        myHolder.quesindex.setText("Question "+(i+1));
                                        myHolder.question.setText(model.getQuestion());

                                        if(model.getAnswer().equalsIgnoreCase(model.getAnsgiven())){
                                            myHolder.corwng.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_mark));
                                        }else{
                                            myHolder.corwng.setImageDrawable(getResources().getDrawable(R.drawable.ic_error));
                                        }

                                        if(model.getAnswer().equalsIgnoreCase("1")) {
                                            myHolder.corrans.setText(model.getOption1());
                                        }else if(model.getAnswer().equalsIgnoreCase("2")) {
                                            myHolder.corrans.setText(model.getOption2());
                                        }else if(model.getAnswer().equalsIgnoreCase("3")) {
                                            myHolder.corrans.setText(model.getOption3());
                                        }else if(model.getAnswer().equalsIgnoreCase("4")) {
                                            myHolder.corrans.setText(model.getOption4());
                                        }

                                        if(model.getAnsgiven().equalsIgnoreCase("1")) {
                                            myHolder.yourans.setText(model.getOption1());
                                        }else if(model.getAnsgiven().equalsIgnoreCase("2")) {
                                            myHolder.yourans.setText(model.getOption2());
                                        }else if(model.getAnsgiven().equalsIgnoreCase("3")) {
                                            myHolder.yourans.setText(model.getOption3());
                                        }else if(model.getAnsgiven().equalsIgnoreCase("4")) {
                                            myHolder.yourans.setText(model.getOption4());
                                        }else if(model.getAnsgiven().equalsIgnoreCase("")) {
                                            myHolder.yourans.setText("Answer Not Given");
                                        }


                                    }

                                    @Override
                                    public int getItemCount() {
                                        return testqueslst.size();
                                    }

                                    class Holder extends RecyclerView.ViewHolder {
                                        TextView quesindex,question,corrans,yourans;
                                        ImageView corwng;

                                        public Holder(@NonNull View itemView) {
                                            super(itemView);
                                            quesindex = itemView.findViewById(R.id.quesindex);
                                            question = itemView.findViewById(R.id.question);
                                            corrans = itemView.findViewById(R.id.corrans);
                                            yourans = itemView.findViewById(R.id.yourans);
                                            corwng = itemView.findViewById(R.id.corwng);
                                        }
                                    }
                                }
        );
    }

    private void submitEntry(String way){
        int selectedId = ansrbgrp.getCheckedRadioButtonId();
        if(selectedId != -1) {
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            int id = radioButton.getId();
            switch (id) {
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
        }

        if(way.equalsIgnoreCase("M")){
            AlertDialog.Builder builder = new AlertDialog.Builder(Quiz.this);
            builder.setCancelable(false);
            builder.setTitle("Submit ?");
            builder.setMessage("Do you wish to recheck ?\nNot required, Submit.");
            builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    double totalmarks = 0.0;
                    double totalachived = 0.0;
                    int NoOfQuestions = testqueslst.size();
                    int TotalCorrect = 0;
                    String timeremain = Long.toString(mTimeLeftInMillis);
                    mCountDownTimer.cancel();
                    mTimerRunning = false;
                    int timetaken = Integer.parseInt(getIntent().getStringExtra("time")) * 60 * 1000 - Integer.parseInt(timeremain);
                    int minutes = (int) (timetaken / 1000) / 60;
                    int seconds = (int) (timetaken / 1000) % 60;
                    String exacttime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                    //Log.d("time---->",exacttime);
                    for(int i=0;i<testqueslst.size();i++){
                        //Log.d("given ans---->",testqueslst.get(i).getAnsgiven());
                        totalmarks+=Integer.parseInt(testqueslst.get(i).getMarks());
                        if(testqueslst.get(i).getAnswer().equalsIgnoreCase(testqueslst.get(i).getAnsgiven())){
                            TotalCorrect++;
                            totalachived+=Integer.parseInt(testqueslst.get(i).getMarks());
                        }
                    }

                    double percent = totalachived / totalmarks * 100 ;
                    //Log.d("percent----->", Double.toString(percent));
        /*Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(testqueslst).getAsJsonArray();
        Log.d("json----->", myCustomArray.toString());*/

                    callSaveApi(percent,TotalCorrect,NoOfQuestions,exacttime);
                }
            });

            builder.setNeutralButton("RECHECK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            subdialog = builder.create();
            subdialog.show();
        }else if(way.equalsIgnoreCase("A")){
            if(subdialog != null) {
                subdialog.dismiss();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(Quiz.this);
            builder.setCancelable(false);
            builder.setTitle("TIME OUT ?");
            builder.setMessage("Do you want to quit the test ? OR Submit the test ?");
            builder.setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    double totalmarks = 0.0;
                    double totalachived = 0.0;
                    int NoOfQuestions = testqueslst.size();
                    int TotalCorrect = 0;
                    String timeremain = Long.toString(mTimeLeftInMillis);
                    mCountDownTimer.cancel();
                    mTimerRunning = false;
                    int timetaken = Integer.parseInt(getIntent().getStringExtra("time")) * 60 * 1000;
                    int minutes = (int) (timetaken / 1000) / 60;
                    int seconds = (int) (timetaken / 1000) % 60;
                    String exacttime = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                    //Log.d("time---->",exacttime);
                    for(int i=0;i<testqueslst.size();i++){
                        //Log.d("given ans---->",testqueslst.get(i).getAnsgiven());
                        totalmarks+=Integer.parseInt(testqueslst.get(i).getMarks());
                        if(testqueslst.get(i).getAnswer().equalsIgnoreCase(testqueslst.get(i).getAnsgiven())){
                            TotalCorrect++;
                            totalachived+=Integer.parseInt(testqueslst.get(i).getMarks());
                        }
                    }

                    double percent = totalachived / totalmarks * 100 ;
                    //Log.d("percent----->", Double.toString(percent));
        /*Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(testqueslst).getAsJsonArray();
        Log.d("json----->", myCustomArray.toString());*/

                    callSaveApi(percent,TotalCorrect,NoOfQuestions,exacttime);
                }
            });

            builder.setNeutralButton("QUIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void callSaveApi(final double percent, int totalCorrect, int noOfQuestions, String exacttime) {
        beforesubmit.setVisibility(View.GONE);
        progressDialoge.show();
        Call<DefaultResponse> call = RetrofitClient.getInstance().getApi().saveTestResult(testid,Global.ecode,Double.toString(percent),Integer.toString(totalCorrect)
                ,Integer.toString(noOfQuestions),exacttime,Global.dbprefix);
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                progressDialoge.dismiss();
                DefaultResponse res = response.body();
                if(!res.isError()){
                    if(percent < 40){
                        msg.setText("Your score is "+percent+" %. Average. Poor preparation. "+testname+" needs your involvement.");
                    }else if(percent >= 40 && percent < 70){
                        msg.setText("Your score is "+percent+" %. Now is time to run harder with "+testname+". All the Best.");
                    }else if(percent >= 70 && percent < 90){
                        msg.setText("Your score is "+percent+" %. Well done! Your efforts can take "+testname+" to the TOP! All the Best!");
                    }else if(percent >= 90){
                        msg.setText("Your score is "+percent+" %. Excellent! You are Prepared to Rule the Market with "+testname+"! All the Best !");
                    }

                    resultsummary.setVisibility(View.VISIBLE);
                    resultsummary.getAdapter().notifyDataSetChanged();
                    aftersubmit.setVisibility(View.VISIBLE);
                }else {
                    onBackPressed();
                    Toast.makeText(Quiz.this, res.getErrormsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                progressDialoge.dismiss();
                Toast.makeText(Quiz.this, "Failed to save ?", Toast.LENGTH_SHORT).show();
            }
        });


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
        ansrbgrp.clearCheck();
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
                submitEntry("A");
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
        Intent intent = new Intent(Quiz.this, HomeActivity.class);
        intent.putExtra("ecode", Global.ecode);
        intent.putExtra("date", Global.date);
        intent.putExtra("dbprefix", Global.dbprefix);
        intent.putExtra("openfrag", "elearn");
        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(Quiz.this, R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
        startActivity(intent, bndlanimation);
        finish();
        finish();
    }
}
