package com.projekti.projekti;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;



import android.view.*;
import android.content.*;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class DoktorListAdapter extends ArrayAdapter<Doctor>  {


    private Context context;
    private List<Doctor> doctors;
    private Button btnRezervo;


    public DoktorListAdapter(Context context, List<Doctor> doctors){
        super(context, R.layout.doctor_list_layout,doctors);
        this.context=context;
        this.doctors=doctors;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view=layoutInflater.inflate(R.layout.doctor_list_layout,parent,false);

        //E vendos foton prej firebase ne imageView
        ImageView img=(ImageView)view.findViewById(R.id.imageView);
        //Bitmap bitmap= BitmapFactory.decodeResource(getRecources(),R.drawable.stlsm);




        Glide.with(context).load(doctors.get(position).getUrl()).into(img);
        
        btnRezervo=(Button)view.findViewById(R.id.appoint);
        btnRezervo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        



        TextView tvName=view.findViewById(R.id.tvName);
        tvName.setText(doctors.get(position).getFirstName());

        //TextView tvLastName=view.findViewById(R.id.tvlastName);
        //tvLastName.setText(doctors.get(position).getLastName());




        TextView tvHospital=view.findViewById(R.id.tvHospital);
        tvHospital.setText(doctors.get(position).getHospital());

        TextView tvSpeciality=view.findViewById(R.id.tvSpeciality);
        tvSpeciality.setText(doctors.get(position).getSpeciality());

        TextView tvPhone=view.findViewById(R.id.tvPhone);
        tvPhone.setText(String.valueOf(doctors.get(position).getPhone()));





        return view;
    }



}

