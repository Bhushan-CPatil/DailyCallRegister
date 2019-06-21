package com.eis.dailycallregister.Activity;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.DBList;
import com.eis.dailycallregister.Pojo.DefaultResponse;
import com.eis.dailycallregister.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreen extends AppCompatActivity {

    public EditText uid, date;
    public TextInputEditText pass;
    public Button login;
    DatePickerDialog datePickerDialog;
    ViewDialog progress;
    Spinner spnArea;
    RequestQueue queue;
    String currentversion;
    RelativeLayout rl;
    boolean allgranted = false;
    boolean callpermissiongranted = false;
    public static String web_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_screen);
        getSupportActionBar().hide();
        new Global().clearGlobal("All");
        queue = Volley.newRequestQueue(LoginScreen.this);
        checkforUpgrade();
        progress = new ViewDialog(this);
        rl = findViewById(R.id.rl);
        uid = findViewById(R.id.uid);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        spnArea = findViewById(R.id.spnarea);
        getArea();

        // initiate the date picker and a button*/
        date = findViewById(R.id.wdate);
        String cdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        date.setText(cdate);
        date.setFocusable(false);
        // perform click event on edit text
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(LoginScreen.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                //date.setText(String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + year);
                                date.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dologin();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestStoragePermission();
    }

    private void checkforUpgrade() {
        final StringRequest request = new StringRequest(StringRequest.Method.POST, RetrofitClient.BASE_URL + "isupdaterequire.php", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    currentversion = pInfo.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    double newverison = Double.parseDouble(jsonObject.getString("version"));
                    double verison = Double.parseDouble(currentversion);
                    final String link = jsonObject.getString("link");

                    if (verison < newverison) {
                        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(LoginScreen.this);
                        View mView = layoutInflaterAndroid.inflate(R.layout.updateapp, null);
                        android.app.AlertDialog.Builder alertDialogBuilderUserInput = new android.app.AlertDialog.Builder(LoginScreen.this);
                        alertDialogBuilderUserInput.setView(mView);
                        MaterialButton button = mView.findViewById(R.id.updateappbtn);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(link));
                                startActivity(intent);
                            }
                        });
                        final android.app.AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                        alertDialogAndroid.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        alertDialogAndroid.setCancelable(false);
                        alertDialogAndroid.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginScreen.this, "Unable to check updates !", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<>();
                map.put("id", "0");
                return map;
            }
        };
        queue.add(request);
    }

    public void dologin() {
        Vibrator vibrator = (Vibrator) getSystemService(LoginScreen.this.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
        if (callpermissiongranted) {


            if (allgranted) {


//            Log.d("pass encrypt --->", Global.password);
                final String lid = uid.getText().toString().trim();
                String password = pass.getText().toString().trim();
                String dt = date.getText().toString().trim();

                if (lid.isEmpty()) {
                    uid.setError("Login ID is required");
                    uid.requestFocus();
                    return;
                }

                if (lid.length() < 5) {
                    uid.setError("Enter a valid login ID");
                    uid.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    pass.setError("Password is required");
                    pass.requestFocus();
                    return;
                }

                if (dt.isEmpty()) {
                    date.setError("Login date is required");
                    date.requestFocus();
                    return;
                }

                web_url = RetrofitClient.BASE_URL + "hitJSfile.php?password=" + password;
                WebViewFragment webViewFragment = new WebViewFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentcont, webViewFragment).commit();
                progress.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //Toast.makeText(LoginScreen.this,Global.password,Toast.LENGTH_LONG).show();
                        if (Global.password == null) {
                            progress.dismiss();
                            notEncryptAlert();
                        } else {
                            Call<DefaultResponse> call = RetrofitClient
                                    .getInstance().getApi().login(lid, Global.password, date.getText().toString().trim(), spnArea.getSelectedItem().toString().trim());
                            call.enqueue(new Callback<DefaultResponse>() {

                                @Override
                                public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                                    DefaultResponse dResponse = response.body();

                                    progress.dismiss();
                                    //Log.d("Errrrrrroooooorrrrr", dResponse.getErrormsg());

                                    if (!dResponse.isError()) {
                                        // Log.d("message",loginResponse.getMessage());

                                        Intent intent = new Intent(LoginScreen.this, HomeActivity.class);
                                        intent.putExtra("ecode", uid.getText().toString().trim());
                                        intent.putExtra("date", date.getText().toString().trim());
                                        intent.putExtra("dbprefix", spnArea.getSelectedItem().toString().trim());
                                        Global.ecode = uid.getText().toString().trim();
                                        Global.date = date.getText().toString().trim();
                                        Global.dbprefix = spnArea.getSelectedItem().toString().trim();
                                        String[] msgsplt = dResponse.getErrormsg().split("~");
                                        Global.netid = msgsplt[0];
                                        Global.hname = msgsplt[1];
                                        Global.ename = msgsplt[2];
                                        Global.ecode = msgsplt[3];
                                        intent.putExtra("openfrag", "home");
                                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(LoginScreen.this, R.anim.trans_left_in, R.anim.trans_left_out).toBundle();
                                        startActivity(intent, bndlanimation);
                                        finish();

                                    } else {
                                /*if (dResponse.getErrormsg().equalsIgnoreCase("Entered date exceeds current date")) {
                                    Snackbar snackbar = Snackbar.make(rl, dResponse.getErrormsg(), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else if (dResponse.getErrormsg().equalsIgnoreCase("Invalid password !")) {
                                    Snackbar snackbar = Snackbar.make(rl, dResponse.getErrormsg(), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else if (dResponse.getErrormsg().equalsIgnoreCase("User does not exist in the selected database.")) {
                                    Snackbar snackbar = Snackbar.make(rl, dResponse.getErrormsg(), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else if (dResponse.getErrormsg().equalsIgnoreCase("Network does not exist for selected working month and year ! OR Wrong division selected !")) {
                                    Snackbar snackbar = Snackbar.make(rl, dResponse.getErrormsg(), Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }*/
                                        Snackbar snackbar = Snackbar.make(rl, dResponse.getErrormsg(), Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<DefaultResponse> call, Throwable t) {
                                    progress.dismiss();
                                    if (t instanceof IOException) {
                                        Snackbar snackbar = Snackbar.make(rl, "Internet Issue ! Failed to process your request !", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    } else {
                                        Snackbar snackbar = Snackbar.make(rl, "Data Conversion Issue ! Contact to admin", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                }
                            });
                        }
                    }
                }, 1200);

            } else {
                requestStoragePermission();
            }
        } else {
            Snackbar snackbar = Snackbar
                    .make(rl, "Field to fetch data !", Snackbar.LENGTH_LONG)
                    .setAction("Re-Load", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recreate();
                        }
                    });

            snackbar.show();
        }

    }

    private void getArea() {
        progress.show();
        Call<DBList> call = RetrofitClient
                .getInstance().getApi().getdblist();
        call.enqueue(new Callback<DBList>() {

            @Override
            public void onResponse(Call<DBList> call, Response<DBList> response) {
                DBList res = response.body();

                progress.dismiss();
                List<String> arrayList = new ArrayList<>();
                String[] dblist = res.getDbnames().split(",");
                for (String s : dblist) {
                    arrayList.add(s);
                }
                callpermissiongranted = true;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(LoginScreen.this, R.layout.spinner_item, arrayList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnArea.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<DBList> call, Throwable t) {
                progress.dismiss();
                Snackbar snackbar = Snackbar.make(rl, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Reload", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recreate();
                            }
                        });
                snackbar.show();
                /*if (t instanceof IOException) {
                    Snackbar snackbar = Snackbar.make(rl, "Internet Issue ! Failed to process your request !", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(rl, "Data Conversion Issue ! Contact to admin", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }*/
            }
        });

    }

    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                            allgranted = true;
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginScreen.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void notEncryptAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Warning !");
        builder.setMessage("Because of slow internet connection your password encryption failed ! \nClick on LOGIN button again.");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginScreen.this);
        builder.setCancelable(true);
        builder.setTitle("EXIT ?");
        builder.setMessage("Do you want to Exit ?");
        builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                LoginScreen.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog2 = builder.create();
        dialog2.show();

    }
}
