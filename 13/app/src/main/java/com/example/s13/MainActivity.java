package com.example.s13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static Button loginbtn, outbtn;
    private static LottieAnimationView chatLottie;

    private static boolean isLoggedIn;


    private LottieAnimationView jellyfish;
    private LottieAnimationView circulo_dog, emergencyContacts;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jellyfish = (LottieAnimationView) findViewById(R.id.jellyfish);
        emergencyContacts = (LottieAnimationView) findViewById(R.id.emergency_contact);
        welcomeText = (TextView) findViewById(R.id.welcometext);
        emergencyContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open emergency contact list activity
                startActivity(new Intent(getApplicationContext(), EmergencyActivity.class));
            }
        });

        jellyfish.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                jellyfish.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        circulo_dog = (LottieAnimationView) findViewById(R.id.circulo_dog);
        circulo_dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open login page
                //finish();
                Bundle bundle = new Bundle();
                bundle.putString("father", "abba");
                startActivity(new Intent(getApplicationContext(), loginactivity.class).putExtras(bundle));

            }
        });

        //findview
        //outbtn = (Button) findViewById(R.id.signout);


        chatLottie = (LottieAnimationView) findViewById(R.id.chat);
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


        }
        else {
           // outbtn.setVisibility(View.GONE);
            chatLottie.setVisibility(View.GONE);
        }


        //setonclick
//        outbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                isLoggedIn = false;
//                updateUI();
//            }
//        });
        chatLottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), chatactivity.class));
                startActivity(new Intent(getApplicationContext(), ActiveInboxes.class));
            }
        });





        getPermissions();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            welcomeText.setText("Hi, \n" + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
            chatLottie.setVisibility(View.VISIBLE);
        }
        else {
            welcomeText.setText("Sign In for more!!");
            chatLottie.setVisibility(View.GONE);
        }
    }

    public static void setLoginbtn(int visibility) {
        //loginbtn.setVisibility(visibility);
    }
    public static void setChatbtn(int visibility) {
        chatLottie.setVisibility(visibility);
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
           // loginbtn.setVisibility(View.GONE);
            chatLottie.setVisibility(View.VISIBLE);
           // outbtn.setVisibility(View.VISIBLE);
        }
        else {
            chatLottie.setVisibility(View.GONE);
           // loginbtn.setVisibility(View.VISIBLE);
            //outbtn.setVisibility(View.GONE);
        }
    }

}