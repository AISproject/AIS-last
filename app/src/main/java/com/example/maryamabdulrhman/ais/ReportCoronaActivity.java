package com.example.maryamabdulrhman.ais;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;
import java.util.HashMap;

public class ReportCoronaActivity extends AppCompatActivity {

    private static final String TAG="ReportCoronaActivity";
    private TextView DisplayDate;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    private Spinner gender1;
    private EditText date1;
    private Spinner age1;
    private Button report1;
    private Button clear;
    private Spinner tofcontent;
    private DatabaseReference database1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_corona);

        //create date picker data and view in textfield by calling the id
        DisplayDate = (TextView) findViewById(R.id.date);
        DisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        ReportCoronaActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy" + day + "/" + month + "/" + year);
                String date = day + "/" + month + "/" + year;
                DisplayDate.setText(date);
            }
        };//end create picker date


        //create back button in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//

        //getting reference and push flu node to firebase
        database1 = FirebaseDatabase.getInstance().getReference().child("Corona").push();//


        //initializing views
        gender1 = (Spinner) findViewById(R.id.gender);
        date1 = (EditText) findViewById(R.id.date);
        age1 = (Spinner) findViewById(R.id.ageRange);
        tofcontent=(Spinner) findViewById(R.id.type);
        report1 = (Button) findViewById(R.id.reportCoronabu);
        clear=(Button) findViewById((R.id.cancelBut));//

        //add listener to report buttons
        report1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreReport();//

            }

        });
        //clear and reset the field to the original
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date1.setText("");
                gender1.setSelection(0);
                age1.setSelection(0);


            }
        });//

    }
    //this method to get report data and push it to firebase
    private void StoreReport() {
        final String date2 = date1.getText().toString();
        final String gender2 = gender1.getSelectedItem().toString();
        final String age2 = age1.getSelectedItem().toString();
        final String type=tofcontent.getSelectedItem().toString();
        //check if the date is enter
        if (TextUtils.isEmpty(date2)) {
            Toast.makeText(getApplicationContext(), "Enter the date", Toast.LENGTH_SHORT).show();
            //push all the data to firebase with attribute name
        } else {
            HashMap h = new HashMap();
            h.put("Date:", date2);
            h.put("Age", age2);
            h.put("Gender", gender2);
            h.put("Type Of Contact",type);
            database1.setValue(h).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ReportCoronaActivity.this, "successfully added", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ReportCoronaActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }

                }

            });


        }

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
