package com.example.s13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chatactivity extends AppCompatActivity {
    private RecyclerView contactRV;
    private ArrayList<userObj> contactArraylist;
    private myAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity);

        contactRV = (RecyclerView) findViewById(R.id.contactListrv);
        contactArraylist = new ArrayList<>();

        adapter = new myAdapter(this);
        adapter.setContactList(contactArraylist);

        contactRV.setAdapter(adapter);
        contactRV.setLayoutManager(new LinearLayoutManager(this));

        getContacts();


    }
    private void chk_isUser(userObj mycontact) {
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        if(!mycontact.getPhone().contains("+88")) {
            mycontact.setPhone("+88" + mycontact.getPhone());
        }
        Query query = mUserDB.orderByChild("phone").equalTo(mycontact.getPhone());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Log.d("chk_isUser", "true");
                    mycontact.setUser(true);
                }
                else {
                   // Log.d("chk_isUser", "") pending work
                    mycontact.setUser(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void getContacts() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while(phones.moveToNext()) {
            @SuppressLint("Range") String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


            userObj contact = new userObj(name, phone);
            Log.d("user issue", "now checking user");
            chk_isUser(contact);
            Log.d("user issue", "chk done, " + contact.getPhone() + " user = " + contact.getUser());


            if(contact.getUser() == true) {
                contactArraylist.add(contact);
                Log.d("user issue", "adding to arraylist done");
            }





        }
    }
}