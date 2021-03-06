package com.example.s13.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s13.Inbox;
import com.example.s13.MainActivity;
import com.example.s13.R;
import com.example.s13.userObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder>{
    private ArrayList<userObj> contactList = new ArrayList<>();
    private Context context;

    public myAdapter(Context context) {
        this.context = context;
    }

    public void setContactList(ArrayList<userObj> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_chat, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    private String generateNewChatThreadID(String uid1, String uid2) {
        if(uid1.compareTo(uid2) == -1) // uid1 < uid2
            return uid1+uid2;
        return uid2+uid1;
    }
    String tag = "new chat issue";
    private void createNewChat(int position, String uid1, String uid2) {

        Log.d(tag, "createnewChat() starting, bhruuuuuuuum bhuuuum bhuuuuuuu.....");

        String key = generateNewChatThreadID(uid1, uid2);

        FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(contactList.get(position).getName());  //uid1+uid2 : otherContactName
        FirebaseDatabase.getInstance().getReference().child("user").child(uid2).child("chat").child(key).setValue(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        //issue : race

        Log.d(tag, "creating chat on other user -> ::" + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() + " : displayName" + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());


    }
    private void openChat(int position) {
        Intent inbox_intent = new Intent(context, Inbox.class);
        Bundle bundle = new Bundle();
        String chatID_forBundle = generateNewChatThreadID(FirebaseAuth.getInstance().getUid(), contactList.get(position).getUid());
        bundle.putString("chatID", chatID_forBundle);
        bundle.putString("receiver", contactList.get(position).getName().toString());


        inbox_intent.putExtras(bundle);
        context.startActivity(inbox_intent);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(contactList.get(position).getName());
        holder.phone.setText(contactList.get(position).getPhone());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //first chck if already exists chat with this person, if so then open existing chat else creat new chat and open that
                Log.d("dup --- clicked on:", holder.name.getText().toString());

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");
                Query query = ref.equalTo(holder.name.getText().toString());
                if(contactList.get(position).getUser() && FirebaseAuth.getInstance().getCurrentUser() != null) {
                    createNewChat(position, FirebaseAuth.getInstance().getUid(), contactList.get(position).getUid());
                    openChat(position );
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + contactList.get(position).getPhone()));
                    context.startActivity(intent);
                }

//                query.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        Log.d("dup found? in your list -> ",String.valueOf(snapshot.exists()));
//                        if(snapshot.exists()){
//                            for (DataSnapshot chats : snapshot.getChildren()) {
//                                Log.d("dup printing snaps: ", chats.getValue().toString() + " : " + holder.name.getText().toString());
//                                if (chats.getValue().toString().equals(holder.name.getText().toString())) {
//
//                                    Intent inbox_intent = new Intent(view.getContext(), Inbox.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("chatID", chats.getKey());
//                                    inbox_intent.putExtras(bundle);
//                                    view.getContext().startActivity(inbox_intent);
//
//                                    break;
//
//                                }
//                            }
//
//                        }
//                        else {
//                            createNewChat(position, FirebaseAuth.getInstance().getUid(), contactList.get(position).getUid());
//                            //new chat creating
////                            Log.d("dup ", "no dup found, so creating new chat");
////                            String key = FirebaseDatabase.getInstance().getReference().push().getKey();
////                            FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(contactList.get(position).getName());
////                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("username");
////                            // FirebaseDatabase.getInstance().getReference().child("user").child(contactList.get(position).getUid()).child("chat").child(key).setValue("test");
////
////                            dbref.addListenerForSingleValueEvent(new ValueEventListener() {
////                                @Override
////                                public void onDataChange(@NonNull DataSnapshot snapshot) {
////                                    Log.d("active chats: ", snapshot.getValue().toString());
////                                    FirebaseDatabase.getInstance().getReference().child("user").child(contactList.get(position).getUid()).child("chat").child(key).setValue(snapshot.getValue().toString());
////                                }
////
////                                @Override
////                                public void onCancelled(@NonNull DatabaseError error) {
////
////                                }
////                            });
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });



                Log.d("chat ", contactList.get(position).getUid());


                //================================
//                Toast.makeText(context, "calling " + contactList.get(position).getName(), Toast.LENGTH_SHORT).show();
//                Uri uri = Uri.parse("tel:" + contactList.get(position).getPhone());
//                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
//
//                try {
//                    context.startActivity(intent);
//                } catch (Exception e) {
//                    Log.d("myAdapter impl: ", "cant open dialer");
//
//                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, phone;
        private RelativeLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            parent = itemView.findViewById(R.id.userList_parent);
        }
    }
}
