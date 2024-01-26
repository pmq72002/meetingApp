package com.example.meetingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference, reference1;

    private Button saveBtn;
    private TextView userNameET;
    private EditText userMnvET, userPositionET;
    private ImageView profileImg;


    private String  userID;
    private DatabaseReference userRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference1 = FirebaseDatabase.getInstance().getReference("Users setting");
        userID = user.getUid();

       // userRef = FirebaseDatabase.getInstance().getReference("Users");

        saveBtn = findViewById(R.id.save_setting);
        userNameET = findViewById(R.id.user_name_setting);
        userMnvET = findViewById(R.id.mnv_setting);
        userPositionET = findViewById(R.id.position_setting);
        profileImg = findViewById(R.id.setting_profile_image);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef = FirebaseDatabase.getInstance().getReference("Users setting");


                saveInfoOnly();

            }
        });
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile = snapshot.getValue(Users.class);
                        if(userProfile!=null){
                            String name = userProfile.name;

                            String profile = userProfile.profile;

                            userNameET.setText(name);

                            Picasso.get().load(profile).placeholder(R.drawable.profile).into(profileImg);


                        }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference1.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile = snapshot.getValue(Users.class);
                if(userProfile!=null){
                    String mnv = userProfile.mnv;
                    String position = userProfile.position;

                    userMnvET.setText(mnv);
                    userPositionET.setText(position);


                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void saveInfoOnly(){
        final String getUserMnv = userMnvET.getText().toString();
        final String getUserPosition = userPositionET.getText().toString();



        if (getUserMnv.equals("")) {
            Toast.makeText(this, "Mã nhân viên không được để trống", Toast.LENGTH_SHORT).show();
        }else if (getUserPosition.equals("")){
            Toast.makeText(this, "Vị trí không được để trống", Toast.LENGTH_SHORT).show();
        }else {

            HashMap profileMap = new HashMap<>();
            profileMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
            profileMap.put("name",FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            profileMap.put("mnv",getUserMnv);
            profileMap.put("position",getUserPosition);
            profileMap.put("profile",FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SettingActivity.this, "Thông tin tài khoản đã được cập nhật", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

}
