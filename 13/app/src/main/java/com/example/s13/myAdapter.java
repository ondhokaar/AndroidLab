package com.example.s13;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.security.PrivateKey;
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(contactList.get(position).getName());
        holder.phone.setText(contactList.get(position).getPhone());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = FirebaseDatabase.getInstance().getReference().push().getKey();
                FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat").setValue(true);
                FirebaseDatabase.getInstance().getReference().child("user").child(contactList.get(position).getUid()).child("chat").setValue(true);


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
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
