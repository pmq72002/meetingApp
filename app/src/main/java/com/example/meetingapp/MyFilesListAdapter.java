package com.example.meetingapp;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;

public class MyFilesListAdapter extends RecyclerView.Adapter<MyFilesListAdapter.MyViewHolder> {
     Context context;
    ArrayList<Files> list;
    DatabaseReference usersRef;


    public MyFilesListAdapter(Context context, ArrayList<Files> list) {
        this.context = context;
        this.list = list;
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.files_list_design,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Files FilesList = list.get(position);
        holder.id.setText(FilesList.getfileID());
        holder.files.setText(FilesList.getFileUrl());
        holder.time.setText(FilesList.getTime());
        holder.date.setText(FilesList.getDate());




    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, files,date,time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.sender_txt_list);
            files = itemView.findViewById(R.id.sender_file_list);
            date = itemView.findViewById(R.id.file_date_list);
            time = itemView.findViewById(R.id.file_time_list);



        }
    }

}