package com.w3xplorers.helloct.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.w3xplorers.helloct.MySingleton;
import com.w3xplorers.helloct.R;
import com.w3xplorers.helloct.ReportClass;
import com.w3xplorers.helloct.adapter.CustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.w3xplorers.helloct.Config.DATA_URL;


public class AllReports extends AppCompatActivity {
    AlertDialog.Builder builder;
    String device_id;
    //String crime_code[] = new String[1000];
    ArrayList<String> crime_code = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reports);
        /* Inside the activity */
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LoadData();
        //initViews();
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
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
//noinspection SimplifiableIfStatement
// Display menu item's title by using a Toast.
        if (id == R.id.action_edit) {
            Intent in = new Intent(AllReports.this,SearchInfo.class);
            in.putExtra("CrimeCategory", id);
            startActivity(in);

            return true;
        }else if(id == R.id.action_report){
            Toast.makeText(getApplicationContext(), "All Reports", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //For getting unique device ID
    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    public synchronized static String id(Context context) {
//        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);

            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
//        }
        return uniqueID;
    }
    public void LoadData(){
        builder = new AlertDialog.Builder(AllReports.this);
        final String SECURITY_CODE = String.valueOf(123);
        //final Boolean[] totalRes= {};
        device_id = id(this);

//        Toast.makeText(AllReports.this, "Please,be patient!We are getting ready.", Toast.LENGTH_SHORT).show();
//TODO
        //GET Reports from server
        StringRequest userRequest = new StringRequest(Request.Method.POST, DATA_URL+"get_all_crime_data_by_user/"+device_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String s = response.trim();
                        Log.d("reportsResponse" ,response);
                        showUserJSON(response);

                        Toast.makeText(AllReports.this,"Thank you for your patience.Your app is ready to go now.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse response = error.networkResponse;

//                Toast.makeText(AllReports.this,"ErrorMessage: We do not find any report for you."+response, Toast.LENGTH_SHORT).show();
                Log.d("ErrorMessage", String.valueOf(response));

                if(response != null && response.data != null){
                    Toast.makeText(AllReports.this,"ErrorMessage: We do not find any report for you."+response.statusCode, Toast.LENGTH_SHORT).show();

                }else{
                    String errorMessage=error.getClass().getSimpleName();
                    if(!TextUtils.isEmpty(errorMessage)){
                        //Toast.makeText(InfoForm.this,"errorMessage:"+errorMessage, Toast.LENGTH_SHORT).show();

                        builder.setTitle("Server user table Response");
                        builder.setMessage("Response: Please Check Internet Connection of your device."+errorMessage);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

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
                params.put("security_code",SECURITY_CODE);
                return params;
            }
        };
        MySingleton.getmInstance(AllReports.this).addTorequestque(userRequest);
    }

    //get crime code
    private void showUserJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("crime_data");

            for(int i = 0;i< result.length();i++) {
                JSONObject userData = result.getJSONObject(i);
                crime_code.add(userData.getString("crime_code"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("crime_code", String.valueOf(crime_code));
        Log.d("DEVICEID",device_id);

        ArrayList crime_report = new ArrayList();
        //Log.d("crime_code.length", String.valueOf(crime_code.length));
        for(int i=0;i<crime_code.size();i++){
            ReportClass reportCode = new ReportClass();
            reportCode.setCrimeReport(crime_code.get(i));

            crime_report.add(reportCode);
        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        ArrayList crimeData = crime_report;
        CustomAdapter adapter = new CustomAdapter(getApplicationContext(),crimeData,response);
        recyclerView.setAdapter(adapter);
    }
}