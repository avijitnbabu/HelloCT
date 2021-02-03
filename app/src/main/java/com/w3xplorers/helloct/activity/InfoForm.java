package com.w3xplorers.helloct.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.PhotoLoader;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.w3xplorers.helloct.Config;
import com.w3xplorers.helloct.MyCommand;
import com.w3xplorers.helloct.MySingleton;
import com.w3xplorers.helloct.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class InfoForm extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    String device_id;
    GalleryPhoto galleryPhoto;
    ProgressDialog progress;

    LinearLayout linearMain;

    LinearLayout imgLayout,vdoLayout,audioLayout,docLayout;
    Button btnSubmit;
    FormEditText CrimeInfo;
    EditText ProviderNo;
    EditText ProviderAdd;
    String cat;

    String server_url = Config.DATA_URL + "submit"; //link for data
    AlertDialog.Builder builder;

    Spinner crimePlace,metro,thana;
    public void openInfo(){
        Button btnCancel = (Button) findViewById(R.id.idCancel);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent cancelIntent = new Intent(InfoForm.this,HomeActivity.class);
                cancelIntent.putExtra("CrimeCategory", v.getId());
                startActivity(cancelIntent);
            }
        });
    }


    //For getting unique device ID

    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);

            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {

        //for videos
        if(requestCode == 10 && resultCode == RESULT_OK){

            progress = new ProgressDialog(InfoForm.this);
            progress.setTitle("Uploading");
            progress.setMessage("Please wait...");
            progress.show();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type  = getMimeType(f.getPath());

                    String file_path = f.getAbsolutePath();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type",content_type)
                            .addFormDataPart("video",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                            .addFormDataPart("parent_id",device_id)
                            .build();
                    okhttp3.Request request = new okhttp3.Request.Builder()
                            //.url("http://192.168.56.1/php_script/save_file.php")
                            //.url("http://www.crime.w3xplorers.com/submit_crime_information.php")
                            .url(Config.DATA_URL+"file_upload")
                            //.url("http://www.hellocmp.org/admin/submit_crime_information/file_upload")
                            .post(request_body)
                            .build();
                    try {
                        okhttp3.Response response = client.newCall(request).execute();
                        if(!response.isSuccessful()){
                            throw new IOException("Error : "+response);
                        }

                        progress.dismiss();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            t.start();
        }

        //for Audio
        if(requestCode == 20 && resultCode == RESULT_OK){

            progress = new ProgressDialog(InfoForm.this);
            progress.setMax(100);
            progress.setTitle("Uploading");
            progress.setMessage("Please wait...");
            progress.setProgress(0);
            progress.show();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type  = getMimeType(f.getPath());

                    String file_path = f.getAbsolutePath();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type",content_type)
                            .addFormDataPart("audio",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                            .addFormDataPart("parent_id",device_id)
                            .build();

                    okhttp3.Request request = new okhttp3.Request.Builder()
                            //.url("http://192.168.56.1/php_script/save_file.php")
                            //.url("http://www.crime.w3xplorers.com/submit_crime_information.php")
                            .url(Config.DATA_URL+"file_upload")
                            //.url("http://www.hellocmp.org/admin/submit_crime_information/file_upload")
                            .post(request_body)
                            .build();

                    try {
                        okhttp3.Response response = client.newCall(request).execute();

                        if(!response.isSuccessful()){
                            throw new IOException("Error : "+response);
                        }

                        progress.dismiss();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            t.start();
        }


        //For documents
        if(requestCode == 30 && resultCode == RESULT_OK){

            progress = new ProgressDialog(InfoForm.this);
            progress.setTitle("Uploading");
            progress.setMessage("Please wait...");
            progress.show();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type  = getMimeType(f.getPath());

                    String file_path = f.getAbsolutePath();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type",content_type)
                            .addFormDataPart("doc",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                            .addFormDataPart("parent_id",device_id)
                            .build();

                    okhttp3.Request request = new okhttp3.Request.Builder()
                            //.url("http://192.168.56.1/php_script/save_file.php")
                            //.url("http://www.crime.w3xplorers.com/submit_crime_information.php")
                            .url(Config.DATA_URL+"file_upload")
                            //.url("http://www.hellocmp.org/admin/submit_crime_information/file_upload")
                            .post(request_body)
                            .build();

                    try {
                        okhttp3.Response response = client.newCall(request).execute();

                        if(!response.isSuccessful()){
                            throw new IOException("Error : "+response);
                        }

                        progress.dismiss();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            t.start();
        }



        //for images

        if(requestCode == 40 && resultCode == RESULT_OK){

            progress = new ProgressDialog(InfoForm.this);
            progress.setTitle("Uploading");
            progress.setMessage("Please wait...");
            progress.show();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type  = getMimeType(f.getPath());

                    String file_path = f.getAbsolutePath();
                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type",content_type)
                            .addFormDataPart("image",file_path.substring(file_path.lastIndexOf("/")+1), file_body)
                            .addFormDataPart("parent_id",device_id)
                            .build();

                    okhttp3.Request request = new okhttp3.Request.Builder()
                            //.url("http://192.168.56.1/php_script/save_file.php")
                            //.url("http://www.crime.w3xplorers.com/submit_crime_information.php")
                            .url(Config.DATA_URL+"file_upload")
                            //.url("http://www.hellocmp.org/admin/submit_crime_information/file_upload")
                            .post(request_body)
                            .build();

                    try {
                        okhttp3.Response response = client.newCall(request).execute();

                        if(!response.isSuccessful()){
                            throw new IOException("Error : "+response);
                        }

                        progress.dismiss();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            t.start();
        }


        if(resultCode == RESULT_OK){
            if(requestCode == 40){
//                galleryPhoto.setPhotoUri(data.getData());
//                String photoPath = galleryPhoto.getPath();
//                imageList.add(photoPath);
//                Log.d(TAG, photoPath);
                File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                String file_path = f.getAbsolutePath();
                try {
                    final Bitmap bitmap = PhotoLoader.init().from(file_path).getBitmap();

                    final ImageView imageView = new ImageView(getApplicationContext());
                    final ImageView btn = new ImageView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setScaleType(ImageView.ScaleType.CENTER);
                    //imageView.setMaxWidth(250);
                    imageView.setPadding(0, 0, 0, 10);
                    imageView.setAdjustViewBounds(true);
                    imageView.setImageBitmap(bitmap);

                    btn.setLayoutParams(layoutParams);
                    btn.setMaxWidth(50);
                    //btn.setScaleType(ImageView.ScaleType.FIT_END);
                    btn.setAdjustViewBounds(true);
                    btn.setImageDrawable(getResources().getDrawable(R.drawable.close));

                    linearMain.addView(btn);
                    linearMain.addView(imageView);

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btn.setVisibility(View.GONE);
                            imageView.setVisibility(View.GONE);
                        }
                    });

                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Error while loading image", Toast.LENGTH_SHORT).show();
                }

            }

            if(requestCode == 10 || requestCode == 20 || requestCode == 30){
                File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                //String content_type  = getMimeType(f.getPath());
                    String file_path = f.getAbsolutePath();
                    final TextView txtView = new TextView(getApplicationContext());
                    final ImageView btn = new ImageView(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                    txtView.setLayoutParams(layoutParams);
                    //txtView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    txtView.setPadding(0, 0, 0, 10);
                    //txtView.setAdjustViewBounds(true);
                    txtView.setTextColor(Color.rgb(200,0,0));
                    txtView.setText(file_path);


                    btn.setLayoutParams(layoutParams);
                    btn.setMaxWidth(50);
                    //btn.setScaleType(ImageView.ScaleType.FIT_END);
                    btn.setAdjustViewBounds(true);
                    btn.setImageDrawable(getResources().getDrawable(R.drawable.close));

                    linearMain.addView(btn);
                    linearMain.addView(txtView);

                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btn.setVisibility(View.GONE);
                            txtView.setVisibility(View.GONE);
                        }
                    });



            }
        }



    }

    private String getMimeType(String path) {

        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_form);

        /* Inside the activity */
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
                return;
            }
        }

        openInfo();

        device_id = id(this);
        Log.d("InfoDEVICEID",device_id);
        //getting the id crime category

        Intent mIntent = getIntent();
        int intValue = mIntent.getIntExtra("CrimeCategory", 0);
        if(intValue == 0) {
            // error handling (Will come in this if when button id is invalid)
            Toast.makeText(InfoForm.this, "Category not found", Toast.LENGTH_SHORT).show();
        }else
        {
            if(intValue == R.id.idTerrorism){
                cat = "01";
                Toast.makeText(InfoForm.this, "Welcome to Terrorism Category.", Toast.LENGTH_LONG).show();
            }

            if(intValue == R.id.idNarcotics){
                cat = "02";
                Toast.makeText(InfoForm.this, "Welcome to Narcotics Category.", Toast.LENGTH_SHORT).show();
            }

            if(intValue == R.id.idBomb){
                cat = "03";
                Toast.makeText(InfoForm.this, "Welcome to Bomb Category.", Toast.LENGTH_SHORT).show();
            }

            if(intValue == R.id.idForgery){
                cat = "04";
                Toast.makeText(InfoForm.this, "Welcome to Forgery Category.", Toast.LENGTH_SHORT).show();
            }
        }
        //uploading media files

        imgLayout = (LinearLayout) findViewById(R.id.idImg);
        vdoLayout = (LinearLayout) findViewById(R.id.idVdo);
        audioLayout = (LinearLayout) findViewById(R.id.idAudio);
        docLayout = (LinearLayout) findViewById(R.id.idDoc);


        linearMain = (LinearLayout)findViewById(R.id.linearMain);
        galleryPhoto = new GalleryPhoto(getApplicationContext());
        vdoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialFilePicker()
                        .withActivity(InfoForm.this)
                        .withRequestCode(10)
                        .start();
            }
        });

        audioLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withActivity(InfoForm.this)
                        .withRequestCode(20)
                        .start();
            }
        });

        docLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialFilePicker()
                        .withActivity(InfoForm.this)
                        .withRequestCode(30)
                        .start();
            }
        });

        imgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withActivity(InfoForm.this)
                        .withRequestCode(40)
                        .start();

            }
        });

        crimePlace = (Spinner) findViewById(R.id.idCrimePlace);
        metro = (Spinner) findViewById(R.id.idMetro);
        thana = (Spinner) findViewById(R.id.idThana);
        crimePlace.setOnItemSelectedListener(this);
        metro.setOnItemSelectedListener(this);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        CrimeInfo = (FormEditText) findViewById(R.id.idcrime_info);
        ProviderNo = (EditText) findViewById(R.id.idProviderNo);
        ProviderAdd = (EditText) findViewById(R.id.idProviderAdd);
        builder = new AlertDialog.Builder(InfoForm.this);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormEditText[] allFields    = { CrimeInfo };
                boolean allValid = true;
                for (FormEditText field: allFields) {
                    allValid = field.testValidity() && allValid;
                }
                if (allValid) {
                    AlertDialog.Builder aBuilder = new AlertDialog.Builder(
                            InfoForm.this);
                    aBuilder.setTitle("Warning:");
                    aBuilder.setMessage("Do you want to submit the provided Information?");
                    aBuilder.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Toast.makeText(getApplicationContext(),"You have denied to submit the Information.",Toast.LENGTH_LONG).show();
                                }
                            });
                    aBuilder.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    //Toast.makeText(getApplicationContext(),"Yes is clicked",Toast.LENGTH_LONG).show();

                                    final String crimeCategory, crimeinfo,div,dis,Thana,providerNo,providerAdd,image,vdo,security_code;
                                    Toast.makeText(InfoForm.this, "Please,Wait for Response.", Toast.LENGTH_SHORT).show();

                                    security_code = String.valueOf(123);
                                    crimeCategory = String.valueOf(cat);
                                    crimeinfo = CrimeInfo.getText().toString();
                                    div = crimePlace.getSelectedItem().toString();
                                    dis = metro.getSelectedItem().toString();
                                    Thana = thana.getSelectedItem().toString();
                                    providerNo = ProviderNo.getText().toString();
                                    providerAdd = ProviderAdd.getText().toString();
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    String s = response.trim();

                                                    builder.setTitle("Response");
                                                    builder.setMessage("Response: " + response);

                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            CrimeInfo.setText("");
                                                            ProviderNo.setText("");
                                                            ProviderAdd.setText("");

                                                            //linearMain.addView();
                                                            Toast.makeText(getApplicationContext(),"Thank you for your informations.",Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                    AlertDialog alertDialog = builder.create();
                                                    alertDialog.show();
                                                }
                                            }
                                            , new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            NetworkResponse response = error.networkResponse;
                                            if(response != null && response.data != null){
                                                Toast.makeText(InfoForm.this,"errorMessage:"+response.statusCode, Toast.LENGTH_SHORT).show();
                                            }else{
                                                String errorMessage=error.getClass().getSimpleName();
                                                if(!TextUtils.isEmpty(errorMessage)){
                                                    //Toast.makeText(InfoForm.this,"errorMessage:"+errorMessage, Toast.LENGTH_SHORT).show();

                                                    builder.setTitle("Server Response");
                                                    builder.setMessage("Response: Please Check Internet Connection of your device."+errorMessage);
                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            CrimeInfo.setText("");
                                                            ProviderNo.setText("");
                                                            ProviderAdd.setText("");
                                                        }
                                                    });
                                                    AlertDialog alertDialog = builder.create();
                                                    alertDialog.show();
                                                }
                                            }
                                        }
                                    }){


                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {


                                            Map<String,String> params = new HashMap<String, String>();
                                            params.put("crime_category",crimeCategory);
                                            params.put("crime_info",crimeinfo);
                                            params.put("division",div);
                                            params.put("district",dis);
                                            params.put("thana",Thana);
                                            params.put("provider_phone",providerNo);
                                            params.put("provider_address",providerAdd);
                                            params.put("parent_id",device_id);
                                            params.put("security_code",security_code);
                                            return params;
                                        }
                                    };

                                    MySingleton.getmInstance(InfoForm.this).addTorequestque(stringRequest);

                                }
                            });
                    aBuilder.show();
                } else {
                    // EditText are going to appear with an exclamation mark and an explicative message.
                }

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Activity's overrided method used to set the menu file
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }
    // Activity's overrided method used to perform click events on menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            Intent terrorIntent = new Intent(InfoForm.this,SearchInfo.class);
            terrorIntent.putExtra("CrimeCategory", id);
            startActivity(terrorIntent);

            return true;
        }else if(id == R.id.action_report){
            Intent in = new Intent(InfoForm.this,AllReports.class);
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.idCrimePlace:
                String sp1 = String.valueOf(crimePlace.getSelectedItem());
                //Toast.makeText(this, arg1.getId(), Toast.LENGTH_SHORT).show();

                if (sp1.contentEquals("চট্টগ্রাম")) {
                    List<String> list = new ArrayList<String>();
                    list.add("চট্টগ্রাম মহানগর");
                    list.add("চট্টগ্রাম");
                    list.add("বান্দরবান");
                    list.add("ব্রাহ্মণবাড়িয়া");
                    list.add("চাঁদপুর");
                    list.add("কুমিল্লা");
                    list.add("কক্সবাজার");
                    list.add("ফেনী");
                    list.add("খাগড়াছড়ি");
                    list.add("লক্ষ্মীপুর");
                    list.add("নোয়াখালী");
                    list.add("রাঙামাটি");
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    metro.setAdapter(dataAdapter);
                }

                if (sp1.contentEquals("রাজশাহী")) {
                    List<String> list = new ArrayList<String>();
                    list.add("বগুড়া");
                    list.add("পাবনা");
                    list.add("রাজশাহী");
                    list.add("জয়পুরহাট");
                    list.add("চাঁপাইনবাবগঞ্জ");
                    list.add("নওগাঁ");
                    list.add("নাটোর");
                    list.add("সিরাজগঞ্জ");
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter2.notifyDataSetChanged();
                    metro.setAdapter(dataAdapter2);
                }

                if (sp1.contentEquals("খুলনা")) {
                    List<String> list = new ArrayList<String>();
                    list.add("যশোর");
                    list.add("সাতক্ষীরা");
                    list.add("মেহেরপুর");
                    list.add("নড়াইল");
                    list.add("চুয়াডাঙ্গা");
                    list.add("কুষ্টিয়া");
                    list.add("মাগুরা");
                    list.add("খুলনা");
                    list.add("বাগেরহাট");
                    list.add("ঝিনাইদহ");
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter2.notifyDataSetChanged();
                    metro.setAdapter(dataAdapter2);
                }

                if(sp1.contentEquals("বরিশাল")) {
                    List<String> list = new ArrayList<String>();
                    list.add("ঝালকাঠি");
                    list.add("পটুয়াখালী");
                    list.add("পিরোজপুর");
                    list.add("বরিশাল");
                    list.add("ভোলা");
                    list.add("বরগুনা");
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter2.notifyDataSetChanged();
                    metro.setAdapter(dataAdapter2);
                }
                if(sp1.contentEquals("সিলেট")) {
                    List<String> list = new ArrayList<String>();
                    list.add("সিলেট");
                    list.add("মৌলভীবাজার");
                    list.add("হবিগঞ্জ");
                    list.add("সুনামগঞ্জ");
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter2.notifyDataSetChanged();
                    metro.setAdapter(dataAdapter2);
                }
                if(sp1.contentEquals("ঢাকা")) {
                    List<String> list = new ArrayList<String>();
                    list.add("নরসিংদী");
                    list.add("গাজীপুর");
                    list.add("শরীয়তপুর");
                    list.add("নারায়ণগঞ্জ");
                    list.add("টাঙ্গাইল");
                    list.add("কিশোরগঞ্জ");
                    list.add("মানিকগঞ্জ");
                    list.add("ঢাকা");
                    list.add("মুন্সিগঞ্জ");
                    list.add("রাজবাড়ী");
                    list.add("মাদারীপুর");
                    list.add("গোপালগঞ্জ");
                    list.add("ফরিদপুর");
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter2.notifyDataSetChanged();
                    metro.setAdapter(dataAdapter2);
                }
                if(sp1.contentEquals("রংপুর")) {
                    List<String> list = new ArrayList<String>();
                    list.add("পঞ্চগড়");
                    list.add("দিনাজপুর");
                    list.add("লালমনিরহাট");
                    list.add("নীলফামারী");
                    list.add("গাইবান্ধা");
                    list.add("ঠাকুরগাঁও");
                    list.add("রংপুর");
                    list.add("কুড়িগ্রাম");
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter2.notifyDataSetChanged();
                    metro.setAdapter(dataAdapter2);
                }
                if(sp1.contentEquals("ময়মনসিংহ")) {
                    List<String> list = new ArrayList<String>();
                    list.add("শেরপুর");
                    list.add("ময়মনসিংহ");
                    list.add("জামালপুর");
                    list.add("নেত্রকোণা");
                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter2.notifyDataSetChanged();
                    metro.setAdapter(dataAdapter2);
                }

                break;


            case R.id.idMetro:

                String sp2= String.valueOf(metro.getSelectedItem());
                //Toast.makeText(this, sp2, Toast.LENGTH_SHORT).show();


                if(sp2.contentEquals("চট্টগ্রাম")) {
                    List<String> listThana = new ArrayList<String>();
                    listThana.add("মিরসরাই");
                    listThana.add("সীতাকুণ্ড");
                    listThana.add("সন্দ্বীপ");

                    listThana.add("ফটিকছড়ি");
                    listThana.add("নাজিরহাট");
                    listThana.add("রাউজান");
                    listThana.add("হাটহাজারী");

                    listThana.add("বোয়ালখালী");
                    listThana.add("আনোয়ারা");
                    listThana.add("চন্দনাইশ");
                    listThana.add("দোহাজারী");
                    listThana.add("সাতকানিয়া");
                    listThana.add("বাঁশখালী");
                    listThana.add("লোহাগড়া");
                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, listThana);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter3.notifyDataSetChanged();
                    thana.setAdapter(dataAdapter3);
                }

                else if(sp2.contentEquals("চট্টগ্রাম মহানগর")) {
                    List<String> listThana = new ArrayList<String>();
                    listThana.add("কোতোয়ালী");
                    listThana.add("খুলশী");
                    listThana.add("চান্দগাঁও");
                    listThana.add("ডবলমুরিং");
                    listThana.add("পতেঙ্গা");
                    listThana.add("পাঁচলাইশ");
                    listThana.add("পাহাড়তলী");
                    listThana.add("বন্দর");
                    listThana.add("বাকলিয়া");
                    listThana.add("বায়জিদ বোস্তামী");
                    listThana.add("হালিশহর");
                    listThana.add("চকবাজার");
                    listThana.add("আকবরশাহ");
                    listThana.add("সদরঘাট");
                    listThana.add("ইপিজেড");
                    listThana.add("কর্ণফুলী");

                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, listThana);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter3.notifyDataSetChanged();
                    thana.setAdapter(dataAdapter3);
                }



                if(sp2.contentEquals("বান্দরবান")) {
                    List<String> listThana = new ArrayList<String>();
                    listThana.add("রোয়াঙ্গছড়ি");
                    listThana.add("রুমা");
                    listThana.add("থানচি");
                    listThana.add("আলীকদম");
                    listThana.add("বাইশাড়ী");
                    listThana.add("নাইখংছড়ি");
                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, listThana);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter3.notifyDataSetChanged();
                    thana.setAdapter(dataAdapter3);
                }

                if(sp2.contentEquals("খাগড়াছড়ি")) {
                    List<String> listThana = new ArrayList<String>();
                    listThana.add(" শিলছড়ি বাজার");
                    listThana.add("দীঘিনালা");
                    listThana.add("পানছড়ি");
                    listThana.add("খাগড়াছড়ি");
                    listThana.add("মাটিরাঙ্গা");
                    listThana.add("রামগড়");
                    listThana.add("মহলছড়ি");
                    listThana.add("মানিকছড়ি");
                    listThana.add("লক্ষীছড়ি");
                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, listThana);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter3.notifyDataSetChanged();
                    thana.setAdapter(dataAdapter3);
                }
                if(sp2.contentEquals("ব্রাহ্মণবাড়িয়া")) {
                    List<String> listThana = new ArrayList<String>();
                    listThana.add("নাছিরাবাদ");
                    listThana.add("সরাইল");
                    listThana.add("আশুগঞ্জ");
                    listThana.add("নবীনগর");
                    listThana.add("আখাউড়া");
                    listThana.add("কসবা");
                    listThana.add("বাঞ্ছারামপুর");
                    listThana.add("কুটি");
                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, listThana);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter3.notifyDataSetChanged();
                    thana.setAdapter(dataAdapter3);
                }
                if(sp2.contentEquals("চাঁদপুর")) {
                    List<String> listThana = new ArrayList<String>();
                    listThana.add("ষাটনল");
                    listThana.add("মতলব");
                    listThana.add("কচুয়া");
                    listThana.add("শাহরাস্তি");
                    listThana.add("ফরিদগঞ্জ");
                    listThana.add("হাইমচর");
                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, listThana);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter3.notifyDataSetChanged();
                    thana.setAdapter(dataAdapter3);
                }

                if(sp2.contentEquals("কুমিল্লা")) {
                    List<String> listThana = new ArrayList<String>();
                    listThana.add("মুরাদনগর");
                    listThana.add("হোমনা");
                    listThana.add("ব্রাহ্মণপাড়া");
                    listThana.add("দাউদকান্দি");
                    listThana.add("দেবিদ্বার");
                    listThana.add("বুড়িচং");
                    listThana.add("বড়ুরা");
                    listThana.add("চৌদ্দ গ্রাম");
                    listThana.add("গুণবতী");
                    listThana.add("লাকসাম");
                    listThana.add("লাঙ্গলকোট");

                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, listThana);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter3.notifyDataSetChanged();
                    thana.setAdapter(dataAdapter3);
                }
                if(sp2.contentEquals("কক্সবাজার")) {

                    List<String> listThana = new ArrayList<String>();
                    listThana.add("হাড়ভাঙ্গা");
                    listThana.add("কুতুবদিয়া");
                    listThana.add("চকরিয়া");
                    listThana.add("মহেশখালী");
                    listThana.add("রামু");
                    listThana.add("উখিয়া");
                    listThana.add("টেকনাফ");
                    listThana.add("শাহপরী দ্বীপ");


                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, listThana);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter3.notifyDataSetChanged();
                    thana.setAdapter(dataAdapter3);
                }
                if(sp2.contentEquals("লক্ষ্মীপুর")) {
                    List<String> listThana = new ArrayList<String>();
                    listThana.add("রামগঞ্জ");
                    listThana.add("রায়পুর");
                    listThana.add("বেগমগঞ্জ");
                    listThana.add("চরআলেকজেন্ডার");
                    listThana.add("রামগতি");

                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, listThana);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter3.notifyDataSetChanged();
                    thana.setAdapter(dataAdapter3);
                }
                if(sp2.contentEquals("নোয়াখালী")) {
                    List<String> listThana = new ArrayList<String>();
                    listThana.add("চাটখিল");
                    listThana.add("সেনবাগ");
                    listThana.add("সোনাইমুড়ি");
                    listThana.add("সুধারাম");
                    listThana.add("কোম্পানীগঞ্জ");
                    listThana.add("খাসেরচর");
                    listThana.add("হাতিয়া");
                    listThana.add("সোনাদিয়া");
                    listThana.add("নিঝুমদ্বীপ");

                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, listThana);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter3.notifyDataSetChanged();
                    thana.setAdapter(dataAdapter3);
                }
                if(sp2.contentEquals("ফেনী")) {
                    List<String> listThana = new ArrayList<String>();
                    listThana.add("বিলুনিয়া");
                    listThana.add("পরশুরাম");
                    listThana.add("ফুলগাজী");
                    listThana.add("ছাগলনাইয়া");
                    listThana.add("দাগনভুইয়া");
                    listThana.add("সোনাগাজী");

                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, listThana);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter3.notifyDataSetChanged();
                    thana.setAdapter(dataAdapter3);
                }
                if(sp2.contentEquals("রাঙামাটি")) {
                    List<String> listThana = new ArrayList<String>();
                    listThana.add("লুংতিয়া");
                    listThana.add("সাজেক");
                    listThana.add("হরিজয়পাড়া");
                    listThana.add("বাঘাইছড়ি");
                    listThana.add("লাঙ্গাড়ু");
                    listThana.add("নান্নেরচর");
                    listThana.add("বরকল");
                    listThana.add("কাউখালী");
                    listThana.add("জুরাইছড়ি");
                    listThana.add("রাঙ্গুনিয়া");
                    listThana.add("কাপ্তাই");
                    listThana.add("বিলাইছড়ি");
                    listThana.add("চন্দ্রঘোনা");
                    listThana.add("রাজস্থালী");
                    listThana.add("ফারুয়া");

                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, listThana);
                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter3.notifyDataSetChanged();
                    thana.setAdapter(dataAdapter3);
                }
                /*String sp2 = String.valueOf(metro.getSelectedItem());
                if (sp2.contentEquals("Salary")) {
                    List<String> list = new ArrayList<String>();
                    list.add("money");
                    list.add("dollar");
                    list.add("rupes");
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    thana.setAdapter(dataAdapter);
                }

                if (sp2.contentEquals("Sales")) {
                    List<String> list = new ArrayList<String>();
                    list.add("money1");
                    list.add("dollar2");
                    list.add("rupes3");
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dataAdapter.notifyDataSetChanged();
                    thana.setAdapter(dataAdapter);
                }*/
                break;


            default:


                break;

        }

