package com.projekti.projekti;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pacient);
        final Button btnRegister=(Button) findViewById(R.id.btnRegisterPacient);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder objBuilder=new AlertDialog.Builder(RegisterActivity.this);
                objBuilder.setTitle("Confirmation");
                objBuilder.setMessage("Are you sure you want to confirm information?");
                objBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent objIntent=new Intent(RegisterActivity.this,SecondActivity.class);
                        startActivity(objIntent);
                    }
                });
                objBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                objBuilder.show();
            }
        });
        AlertDialog.Builder objBuilder=new AlertDialog.Builder(RegisterActivity.this);
        LayoutInflater objInflater=RegisterActivity.this.getLayoutInflater();




    }
}
