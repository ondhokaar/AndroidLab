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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static Button loginbtn, chatbtn, outbtn;


    private boolean isLoggedIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //findview
        outbtn = (Button) findViewById(R.id.signout);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        //test_login = (Switch) findViewById(R.id.test_login);
        chatbtn = (Button) findViewById(R.id.chat);

        //checkLoginStatus();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            Toast.makeText(this, "already logged in ", Toast.LENGTH_SHORT).show();
            //test_login.setChecked(true);
            isLoggedIn = true;
        }
        //intents
        //chat intent inside onclick of chat button
        Intent intent_login = new Intent(this, loginactivity.class);
        loginactivity.setContext(this);


        //setup ui

        if(test_login.isChecked()) {
            loginbtn.setVisibility(View.GONE);

        }
        else {
            outbtn.setVisibility(View.GONE);
            chatbtn.setVisibility(View.GONE);
        }

        test_login.setVisibility(View.GONE);
        //setonclick
        outbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                test_login.setChecked(false);
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



        test_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(test_login.isChecked()) {
                    loginbtn.setVisibility(View.GONE);
                    chatbtn.setVisibility(View.VISIBLE);
                    outbtn.setVisibility(View.VISIBLE);
                }
                else {
                    loginbtn.setVisibility(View.VISIBLE);
                    chatbtn.setVisibility(View.GONE);
                    outbtn.setVisibility(View.GONE);
                }

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
    public static void setToggle(boolean loggedIn) {
        test_login.setChecked(loggedIn);
    }

    private void checkLoginStatus() {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser != null) {
                    //Do what you need to do
                    test_login.setChecked(true);
                    Toast.makeText(getApplicationContext(), "already logged in ", Toast.LENGTH_SHORT).show();
                }
                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    test_login.setChecked(true);
                    Toast.makeText(getApplicationContext(), "already logged in ", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void getPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 1);
        }

    }


}