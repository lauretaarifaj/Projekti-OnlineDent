package com.projekti.projekti;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerDoctor extends AppCompatActivity implements View.OnClickListener{

    private EditText etemail,etpassword;
    private Button btnregisterPacient;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();



        btnregisterPacient=(Button) findViewById(R.id.btnRegisterPacient);

        etemail=(EditText)findViewById(R.id.etEmail);
        etpassword=(EditText)findViewById(R.id.etPassword);

        progressDialog = new ProgressDialog(this);

        btnregisterPacient.setOnClickListener(this);
    }

    private void registerUser() {

        String email = etemail.getText().toString().trim();
        String password = etpassword.getText().toString().trim();


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;}

        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
        }
        else {

            progressDialog.setMessage("Registering Please Wait...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), DoctorInfo.class));
                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    progressDialog.dismiss();
                                    Toast.makeText(registerDoctor.this, "Email is already registered", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(registerDoctor.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    }



    @Override
    public void onClick(View view) {
        registerUser();


    }
}

