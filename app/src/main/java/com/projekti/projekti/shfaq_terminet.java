package com.projekti.projekti;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class shfaq_terminet extends AppCompatActivity {

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference mref;
    private ListView listViewAppointments;
    private List<Appointment> appointments;
    private AppointmentListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shfaq_terminet);

        appointments=new ArrayList<Appointment>();

        //FirebaseDatabase database=FirebaseDatabase.getInstance();
        mref = FirebaseDatabase.getInstance().getReference("appointments");
        this.listViewAppointments=findViewById(R.id.listViewAppointments);
        adapter = new AppointmentListAdapter(this, appointments);
        listViewAppointments.setAdapter(adapter);

/*
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Appointment appoint=dataSnapshot.getValue(Appointment.class);
                appointments.add(appoint);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/





        //doctors.add(new Doctor("Laureta ","Arifaj","LA","jsdfo","sdi","sdi","doctor","123"));
        mAuth=FirebaseAuth.getInstance();

        FirebaseUser user=mAuth.getCurrentUser();

        Query query =FirebaseDatabase.getInstance().getReference("appointments")
                .orderByChild("patientId").equalTo(user.getUid());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                appointments.clear();
                if (dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        Appointment appoint=snapshot.getValue(Appointment.class);
                        appointments.add(appoint);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //this.listViewAppointments.setAdapter(new AppointmentListAdapter(this,appointments));



    }




}
