package com.example.maryamabdulrhman.ais;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class ReportCoronaActivity extends AppCompatActivity {
    private static final String TAG="ReportCoronaActivity";
    private TextView DisplayDate;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_corona);

        //create date picker data
        DisplayDate= (TextView) findViewById(R.id.date);
        DisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year=c.get(Calendar.YEAR);
                int month=c.get(Calendar.MONTH);
                int day=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        ReportCoronaActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                Log.d(TAG,"onDateSet: dd/mm/yyy" + day + "/" + month + "/" + year );
                String date= day + "/" + month+ "/" + year;
                DisplayDate.setText(date);
            }
        };
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

