package com.example.meetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceConfig;
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceFragment;

import java.util.ArrayList;
import java.util.Locale;

public class RoomMeetingActivity extends AppCompatActivity {
    TextView meetingIDText, meetingNameTextview;
    TextView share_btn, file_btn;

    TextView meetingTopicText;
    ImageView record_btn;
    DatabaseReference contentRef;
    String meetingID, meetingTopic, name, userID, meetingPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_meeting);

        contentRef = FirebaseDatabase.getInstance().getReference().child("Contents");

        meetingIDText = findViewById(R.id.meeting_id_textview);
        share_btn = findViewById(R.id.share_btn);
        file_btn = findViewById(R.id.file_btn);
        meetingTopicText = findViewById(R.id.meeting_topic_textview);
        meetingNameTextview = findViewById(R.id.meeting_name_textview);
        record_btn = findViewById(R.id.mic_record);

        meetingID = getIntent().getStringExtra("meeting_ID");
        userID = getIntent().getStringExtra(("user_ID"));
        meetingTopic = getIntent().getStringExtra("meeting_Topic");
        name = getIntent().getStringExtra("name");
        meetingPass = getIntent().getStringExtra("pass");


        meetingIDText.setText("ID: " + meetingID);
        meetingTopicText.setText("Chủ đề: " + meetingTopic);
        meetingNameTextview.setText("Nguời tạo: " + name);
        addFragment();

        file_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomMeetingActivity.this, FileRoomActivity.class);
                intent.putExtra("Meeting_ID",meetingID);
                startActivity(intent);
            }
        });

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

        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAudioPermission();
                record_btn.setImageResource(R.drawable.mic_stop);
                startSpeechToText();
            }
        });
    }


    public void addFragment() {
        long appID = AppConstant.appId;
        String appSign = AppConstant.appSigns;


        ZegoUIKitPrebuiltVideoConferenceConfig config = new ZegoUIKitPrebuiltVideoConferenceConfig();
         config.turnOnCameraWhenJoining = false;
         config.turnOnMicrophoneWhenJoining = false;
        ZegoUIKitPrebuiltVideoConferenceFragment fragment = ZegoUIKitPrebuiltVideoConferenceFragment.newInstance(appID, appSign,userID, name, meetingID, config);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }
    private void startSpeechToText() {
        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {}

            @Override
            public void onBeginningOfSpeech() {}

            @Override
            public void onRmsChanged(float v) {}

            @Override
            public void onBufferReceived(byte[] bytes) {}


            @Override
            public void onEndOfSpeech() {

                record_btn.setImageResource(R.drawable.mic_start);
            }

            @Override
            public void onError(int i) {}

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (result != null) {
                    // attaching the output
                    // to our textview

                    final String getMeetContent ="";
                    final String getUserId = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString();
                    String id = contentRef.push().getKey();
                    String meetContent = getMeetContent.concat(result.get(0));
                    Contents contents = new Contents(meetingID,getUserId,meetContent);

//                    HashMap<String, String> contentMap = new HashMap<>();
//
//                    contentMap.put("meetingId",meetingID);
//                    contentMap.put("id", getUserId);
//                    contentMap.put("contents", meetContent);

                    contentRef.child(meetingID).child(id).setValue(contents).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(RoomMeetingActivity.this, "Đã ghi âm cuộc họp", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
            @Override
            public void onPartialResults(Bundle bundle) {}

            @Override
            public void onEvent(int i, Bundle bundle) {}
        });
        speechRecognizer.startListening(speechRecognizerIntent);
    }
    private void checkAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0) {
            Toast.makeText(this, "Đang ghi âm", Toast.LENGTH_SHORT).show();
        }
    }
    public static final int RecordAudioRequestCode = 1;

}