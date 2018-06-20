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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class update_profile extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 101;
    private EditText etFirstName,etLastName,etAddress,etPhone,etType;
    private Button btnSaveInfo;
    private ImageView fotojaProfilit;
    private Button btnUpdate;
    private EditText et;
    private Uri uriProfileImage;
    private ProgressBar progressBar;
    private String profileImgUrl;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        firebaseAuth =FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }



        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        etFirstName=(EditText)findViewById(R.id.etFirstName);
        etLastName=(EditText)findViewById(R.id.etLastName);
        etPhone=(EditText)findViewById(R.id.etPhone);
        etAddress=(EditText)findViewById(R.id.etAddres);
        //etType=(EditText)findViewById(R.id.etType);
        btnSaveInfo=(Button) findViewById(R.id.btnSaveInfo);




        btnUpdate=(Button) findViewById(R.id.Update);
        fotojaProfilit=(ImageView)findViewById(R.id.fotojaProfilit);
        et=(EditText)findViewById(R.id.editText2);
        progressBar=findViewById(R.id.progress_bar);

        fotojaProfilit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();


            }
        });
        loadUserInfo();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();

            }
        });

        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
                Intent objIntent=new Intent(update_profile.this,Profili_pacientit.class);
                startActivity(objIntent);
                finish();
            }
        });





    }
    private void saveUserInfo(){
        FirebaseUser user=firebaseAuth.getCurrentUser();

        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String type = "pacient".toString().trim();

        PacInfo userInfo=new PacInfo(firstName,lastName,address,phone,type,user.getPhotoUrl().toString(),user.getUid().toString());
        //FirebaseUser user=firebaseAuth.getCurrentUser();
        //String uploadId=databaseReference.push().getKey();


        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userInfo);
        Toast.makeText(this, "Informatio Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }


    private void loadUserInfo() {

        FirebaseUser user=firebaseAuth.getCurrentUser();

        if(user!=null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl().toString()).into(fotojaProfilit);
            }
            if(user.getDisplayName()!=null) {
                et.setText(user.getDisplayName());
            }


        }

    }

    private void saveUserInformation() {
        String displayName=et.getText().toString();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(displayName.isEmpty()){
            et.setError("Name required");
            et.requestFocus();
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
                                Toast.makeText(update_profile.this, "Profile updated", Toast.LENGTH_SHORT).show();
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
                fotojaProfilit.setImageBitmap(bitmap);

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
                    Toast.makeText(update_profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void showImageChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select profile image"),CHOOSE_IMAGE);
    }
}
