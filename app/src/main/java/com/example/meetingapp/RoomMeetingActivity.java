package com.example.meetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceConfig;
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceFragment;

public class RoomMeetingActivity extends AppCompatActivity {
    TextView meetingIDText, meetingNameTextview;
    TextView share_btn;

    TextView meetingTopicText;

    String meetingID, meetingTopic, name, userID, meetingPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_meeting);


        meetingIDText = findViewById(R.id.meeting_id_textview);
        share_btn = findViewById(R.id.share_btn);
        meetingTopicText = findViewById(R.id.meeting_topic_textview);
        meetingNameTextview = findViewById(R.id.meeting_name_textview);

        meetingID = getIntent().getStringExtra("meeting_ID");
        userID = getIntent().getStringExtra(("user_ID"));
        meetingTopic = getIntent().getStringExtra("meeting_Topic");
        name = getIntent().getStringExtra("name");
        meetingPass = getIntent().getStringExtra("pass");


        meetingIDText.setText("ID: " + meetingID);
        meetingTopicText.setText("Chủ đề: " + meetingTopic);
        meetingNameTextview.setText("Nguời tạo: " + name);
        addFragment();

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra((Intent.EXTRA_TEXT), "ID: " + meetingID+"\n Mật khẩu: "+meetingPass);
                startActivity(Intent.createChooser(intent, "Share via"));
            }
        });
    }


    public void addFragment() {
        long appID = AppConstant.appId;
        String appSign = AppConstant.appSigns;


        ZegoUIKitPrebuiltVideoConferenceConfig config = new ZegoUIKitPrebuiltVideoConferenceConfig();
        // config.turnOnCameraWhenJoining = false;
        // config.turnOnMicrophoneWhenJoining = false;
        ZegoUIKitPrebuiltVideoConferenceFragment fragment = ZegoUIKitPrebuiltVideoConferenceFragment.newInstance(appID, appSign,userID, name, meetingID, config);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }
}