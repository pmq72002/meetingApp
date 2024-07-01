package com.example.meetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.media.MediaRecorder;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.locks.ReentrantLock;

public class RoomMeetingActivity extends AppCompatActivity {
    TextView meetingIDText, meetingNameTextview;
    TextView share_btn, file_btn;
    SpeechRecognizer speechRecognizer;

    TextView meetingTopicText;
    ImageView record_btn, stopRecord_btn;
    DatabaseReference contentRef;
    String meetingID, meetingTopic, name, userID, meetingPass,meetingName;
    private boolean isRecording = false;
    private boolean isClicked = false;
    private ReentrantLock buttonLock = new ReentrantLock();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_meeting);

        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        contentRef = FirebaseDatabase.getInstance().getReference().child("Contents");

        meetingIDText = findViewById(R.id.meeting_id_textview);
        share_btn = findViewById(R.id.share_btn);
        file_btn = findViewById(R.id.file_btn);
        meetingTopicText = findViewById(R.id.meeting_topic_textview);
        meetingNameTextview = findViewById(R.id.meeting_name_textview);
        record_btn = findViewById(R.id.mic_record);
        stopRecord_btn = findViewById(R.id.mic_stopRecord);
        stopRecord_btn.setEnabled(false);

        meetingID = getIntent().getStringExtra("meeting_ID");
        userID = getIntent().getStringExtra(("user_ID"));
        meetingTopic = getIntent().getStringExtra("meeting_Topic");
        name = getIntent().getStringExtra("name");
        meetingName = getIntent().getStringExtra("meeting_Name");
        meetingPass = getIntent().getStringExtra("pass");


        meetingIDText.setText("ID: " + meetingID);
        meetingTopicText.setText("Chủ đề: " + meetingTopic);
        meetingNameTextview.setText("Nguời tạo: " + meetingName);
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
                startActivity(Intent.createChooser(intent, "Chia sẻ qua "));
            }
        });

        record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checkAudioPermission();
                record_btn.setImageResource(R.drawable.mic_stop);
                stopRecord_btn.setImageResource(R.drawable.stop_record_true);
                //startSpeechToText();
                stopRecord_btn.setEnabled(true);
                isRecording = true;
            }
        });
        stopRecord_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record_btn.setImageResource(R.drawable.mic_start);
                stopRecord_btn.setImageResource(R.drawable.stop_record_false);
                //stopSpeechtoText();

            }
        });
    }


    public void addFragment() {
        long appID = AppConstant.appId;
        String appSign = AppConstant.appSigns;


        ZegoUIKitPrebuiltVideoConferenceConfig config = new ZegoUIKitPrebuiltVideoConferenceConfig();
        ZegoUIKitPrebuiltVideoConferenceFragment fragment = ZegoUIKitPrebuiltVideoConferenceFragment.newInstance(appID, appSign, userID, name,meetingID,config);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
    }
    private void startSpeechToText() {
         speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
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
    private void stopSpeechtoText(){
        if(isRecording){
            speechRecognizer.stopListening();
            isRecording = false;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Giải phóng tài nguyên khi ứng dụng kết thúc
        speechRecognizer.destroy();
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
    public static final int RecordAudioRequestCode = 2;


}