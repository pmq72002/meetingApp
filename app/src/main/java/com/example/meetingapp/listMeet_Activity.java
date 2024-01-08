package com.example.meetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class listMeet_Activity extends AppCompatActivity {
    ListView lv_listMeet;
    String topic[] = {"Cuộc họp 1","Cuộc họp 2", "Cuộc họp 3", "Cuộc họp 4", "Cuộc họp 5"};
    String describe[] = {"Đây là cuộc họp thứ nhất","Đây là cuộc họp thứ hai","Đây là cuộc họp thứ ba","Đây là cuộc họp thứ bốn","Đây là cuộc họp thứ năm"};
    ArrayList<ListMeet> myList;
    ArrayListMeet myadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_meet);
        lv_listMeet = findViewById(R.id.lv_listMeet);
        myList = new ArrayList<>();
        for (int i = 0; i < topic.length; i++) {
            myList.add(new ListMeet(topic[i],describe[i]));
        }
        myadapter = new ArrayListMeet(listMeet_Activity.this, R.layout.layout_list_meet_child, myList);
        lv_listMeet.setAdapter(myadapter);
    }
}