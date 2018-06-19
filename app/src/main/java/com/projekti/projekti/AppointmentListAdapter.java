package com.projekti.projekti;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Admin on 6/9/2018.
 */

public class AppointmentListAdapter extends ArrayAdapter<Appointment> {


    private Context context;
    private List<Appointment> appointments;


    public AppointmentListAdapter(Context context, List<Appointment> appointments){
        super(context, R.layout.appointment_list_layout,appointments);
        this.context=context;
        this.appointments=appointments;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view=layoutInflater.inflate(R.layout.appointment_list_layout,parent,false);


        TextView tvNameDoc=view.findViewById(R.id.tvDoktoriFirstName);
        tvNameDoc.setText("Dr."+appointments.get(position).getDocfirstname()+" "+appointments.get(position).getDoclastname());

        //TextView LastName=view.findViewById(R.id.tvDoktoriLastName);
        //LastName.setText(appointments.get(position).getDoclastname());

        TextView Spitali=view.findViewById(R.id.tvSpitali);
        Spitali.setText(appointments.get(position).getHospital());

        TextView Data=view.findViewById(R.id.data);
        Data.setText(appointments.get(position).getDate());

        TextView Ora=view.findViewById(R.id.ora);
        Ora.setText(String.valueOf(appointments.get(position).getTime()));

        TextView EmriPacientit=view.findViewById(R.id.emriPacientit);
        EmriPacientit.setText(String.valueOf(appointments.get(position).getEmriPacientit()));





        return view;
    }



}
