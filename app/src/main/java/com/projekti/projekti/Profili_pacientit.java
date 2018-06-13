package com.projekti.projekti;

import android.app.DatePickerDialog;
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

public class Profili_pacientit extends AppCompatActivity  {
    private TextView emri,email,profili,appointDoc,location,appointments,logout;
    //private static final int CHOOSE_IMAGE = 101;
    ImageView imageView;
    //Button choose;
    //Uri uriProfileImage;
    //ProgressBar progressBar;
    //String profileImgUrl;
    FirebaseAuth mAuth;
    EditText editText;
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

        email=(TextView)findViewById(R.id.emailPac);
        profili=(TextView)findViewById(R.id.tvProfili);
        appointDoc=(TextView)findViewById(R.id.tvAppoint);
        appointments=(TextView)findViewById(R.id.tvAppointments);
        location=(TextView)findViewById(R.id.tvLocation);
        logout=(TextView)findViewById(R.id.tvLogout);
        username=(TextView)findViewById(R.id.displayName);

        imageView=(ImageView)findViewById(R.id.fotoProfil);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        //Glide.with(context).load(doctors.get(position).getUrl()).into(img);
        if(user.getPhotoUrl()!=null){
        String url=user.getPhotoUrl().toString();
       Glide.with(getApplicationContext()).load(url).into(imageView);}
        //String img=user.getPhotoUrl();
        //choose=(Button)findViewById(R.id.zgjedh);
        //editText=(EditText)findViewById(R.id.displayName);

        //progressBar=findViewById(R.id.progress_bar);

       /* btnRezervo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Profili_pacientit.this, "Rezervo", Toast.LENGTH_SHORT).show();
            }
        });*/


        //FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        email.setText(user.getEmail());
        //editText.setText(user.getDisplayName());
        username.setText(user.getDisplayName());
/*
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();


            }
        });
        loadUserInfo();

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();

            }
        });*/

        profili.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent=new Intent(Profili_pacientit.this,update_profile.class);
                startActivity(objIntent);

            }
        });
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
/*
    private void loadUserInfo() {

        FirebaseUser user=mAuth.getCurrentUser();

        if(user!=null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl().toString()).into(imageView);
            }
            if(user.getDisplayName()!=null) {
            editText.setText(user.getDisplayName());
            }

        }

    }

    private void saveUserInformation() {
        String displayName=editText.getText().toString();
        FirebaseUser user=mAuth.getCurrentUser();
        if(displayName.isEmpty()){
            editText.setError("Name required");
            editText.requestFocus();
            return;
        }
        if (user!=null && profileImgUrl!=null){
            UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImgUrl)).build();
            user.updateProfile(profileChangeRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Profili_pacientit.this, "Profile updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data !=null && data.getData()!=null){

            uriProfileImage=data.getData();
            try {
                //E merr foton e qet ne imageview
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef= FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if(uriProfileImage!=null){
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    profileImgUrl=taskSnapshot.getDownloadUrl().toString();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Profili_pacientit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showImageChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select profile image"),CHOOSE_IMAGE);
    }*/
}
