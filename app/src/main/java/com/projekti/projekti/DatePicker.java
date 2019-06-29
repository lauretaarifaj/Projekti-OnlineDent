package com.projekti.projekti;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class DatePicker extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    public String koha,dataF,text,H,D;
    public TextView prova;
    private TextView tv,time,docFirstName,docLastName;
    private Button btnAppoint;
    private String emriDoc,mbiemriDoc,location;
    Calendar mCurrentDate;
    Calendar currentTime;
    int hour,minute;
    String format;
    int day,month,year;
    FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private TextView userName;
     private EditText NrPersonal;
    public String doctorId, hospital;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        databaseReference= FirebaseDatabase.getInstance().getReference("requests");

        mAuth=FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(mAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        docFirstName=(TextView)findViewById(R.id.docFirstName);
       // docLastName=(TextView)findViewById(R.id.docLastName);
        NrPersonal=(EditText) findViewById(R.id.NrPersonal);
        userName=(TextView)findViewById(R.id.userName);




        //i merr te dhanat prej shfaq_doktoret
        Bundle mbundle=getIntent().getExtras();
        if (mbundle!=null){
            emriDoc=(mbundle.getString("tvName"));
             doctorId=(mbundle.getString("docId"));
             mbiemriDoc=(mbundle.getString("docLastName"));
             hospital=(mbundle.getString("hospital"));
             location=(mbundle.getString("location"));

            docFirstName.setText(emriDoc+" "+mbiemriDoc);

        }


        Spinner spinner=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.orari,android.R.layout.simple_spinner_item);
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
spinner.setAdapter(adapter);
spinner.setOnItemSelectedListener(this);








        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        //E shfaq emrin e pacientit
        userName.setText(user.getDisplayName());

        //Per me e zgjedh daten
        tv=(TextView)findViewById(R.id.tv);
        mCurrentDate=Calendar.getInstance();
        day=mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month=mCurrentDate.get(Calendar.MONTH);
        year=mCurrentDate.get(Calendar.YEAR);

        month=month+1;
        tv.setText(day+"/"+month+"/"+year);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(DatePicker.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        month=month+1;
                        tv.setText(dayOfMonth+"/"+month+"/"+year);

                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("requests");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    Appointment appoint=dataSnapshot.getValue(Appointment.class);
                     H=appoint.getTime().toString();
                     D=appoint.getDate().toString();
                }
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
        });





        btnAppoint=(Button)findViewById(R.id.btnRezervo);
        btnAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numriPersonal=NrPersonal.getText().toString();
                String Hour=text.toString();
                String Data=tv.getText().toString();
                if (TextUtils.isEmpty(numriPersonal)) {


                    Toast.makeText(DatePicker.this, "Ju lutem shkruani numrin personal", Toast.LENGTH_SHORT).show();
                    return;




                }
                else if(Hour.equals(H) && Data.equals(D)) {
                    Toast.makeText(DatePicker.this, "Termini i zene", Toast.LENGTH_SHORT).show();


                }


                    else {
                    saveAppointment();
                Intent objIntent=new Intent(DatePicker.this,MainActivity.class);
                startActivity(objIntent);
                }



            }
        });

    }/*
    public void selectedTimeFormat(int hour){
        if(hour==0){
            hour+=12;
            format="AM";

        }
        else if(hour==12){
            format="PM";
        }else if(hour>12){
            hour-=12;
            format="PM";
        }
        else{
            format="AM";
        }
    }*/
    //I run te dhanat ne firebase te appoint
    private void saveAppointment(){

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String patientId = user.getUid().toString().trim();
        String date = tv.getText().toString().trim();
        String timee = text.toString().trim();
        String nrPersonal = NrPersonal.getText().toString().trim();
        String docfirstname=emriDoc.toString().trim();
        String doclastname=mbiemriDoc.toString().trim();
        String username=userName.getText().toString().trim();
        String lokacioni=location.toString().trim();


        Appointment appointment=new Appointment(patientId,doctorId,timee,date,hospital,nrPersonal,docfirstname,doclastname,username,lokacioni);


        String id=databaseReference.push().getKey();

        databaseReference.child(id).setValue(appointment);
        Toast.makeText(this, "Rezervimi u krye me sukses.", Toast.LENGTH_SHORT).show();


    }

//per spiner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         text=parent.getItemAtPosition(position).toString();
       // Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
