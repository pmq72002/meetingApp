package com.example.meetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class newMeet_Activity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button btn_date;
    Button btn_time;
    int day, month, year;
    int hour,minute;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    Button btn_create;
    TextView edt_Topic, edt_PassNew;

    SQLiteDatabase mydatabase;

    DatabaseReference meetRef;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meet);
        edt_Topic = findViewById(R.id.edt_Topic);
        edt_PassNew = findViewById(R.id.edt_PassNew);
        btn_create = findViewById(R.id.btn_create);

        meetRef =FirebaseDatabase.getInstance().getReference().child("Meetings");

        progressDialog = new ProgressDialog(this);



        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createMeetingData();
            }

        });





        //datepicker
        initDatePicker();
        btn_date =findViewById(R.id.btn_date);

        btn_date.setText(getTodayDate());
         //timepicker
        btn_time = findViewById(R.id.btn_time);
    }
    //datepicker
    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
         year = cal.get(Calendar.YEAR);
         month = cal.get(Calendar.MONTH);
         day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = makeDateString(dayOfMonth, month, year);
                btn_date.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = android.app.AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener,year,month,day);

    }
    private String makeDateString(int dayOfMonth, int month, int year){
        return dayOfMonth + "/"+(month+1)+"/"+year;
    }

    public void openDatePicker(View view) {

        datePickerDialog.show();
    }

    //timepicker
    public void popTimePicker(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            hour = selectedHour;
            minute = selectedMinute;
            btn_time.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));
            }
        };
        int style = android.app.AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,style,onTimeSetListener,hour,minute,true);
        timePickerDialog.setTitle("Chọn thời gian");
        timePickerDialog.show();
    }


    private void createMeetingData() {
    final String getMeetTopic = edt_Topic.getText().toString();
    final String getMeetPass = edt_PassNew.getText().toString();
    final String getMeetDate = btn_date.getText().toString();
    final String getMeetTime = btn_time.getText().toString();

    final String getMeetName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString();
        Date dateAndTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormatY = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat dateFormatM = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat dateFormatD = new SimpleDateFormat("dd", Locale.getDefault());
        String dateNowY = dateFormatY.format(dateAndTime).toString();
        String dateNowM = dateFormatM.format(dateAndTime).toString();
        String dateNowD = dateFormatD.format(dateAndTime).toString();

        String [] getMeetDatePath = getMeetDate.split("/");
        String getYear = getMeetDatePath[2].toString();
        String getMonth = getMeetDatePath[1].toString();
        String getDay = getMeetDatePath[0].toString();

        SimpleDateFormat timeFormatH = new SimpleDateFormat("hh", Locale.getDefault());
        SimpleDateFormat timeFormatM = new SimpleDateFormat("mm", Locale.getDefault());
        String timeH = timeFormatH.format(dateAndTime).toString();
        String timeM = timeFormatM.format(dateAndTime).toString();
        String [] getTimePath = getMeetTime.split(":");
        String getHour = getTimePath[0].toString();
        String getMinute = getTimePath[1].toString();

        if(getMeetTopic.equals("")){
            Toast.makeText(this, "Chủ đề không được để trống", Toast.LENGTH_SHORT).show();
        } else if (getMeetPass.equals("")) {
            Toast.makeText(this, "Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
        } else if (getYear.compareTo(dateNowY) < 0) {
            Toast.makeText(this,"Ngày không phù hợp" , Toast.LENGTH_SHORT).show();
        } else if (getYear.compareTo(dateNowY) == 0&&getMonth.compareTo(dateNowM)<0) {
            Toast.makeText(this,"Ngày không phù hợp" , Toast.LENGTH_SHORT).show();
        }else if (getYear.compareTo(dateNowY) == 0&&getMonth.compareTo(dateNowM)==0&&getDay.compareTo(dateNowD)<0) {
            Toast.makeText(this,"Ngày không phù hợp" , Toast.LENGTH_SHORT).show();
        }else if (getHour.compareTo(timeH)<0) {
                Toast.makeText(this, "Giờ không phù hợp", Toast.LENGTH_SHORT).show();
        }else if (getHour.compareTo(timeH)==0&&getMinute.compareTo(timeM)<0) {
                Toast.makeText(this,"Giờ không phù hợp" , Toast.LENGTH_SHORT).show();

        } else{
            progressDialog.setTitle("Đang tạo cuộc họp");
            progressDialog.setMessage("Vui lòng đợi trong giây lát...");
            progressDialog.show();

            Random random = new Random();
            int randomNum = random.nextInt(90000)+10000;

            String id = String.valueOf(randomNum);


            HashMap<String,Object> profileMap = new HashMap<>();
            profileMap.put("id",id);
            profileMap.put("topic",getMeetTopic);
            profileMap.put("password",getMeetPass);
            profileMap.put("date",getMeetDate);
            profileMap.put("time",getMeetTime);
            profileMap.put("name",getMeetName);


            meetRef.child(id).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(newMeet_Activity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();
                        Toast.makeText(newMeet_Activity.this, "Cuộc họp đã được tạo", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}