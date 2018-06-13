package com.projekti.projekti;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class DatePicker extends AppCompatActivity
{
    private TextView tv,time,docFirstName,docLastName;
    private Button btnAppoint;
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

        databaseReference= FirebaseDatabase.getInstance().getReference("appointments");

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
        docLastName=(TextView)findViewById(R.id.docLastName);
        NrPersonal=(EditText) findViewById(R.id.NrPersonal);
        userName=(TextView)findViewById(R.id.userName);




        //i merr te dhanat prej shfaq_doktoret
        Bundle mbundle=getIntent().getExtras();
        if (mbundle!=null){
            docFirstName.setText(mbundle.getString("tvName"));
             doctorId=(mbundle.getString("docId"));
             docLastName.setText(mbundle.getString("docLastName"));
             hospital=(mbundle.getString("hospital"));



        }








        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        userName.setText(user.getDisplayName());

        //per me e zgjedh oren
        time=(TextView)findViewById(R.id.time);
        currentTime=Calendar.getInstance();
        hour=currentTime.get(Calendar.HOUR_OF_DAY);
        minute=currentTime.get(Calendar.MINUTE);

        selectedTimeFormat(hour);

        time.setText(hour+" : "+minute+" "+format);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(DatePicker.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedTimeFormat(hourOfDay);
                        time.setText(hourOfDay+" : "+minute+ " "+format);

                    }
                },hour,minute,true);
                timePickerDialog.show();
            }
        });






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
        btnAppoint=(Button)findViewById(R.id.btnRezervo);
        btnAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numriPersonal=NrPersonal.getText().toString().trim();
                if (numriPersonal!=null) {
                    saveAppointment();
                }
                else{
                    Toast.makeText(DatePicker.this, "Please write your personal number to make an appointment", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
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
    }
    //I run te dhanat ne firebase te appoint
    private void saveAppointment(){

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String patientId = user.getUid().toString().trim();
        String date = tv.getText().toString().trim();
        String timee = time.getText().toString().trim();
        String nrPersonal = NrPersonal.getText().toString().trim();
        String docfirstname=docFirstName.getText().toString().trim();
        String doclastname=docLastName.getText().toString().trim();


        Appointment appointment=new Appointment(patientId,doctorId,timee,date,hospital,nrPersonal,docfirstname,doclastname);


        String id=databaseReference.push().getKey();

        databaseReference.child(id).setValue(appointment);
        Toast.makeText(this, "Appointment is set", Toast.LENGTH_SHORT).show();


    }


}
