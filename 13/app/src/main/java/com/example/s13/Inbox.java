package com.example.s13;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Inbox extends AppCompatActivity {
    private RecyclerView inbox_rv;
    private ArrayList<messageObj> message_list;
    private Adapter_inbox message_adapter;
    private String chatID;

    private Button sendbtn;
    private EditText writemsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        writemsg = (EditText) findViewById(R.id.writemsg);
        sendbtn = (Button) findViewById(R.id.send);
        chatID = getIntent().getExtras().getString("chatID");

        Log.d("msg : ", chatID);

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

        getAllMsg();

    }
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
                    message_adapter.adjustColor(sender, FirebaseAuth.getInstance().getUid().toString());
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
        DatabaseReference dbref_newText = FirebaseDatabase.getInstance().getReference().child("chat").child(chatID).push();
        DatabaseReference dbref_username = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("username");



        //final String senderUsername;
        //msgMap.put("sender", "hola");

        dbref_username.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("name :" ,  snapshot.getValue().toString());
                final String senderUsername = snapshot.getValue().toString();
                Log.d("name 2 ", senderUsername);
                Map msgMap = new HashMap<String, String>();
                msgMap.put("sender", senderUsername);
                Log.d("buggu", writemsg.getText().toString());
                msgMap.put("text", writemsg.getText().toString());
                dbref_newText.updateChildren(msgMap);

                writemsg.getText().clear();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("name: ", dbref_username.get().toString());



       // dbref_newText.updateChildren(msgMap);
       // writemsg.getText().clear();

    }
}