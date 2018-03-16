package com.example.maryamabdulrhman.ais;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class FragmentReportp  extends Fragment {


 @Nullable
 @Override
 public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

  View view= inflater.inflate(R.layout.fragment_reportpatient, container ,false);
  //declare and Intialize the button
  Button btn1=(Button) view.findViewById(R.id.reportFlu);

//create on click method to move to reportFluActivity activity when press in button
  btn1.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View view) {
    Intent int1= new Intent(getActivity(),ReportfluActivity.class);
    startActivity(int1);
   }
  });
  return view;


 }
}
