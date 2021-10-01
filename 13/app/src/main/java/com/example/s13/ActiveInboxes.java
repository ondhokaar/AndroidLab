package com.example.s13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_inboxes);

        newchat = (Button) findViewById(R.id.newchat);
        active_chats_rv = (RecyclerView) findViewById(R.id.active_chat_rv);
        activeChats_list = new ArrayList<>();

        activeChats_list.add(new chatObj("new chat id"));
        activeChats_list.add(new chatObj("new chat id"));

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

        getActiveChats();
    }


    private void getActiveChats() {
        DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");

        mUserChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot childSnaps : snapshot.getChildren()) {

                        chatObj mChat = new chatObj(childSnaps.getValue().toString());
                        activeChats_list.add(mChat);
                        activeChats_adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}