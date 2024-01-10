package com.example.meetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContentsActivity extends AppCompatActivity {
    RecyclerView rv_myContents;
    ArrayList<Meetings> myList;
    DatabaseReference meetRef;
    MyContentAdapter contentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        rv_myContents = findViewById(R.id.rv_MyContents);
        meetRef = FirebaseDatabase.getInstance().getReference("Meetings");

        myList = new ArrayList<>();
        rv_myContents.setLayoutManager(new LinearLayoutManager(this));
        contentAdapter = new MyContentAdapter(this, myList);
        rv_myContents.setAdapter(contentAdapter);

        meetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Meetings meetings =  dataSnapshot.getValue(Meetings.class);
                    myList.add(meetings);


                }
                contentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}