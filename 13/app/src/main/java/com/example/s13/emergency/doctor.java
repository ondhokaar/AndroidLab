package com.example.s13.emergency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

import com.example.s13.R;
import com.example.s13.adapters.myAdapter;
import com.example.s13.userObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class doctor extends AppCompatActivity {
    private RecyclerView contactRV;
    private ArrayList<userObj> contactArraylist;


    private myAdapter adapter;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        contactRV = (RecyclerView) findViewById(R.id.contactdocrv);
        contactArraylist = new ArrayList<>();

        adapter = new myAdapter(this);
        adapter.setContactList(contactArraylist);

        contactRV.setAdapter(adapter);
        contactRV.setLayoutManager(new LinearLayoutManager(this));

        title = (TextView) findViewById(R.id.chatHeading);
        title.setText(getIntent().getExtras().get("service").toString());
        getDoctors();
    }

    private void getDoctors() {
        String service = (String) getIntent().getExtras().get("service");
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child(service);
        dbref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contactArraylist.clear();

                if(snapshot.exists()) {
                    for(DataSnapshot contacts : snapshot.getChildren()) {
                        contactArraylist.add(new userObj("", contacts.getKey().toString(), contacts.getValue().toString()));
                        adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}