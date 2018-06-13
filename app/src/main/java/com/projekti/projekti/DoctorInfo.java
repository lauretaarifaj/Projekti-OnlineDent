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

public class DoctorInfo extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 101;


    private EditText etFirstName,etLastName,etAddress,etPhone,etSpeciality,etHospital,etType;
    private Button btnSaveInfo;
    private ImageView fotojaProfilit;
    private Button btnUpdate;
    private EditText et;
    private Uri uriProfileImage;
    private String profileImgUrl;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);
        firebaseAuth =FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,MainActivity.class));

        }

        databaseReference= FirebaseDatabase.getInstance().getReference("doctors")
        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        btnSaveInfo=(Button) findViewById(R.id.btnSaveInfo);
        etFirstName=(EditText)findViewById(R.id.etFirstName);
        etLastName=(EditText)findViewById(R.id.etLastName);
        etPhone=(EditText)findViewById(R.id.etPhone);
        etAddress=(EditText)findViewById(R.id.etAddres);
        etSpeciality=(EditText)findViewById(R.id.etSpeciality);
        etHospital=(EditText) findViewById(R.id.etHospital);
        etType=(EditText) findViewById(R.id.etType);

        btnUpdate=(Button) findViewById(R.id.Uupdate);
        fotojaProfilit=(ImageView)findViewById(R.id.footojaProfilit);
        et=(EditText)findViewById(R.id.editText3);
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

        //progressBar=findViewById(R.id.progressbar);



       // FirebaseUser user=firebaseAuth.getCurrentUser();
        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent objIntent=new Intent(DoctorInfo.this,doctor_view.class);
                startActivity(objIntent);
                saveDocInfo();

            }
        });
    }
    private void saveDocInfo(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        //String url=user.getPhotoUrl().toString();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String speciality = etSpeciality.getText().toString().trim();
        String hospital = etHospital.getText().toString().trim();
        String type = etType.getText().toString().trim();
        DocInfo docInfo=new DocInfo(firstName,lastName,address,phone,speciality,hospital,type,user.getPhotoUrl().toString(),user.getUid().toString());
        //FirebaseUser user=firebaseAuth.getCurrentUser();

        //String uploadId=databaseReference.push().getKey();


        FirebaseDatabase.getInstance().getReference("doctors")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(docInfo);
        //Toast.makeText(this, "Informatio Saved", Toast.LENGTH_SHORT).show();
        //FirebaseUser user=firebaseAuth.getCurrentUser();

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
                                Toast.makeText(DoctorInfo.this, "Profile updated", Toast.LENGTH_SHORT).show();
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
            //progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //   progressBar.setVisibility(View.GONE);
                    profileImgUrl=taskSnapshot.getDownloadUrl().toString();



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(DoctorInfo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
