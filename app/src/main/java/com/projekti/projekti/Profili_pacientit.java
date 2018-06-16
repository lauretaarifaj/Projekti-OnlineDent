package com.projekti.projekti;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import java.io.IOException;

public class Profili_pacientit extends AppCompatActivity  {
   /* String arrayName[]= {"Add Appointment",
                          "Appointments",
                          "Update Profile"
    };*/
    private TextView email,profili,appointDoc,location,appointments,logout;
    ImageView imageView;
    FirebaseAuth mAuth;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profili_pacientit);
        mAuth=FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(mAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }
        /*
        CircleMenu circleMenu=(CircleMenu)findViewById(R.id.circleMenu);
        circleMenu.setMainMenu(Color.parseColor("red"),R.drawable.plus,R.drawable.plus)
        .addSubMenu(Color.parseColor("red"),R.drawable.plus)
        .addSubMenu(Color.parseColor("red"),R.drawable.ic_calendar)
        .addSubMenu(Color.parseColor("red"),R.drawable.ic_action_person)
        .setOnMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            public void onMenuSelected(int i) {
                switch (i){
                    case 0:
                        Intent objIntent=new Intent(Profili_pacientit.this,shfaq_doktoret.class);
                        startActivity(objIntent);
                        break;
                    case 1:
                        Intent intent=new Intent(Profili_pacientit.this,shfaq_terminet.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Intent intent1=new Intent(Profili_pacientit.this,update_profile.class);
                        startActivity(intent1);
                        break;


                }

               // Toast.makeText(Profili_pacientit.this, "You selected"+arrayName[i], Toast.LENGTH_SHORT).show();

            }
        });*/

        //email=(TextView)findViewById(R.id.emailPac);
        //profili=(TextView)findViewById(R.id.tvProfili);
        appointDoc=(TextView)findViewById(R.id.tvAppoint);
        appointments=(TextView)findViewById(R.id.tvAppointments);
        //location=(TextView)findViewById(R.id.tvLocation);
        logout=(TextView)findViewById(R.id.tvLogout);
        username=(TextView)findViewById(R.id.displayName);

        imageView=(ImageView)findViewById(R.id.fotoProfil);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        if(user.getPhotoUrl()!=null){
        String url=user.getPhotoUrl().toString();
       Glide.with(getApplicationContext()).load(url).into(imageView);}

        //email.setText(user.getEmail());
        username.setText(user.getDisplayName());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent=new Intent(Profili_pacientit.this,update_profile.class);
                startActivity(objIntent);
            }
        });


       /* profili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent=new Intent(Profili_pacientit.this,update_profile.class);
                startActivity(objIntent);

            }
        });*/
        appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent=new Intent(Profili_pacientit.this,shfaq_terminet.class);
                startActivity(objIntent);
            }
        });
        appointDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent=new Intent(Profili_pacientit.this,shfaq_doktoret.class);
                startActivity(objIntent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                Intent objIntent=new Intent(Profili_pacientit.this,MainActivity.class);
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
