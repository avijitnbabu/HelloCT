package com.w3xplorers.helloct.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.w3xplorers.helloct.R;

public class HomeActivity extends AppCompatActivity {
    private TextView tv;
    
    public void openInfo(){
        LinearLayout layoutTerrorism = (LinearLayout) findViewById(R.id.idTerrorism);
        LinearLayout layoutCrime = (LinearLayout) findViewById(R.id.idNarcotics);
        LinearLayout layoutBomb = (LinearLayout) findViewById(R.id.idBomb);
        LinearLayout layoutForgery = (LinearLayout) findViewById(R.id.idForgery);
        LinearLayout layoutWanted = (LinearLayout) findViewById(R.id.idWanted);
        LinearLayout layoutinfo = (LinearLayout) findViewById(R.id.idInfo);
        tv = (TextView) this.findViewById(R.id.marquee);
        tv.setSelected(true);


        layoutTerrorism.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent terrorIntent = new Intent(HomeActivity.this,InfoForm.class);
                terrorIntent.putExtra("CrimeCategory", v.getId());
                startActivity(terrorIntent);
            }
        });
        layoutCrime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(HomeActivity.this,InfoForm.class);
                aboutIntent.putExtra("CrimeCategory", v.getId());
                startActivity(aboutIntent);
            }
        });
        layoutBomb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(HomeActivity.this,InfoForm.class);
                aboutIntent.putExtra("CrimeCategory", v.getId());
                startActivity(aboutIntent);
            }
        });
        layoutForgery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(HomeActivity.this,InfoForm.class);
                aboutIntent.putExtra("CrimeCategory", v.getId());
                startActivity(aboutIntent);
            }
        });
        layoutWanted.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(HomeActivity.this,WantedActivity.class);
                aboutIntent.putExtra("CrimeCategory", v.getId());
                startActivity(aboutIntent);
            }
        });
        layoutinfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(HomeActivity.this,InfoCenter.class);
                aboutIntent.putExtra("CrimeCategory", v.getId());
                startActivity(aboutIntent);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* Inside the activity */
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        openInfo();
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
            Intent terrorIntent = new Intent(HomeActivity.this,SearchInfo.class);
            terrorIntent.putExtra("CrimeCategory", id);
            startActivity(terrorIntent);
            return true;
        }else if(id == R.id.action_report){
            Intent in = new Intent(HomeActivity.this,AllReports.class);
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }
}
