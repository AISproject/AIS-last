package com.example.maryamabdulrhman.ais;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class ReportfluActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportflu);
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
