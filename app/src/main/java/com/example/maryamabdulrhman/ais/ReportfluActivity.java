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

public class ReportfluActivity extends AppCompatActivity {

    private static final String TAG="ReportfluActivity";
    private TextView DisplayDate;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    private Spinner gender;
    private EditText date;
    private Spinner age;
    private Button report;
    private Button clear;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportflu);

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
                        ReportfluActivity.this,
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
        database = FirebaseDatabase.getInstance().getReference().child("Flu").push();//


        //initializing views
        gender = (Spinner) findViewById(R.id.gender);
        date = (EditText) findViewById(R.id.date);
        age = (Spinner) findViewById(R.id.ageRange);
        report = (Button) findViewById(R.id.reportflubtn);
        clear=(Button) findViewById((R.id.cancelBut));//

        //add listener to report buttons
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreReport();//

            }

        });
        //clear and reset the field to the ordinal
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date.setText("");
                gender.setSelection(0);
                age.setSelection(0);


            }
        });//

    }
//this method to get report data and push it to firebase
private void StoreReport() {
    final String date1 = date.getText().toString();
    final String gender1 = gender.getSelectedItem().toString();
    final String age1 = age.getSelectedItem().toString();
    //check if the date is enter
    if (TextUtils.isEmpty(date1)) {
        Toast.makeText(getApplicationContext(), "Enter the date", Toast.LENGTH_SHORT).show();
        //push all the data to firebase with attribute name
    } else {
        HashMap h = new HashMap();
        h.put("Date:", date1);
        h.put("Age", age1);
        h.put("Gender", gender1);
        database.setValue(h).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ReportfluActivity.this, "successfully added", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ReportfluActivity.this, "Error", Toast.LENGTH_LONG).show();
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
