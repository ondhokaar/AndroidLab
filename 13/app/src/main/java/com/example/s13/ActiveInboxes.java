package com.example.s13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.example.s13.adapters.Adapter_activeChats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActiveInboxes extends AppCompatActivity {
    private Button newchat;
    private RecyclerView active_chats_rv;
    private ArrayList<chatObj> activeChats_list;
    private Adapter_activeChats activeChats_adapter;
    LottieAnimationView lottieNoface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_inboxes);

        lottieNoface = (LottieAnimationView) findViewById(R.id.noface);
        newchat = (Button) findViewById(R.id.newchat);
        active_chats_rv = (RecyclerView) findViewById(R.id.active_chat_rv);
        activeChats_list = new ArrayList<>();



        activeChats_adapter = new Adapter_activeChats(this);
        activeChats_adapter.setArrayList(activeChats_list);

        active_chats_rv.setAdapter(activeChats_adapter);
        active_chats_rv.setLayoutManager(new LinearLayoutManager(this));




        newchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), chatactivity.class));
            }
        });

        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                lottieNoface.clearAnimation();
                lottieNoface.setVisibility(View.GONE);
                newchat.setVisibility(View.VISIBLE);
            }

        }.start();

        getActiveChats();
        Log.d("prom", "what");
        int j = 0;
        for(chatObj i : activeChats_list) {
            Log.d("active dup", String.valueOf(j) + i.getChatID());
        }
    }


    private void getActiveChats() {
        DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");
        activeChats_list.clear();
        mUserChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                activeChats_list.clear();
                if(snapshot.exists()) {
                    for(DataSnapshot childSnaps : snapshot.getChildren()) {

                        chatObj mChat = new chatObj(childSnaps.getKey().toString(), childSnaps.getValue().toString());
                        activeChats_list.add(mChat);
                        activeChats_adapter.notifyDataSetChanged();
                    }
                }
                else {
                    activeChats_list.add(new chatObj("no active chat :-(", "illegal"));
                    activeChats_adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

}