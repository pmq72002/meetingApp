package com.example.meetingapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ArrayListMeet extends ArrayAdapter<ListMeet> {
    Activity context;
    int Idlayout;
    ArrayList<ListMeet> mylist;

    public ArrayListMeet(Activity context, int idlayout, ArrayList<ListMeet> mylist) {
        super(context, idlayout, mylist);
        this.context = context;
        Idlayout = idlayout;
        this.mylist = mylist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myflacter = context.getLayoutInflater();
        convertView = myflacter.inflate(Idlayout,null);
        ListMeet myMeet = mylist.get(position);
        TextView txt_topic_listMeet = convertView.findViewById(R.id.txt_topic_listMeet);
        txt_topic_listMeet.setText(myMeet.getTopic());
        TextView txt_des_listMeet = convertView.findViewById(R.id.txt_des_listMeet);
        txt_des_listMeet.setText(myMeet.getDescribe());
        return convertView;
    }
}
