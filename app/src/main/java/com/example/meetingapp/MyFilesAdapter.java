package com.example.meetingapp;

import android.content.Context;
import android.content.Intent;
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

public class MyFilesAdapter extends RecyclerView.Adapter<MyFilesAdapter.MyViewHolder> {
    Context context;
    ArrayList<Meetings> list;
    DatabaseReference usersRef, meetRef;

    public MyFilesAdapter(Context context, ArrayList<Meetings> list) {
        this.context = context;
        this.list = list;
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_files_design,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Meetings meetings = list.get(position);
        holder.id.setText(meetings.getId());
        holder.topic.setText(meetings.getTopic());
        holder.date.setText(meetings.getDate());
        holder.time.setText(meetings.getTime());
        String meetingID = meetings.getId().toString();
        String userID = meetings.getName();

        Button reviewFile = holder.itemView.findViewById(R.id.review_files_btn);
        reviewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileList = new Intent(holder.itemView.getContext(), FilesListActivity.class);
                fileList.putExtra("meetingID",meetingID);
                fileList.putExtra("userID", userID);
                holder.itemView.getContext().startActivity(fileList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, topic, date, time;
        Button createBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.meeting_id_file);
            topic = itemView.findViewById(R.id.meeting_topic_file);
            date = itemView.findViewById(R.id.meeting_date_file);
            time = itemView.findViewById(R.id.meeting_time_file);


        }
    }

}