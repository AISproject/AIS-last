package com.example.maryamabdulrhman.ais;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentReportActivity extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.activity_fragment_report, container ,false);
        Button btn1=(Button) view.findViewById(R.id.reportFlu);
        Button btn2=(Button) view.findViewById(R.id.reportCorona);

//move to reportFluActivity or reportCoronaActivity activity when press in button
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int1= new Intent(getActivity(),MapsActivity.class);
                int1.putExtra("reportfluDoctor",true);
                startActivity(int1);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int2= new Intent(getActivity(),MapsActivity.class);
                int2.putExtra("reportcoronaDoctor",false);
                startActivity(int2);

            }
        });
return view;


    }
}
