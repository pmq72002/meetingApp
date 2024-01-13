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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContentsListActivity extends AppCompatActivity {

    protected static final int RESULT_SPEECH = 2;
    RecyclerView rv_myContentList;
    ArrayList<Contents> mylist;
    DatabaseReference contRef;
    MyContentListAdapter contentListAdapter;
    EditText edt_search_list;
    ImageView mic_stt_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_list);


        mic_stt_list = findViewById(R.id.mic_stt_list);
        mic_stt_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    edt_search_list.setText("");
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Thiết bị của bạn không hỗ trợ Speech To Text", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        edt_search_list = findViewById(R.id.edt_search_list);
        edt_search_list.addTextChangedListener(new TextWatcher() {
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


        rv_myContentList = findViewById(R.id.rv_contentList);

        contRef = FirebaseDatabase.getInstance().getReference("Contents");

        mylist = new ArrayList<>();
        rv_myContentList.setLayoutManager(new LinearLayoutManager(this));
        contentListAdapter = new MyContentListAdapter(this, mylist);
        rv_myContentList.setAdapter(contentListAdapter);


        String meetingID = getIntent().getStringExtra("meetingID");


        contRef.child(meetingID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Contents contentsLists =  dataSnapshot.getValue(Contents.class);
                    mylist.add(contentsLists);


                }
                contentListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void searchVoice(String s){
        String meetingID = getIntent().getStringExtra("MeetingID");
        Query query1 = contRef.child(meetingID).orderByChild("id").equalTo(s);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    mylist.clear();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        final Contents contents = dataSnapshot.getValue(Contents.class);
                        mylist.add(contents);
                    }
                    contentListAdapter = new MyContentListAdapter(getApplicationContext(),mylist);
                    rv_myContentList.setAdapter(contentListAdapter);
                    contentListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void search(String s) {
        String meetingID = getIntent().getStringExtra("meetingID");
        Query query = contRef.child(meetingID).orderByChild("id").startAt(s).endAt(s+"\uf8ff");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    mylist.clear();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        final Contents contents = dataSnapshot.getValue(Contents.class);
                        mylist.add(contents);
                    }
                    contentListAdapter = new MyContentListAdapter(getApplicationContext(),mylist);
                    rv_myContentList.setAdapter(contentListAdapter);
                    contentListAdapter.notifyDataSetChanged();
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
                    edt_search_list.setText(text.get(0));

                }
                break;
        }
    }

}