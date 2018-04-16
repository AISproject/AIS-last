package com.example.maryamabdulrhman.ais;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class signUpPatient extends AppCompatActivity {
    //Declare the variables
    private static final String TAG="signUpPatient";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText Fname, Lname, Pass, CPass, Email, mDisplayDate;
    private Spinner gender;
    private RadioGroup Smartwatch;
    private RadioButton Yes, No;
    private CheckBox Diab, Asthm, Hypert, None;
    private Button SignUp;
    private ArrayList<String> DiseaseSele = new ArrayList<String>();
    private String finalSelection = " ";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog mPd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_patient);
        //Initialize the variables with the exact interface content
        Fname = (EditText) findViewById(R.id.FirstName);
        Lname = (EditText) findViewById(R.id.LastName);
        Pass = (EditText) findViewById(R.id.password);
        CPass = (EditText) findViewById(R.id.cpassword);
        Email= (EditText) findViewById(R.id.email);
        mDisplayDate= (EditText) findViewById(R.id.bdate);
        gender = (Spinner) findViewById(R.id.sgender);
        Smartwatch = (RadioGroup) findViewById(R.id.QuestionGarmin);
        Yes = (RadioButton) findViewById(R.id.yes);
        No = (RadioButton) findViewById(R.id.no);
        Diab = (CheckBox) findViewById(R.id.diabetes);
        Asthm = (CheckBox) findViewById(R.id.Asthma);
        Hypert = (CheckBox) findViewById (R.id.b_pressure);
        None = (CheckBox) findViewById(R.id.None);
        SignUp = (Button) findViewById(R.id.SignupBtn);
        //Access DB Authentication and achieve child reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Patients");
        mPd = new ProgressDialog(this);

        None.setChecked(true);
        //CheckBox listener
        Diab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Diab.isChecked()){
                    None.setEnabled(false);
                    None.setChecked(false);
                    DiseaseSele.add("Diabetes");

                }
                if (!Diab.isChecked() )
                    None.setEnabled(true);
                DiseaseSele.remove("Diabetes");
            }
        });
        Asthm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Asthm.isChecked()){
                    None.setEnabled(false);
                    None.setChecked(false);
                    DiseaseSele.add("Asthma");
                }

                if (!Asthm.isChecked() ){
                    None.setEnabled(true);
                    DiseaseSele.remove("Asthma");
                }
            }
        });

        Hypert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Hypert.isChecked()){
                    None.setEnabled(false);
                    None.setChecked(false);
                    DiseaseSele.add("Hypertension");

                }
                if (!Hypert.isChecked() ){
                    None.setEnabled(true);
                    DiseaseSele.remove("Hypertension");
                }

            }
        });
        None.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(None.isChecked()){
                    Diab.setEnabled(false);
                    Asthm.setEnabled(false);
                    Hypert.setEnabled(false);
                    Diab.setChecked(false);
                    Asthm.setChecked(false);
                    Hypert.setChecked(false);
                    DiseaseSele.add("None");
                }
                if (!None.isChecked()){
                    Diab.setEnabled(true);
                    Asthm.setEnabled(true);
                    Hypert.setEnabled(true);
                    DiseaseSele.remove("None");
                }
            }
        });
        //End of CheckBox listener

        //Stores CheckBox values into one variable
        for (String selection : DiseaseSele){
            finalSelection = finalSelection + selection + " , ";
        }

        Yes.setChecked(false);
        No.setChecked(true);

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


        gender.setAdapter(adapter);
        gender.setSelection(adapter.getCount()); //set the hint as default selection

//end of spinner hint for gender


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year=c.get(Calendar.YEAR);
                int month=c.get(Calendar.MONTH);
                int day=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        signUpPatient.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy" + day + "/" + month + "/" + year);
                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };//end create picker date

        //create back button in the toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//
        //Button click listener
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupPatient();
            }
        });



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
    private void SignupPatient (){
        final String Disese = finalSelection;
        final String fname = Fname.getText().toString().trim();
        final String lname = Lname.getText().toString().trim();
        final String pass = Pass.getText().toString().trim();
        String cpass = CPass.getText().toString().trim();
        final String email = Email.getText().toString().trim();
        final String BDate= mDisplayDate.getText().toString().trim();
        final String Gender = gender.getSelectedItem().toString().trim();
        final int Q = Smartwatch.getCheckedRadioButtonId();
        final RadioButton choose = (RadioButton) findViewById(Q);
        final String choose2 = choose.getText().toString().trim();
        TextView errorText = (TextView) gender.getSelectedView();

        if (fname.isEmpty()) {
            Fname.setError("First name is required");
            Fname.requestFocus();
        }
        if (lname.isEmpty()) {
            Lname.setError("Last name is required");
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Email.setError("Please enter a valid email address, email@example.com");
        }
        if (pass.isEmpty() || pass.length() < 6) {
            Pass.setError("Please enter a valid password with length of more than six");
        }
        if(cpass.isEmpty()){
            CPass.setError("Password confirmation is required");
        }
        if (!pass.equals(cpass)) {
            CPass.setError("Passwords are not matched");
        }
        if (BDate.isEmpty()) {
            mDisplayDate.setError("Your BirthDate is required");
        }
        if (gender.getSelectedItem() == "Select the Gender") {
            errorText.setError("Please select your gender");
        }else
        {
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(signUpPatient.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        String userid = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUser = mDatabase.child(userid);
                        currentUser.child("Email").setValue(email);
                        currentUser.child("FirstName").setValue(fname);
                        currentUser.child("LastName").setValue(lname);
                        currentUser.child("BirthDate").setValue(BDate);
                        currentUser.child("Gender").setValue(Gender);
                        currentUser.child("ExistDiseases").setValue(Disese);
                        currentUser.child("SmartWatchExistense").setValue(choose2);



                        Toast.makeText(signUpPatient.this,
                                "Thank you for being one of the AIS community", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(signUpPatient.this, MainActivityPatient.class);
                        startActivity(intent);


                    } else {
                        try {
                            throw task.getException();
                        } catch(FirebaseAuthUserCollisionException e) {
                            Email.setError(getString(R.string.error_user_exists));
                            Email.requestFocus();
                        } catch(Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }

            });
        }





    }












}