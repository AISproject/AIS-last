package com.example.maryamabdulrhman.ais;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;

public class SignupDoctor extends AppCompatActivity {
    //Define local variables
    private static final String TAG = "SignupDoctor";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText Fname, Lname, EmailF, PassF, CpassF, MedID, AboutF, mDisplayDate;
    private Spinner GendS, MajorS;
    private Button SignupB;
    private ProgressDialog mPd;
    Boolean flagEXist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_doctor);
        //Initialize variables with the interfaces' contents
        Fname = (EditText) findViewById(R.id.FirstName);
        Lname = (EditText) findViewById(R.id.LastName);
        EmailF = (EditText) findViewById(R.id.email);
        PassF = (EditText) findViewById(R.id.password);
        CpassF = (EditText) findViewById(R.id.cpassword);
        MedID = (EditText) findViewById(R.id.medicalid);
        AboutF = (EditText) findViewById(R.id.aboutMe);
        GendS = (Spinner) findViewById(R.id.sgender);
        SignupB = (Button) findViewById(R.id.SignupBtn);
        MajorS=(Spinner) findViewById(R.id.Major);
        mDisplayDate = (EditText) findViewById(R.id.bdate);
        mPd = new ProgressDialog(this);
        //Direct firebase authentication variable to the right instance (root, child..etc)

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctors");

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
        adapter.add("Select your gender"); //don't display last item put it as a hint.


        GendS.setAdapter(adapter);
        GendS.setSelection(adapter.getCount());
        //set the hint as default selection

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
                return super.getCount()-1; //don't display the hint in the list.
            }

        };
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.add("ER");
        adapter2.add("Family Medicine");
        adapter2.add("General");
        adapter2.add("Infection Disease");
        adapter2.add("Select your major");
        //don't display last item put it as a hint.


        MajorS.setAdapter(adapter2);
        MajorS.setSelection(adapter2.getCount());


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        SignupDoctor.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


                mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        Log.d(TAG, "onDateSet: mm/dd/yyy" + month + "/" + day + "/" + year);
                        String date = month + "/" + day + "/" + year;
                        mDisplayDate.setText(date);
                    }
                };
            }
        });
        //create back button in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//

        //Create onClick method which interact with the button
        SignupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signup();
            }
        });
    };
    //back to report interface method
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id =item.getItemId();
        if(id== android.R.id.home){
            this.finish();
        }
        return onContextItemSelected(item);
    }//


    private void Signup() {
        final String fname = Fname.getText().toString().trim();
        final String lname = Lname.getText().toString().trim();
        final String email = EmailF.getText().toString().trim();
        final String pass = PassF.getText().toString().trim();
        String cpass = CpassF.getText().toString().trim();
        final String date = mDisplayDate.getText().toString().trim();
        final String gender = GendS.getSelectedItem().toString().trim();
        final String major = MajorS.getSelectedItem().toString().trim();
        final String about = AboutF.getText().toString().trim();
        final String medid = MedID.getText().toString().trim();
        TextView errorText = (TextView) GendS.getSelectedView();
        TextView errorText2 = (TextView) MajorS.getSelectedView();

        int counter = 0;
        if (fname.isEmpty()) {
            Fname.setError("First name is required");
            Fname.requestFocus();
            counter++;
        }
        if (lname.isEmpty()) {
            Lname.setError("Last name is required");
            counter++;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EmailF.setError("Please enter a valid email address, email@example.com");
            counter++;
        }
        if (pass.isEmpty() || pass.length() < 6) {
            PassF.setError("Please enter a valid password with length of more than six");
            counter++;
        }
        if (cpass.isEmpty()) {
            CpassF.setError("Password confirmation is required");
            counter++;
        }
        if (!pass.equals(cpass)) {
            CpassF.setError("Passwords are not matched");
            counter++;
        }
        if (date.isEmpty()) {
            mDisplayDate.setError("Your BirthDate is required");
            counter++;
        }
        if (GendS.getSelectedItem() == "Select your gender") {
            errorText.setError("Please select your gender");
            counter++;
        }
        if (MajorS.getSelectedItem() == "Select your major") {
            errorText2.setError("Please select your major");
            counter++;
        }

        if (counter == 0) {

            mDatabase = FirebaseDatabase.getInstance().getReference();

            Query q = mDatabase.child("ValidDoctors").orderByValue().equalTo(medid);
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        mDatabase = mDatabase.child("Doctors");
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    String id = ds.child("Id").getValue().toString();
                                    if(id.equals(medid)){
                                        System.out.println("Med id exist!!!!!!!!!!!");
                                        flagEXist = true;
                                    }
                                }
                                if(flagEXist== false){
                                    System.out.println("دخل هناااااا");
                                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignupDoctor.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                String userid = mAuth.getCurrentUser().getUid();
                                                DatabaseReference currentUser = mDatabase.child("Doctors").child(userid);
                                                currentUser.child("Email").setValue(email);
                                                currentUser.child("FirstName").setValue(fname);
                                                currentUser.child("LastName").setValue(lname);
                                                currentUser.child("MedicalLicense").setValue(medid);
                                                currentUser.child("BirthDate").setValue(date);
                                                currentUser.child("Gender").setValue(gender);
                                                currentUser.child("Major").setValue(major);
                                                currentUser.child("AboutMe").setValue(about);

                                                Toast.makeText(SignupDoctor.this,
                                                        "Thank you for being one of the AIS community", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(SignupDoctor.this, MainDoctorActivity.class);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(SignupDoctor.this, "Error store..." + task.getException(), Toast.LENGTH_LONG).show();
                                            }
                                        }

                                    });

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        /*Query q2 = mDatabase.child("Doctors").child("Id").orderByValue().equalTo(medid);
                        q2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    //Error
                                    System.out.println("Med id exist!!!!!!!!!!!");
                                } else {
                                    System.out.println("دخل هناااااا");
                                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(SignupDoctor.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                String userid = mAuth.getCurrentUser().getUid();
                                                DatabaseReference currentUser = mDatabase.child("Doctors").child(userid);
                                                currentUser.child("Email").setValue(email);
                                                currentUser.child("FirstName").setValue(fname);
                                                currentUser.child("LastName").setValue(lname);
                                                //currentUser.child("MedicalLicense").setValue(medid);
                                                currentUser.child("BirthDate").setValue(date);
                                                currentUser.child("Gender").setValue(gender);
                                                currentUser.child("Major").setValue(major);
                                                currentUser.child("AboutMe").setValue(about);

                                                Toast.makeText(SignupDoctor.this,
                                                        "Thank you for being one of the AIS community", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(SignupDoctor.this, MainDoctorActivity.class);
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(SignupDoctor.this, "Error store..." + task.getException(), Toast.LENGTH_LONG).show();
                                            }
                                        }

                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });*/
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        /*if (!medid.isEmpty()){
            mDatabase = FirebaseDatabase.getInstance().getReference();

            Query q =mDatabase.child("ValidDoctors").orderByValue().equalTo(medid);
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())*/


            //createUser(fname, lname, email, pass, date, gender, phon, about, medid, check);


        }
                   /* else {
                        Toast.makeText(SignupDoctor.this, "Please enter valid license ID",Toast.LENGTH_LONG).show();

                    }*/
    }}

