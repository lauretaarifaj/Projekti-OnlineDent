package com.projekti.projekti;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Pacient_view extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final int CHOOSE_IMAGE =101 ;
    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle toggle;
    private FirebaseAuth firebaseAuth;
    private ImageView profilePic;
    private Button choose;
    private TextView email;
    private Firebase mRef;
    private Uri uriProfileImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacient_view);
        mdrawerLayout=(DrawerLayout)findViewById(R.id.drawer);

        profilePic=(ImageView)findViewById(R.id.profilePic);
        choose=(Button)findViewById(R.id.choose);
        email=(TextView)findViewById(R.id.email);

        firebaseAuth = FirebaseAuth.getInstance();
        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();


        toggle=new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
       mdrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);


      /*  profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK&& data !=null&&data.getData()!=null){

            uriProfileImage=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                profilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
if(toggle.onOptionsItemSelected(item)){
    return true;
}
      return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.profile){
            //Intent objIntent=new Intent(Pacient_view.this,pacientView.class);
            //startActivity(objIntent);
        }
        if(id==R.id.appoint){
            Intent objIntent=new Intent(Pacient_view.this,shfaq_doktoret.class);
            startActivity(objIntent);
        }
        if(id==R.id.location){
            Toast.makeText(this, "Location is selected", Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.appointments){
            Toast.makeText(this, "Appointments is selected", Toast.LENGTH_SHORT).show();
        }
        
        if(id==R.id.logout){
            firebaseAuth.signOut();
            finish();
            Intent objIntent=new Intent(Pacient_view.this,MainActivity.class);
            startActivity(objIntent);
        }
        return false;
    }
/*
    private void showImageChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select profile image"),CHOOSE_IMAGE);
    }*/
}
