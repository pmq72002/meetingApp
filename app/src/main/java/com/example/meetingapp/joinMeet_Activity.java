package com.example.meetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class joinMeet_Activity extends AppCompatActivity {
    Button btn_Join;
    TextView edt_Id,edt_PassJoin;
    CheckBox cb_mic, cb_cam;
    DatabaseReference meetRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_meet);

        meetRef = FirebaseDatabase.getInstance().getReference().child("Meetings");

        meetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    Meetings meetings = snap.getValue(Meetings.class);
                    String passF = meetings.getPass();

                        btn_Join.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (edt_PassJoin.getText().toString().equals(passF)) {
                                    jointMeeting(meetings.getId(), meetings.getPass());
                                } else {
                                    Toast.makeText(joinMeet_Activity.this, "Nhập sai mật khẩu", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edt_Id = findViewById(R.id.edt_Id);
        edt_PassJoin = findViewById(R.id.edt_PassJoin);
        cb_mic = findViewById(R.id.cb_mic);
        cb_cam = findViewById(R.id.cb_cam);
        btn_Join = findViewById(R.id.btn_Join);



    }

    void jointMeeting(String meetingID, String meetingPass){

            String userID = UUID.randomUUID().toString();

//            Intent intent = new Intent(joinMeet_Activity.this, RoomMeetingActivity.class);
//            intent.putExtra("meeting_ID", meetingID);
//            intent.putExtra("user_ID",userID);
//            intent.putExtra("pass", meetingPass);
            //startActivity(intent);


    }
}