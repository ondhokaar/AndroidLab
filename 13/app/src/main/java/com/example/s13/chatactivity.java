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

import com.example.s13.adapters.myAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private ArrayList<String> uidList;

    private myAdapter adapter;
    FirebaseUser loggedInUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity);
        uidList = new ArrayList<>();
        contactRV = (RecyclerView) findViewById(R.id.contactListrv);
        contactArraylist = new ArrayList<>();

        adapter = new myAdapter(this);
        adapter.setContactList(contactArraylist);

        contactRV.setAdapter(adapter);
        Log.d("snap init", "setting adapter");
        contactRV.setLayoutManager(new LinearLayoutManager(this));
        Log.d("snap init", "now i'll getContacts");
        getContacts();
        Log.d("snap init", "getting contacts done");


    }


    private void chk_isUser(userObj mycontact) {
        Log.d("snap chk_isUser: ", "start checking");

        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        if(!mycontact.getPhone().contains("+88")) {
            mycontact.setPhone("+88" + mycontact.getPhone());
        }
        if(mycontact.getPhone().contains("-")) {
            mycontact.setPhone(mycontact.getPhone().replace("-", ""));
        }
        if(mycontact.getPhone().contains(" ")) {
            mycontact.setPhone(mycontact.getPhone().replace(" ", ""));
        }

        Query query = mUserDB.orderByChild("phone").equalTo(mycontact.getPhone());


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot childSnap : snapshot.getChildren()) {
                    Log.d("snapshots ", childSnap.child("phone").getValue().toString() + "(" + childSnap.getKey() + ")" + "(" + childSnap.child("username").getValue().toString() + ")" + " -> local : " + mycontact.getPhone());

                    Log.d("snapshots: ", String.valueOf(snapshot.exists()));

                    Log.d("snap if me: ", childSnap.child("phone").getValue().toString() + " and me : " + loggedInUser.getPhoneNumber());

                    mycontact.setUid(childSnap.getKey());
                    Log.d("uid", childSnap.getKey());

                    if(childSnap.child("phone").getValue().toString().equals(loggedInUser.getPhoneNumber())) {
                        mycontact.setMe(true);
                    }


                }

                if(snapshot.exists()) {

                    Log.d("snapshots chk_isUser", "true");

                    mycontact.setUser(true);

                    Log.d("uid ", snapshot.getKey());
                    //mycontact.setUid(snapshot.getKey());
                    //Log.d("snap chk if me", snapshot.child("phone").getValue().toString() + " and me : " );//+ loggedInUser.getPhoneNumber());
                    if(!mycontact.isMe() && !uidList.contains(mycontact.getUid())) {
                        uidList.add(mycontact.getUid());
                        Log.d("dup", String.valueOf(contactArraylist.contains(mycontact.getPhone())));
                        contactArraylist.add(mycontact);
                        adapter.notifyDataSetChanged();
                    }

                }
//                else {
//                    Log.d("chk_isUser", "false");
//                    mycontact.setUser(false);
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void getContacts() {
        Log.d("snap getCon", "starting to get contacts");
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        Log.d("snap getCon", "cursor set");
        while(phones.moveToNext()) {
            Log.d("snap getCon", "inside while move next");
            @SuppressLint("Range") String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


            userObj contact = new userObj("", name, phone);
            Log.d("snap user issue", "now checking user");
            chk_isUser(contact);
            Log.d("snap user issue", "chk done, " + contact.getPhone() + " user = " + contact.getUser());


            //public void onDataChange() was taking action after getContacts done, check chk_isUser() just above this method




        }
    }
}