package com.example.s13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static Button loginbtn, chatbtn, outbtn;


    private static boolean isLoggedIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //findview
        outbtn = (Button) findViewById(R.id.signout);
        loginbtn = (Button) findViewById(R.id.loginbtn);

        chatbtn = (Button) findViewById(R.id.chat);

        //checkLoginStatus();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            Toast.makeText(this, "already logged in ", Toast.LENGTH_SHORT).show();

            isLoggedIn = true;
        }
        //intents
        //chat intent inside onclick of chat button
        Intent intent_login = new Intent(this, loginactivity.class);
        loginactivity.setContext(this);


        //setup ui

        if(isLoggedIn) {
            loginbtn.setVisibility(View.GONE);

        }
        else {
            outbtn.setVisibility(View.GONE);
            chatbtn.setVisibility(View.GONE);
        }


        //setonclick
        outbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                isLoggedIn = false;
                updateUI();
            }
        });
        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), chatactivity.class));
                startActivity(new Intent(getApplicationContext(), ActiveInboxes.class));
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent_login);
            }
        });



        getPermissions();

    }
    public static void setLoginbtn(int visibility) {
        loginbtn.setVisibility(visibility);
    }
    public static void setChatbtn(int visibility) {
        chatbtn.setVisibility(visibility);
    }
    public static void setLoginStatus(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }



    private void getPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
        }

    }

    private void updateUI() {
        if(isLoggedIn) {
            loginbtn.setVisibility(View.GONE);
            chatbtn.setVisibility(View.VISIBLE);
            outbtn.setVisibility(View.VISIBLE);
        }
        else {
            chatbtn.setVisibility(View.GONE);
            loginbtn.setVisibility(View.VISIBLE);
            outbtn.setVisibility(View.GONE);
        }
    }

}