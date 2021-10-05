package com.example.s13.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.s13.Inbox;
import com.example.s13.R;
import com.example.s13.chatObj;

import java.util.ArrayList;

public class Adapter_activeChats extends RecyclerView.Adapter<Adapter_activeChats.ViewHolder>{
    private ArrayList<chatObj> activeChats_list = new ArrayList<>();
    private Context context;

    public Adapter_activeChats(Context context) {
        this.context = context;
    }

    public void setArrayList(ArrayList<chatObj> activeChats_list) {
        this.activeChats_list = activeChats_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_chats_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.chatID.setText(activeChats_list.get(position).getReceiverName());


        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent inbox_intent = new Intent(view.getContext(), Inbox.class);
                Bundle bundle = new Bundle();

                bundle.putString("chatID", activeChats_list.get(holder.getAdapterPosition()).getChatID());
                bundle.putString("receiver", activeChats_list.get(holder.getAdapterPosition()).getReceiverName());

                inbox_intent.putExtras(bundle);
                view.getContext().startActivity(inbox_intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return activeChats_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView chatID;
        private RelativeLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatID = itemView.findViewById(R.id.chatID);

            parent = itemView.findViewById(R.id.activeChats_parent);
        }
    }
}
