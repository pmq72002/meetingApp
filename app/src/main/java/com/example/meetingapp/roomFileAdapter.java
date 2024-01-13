package com.example.meetingapp;

import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class roomFileAdapter extends RecyclerView.Adapter<roomFileAdapter.MyViewHolder> {
    Context context;
    ArrayList<Files> list;
    DatabaseReference usersRef, meetRef;

    public roomFileAdapter(Context context, ArrayList<Files> list) {
        this.context = context;
        this.list = list;
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_file_room,parent, false);
        return new roomFileAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Files files = list.get(position);
        holder.fileID.setText(files.getfileID());
        holder.fileUrl.setText(files.getFileUrl());
        holder.date.setText(files.getDate());
        holder.time.setText(files.getTime());




    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fileID, fileUrl,date, time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fileID = itemView.findViewById(R.id.sender_txt);
            fileUrl = itemView.findViewById(R.id.sender_file);
            date = itemView.findViewById(R.id.file_date);
            time = itemView.findViewById(R.id.file_time);


        }
    }

}

