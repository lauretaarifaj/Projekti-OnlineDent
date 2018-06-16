package com.projekti.projekti;

//import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Query;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projekti.projekti.R;
import com.projekti.projekti.SecondActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnlogin;
    private Button registerDoctor;
    private Button registerPacient;
    private EditText etEmail;
    private EditText etPassword;
    private FirebaseAuth firebaseAuth;
    //private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //databaseReference=FirebaseDatabase.getInstance().getReference("doctor");
        firebaseAuth=FirebaseAuth.getInstance();

       /* if(firebaseAuth.getCurrentUser()!= null){
            //profile activity
            finish();
           startActivity(new Intent(getApplicationContext(),doctor_view.class));
        }*/

        //progressDialog=new ProgressDialog(this);
       btnlogin = (Button) findViewById(R.id.btnLogin);
       etEmail = (EditText) findViewById(R.id.etEmail);
       registerPacient = (Button) findViewById(R.id.btnRegisterPacient);
       registerDoctor=(Button) findViewById(R.id.btnRegisterDoctor);
       etPassword=(EditText) findViewById(R.id.etPassword);

        btnlogin.setOnClickListener(this);
        registerDoctor.setOnClickListener(this);
        registerPacient.setOnClickListener(this);


    }

    private void userLogin(){
        String email=etEmail.getText().toString().trim();
        String password=etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        //progressDialog.setMessage("Logining...");
        //progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //progressDialog.dismiss();
                        if(task.isSuccessful()){

                            //finish();
                            //startActivity(new Intent(getApplicationContext(),doctor_view.class));

                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String userID=currentUser.getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID);


                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                   // finish();
                                //startActivity(new Intent(MainActivity.this,doctor_view.class));

                                    String userType = dataSnapshot.child("type").getValue().toString();
                                    if(userType.equals("pacient")){
                                        Intent intentResident = new Intent(MainActivity.this, Profili_pacientit.class);
                                        intentResident.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentResident);
                                        //finish();

                                    }else if (userType.equals("doctor")){
                                        Intent intentResident = new Intent(MainActivity.this, doctor_view.class);
                                        intentResident.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentResident);
                                        //finish();

                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });


    }



    @Override
    public void onClick(View view) {
        if (view==btnlogin){
            userLogin();
        }

        if (view==registerPacient){
            finish();
            startActivity(new Intent(this,registerPacient.class));
        }
        if (view==registerDoctor){
            finish();
            startActivity(new Intent(this,registerDoctor.class));
        }
    }
}
