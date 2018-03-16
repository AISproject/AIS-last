package com.example.maryamabdulrhman.ais;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class ReportCoronaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_corona);
        //create back button in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
    //back to report interface method
    @Override
    public boolean onOptionsItemSelected(MenuItem item){


        int id =item.getItemId();
        if(id== android.R.id.home){
            this.finish();
        }
        return onContextItemSelected(item);
    }
    }

