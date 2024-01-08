package com.example.meetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    EditText edtFname, edtLname, edtRuser, edtRpass, edtRrepass;
    Button btn_register, btn_ChildLogin;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtFname = findViewById(R.id.edtFname);
        edtLname = findViewById(R.id.edtLname);
        edtRuser = findViewById(R.id.edtRuser);
        edtRpass = findViewById(R.id.edtRpass);
        edtRrepass = findViewById(R.id.edtRrepass);

        dbHelper =new DBHelper(this);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, password, firstname, lastname, repassword;
                user = edtRuser.getText().toString();
                password = edtRpass.getText().toString();
                repassword = edtRrepass.getText().toString();
                firstname = edtFname.getText().toString();
                lastname = edtLname.getText().toString();

                if(user.equals("") || password.equals("") || repassword.equals("") || firstname.equals("") || lastname.equals("")  ){
                    Toast.makeText(RegisterActivity.this, "Hãy nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(password.equals(repassword)){
                        if(dbHelper.checkUsername(user)){
                            Toast.makeText(RegisterActivity.this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                            return;
                        }
                       boolean registerSuccess =  dbHelper.insertData(user,password, firstname, lastname);
                       if(registerSuccess){
                           Toast.makeText(RegisterActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                           new Handler().postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                   startActivity(intent);
                               }
                           }, 1000);
                       }else{
                           Toast.makeText(RegisterActivity.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                       }
                    }else {
                        Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_ChildLogin = findViewById(R.id.btn_ChildLogin);
        btn_ChildLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(myintent);
            }
        });
    }
}