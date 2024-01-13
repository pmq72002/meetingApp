package com.example.meetingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyFilesActivity extends AppCompatActivity {

    protected static final int RESULT_SPEECH = 1;
    EditText search_file;
    ImageView mic_stt_file;
    DatabaseReference fileRef;
    RecyclerView rv_myFiles;
    ArrayList<Meetings> myList;
    MyFilesAdapter myFilesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_files);

        mic_stt_file = findViewById(R.id.mic_stt_file);
        mic_stt_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    search_file.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Thiết bị của bạn không hỗ trợ Speech To Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        search_file = findViewById(R.id.edt_search_file);

        search_file.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    search(s.toString());
                }else {
                    search("");
                }
            }
        });

        rv_myFiles = findViewById(R.id.rv_MyFiles);
        fileRef = FirebaseDatabase.getInstance().getReference("Meetings");

        myList = new ArrayList<>();
        rv_myFiles.setLayoutManager(new LinearLayoutManager(this));
        myFilesAdapter = new MyFilesAdapter(this, myList);
        rv_myFiles.setAdapter(myFilesAdapter);

        fileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Meetings meetings =  dataSnapshot.getValue(Meetings.class);
                    myList.add(meetings);


                }
                myFilesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void search(String s) {
        Query query = fileRef.orderByChild("id").startAt(s).endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    myList.clear();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        final Meetings meetings = dataSnapshot.getValue(Meetings.class);
                        myList.add(meetings);
                    }
                    myFilesAdapter = new MyFilesAdapter(getApplicationContext(),myList);
                    rv_myFiles.setAdapter(myFilesAdapter);
                    myFilesAdapter.notifyDataSetChanged();
                }else {
                    myList.clear();
                    rv_myFiles.setAdapter(myFilesAdapter);
                    myFilesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SPEECH:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    search_file.setText(text.get(0));
                }
                break;
        }
    }
}