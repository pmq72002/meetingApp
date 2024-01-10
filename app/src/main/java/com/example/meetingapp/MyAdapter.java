package com.example.meetingapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<Meetings> list;
    DatabaseReference usersRef, meetRef;


    public MyAdapter(Context context, ArrayList<Meetings> list) {
        this.context = context;
        this.list = list;
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_meet_list_design,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Meetings meetings = list.get(position);
        holder.id.setText(meetings.getId());
        holder.topic.setText(meetings.getTopic());
        holder.date.setText(meetings.getDate());
        holder.time.setText(meetings.getTime());
        Button createMeetBtn = holder.itemView.findViewById(R.id.join_meeting_btn);
        createMeetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meetRef = FirebaseDatabase.getInstance().getReference().child("Meetings");
                String meetingID = meetings.getId().toString();
                meetRef.child(meetingID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()){
                            DataSnapshot snapshot = task.getResult();
                            String pass = snapshot.child("password").getValue().toString();
                            if(holder.passConfirm.getText().toString().equals(pass)){
                                startMeeting(meetingID,getName(),meetings.getTopic(),pass);
                            }else {
                                Toast.makeText(context, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                            }


                        }


                    }

                });


            }
        });

    }

    void startMeeting(String meetingID, String name, String meetingTopic,String meetingPass){

        String userID = UUID.randomUUID().toString();

        Intent intent = new Intent(context, RoomMeetingActivity.class);
        intent.putExtra("meeting_ID", meetingID);
        intent.putExtra("name",name);
        intent.putExtra("meeting_Topic",meetingTopic);
        intent.putExtra("user_ID",userID);
        intent.putExtra("pass",meetingPass);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, topic, date, time,passConfirm;
        Button createBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.meeting_id);
            topic = itemView.findViewById(R.id.meeting_topic);
            date = itemView.findViewById(R.id.meeting_date);
            time = itemView.findViewById(R.id.meeting_time);
            passConfirm = itemView.findViewById(R.id.edt_pass);
            //createBtn = itemView.findViewById(R.id.create_meeting_btn);

        }
    }

    String getName(){
        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString();
        return name;
    }

}
