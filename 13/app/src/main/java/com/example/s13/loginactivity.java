package com.example.s13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class loginactivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button sendcode;


    private static Context context;
    private EditText phone;
    private EditText code;
    private Intent intent_mainactivity;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callback;

    private String vid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);
        //firebase init
        //FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        //intents
        intent_mainactivity = new Intent(getApplicationContext(), MainActivity.class);

        //findviewbyid()
        sendcode = (Button) findViewById(R.id.sendcode);
        phone = (EditText) findViewById(R.id.phone);
        code = (EditText) findViewById(R.id.code);

        callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d("verification: ", "onverificaiton complete: " + phoneAuthCredential);
                signin_phoneauth(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d("verification: ", "failed:"  + e);

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.d("verification: ", "code sent: "+s);
                vid = s;
                sendcode.setText("verify code");
            }
        };

        //onclick
        sendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("verification: ", "clicked on send code");
                if(vid != null) {
                    Log.d("verification: ", "already have vid, nextline will verify phone num with vid");
                    verify_phone_with_code();
                    Log.d("verification: ", "verify_phone_with_code done");
                }
                else {
;                    Log.d("verification: ", "starting verificaiton");
                    startVerification();

                }

            }
        });



    }
    public static void setContext(Context context) {
        loginactivity.context = context;
    }


    //other methods
    @Override
    public void onStart() {

        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("verification: ", "on start");
    }
    private void startVerification() {
        Log.d("verification: ", "started startverification");
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone.getText().toString())
                .setTimeout(10L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callback)
                .build();


        PhoneAuthProvider.verifyPhoneNumber(options);
        Log.d("verification: ", "code should be sent");
    }


    private void signin_phoneauth(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Log.d("verification: ", "user logged in");
                        //userisloggedin();
                        Toast.makeText(getApplicationContext(), "login successful", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = task.getResult().getUser();
                        //now add this user to the database:
                        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
                        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //if user doesn't exists:
                                if(!snapshot.exists()) {
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("phone", user.getPhoneNumber());
                                    userMap.put("username", user.getPhoneNumber());
                                    mUserDB.updateChildren(userMap);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        MainActivity.setLoginbtn(View.GONE);
                        MainActivity.setChatbtn(View.VISIBLE);
                        MainActivity.setToggle(true);
                        finish();
                        return;
                    }
                    else {
                        Log.d("verification: ", "failed");
                        sendcode.setText("sorry, try again");
                    }

                }
        });
    }

    private void userisloggedin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            startActivity(intent_mainactivity);
            finish();
            return;
        }

    }
    private void verify_phone_with_code() {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(vid, code.getText().toString());
        signin_phoneauth(credential);
    }

    //

}