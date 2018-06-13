package com.projekti.projekti;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnLogout;
    private FirebaseAuth firebaseAuth;
    GridLayout mainGrid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

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

        //email.setText(user.getEmail());

        btnLogout=(Button)findViewById(R.id.btnLogout) ;

        mainGrid=(GridLayout) findViewById(R.id.mainGrid);

        setSingleEvent (mainGrid);
       // setToggleEvent(mainGrid);

    }

    private void setSingleEvent(GridLayout mainGrid) {
        for(int i=0;i<mainGrid.getChildCount();i++){
            CardView cardView =(CardView) mainGrid.getChildAt(i);
            final int finalI=i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent objIntent=new Intent(SecondActivity.this,Pacient_view.class);
                    startActivity(objIntent);
                }
            });
        }

        //FirebaseUser user = firebaseAuth.getCurrentUser();
        btnLogout.setOnClickListener(this);

    }




/*
    private void loadUserInformation(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into (imgView);


            }
            if (user.getDisplayName() != null) {
                email.setText(user.getDisplayName());
            }
        }

    }*/

    @Override
    public void onClick(View view) {
        if(view == btnLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}
