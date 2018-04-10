package com.example.maryamabdulrhman.ais;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class reportfluDoctor extends AppCompatActivity {
    private static final String TAG="ReportfluPatientActivity";
    private EditText DisplayDate;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    private Spinner gender1,age1;
    private EditText date1;
    private Button report1;
    private Button clear;
    private DatabaseReference database1;
    private TextView loclat1;
    private TextView loclong1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportflu_doctor);
        //create date picker data and view in textfield by calling the id
        DisplayDate = (EditText) findViewById(R.id.date);
        DisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.YEAR,0);
                long ul = c.getTimeInMillis();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        reportfluDoctor.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListener,
                        year, month, day);
                dialog.getDatePicker().setMinDate(ul);
                dialog.getDatePicker().setMaxDate(ul);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }
        });
        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("LongLogTag")
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
        database1 = FirebaseDatabase.getInstance().getReference().child("Flu").push();//



        //initializing views
        gender1 = (Spinner) findViewById(R.id.gender);
        date1 = (EditText) findViewById(R.id.date);
        age1 = (Spinner) findViewById(R.id.ageRange);
        report1 = (Button) findViewById(R.id.reportflubtn);
        clear=(Button) findViewById((R.id.cancelBut));
        loclat1=(TextView) findViewById(R.id.lat1);
        loclong1=(TextView) findViewById(R.id.long1);
        Intent inte=getIntent();
        Bundle b=inte.getExtras();
        if(b!= null){

            Double lat=(Double) b.get("Latitude");
            String lat2=Double.toString(lat);
            loclat1.setText(lat2);
            Double longe=(Double) b.get("Longitude");
            String long2=Double.toString(longe);
            loclong1.setText(long2);

        }

//

        //set hint text on gender spinner
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; //don't display the hint in the list.
            }

        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Female");
        adapter.add("Male");
        adapter.add("Select the Gender"); //don't display last item put it as a hint.


        gender1.setAdapter(adapter);
        gender1.setSelection(adapter.getCount()); //set the hint as default selection

//end of spinner hint for gender

        //set hint text on Agerange spinner
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // don't display last item put it as a hint.
            }

        };
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.add("00–14 Years(Children)");
        adapter2.add("15–24 Years(Youth)");
        adapter2.add("25–64 Years(Adults)");
        adapter2.add("65 and over(Seniors)");
        adapter2.add("Select your age range"); //This is the text that will be displayed as hint.


        age1.setAdapter(adapter2);
        age1.setSelection(adapter2.getCount()); //set the hint as default selection

        //end of spinner hint for age range



        //add listener to report buttons
        report1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreReport();//

            }

        });
        //clear and reset the field to the original
       /* clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date1.setText("");
                gender1.setSelection(2);
                age1.setSelection(4);
                loclat1.setText("");
                loclong1.setText("");


            }
        });//*/


    }
    //this method to get report data and push it to firebase
    private void StoreReport() {
        final String date2 = date1.getText().toString();
        final String gender2 = gender1.getSelectedItem().toString();
        final String age2 = age1.getSelectedItem().toString();
        final String loclt=loclat1.getText().toString();
        final String loclg=loclong1.getText().toString();




        //check if all data are entered
        if (TextUtils.isEmpty(date2)|| gender1.getSelectedItem()=="Select the Gender"||age1.getSelectedItem()=="Select the age range") {
            Toast.makeText(getApplicationContext(), "Make sure you entered all the required data", Toast.LENGTH_SHORT).show();
            if(TextUtils.isEmpty(loclt)||TextUtils.isEmpty(loclg)){
            Intent int1= new Intent(reportfluDoctor.this,MapsActivity.class);
            startActivity(int1);}
            return;

        }

        //push all the data to firebase with attribute name

        else {
            HashMap h = new HashMap();
            h.put("Date:", date2);
            h.put("Age", age2);
            h.put("Gender", gender2);
            h.put("Location-lat",loclt);
            h.put("Location-long",loclg);
            database1.setValue(h).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(reportfluDoctor.this, "successfully added", Toast.LENGTH_LONG).show();
                        date1.setText("");
                        gender1.setSelection(2);
                        age1.setSelection(4);
                        loclat1.setText("");
                        loclong1.setText("");

                    } else {
                        Toast.makeText(reportfluDoctor.this, "Error", Toast.LENGTH_LONG).show();
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
    }//


}
