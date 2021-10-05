package com.example.s13;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.s13.adapters.Adapter_inbox;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Inbox extends AppCompatActivity {
    private static final long LOCATION_REFRESH_TIME = 1000;
    private static final float LOCATION_REFRESH_DISTANCE = 1;
    private RecyclerView inbox_rv;
    private ArrayList<messageObj> message_list;
    private Adapter_inbox message_adapter;
    private String chatID;

    private Button sendbtn;
    private EditText writemsg;
    private TextView receiver;
    LottieAnimationView lottie_sendmsg;

    FusedLocationProviderClient fusedLocClient;
    com.google.android.gms.location.LocationRequest locationRequest;// = new com.google.android.gms.location.LocationRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        lottie_sendmsg = (LottieAnimationView) findViewById(R.id.sendmsglottie);
        locationRequest = new com.google.android.gms.location.LocationRequest();
        fusedLocClient = LocationServices.getFusedLocationProviderClient(this);
        writemsg = (EditText) findViewById(R.id.writemsg);
        sendbtn = (Button) findViewById(R.id.send);
        chatID = getIntent().getExtras().getString("chatID");
        receiver = (TextView) findViewById(R.id.inbox_rcvertitle);
        receiver.setText(getIntent().getExtras().getString("receiver"));
        Log.d("hell", "chat id is : (uid1+uid2)  -> " + chatID);

        inbox_rv = (RecyclerView) findViewById(R.id.inbox_rv);
        message_list = new ArrayList<>();
        message_adapter = new Adapter_inbox(this);


        message_adapter.setArrayList(message_list);

        inbox_rv.setAdapter(message_adapter);

        inbox_rv.setLayoutManager(new LinearLayoutManager(this));

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendmsg();
            }
        });
        getPermission();
        getLastLocation();


        getAllMsg();

    }



    void getPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.getResult() == null) {
                    requestNewLoc();
                }
                else {
                    Log.d("location", "location found: " + task.getResult().getAltitude() + ", " + task.getResult().getLongitude());

                }
            }
        });
    }
    @SuppressLint("MissingPermission")
    private void requestNewLoc() {
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);



        fusedLocClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocClient.requestLocationUpdates(locationRequest, mLocCallBack, Looper.myLooper());



    }
    private LocationCallback mLocCallBack = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locRes) {
            Location lastLoc = locRes.getLastLocation();
            Log.d("location", "LocationCallback() inside inbox - " + lastLoc.getAltitude());
        }
    };
    private void getAllMsg() {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("chat").child(chatID);
        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()) {
                    String text = "";
                    String sender = "";

                    if(snapshot.child("text").getValue() != null) {
                        text = snapshot.child("text").getValue().toString();
                    }
                    if(snapshot.child("sender").getValue() != null) {
                        sender = snapshot.child("sender").getValue().toString();
                    }

                    messageObj newmsg = new messageObj(text, snapshot.getKey(), sender);
                    message_list.add(newmsg);
                    //message_adapter.adjustColor(sender, FirebaseAuth.getInstance().getUid().toString());
                    inbox_rv.getLayoutManager().scrollToPosition(message_list.size() - 1);
                    message_adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendmsg() {
        lottie_sendmsg.resumeAnimation();
        requestNewLoc();
        DatabaseReference dbref_newText = FirebaseDatabase.getInstance().getReference().child("chat").child(chatID).push();
        Log.d("hell", chatID);
        DatabaseReference dbref_username = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("username");

        Map msgMap = new HashMap<String, String>();
        msgMap.put("sender", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());



        //writemsg.setText(mLocationListener);

        msgMap.put("text", writemsg.getText().toString());
        Log.d("what: ", writemsg.getText().toString());
        dbref_newText.updateChildren(msgMap);

        writemsg.getText().clear();
        //final String senderUsername;
        //msgMap.put("sender", "hola");

//        dbref_username.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Log.d("creating child on chat -> msg" ,  snapshot.getValue().toString());
//                final String senderUsername = snapshot.getValue().toString();
//                Log.d("name 2 ", senderUsername);
//                Map msgMap = new HashMap<String, String>();
//                msgMap.put("sender", senderUsername);
//                Log.d("buggu", writemsg.getText().toString());
//                msgMap.put("text", writemsg.getText().toString());
//                dbref_newText.updateChildren(msgMap);
//
//                writemsg.getText().clear();
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        Log.d("name: ", dbref_username.get().toString());



        // dbref_newText.updateChildren(msgMap);
        // writemsg.getText().clear();

    }
}