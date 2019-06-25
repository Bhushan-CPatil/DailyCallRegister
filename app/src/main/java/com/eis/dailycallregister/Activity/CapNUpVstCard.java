package com.eis.dailycallregister.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eis.dailycallregister.Api.RetrofitClient;
import com.eis.dailycallregister.Others.Global;
import com.eis.dailycallregister.Others.ViewDialog;
import com.eis.dailycallregister.Pojo.DefaultResponse;
import com.eis.dailycallregister.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

//import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CapNUpVstCard extends AppCompatActivity {

    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();
    public LinearLayout ll1, ll2;
    public RelativeLayout rl;
    public String cntcd, status;
    public boolean isimgcropped = false;
    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    final int CROP_PIC = 98;
    private Uri picUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    ViewDialog progressDialoge;
    private Uri fileUri; // file url to store image/video

    private ImageView btnCapturePicture, cardpic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nup_vst_card);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        cntcd = getIntent().getStringExtra("cntcd");
        status = getIntent().getStringExtra("status");
        // Changing action bar background color
        // These two lines are not needed
        //getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.action_bar))));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>Visiting Card</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        btnCapturePicture = findViewById(R.id.btnCapturePicture);
        cardpic = findViewById(R.id.cardpic);
        progressDialoge = new ViewDialog(CapNUpVstCard.this);
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        //btnRecordVideo = findViewById(R.id.btnRecordVideo);
        callApiFirst();
        /**
         * Capture image button click event
         */
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });

        /**
         * Record video button click event
         */
        /*btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video
                recordVideo();
            }
        });*/

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reupload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.attach) {
            //todo get image from gallary
            getImageFromGallery();
            return true;
        } else if (id == R.id.edit) {
            captureImage();
            return true;
        } else if (id == R.id.delete) {
            deleteUpImage();
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteUpImage() {
        if (status.equalsIgnoreCase("A")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Delete ?");
            builder.setMessage("Are you sure you want to delete ?");
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialoge.show();

                            retrofit2.Call<DefaultResponse> call1 = RetrofitClient
                                    .getInstance().getApi().getDeleteExistingImg(Global.netid, cntcd, Global.dbprefix);
                            call1.enqueue(new Callback<DefaultResponse>() {
                                @Override
                                public void onResponse(retrofit2.Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                                    DefaultResponse res = response.body();
                                    progressDialoge.dismiss();
                                    if (!res.isError()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CapNUpVstCard.this);
                                        builder.setCancelable(true);
                                        builder.setTitle("Success");
                                        builder.setMessage("Visiting card successfully deleted !");
                                        builder.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(CapNUpVstCard.this, HomeActivity.class);
                                                        intent.putExtra("ecode", Global.ecode);
                                                        intent.putExtra("date", Global.date);
                                                        intent.putExtra("dbprefix", Global.dbprefix);
                                                        intent.putExtra("openfrag", "visitingcard");
                                                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(CapNUpVstCard.this, R.anim.trans_right_in, R.anim.trans_right_out).toBundle();
                                                        startActivity(intent, bndlanimation);
                                                        finish();
                                                    }
                                                });

                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        /*ll2.setVisibility(View.VISIBLE);
                                        ll1.setVisibility(View.GONE);
                                        Snackbar snackbar = Snackbar.make(ll2, res.getErrormsg(), Snackbar.LENGTH_LONG);
                                        snackbar.show();*/
                                    } else {
                                        Snackbar snackbar = Snackbar.make(ll1, res.getErrormsg(), Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                                    progressDialoge.dismiss();
                                    Snackbar snackbar = Snackbar.make(ll1, "Failed to fetch data !", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            });
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //do nothing
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Warning !");
            builder.setMessage("Visiting card not uploaded !");
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

    private void callApiFirst() {
        progressDialoge.show();

        retrofit2.Call<DefaultResponse> call1 = RetrofitClient
                .getInstance().getApi().getAlreadtExistImg(Global.netid, cntcd, Global.dbprefix);
        call1.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(retrofit2.Call<DefaultResponse> call1, Response<DefaultResponse> response) {
                DefaultResponse res = response.body();
                progressDialoge.dismiss();
                if (!res.isError()) {
                    ll2.setVisibility(View.GONE);
                    ll1.setVisibility(View.VISIBLE);
                    Glide.with(CapNUpVstCard.this).load(res.getErrormsg()).into(cardpic);
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call1, Throwable t) {
                progressDialoge.dismiss();
                Snackbar snackbar = Snackbar.make(ll2, "Failed to fetch data !", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Re-try", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callApiFirst();
                            }
                        });
                snackbar.show();
            }
        });
    }

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            intent.putExtra("return-data", true);
                            // start the image capture Intent
                            //startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    private void getImageFromGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, 99);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
       /* Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, 99);*/
    }

    /**
     * Launching camera app to record video
     */
    /*private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }*/

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                //picUri = data.getData();
                //Log.d("pic uri 1",picUri.toString());
                performCrop();

                //todo call crop image method
                // successfully captured the image
                // launching upload activity


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == 98) {
            //picUri = data.getExtras();
            //Log.d("pic uri 2",picUri.toString());
            if (resultCode == RESULT_OK) {
                isimgcropped = true;
                launchUploadActivity(true);
            } else if (resultCode == RESULT_CANCELED) {
                isimgcropped = false;
                picUri = fileUri;
                launchUploadActivity(true);
                Toast.makeText(getApplicationContext(),
                        "Crop operation canceled !", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to get image !", Toast.LENGTH_SHORT)
                        .show();
            }
            /*else if (resultCode == RESULT_CANCELED) {
                picUri = fileUri;
                launchUploadActivity(true);
                Toast.makeText(getApplicationContext(),
                        "Crop operation canceled !", Toast.LENGTH_SHORT)
                        .show();
            }*/
        } else if (requestCode == 3) {
            //picUri = data.getExtras();
            //Log.d("pic uri 2",picUri.toString());
            if (resultCode == RESULT_OK) {
                fileUri = Uri.parse(getRealPathFromURI(fileUri));
                isimgcropped = true;
                launchUploadActivity(true);
            } else if (resultCode == RESULT_CANCELED) {
                picUri = Uri.parse(getRealPathFromURI(fileUri));
                fileUri = Uri.parse(getRealPathFromURI(fileUri));
                isimgcropped = false;
                //Log.d("gallery pic path 2222",fileUri.toString());
                launchUploadActivity(true);
                Toast.makeText(getApplicationContext(),
                        "Crop operation canceled !", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to get image !", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == 99) {
            //picUri = data.getExtras();
            //Log.d("pic uri 2",picUri.toString());
            if (resultCode == RESULT_OK) {
                fileUri = data.getData();
                //Log.d("gallery pic path",fileUri.toString());
                performCrop2();
            } else if (requestCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled image selection !", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to get image !", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void launchUploadActivity(boolean isImage) {
        Intent i = new Intent(CapNUpVstCard.this, UploadActivity.class);
        i.putExtra("filePath", picUri.getPath());
        i.putExtra("fileUri", fileUri.getPath());
        i.putExtra("isImage", isImage);
        i.putExtra("isimgcropped", isimgcropped);
        i.putExtra("cntcd", cntcd);
        startActivity(i);
        finish();
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type, cntcd));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type, String cntcd) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                RetrofitClient.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                //Log.d(TAG, "Oops! Failed create "+ RetrofitClient.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + Global.netid + "_" + cntcd + "_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        } return true;
    }*/

    @Override
    public void onBackPressed() {
        finish();
        CapNUpVstCard.this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            //cropIntent.setClassName("com.google.android.gallery3d", "com.android.gallery3d.app.CropImage");
            //cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            // indicate image type and Uri
            //cropIntent.setDataAndType(fileUri, "image/*");
            //Log.d("fileUri 1",fileUri.toString());
            cropIntent.setDataAndType(fileUri, "image/*");

            // set crop properties
            cropIntent.putExtra("crop", "true");

            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 300);
            //cropIntent.putExtra("scaleUpIfNeeded",true);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);

            File f = createNewFile();
            try {
                f.createNewFile();
            } catch (IOException ex) {
                // Log.e("io", ex.getMessage());
            }

            picUri = Uri.fromFile(f);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
            /*} else {
                picUri = fileUri;
                launchUploadActivity(true);
                Toast toast = Toast
                        .makeText(this, "Device version below 5.0 doesn't support the crop action !", Toast.LENGTH_SHORT);
                toast.show();
            }*/

        } catch (ActivityNotFoundException anfe) {

            picUri = fileUri;
            launchUploadActivity(true);
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void performCrop2() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");

            //cropIntent.setClassName("com.google.android.gallery3d", "com.android.gallery3d.app.CropImage");
            //cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            // indicate image type and Uri
            //cropIntent.setDataAndType(fileUri, "image/*");
            //Log.d("fileUri 1",fileUri.toString());
            cropIntent.setDataAndType(fileUri, "image/*");

            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 300);
            cropIntent.putExtra("scaleUpIfNeeded", true);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);

            File f = createNewFile();
            try {
                f.createNewFile();
            } catch (IOException ex) {
                // Log.e("io", ex.getMessage());
            }

            picUri = Uri.fromFile(f);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 3);
            /*} else {
                picUri = Uri.parse(getRealPathFromURI(fileUri));
                launchUploadActivity(true);
                Toast toast = Toast
                        .makeText(this, "Device version below 5.0 doesn't support the crop action !", Toast.LENGTH_SHORT);
                toast.show();
            }*/

        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {

            picUri = fileUri;
            launchUploadActivity(true);
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /*private File createNewFile(String prefix){
        if(prefix==null || "".equalsIgnoreCase(prefix)){
            prefix="IMG_";
        }
        File newDirectory = new File(Environment.getExternalStorageDirectory()+"/mypics/");
        if(!newDirectory.exists()){
            if(newDirectory.mkdir()){
                Log.d(CapNUpVstCard.this.getClass().getName(), newDirectory.getAbsolutePath()+" directory created");
            }
        }
        File file = new File(newDirectory,(prefix+System.currentTimeMillis()+".jpg"));
        if(file.exists()){
            //this wont be executed
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }*/

    private File createNewFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        String prefix = "CRPIMG_" + Global.netid + "_" + cntcd + "_" + timeStamp + ".jpg";

        File newDirectory = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                RetrofitClient.IMAGE_DIRECTORY_NAME);
        if (!newDirectory.exists()) {
            if (newDirectory.mkdir()) {
                //Log.d(CapNUpVstCard.this.getClass().getName(), newDirectory.getAbsolutePath()+" directory created");
            }
        }

        File file = new File(newDirectory, (prefix));
        if (file.exists()) {
            //this wont be executed
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                Toast.makeText(CapNUpVstCard.this, "File creation Failed ! Try again.", Toast.LENGTH_LONG).show();
            }
        }

        return file;
    }

    public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri,
                proj, // Which columns to return
                null,       // WHERE clause; which rows to return (all rows)
                null,       // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
}/*extends AppCompatActivity {

    ViewDialog progressDialoge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nup_vst_card);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#00E0C6'>Visiting Card</font>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_black);
        progressDialoge=new ViewDialog(CapNUpVstCard.this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        } return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        CapNUpVstCard.this.overridePendingTransition(R.anim.trans_right_in,R.anim.trans_right_out);
    }
}*/
