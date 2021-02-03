package com.w3xplorers.helloct.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.w3xplorers.helloct.R;

public class InfoCenter extends AppCompatActivity {

    public void openInfo() {
        LinearLayout layoutUser = (LinearLayout) findViewById(R.id.idUser);
        LinearLayout layoutUnit = (LinearLayout) findViewById(R.id.idUnit);
        LinearLayout layoutNews = (LinearLayout) findViewById(R.id.idNews);

        layoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terrorIntent = new Intent(InfoCenter.this, UsermenuActivity.class);
                terrorIntent.putExtra("CrimeCategory", v.getId());
                startActivity(terrorIntent);
            }
        });
        layoutUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terrorIntent = new Intent(InfoCenter.this, UnitActivity.class);
                terrorIntent.putExtra("CrimeCategory", v.getId());
                startActivity(terrorIntent);
            }
        });
        layoutNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terrorIntent = new Intent(InfoCenter.this, NewsFeed.class);
                terrorIntent.putExtra("CrimeCategory", v.getId());
                startActivity(terrorIntent);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_center);

        /* Inside the activity */
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        openInfo();
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
            Intent in = new Intent(InfoCenter.this,SearchInfo.class);
            in.putExtra("CrimeCategory", id);
            startActivity(in);
            return true;
        }else if(id == R.id.action_report){
            Intent in = new Intent(InfoCenter.this,AllReports.class);
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }
}
