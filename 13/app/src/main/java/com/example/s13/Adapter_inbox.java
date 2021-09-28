package com.example.s13;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Adapter_inbox extends RecyclerView.Adapter<Adapter_inbox.ViewHolder>{
    private ArrayList<messageObj> msg_list = new ArrayList<>();
    private Context context;
    private int redcolor = 0;
    private int bluecolor = 0;
    void adjustColor(String sender, String user) {
        if(sender == user) {
            //redcolor = 255;
            //bluecolor = 0;
            Log.d("color", "setting red");
        }
        else {
           // redcolor = 0;
            //bluecolor = 255;
        }
    }
    public Adapter_inbox(Context context) {
        this.context = context;
    }

    public void setArrayList(ArrayList<messageObj> msg_list) {
        this.msg_list = msg_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inbox_eachmsg, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.senderID.setText(msg_list.get(position).getSenderID());
        holder.msg.setText(msg_list.get(position).getMsg());

        if(msg_list.get(position).getSenderID() == FirebaseAuth.getInstance().getUid()) {
            holder.senderID.setTextColor(Color.argb(100, redcolor, 255, 255));
            holder.msg.setTextColor(Color.argb(100, redcolor, 255, 255));
        }
        else {
            holder.senderID.setTextColor(Color.argb(100, 255, 255, 255));

            holder.msg.setTextColor(Color.argb(100, 255, 255, 255));
        }
//        holder.senderID.setText("sender: ");
//        holder.msg.setText("msg msg msg onek msg");
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return msg_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView senderID, msg;
        private RelativeLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            senderID = itemView.findViewById(R.id.senderID);
            msg = itemView.findViewById(R.id.msg);
            parent = itemView.findViewById(R.id.msg_parent);
        }
    }
}
