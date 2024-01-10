package com.example.meetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContentsListActivity extends AppCompatActivity {

    RecyclerView rv_myContentList;
    ArrayList<Contents> myList;
    DatabaseReference contRef;
    MyContentListAdapter contentListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_list);

        rv_myContentList = findViewById(R.id.rv_contentList);

        contRef = FirebaseDatabase.getInstance().getReference("Contents");

        myList = new ArrayList<>();
        rv_myContentList.setLayoutManager(new LinearLayoutManager(this));
        contentListAdapter = new MyContentListAdapter(this, myList);
        rv_myContentList.setAdapter(contentListAdapter);


        String meetingID = getIntent().getStringExtra("meetingID");
        String userID = getIntent().getStringExtra("userID");

        contRef.child(meetingID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Contents contentsLists =  dataSnapshot.getValue(Contents.class);
                    myList.add(contentsLists);


                }
                contentListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}