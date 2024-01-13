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

public class FilesListActivity extends AppCompatActivity {
    protected static final int RESULT_SPEECH = 2;
    EditText edt_search_list_file;
    ImageView mic_stt_list_file;
    RecyclerView rv_list_file_read;
    DatabaseReference databaseReference, fileRef;
    String meetingID;
    ArrayList<Files> myList;
    MyFilesListAdapter myFilesListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_list);


        mic_stt_list_file = findViewById(R.id.mic_stt_list_file);
        mic_stt_list_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    edt_search_list_file.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Thiết bị của bạn không hỗ trợ Speech To Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        edt_search_list_file = findViewById(R.id.edt_search_list_file);
        edt_search_list_file.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    search(s.toString().substring(0,1).toUpperCase());
                }else {
                    search("");
                }
            }
        });
        rv_list_file_read = findViewById(R.id.rv_MyFilesList);
        fileRef= FirebaseDatabase.getInstance().getReference("Files");
        myList = new ArrayList<>();
        rv_list_file_read.setLayoutManager(new LinearLayoutManager(this));
        myFilesListAdapter  =new MyFilesListAdapter(this,myList);
        rv_list_file_read.setAdapter(myFilesListAdapter);

        meetingID = getIntent().getStringExtra("meetingID");

        fileRef.child(meetingID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Files files = dataSnapshot.getValue(Files.class);
                    myList.add(files);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void search(String s) {
        String meetingID = getIntent().getStringExtra("meetingID");
        Query query = fileRef.child(meetingID).orderByChild("fileID").startAt(s).endAt(s+"\uf8ff");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    myList.clear();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        final Files files = dataSnapshot.getValue(Files.class);
                        myList.add(files);
                    }
                    myFilesListAdapter = new MyFilesListAdapter(getApplicationContext(),myList);
                    rv_list_file_read.setAdapter(myFilesListAdapter);
                    myFilesListAdapter.notifyDataSetChanged();
                }else{
                    myList.clear();
                    rv_list_file_read.setAdapter(myFilesListAdapter);
                    myFilesListAdapter.notifyDataSetChanged();
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
                    edt_search_list_file.setText(text.get(0));

                }
                break;
        }
    }
}