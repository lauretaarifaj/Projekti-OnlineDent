package com.projekti.projekti;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
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

public class DoctorInfo extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final int CHOOSE_IMAGE = 101;


    private EditText etFirstName,etLastName,etPhone,etSpeciality,etHospital,etType;
    private String spitali;
    private TextView getPlace;
    private Button btnSaveInfo;
    private ImageView fotojaProfilit;
    private Button btnUpdate;
    private EditText et;
    private Uri uriProfileImage;
    private String profileImgUrl;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    WebView attributionText;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    private final static int PLACE_PICKER_REQUEST = 1;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);
        firebaseAuth =FirebaseAuth.getInstance();

        requestPermission();
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

        Spinner spinner=findViewById(R.id.spineri);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.spitalet,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        databaseReference= FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        btnSaveInfo=(Button) findViewById(R.id.btnSaveInfo);
        etFirstName=(EditText)findViewById(R.id.etFirstName);
        etLastName=(EditText)findViewById(R.id.etLastName);
        etPhone=(EditText)findViewById(R.id.etPhone);
        getPlace=(TextView)findViewById(R.id.etAddres);
        etSpeciality=(EditText)findViewById(R.id.etSpeciality);
        etHospital=(EditText) findViewById(R.id.etHospital);
        //etType=(EditText) findViewById(R.id.etType);
        attributionText = (WebView) findViewById(R.id.wvAttribution);



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
        getPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(DoctorInfo.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

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

                saveDocInfo();
                Intent objIntent=new Intent(DoctorInfo.this,doctor_view.class);
                startActivity(objIntent);

            }
        });
    }
    private void saveDocInfo(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = getPlace.getText().toString().trim();
        String speciality = etSpeciality.getText().toString().trim();
        String hospital = spitali.toString().trim();
        String type = "doctor".toString().trim();
        DocInfo docInfo=new DocInfo(firstName,lastName,address,phone,speciality,hospital,type,user.getPhotoUrl().toString(),user.getUid().toString(),user.getEmail());


        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(docInfo);
        Toast.makeText(this, "Informatio Saved", Toast.LENGTH_SHORT).show();

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
        //lokacioni
        else if (requestCode == PLACE_PICKER_REQUEST){
            if (resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(DoctorInfo.this, data);
                getPlace.setText(place.getAddress());
                /*
                if (place.getAttributions() == null) {
                    attributionText.loadData("no attribution", "text/html; charset=utf-8", "UFT-8");
                } else {
                    attributionText.loadData(place.getAttributions().toString(), "text/html; charset=utf-8", "UFT-8");
                }*/
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
    //Per me pyt per me shfrytzu lokacionin
    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spitali=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}