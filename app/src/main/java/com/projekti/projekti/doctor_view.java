package com.projekti.projekti;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class doctor_view extends AppCompatActivity {
    private TextView username,email,profili,appointDoc,location,appointments,logout;
   private ImageView imgView;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_view);





        mAuth=FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(mAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        //email=(TextView)findViewById(R.id.emailDoc);
        //profili=(TextView)findViewById(R.id.Profili);
        appointDoc=(TextView)findViewById(R.id.Appoint);
        appointments=(TextView)findViewById(R.id.Appointments);
        //location=(TextView)findViewById(R.id.docLocation);
        logout=(TextView)findViewById(R.id.Logout);
        username=(TextView)findViewById(R.id.displayDocName);

        imgView=(ImageView)findViewById(R.id.imgProfil);





        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        //email.setText(user.getEmail());
        username.setText(user.getDisplayName());

        if(user.getPhotoUrl()!=null){
        String url=user.getPhotoUrl().toString();
        Glide.with(getApplicationContext()).load(url).into(imgView);}

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent=new Intent(doctor_view.this,DoctorInfo.class);
                startActivity(objIntent);
            }
        });






      /*  profili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent=new Intent(doctor_view.this,DoctorInfo.class);
                startActivity(objIntent);

            }
        });*/
        appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent=new Intent(doctor_view.this,shfaq_terminetEdoktorit.class);
                startActivity(objIntent);
            }
        });
        appointDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent=new Intent(doctor_view.this,shto_termin.class);
                startActivity(objIntent);
            }
        });
        /*
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent=new Intent(doctor_view.this,location.class);
                startActivity(objIntent);            }
        });*/
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                Intent objIntent=new Intent(doctor_view.this,MainActivity.class);
                startActivity(objIntent);

            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

}
