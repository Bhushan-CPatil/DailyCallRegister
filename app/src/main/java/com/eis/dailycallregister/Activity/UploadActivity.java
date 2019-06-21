package com.eis.dailycallregister.Activity;


import java.io.File;
import java.io.IOException;

import com.bumptech.glide.Glide;
import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import id.zelory.compressor.Compressor;

public class UploadActivity extends AppCompatActivity {
    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();
    ViewDialog progressDialoge;
    private ProgressBar progressBar;
    private String filePath = null, fileUri = null;
    private TextView txtPercentage;
    private AppCompatImageView imgPreview;
    LinearLayout lin1;
    //private VideoView vidPreview;
    private Button btnUpload;
    long totalSize = 0;
    public String cntcd;
    public boolean isimgcropped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>Upload Card</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        progressDialoge = new ViewDialog(UploadActivity.this);
        cntcd = getIntent().getStringExtra("cntcd");
        txtPercentage = findViewById(R.id.txtPercentage);
        btnUpload = findViewById(R.id.btnUpload);
        progressBar = findViewById(R.id.progressBar);
        imgPreview = findViewById(R.id.imgPreview);
        lin1 = findViewById(R.id.lin1);
        //vidPreview = findViewById(R.id.videoPreview);

        /*// Changing action bar background color
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor(getResources().getString(
                        R.color.action_bar))));*/

        // Receiving the data from previous activity
        Intent i = getIntent();

        // image or video path that is captured in previous activity
        filePath = i.getStringExtra("filePath");
        fileUri = i.getStringExtra("fileUri");
        isimgcropped = i.getExtras().getBoolean("isimgcropped");

        // boolean flag to identify the media type, image or video
        boolean isImage = i.getBooleanExtra("isImage", true);

        if (filePath != null) {
            // Displaying the image or video on the screen
            previewMedia(isImage);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnUpload.setEnabled(false);
                // uploading the file to server
                new UploadFileToServer().execute();
            }
        });

    }

    /**
     * Displaying captured image/video on the screen
     */
    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        //if (isImage) {

        //vidPreview.setVisibility(View.GONE);
        // bimatp factory


        // down sizing image as it throws OutOfMemory Exception for larger
        // images
        //options.inSampleSize = 8;
        /*imgPreview.setVisibility(View.VISIBLE);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            imgPreview.setImageBitmap(bitmap);*/
        /*} else {
            imgPreview.setVisibility(View.GONE);
            vidPreview.setVisibility(View.VISIBLE);
            vidPreview.setVideoPath(filePath);
            // start playing
            vidPreview.start();
        }*/
        imgPreview.setVisibility(View.VISIBLE);
        File file = new File(filePath);
        long fileSizeInBytes = file.length();
        long fileSizeInKB = fileSizeInBytes / 1024;
        if (file.exists() && fileSizeInKB > 5) {
            Glide.with(this).load(filePath).into(imgPreview);
        } else {
            filePath = fileUri;
            isimgcropped = false;
            Glide.with(this).load(fileUri).into(imgPreview);
            Toast.makeText(UploadActivity.this, "Croped Image not saved !", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Uploading the file to server
     */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressDialoge.show();
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(RetrofitClient.BASE_URL + "fileUpload.php");

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                //Log.d("file path", filePath);
                File sourceFile = new File(filePath);
                if (!isimgcropped) {
                    File compressedImageFile = new Compressor(UploadActivity.this).compressToFile(sourceFile);
                    // Adding file data to http body
                    entity.addPart("image", new FileBody(compressedImageFile));
                } else {
                    entity.addPart("image", new FileBody(sourceFile));
                }
                // Extra parameters if you want to pass to server
                entity.addPart("netid", new StringBody(Global.netid));
                entity.addPart("cntcd", new StringBody(cntcd));
                entity.addPart("DBPrefix", new StringBody(Global.dbprefix));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            //Log.e(TAG, "Response from server: " + result);
            progressDialoge.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (!jsonObject.getBoolean("error")) {
                    // showing the server response in an alert dialog
                    showAlert(jsonObject.getString("errormsg"));

                } else {
                    Snackbar snackbar = Snackbar.make(lin1, jsonObject.getString("errormsg"), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     */
    private void showAlert(String message) {
        final Dialog dialog = new Dialog(UploadActivity.this);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.success_dilogue);
        MaterialButton button = dialog.findViewById(R.id.btnsucces);
        AppCompatTextView textView = dialog.findViewById(R.id.successtext);
        //textView.setText("Visiting card added successfully in system. \nClick on OK to get back.");
        textView.setText(message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(UploadActivity.this, HomeActivity.class);
                intent.putExtra("ecode", Global.ecode);
                intent.putExtra("date", Global.date);
                intent.putExtra("dbprefix", Global.dbprefix);
                intent.putExtra("openfrag", "visitingcard");
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(UploadActivity.this, R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                startActivity(intent, bndlanimation);
                finish();
                /*finish();
                UploadActivity.this.overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);*/
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
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
        UploadActivity.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}