/*
        //Spinner for metro
        if(sp1.contentEquals("চট্টগ্রাম")) {
            List<String> list = new ArrayList<String>();
            list.add("চট্টগ্রাম মহানগর");
            list.add("চট্টগ্রাম");
            list.add("বান্দরবান");
            list.add("ব্রাহ্মণবাড়িয়া");
            list.add("চাঁদপুর");
            list.add("কুমিল্লা");
            list.add("কক্সবাজার");
            list.add("ফেনী");
            list.add("খাগড়াছড়ি");
            list.add("লক্ষ্মীপুর");
            list.add("নোয়াখালী");
            list.add("রাঙামাটি");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            metro.setAdapter(dataAdapter);
        }
        if(sp1.contentEquals("রাজশাহী")) {
            List<String> list = new ArrayList<String>();
            list.add("বগুড়া");
            list.add("পাবনা");
            list.add("রাজশাহী");
            list.add("জয়পুরহাট");
            list.add("চাঁপাইনবাবগঞ্জ");
            list.add("নওগাঁ");
            list.add("নাটোর");
            list.add("সিরাজগঞ্জ");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            metro.setAdapter(dataAdapter2);
        }
        if(sp1.contentEquals("খুলনা")) {
            List<String> list = new ArrayList<String>();
            list.add("যশোর");
            list.add("সাতক্ষীরা");
            list.add("মেহেরপুর");
            list.add("নড়াইল");
            list.add("চুয়াডাঙ্গা");
            list.add("কুষ্টিয়া");
            list.add("মাগুরা");
            list.add("খুলনা");
            list.add("বাগেরহাট");
            list.add("ঝিনাইদহ");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            metro.setAdapter(dataAdapter2);
        }

        if(sp1.contentEquals("বরিশাল")) {
            List<String> list = new ArrayList<String>();
            list.add("ঝালকাঠি");
            list.add("পটুয়াখালী");
            list.add("পিরোজপুর");
            list.add("বরিশাল");
            list.add("ভোলা");
            list.add("বরগুনা");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            metro.setAdapter(dataAdapter2);
        }
        if(sp1.contentEquals("সিলেট")) {
            List<String> list = new ArrayList<String>();
            list.add("সিলেট");
            list.add("মৌলভীবাজার");
            list.add("হবিগঞ্জ");
            list.add("সুনামগঞ্জ");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            metro.setAdapter(dataAdapter2);
        }
        if(sp1.contentEquals("ঢাকা")) {
            List<String> list = new ArrayList<String>();
            list.add("নরসিংদী");
            list.add("গাজীপুর");
            list.add("শরীয়তপুর");
            list.add("নারায়ণগঞ্জ");
            list.add("টাঙ্গাইল");
            list.add("কিশোরগঞ্জ");
            list.add("মানিকগঞ্জ");
            list.add("ঢাকা");
            list.add("মুন্সিগঞ্জ");
            list.add("রাজবাড়ী");
            list.add("মাদারীপুর");
            list.add("গোপালগঞ্জ");
            list.add("ফরিদপুর");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            metro.setAdapter(dataAdapter2);
        }
        if(sp1.contentEquals("রংপুর")) {
            List<String> list = new ArrayList<String>();
            list.add("পঞ্চগড়");
            list.add("দিনাজপুর");
            list.add("লালমনিরহাট");
            list.add("নীলফামারী");
            list.add("গাইবান্ধা");
            list.add("ঠাকুরগাঁও");
            list.add("রংপুর");
            list.add("কুড়িগ্রাম");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            metro.setAdapter(dataAdapter2);
        }
        if(sp1.contentEquals("ময়মনসিংহ")) {
            List<String> list = new ArrayList<String>();
            list.add("শেরপুর");
            list.add("ময়মনসিংহ");
            list.add("জামালপুর");
            list.add("নেত্রকোণা");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            metro.setAdapter(dataAdapter2);
        }

        //Spinner for thana

/*
        String sp2= String.valueOf(metro.getSelectedItem());
       Toast.makeText(this, sp2, Toast.LENGTH_SHORT).show();


        if(sp2.contentEquals("চট্টগ্রাম")) {
            List<String> listThana = new ArrayList<String>();
            listThana.add("মিরসরাই");
            listThana.add("সীতাকুণ্ড");
            listThana.add("সন্দ্বীপ");

            listThana.add("ফটিকছড়ি");
            listThana.add("নাজিরহাট");
            listThana.add("রাউজান");
            listThana.add("হাটহাজারী");

            listThana.add("বোয়ালখালী");
            listThana.add("আনোয়ারা");
            listThana.add("চন্দনাইশ");
            listThana.add("দোহাজারী");
            listThana.add("সাতকানিয়া");
            listThana.add("বাঁশখালী");
            listThana.add("লোহাগড়া");
            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listThana);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            thana.setAdapter(dataAdapter3);
        }

        else if(sp2.contentEquals("চট্টগ্রাম মহানগর")) {
            List<String> listThana = new ArrayList<String>();
            listThana.add("কোতোয়ালী");
            listThana.add("খুলশী");
            listThana.add("চান্দগাঁও");
            listThana.add("ডবলমুরিং");
            listThana.add("পতেঙ্গা");
            listThana.add("পাঁচলাইশ");
            listThana.add("পাহাড়তলী");
            listThana.add("বন্দর");
            listThana.add("বাকলিয়া");
            listThana.add("বায়জিদ বোস্তামী");
            listThana.add("হালিশহর");
            listThana.add("চকবাজার");
            listThana.add("আকবরশাহ");
            listThana.add("সদরঘাট");
            listThana.add("ইপিজেড");
            listThana.add("কর্ণফুলী");

            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listThana);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            thana.setAdapter(dataAdapter3);
        }



        if(sp2.contentEquals("বান্দরবান")) {
            List<String> listThana = new ArrayList<String>();
            listThana.add("রোয়াঙ্গছড়ি");
            listThana.add("রুমা");
            listThana.add("থানচি");
            listThana.add("আলীকদম");
            listThana.add("বাইশাড়ী");
            listThana.add("নাইখংছড়ি");
            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listThana);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            thana.setAdapter(dataAdapter3);
        }

        if(sp2.contentEquals("খাগড়াছড়ি")) {
            List<String> listThana = new ArrayList<String>();
            listThana.add(" শিলছড়ি বাজার");
            listThana.add("দীঘিনালা");
            listThana.add("পানছড়ি");
            listThana.add("খাগড়াছড়ি");
            listThana.add("মাটিরাঙ্গা");
            listThana.add("রামগড়");
            listThana.add("মহলছড়ি");
            listThana.add("মানিকছড়ি");
            listThana.add("লক্ষীছড়ি");
            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listThana);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            thana.setAdapter(dataAdapter3);
        }
        if(sp2.contentEquals("ব্রাহ্মণবাড়িয়া")) {
            List<String> listThana = new ArrayList<String>();
            listThana.add("নাছিরাবাদ");
            listThana.add("সরাইল");
            listThana.add("আশুগঞ্জ");
            listThana.add("নবীনগর");
            listThana.add("আখাউড়া");
            listThana.add("কসবা");
            listThana.add("বাঞ্ছারামপুর");
            listThana.add("কুটি");
            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listThana);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            thana.setAdapter(dataAdapter3);
        }
        if(sp2.contentEquals("চাঁদপুর")) {
            List<String> listThana = new ArrayList<String>();
            listThana.add("ষাটনল");
            listThana.add("মতলব");
            listThana.add("কচুয়া");
            listThana.add("শাহরাস্তি");
            listThana.add("ফরিদগঞ্জ");
            listThana.add("হাইমচর");
            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listThana);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            thana.setAdapter(dataAdapter3);
        }

        if(sp2.contentEquals("কুমিল্লা")) {
            List<String> listThana = new ArrayList<String>();
            listThana.add("মুরাদনগর");
            listThana.add("হোমনা");
            listThana.add("ব্রাহ্মণপাড়া");
            listThana.add("দাউদকান্দি");
            listThana.add("দেবিদ্বার");
            listThana.add("বুড়িচং");
            listThana.add("বড়ুরা");
            listThana.add("চৌদ্দ গ্রাম");
            listThana.add("গুণবতী");
            listThana.add("লাকসাম");
            listThana.add("লাঙ্গলকোট");

            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listThana);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            thana.setAdapter(dataAdapter3);
        }
        if(sp2.contentEquals("কক্সবাজার")) {

            List<String> listThana = new ArrayList<String>();
            listThana.add("হাড়ভাঙ্গা");
            listThana.add("কুতুবদিয়া");
            listThana.add("চকরিয়া");
            listThana.add("মহেশখালী");
            listThana.add("রামু");
            listThana.add("উখিয়া");
            listThana.add("টেকনাফ");
            listThana.add("শাহপরী দ্বীপ");


            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listThana);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            thana.setAdapter(dataAdapter3);
        }
        if(sp2.contentEquals("লক্ষ্মীপুর")) {
            List<String> listThana = new ArrayList<String>();
            listThana.add("রামগঞ্জ");
            listThana.add("রায়পুর");
            listThana.add("বেগমগঞ্জ");
            listThana.add("চরআলেকজেন্ডার");
            listThana.add("রামগতি");

            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listThana);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            thana.setAdapter(dataAdapter3);
        }
        if(sp2.contentEquals("নোয়াখালী")) {
            List<String> listThana = new ArrayList<String>();
            listThana.add("চাটখিল");
            listThana.add("সেনবাগ");
            listThana.add("সোনাইমুড়ি");
            listThana.add("সুধারাম");
            listThana.add("কোম্পানীগঞ্জ");
            listThana.add("খাসেরচর");
            listThana.add("হাতিয়া");
            listThana.add("সোনাদিয়া");
            listThana.add("নিঝুমদ্বীপ");

            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listThana);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            thana.setAdapter(dataAdapter3);
        }
        if(sp2.contentEquals("ফেনী")) {
            List<String> listThana = new ArrayList<String>();
            listThana.add("বিলুনিয়া");
            listThana.add("পরশুরাম");
            listThana.add("ফুলগাজী");
            listThana.add("ছাগলনাইয়া");
            listThana.add("দাগনভুইয়া");
            listThana.add("সোনাগাজী");

            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listThana);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            thana.setAdapter(dataAdapter3);
        }
        if(sp2.contentEquals("রাঙামাটি")) {
            List<String> listThana = new ArrayList<String>();
            listThana.add("লুংতিয়া");
            listThana.add("সাজেক");
            listThana.add("হরিজয়পাড়া");
            listThana.add("বাঘাইছড়ি");
            listThana.add("লাঙ্গাড়ু");
            listThana.add("নান্নেরচর");
            listThana.add("বরকল");
            listThana.add("কাউখালী");
            listThana.add("জুরাইছড়ি");
            listThana.add("রাঙ্গুনিয়া");
            listThana.add("কাপ্তাই");
            listThana.add("বিলাইছড়ি");
            listThana.add("চন্দ্রঘোনা");
            listThana.add("রাজস্থালী");
            listThana.add("ফারুয়া");

            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, listThana);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            thana.setAdapter(dataAdapter3);
        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    
}
