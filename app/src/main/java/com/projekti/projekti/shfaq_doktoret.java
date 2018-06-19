package com.projekti.projekti;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class shfaq_doktoret extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference mref;
    private ListView listViewDoctors;
    private List<Doctor> doctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shfaq_doktoret);

        doctors=new ArrayList<Doctor>();

        //FirebaseDatabase database=FirebaseDatabase.getInstance();
        mref = FirebaseDatabase.getInstance().getReference("users");

        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    Doctor doc=dataSnapshot.getValue(Doctor.class);
                    String type=doc.getType();
                    if (type.equals("doctor")){
                        doctors.add(doc);}
                }}

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
        });





        //doctors.add(new Doctor("Laureta ","Arifaj","LA","jsdfo","sdi","sdi","doctor","123"));




        this.listViewDoctors=findViewById(R.id.listViewDoctors);
        this.listViewDoctors.setAdapter(new DoktorListAdapter(this,doctors));
        //i merr te dhanat prej liste i qon ne DatePicker activity

        listViewDoctors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent objIntent=new Intent(shfaq_doktoret.this,DatePicker.class);
                objIntent.putExtra("tvName",doctors.get(position).getFirstName().toString());
                objIntent.putExtra("docId",doctors.get(position).getId().toString());
                objIntent.putExtra("docLastName",doctors.get(position).getLastName().toString());
                objIntent.putExtra("hospital",doctors.get(position).getHospital().toString());
                objIntent.putExtra("location",doctors.get(position).getAddress().toString());
                //objIntent.putExtra("location",doctors.get(position).get().toString());
                startActivity(objIntent);
                //Toast.makeText(shfaq_doktoret.this, "sdbhfhs", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
