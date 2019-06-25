package com.eis.dailycallregister.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.DCRExpenseListRes;
import com.eis.dailycallregister.Pojo.DefaultResponse;
import com.eis.dailycallregister.Pojo.ExpentlstItem;
import com.eis.dailycallregister.Pojo.ExpfetchItem;
import com.eis.dailycallregister.Pojo.FetchExpdtRes;
import com.eis.dailycallregister.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseData extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 60000;
    public static final int READ_TIMEOUT = 90000;
    CardView c;
    MaterialButton expent;
    ViewDialog progressDialoge;
    LinearLayout mainlyout;
    public List<ExpfetchItem> expdatajson = new ArrayList<>();
    public List<ExpentlstItem> exentrylist = new ArrayList<>();
    public static RecyclerView expenselist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_data);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>Expense Data</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);

        c = findViewById(R.id.card);

        expent = findViewById(R.id.expent);
        expenselist = findViewById(R.id.expenselist);
        mainlyout = findViewById(R.id.mainlyout);
        progressDialoge = new ViewDialog(ExpenseData.this);

        c.setVisibility(View.VISIBLE);
        setExpAdapter();
        if (Global.dcrno != null) {
            expent.setVisibility(View.VISIBLE);
            callapi();
        } else {
            expent.setVisibility(View.GONE);
            c.setVisibility(View.VISIBLE);
        }

        expent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apicall();
            }
        });

    }

    public void setExpAdapter() {
        expenselist.setNestedScrollingEnabled(false);
        expenselist.setLayoutManager(new LinearLayoutManager(ExpenseData.this));
        expenselist.setAdapter(new RecyclerView.Adapter() {
                                   @NonNull
                                   @Override
                                   public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                       View view = LayoutInflater.from(ExpenseData.this).inflate(R.layout.view_expense_adapter, viewGroup, false);
                                       Holder holder = new Holder(view);
                                       return holder;
                                   }

                                   @Override
                                   public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                                       final Holder myHolder = (Holder) viewHolder;
                                       final ExpfetchItem model = expdatajson.get(i);
                                       myHolder.edesc.setText(model.getEdesc());
                                       myHolder.amount.setText("₹. " + model.getAmount());


                                       myHolder.deleteexp.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               showconfirmationdialog(model.getExpcd());
                                           }
                                       });

                                   }

                                   @Override
                                   public int getItemCount() {
                                       return expdatajson.size();
                                   }

                                   class Holder extends RecyclerView.ViewHolder {
                                       TextView edesc, amount;
                                       ImageButton deleteexp;

                                       public Holder(@NonNull View itemView) {
                                           super(itemView);
                                           edesc = itemView.findViewById(R.id.edesc);
                                           amount = itemView.findViewById(R.id.amount);
                                           deleteexp = itemView.findViewById(R.id.deleteexp);
                                       }
                                   }
                               }
        );
    }

    private void showconfirmationdialog(final String expcd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete ?");
        builder.setMessage("Are you sure you want to delete ?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteentry(expcd);
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

    private void callapi() {
        progressDialoge.show();

        retrofit2.Call<FetchExpdtRes> call1 = RetrofitClient
                .getInstance().getApi().fetchExpData(Global.dcrno, Global.dbprefix);
        call1.enqueue(new Callback<FetchExpdtRes>() {
            @Override
            public void onResponse(retrofit2.Call<FetchExpdtRes> call1, Response<FetchExpdtRes> response) {
                FetchExpdtRes res = response.body();
                progressDialoge.dismiss();
                if (!res.isError()) {
                    c.setVisibility(View.GONE);
                    expdatajson = res.getExpfetch();
                    expenselist.setVisibility(View.VISIBLE);
                    expenselist.getAdapter().notifyDataSetChanged();
                } else {
                    c.setVisibility(View.VISIBLE);
                    expenselist.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<FetchExpdtRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(mainlyout, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callapi();
                            }
                        });
                snackbar.show();
            }
        });
    }

    private void apicall() {
        progressDialoge.show();

        retrofit2.Call<DCRExpenseListRes> call1 = RetrofitClient
                .getInstance().getApi().DCRExpenseReq(Global.dcrno, Global.dbprefix);
        call1.enqueue(new Callback<DCRExpenseListRes>() {
            @Override
            public void onResponse(retrofit2.Call<DCRExpenseListRes> call1, Response<DCRExpenseListRes> response) {
                DCRExpenseListRes res = response.body();
                progressDialoge.dismiss();
                if (!res.isError()) {
                    exentrylist = res.getExpentlst();
                    showExpenseEntryPopup();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseData.this);
                    builder.setCancelable(true);
                    //builder.setTitle("LOGOUT ?");
                    builder.setMessage("Nothing to show !");
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
            public void onFailure(Call<DCRExpenseListRes> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(mainlyout, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callapi();
                            }
                        });
                snackbar.show();
            }
        });
    }

    private void deleteentry(String expcd) {
        progressDialoge.show();

        retrofit2.Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().deleteExpenseEntry(Global.dcrno, expcd, Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                progressDialoge.dismiss();
                if (!res.isError()) {
                    Snackbar snackbar = Snackbar.make(mainlyout, res.getErrormsg(), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    callapi();
                } else {
                    Snackbar snackbar = Snackbar.make(mainlyout, res.getErrormsg(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(mainlyout, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callapi();
                            }
                        });
                snackbar.show();
            }
        });
    }

    public void showExpenseEntryPopup() {
        final Dialog dialog = new Dialog(ExpenseData.this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.jointwrklstpopup);
        TextView heading = dialog.findViewById(R.id.heading);
        heading.setText("EXPENSE ENTRY");
        CardView cancelbtn = dialog.findViewById(R.id.no);
        CardView submitbtn = dialog.findViewById(R.id.yes);
        final RecyclerView rv_list_popup = dialog.findViewById(R.id.jointwrkpopuplist);
        rv_list_popup.setNestedScrollingEnabled(false);
        rv_list_popup.setLayoutManager(new LinearLayoutManager(ExpenseData.this));
        rv_list_popup.setAdapter(new RecyclerView.Adapter() {
                                     @NonNull
                                     @Override
                                     public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                         View view = LayoutInflater.from(ExpenseData.this).inflate(R.layout.expense_entry_adapter, viewGroup, false);
                                         Holder holder = new Holder(view);
                                         return holder;
                                     }

                                     @Override
                                     public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                                         final Holder myHolder = (Holder) viewHolder;
                                         final ExpentlstItem model = exentrylist.get(i);
                                         myHolder.expname.setText(model.getEDESC());
                                         myHolder.expamount.setText(model.getAmount());
                                         myHolder.expamount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                             @Override
                                             public void onFocusChange(View view, boolean hasFocus) {
                                                 if (!hasFocus) {
                                                     if (myHolder.expamount.getText().toString().equalsIgnoreCase("")) {
                                                         model.setAmount("");
                                                     } else {
                                                         @SuppressLint("DefaultLocale") String xyz = String.format("%.2f", Double.parseDouble(myHolder.expamount.getText().toString()));
                                                         model.setAmount(xyz);
                                                     }
                                                     //Toast.makeText(DocDCRGift.this, "Focus Lose", Toast.LENGTH_SHORT).show();
                                                     InputMethodManager imm = (InputMethodManager) getSystemService(ExpenseData.this.INPUT_METHOD_SERVICE);
                                                     imm.hideSoftInputFromWindow(mainlyout.getWindowToken(), 0);
                                                 }

                                             }
                                         });
                                     }

                                     @Override
                                     public int getItemCount() {
                                         return exentrylist.size();
                                     }

                                     class Holder extends RecyclerView.ViewHolder {
                                         TextView expname;
                                         EditText expamount;

                                         public Holder(@NonNull View itemView) {
                                             super(itemView);
                                             expname = itemView.findViewById(R.id.expname);
                                             expamount = itemView.findViewById(R.id.expamount);
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
                rv_list_popup.clearFocus();
                Gson gson = new GsonBuilder().create();
                JsonArray myCustomArray = gson.toJsonTree(exentrylist).getAsJsonArray();
                //Toast.makeText(ExpenseData.this, myCustomArray.toString(), Toast.LENGTH_LONG).show();
                //Log.d("json-->",myCustomArray.toString());
                new ExpenseData.addExpenseEntryInDB().execute(Global.dcrno, myCustomArray.toString(), Global.dbprefix);
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

    public class addExpenseEntryInDB extends AsyncTask<String, String, String> {
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
                url = new URL(RetrofitClient.BASE_URL + "addDcrExpenseEntry.php");

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
                        .appendQueryParameter("dcrno", params[0])
                        .appendQueryParameter("jsonarray", params[1])
                        .appendQueryParameter("DBPrefix", params[2]);

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

                if (!jobj.getBoolean("error")) {
                    Toast.makeText(ExpenseData.this, jobj.getString("errormsg"), Toast.LENGTH_SHORT).show();
                    callapi();
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
        ExpenseData.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
