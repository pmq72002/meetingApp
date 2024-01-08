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
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    private Button saveBtn;
    private EditText userNameET;
    private EditText userClassET;
    private ImageView profileImg;
    private static int GallaryPick = 1;
    private Uri ImageUri;
    private StorageReference userProfileImgRef;
    private String downloadUrl;
    private DatabaseReference userRef;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        userProfileImgRef = FirebaseStorage.getInstance().getReference().child("Profile Image");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        saveBtn = findViewById(R.id.save_setting);
        userNameET = findViewById(R.id.username_setting);
        userClassET = findViewById(R.id.class_setting);
        profileImg = findViewById(R.id.setting_profile_image);

        progressDialog = new ProgressDialog(this);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallaryIntent = new Intent();
                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                gallaryIntent.setType("image/*");
                startActivityForResult(gallaryIntent, GallaryPick);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });

        retrieveUserInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GallaryPick && resultCode==RESULT_OK && data!=null){
            ImageUri = data.getData();
            profileImg.setImageURI(ImageUri);
        }
    }

    private void saveUserData(){
        final String getUserName = userNameET.getText().toString();
        final String getUserClass = userClassET.getText().toString();
        if(ImageUri == null){
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("image")){
                        saveInfoOnly();
                    }else {
                        Toast.makeText(SettingActivity.this, "Hãy chọn hình ảnh trước", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (getUserName.equals("")) {
            Toast.makeText(this, "Họ tên không được để trống", Toast.LENGTH_SHORT).show();
        }else if (getUserClass.equals("")){
            Toast.makeText(this, "Lớp không được để trống", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.setTitle("Đang cài đặt tài khoản");
            progressDialog.setMessage("Vui lòng đợi trong giây lát...");
            progressDialog.show();
            final StorageReference filePath = userProfileImgRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            final UploadTask uploadTask = filePath.putFile(ImageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    downloadUrl=filePath.getDownloadUrl().toString();
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        downloadUrl=task.getResult().toString();
                        HashMap<String,Object> profileMap = new HashMap<>();
                        profileMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        profileMap.put("name",getUserName);
                        profileMap.put("class",getUserClass);
                        profileMap.put("image",downloadUrl);

                        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    progressDialog.dismiss();
                                    Toast.makeText(SettingActivity.this, "Thông tin tài khoản đã được cập nhật", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }
    private void saveInfoOnly(){
        final String getUserName = userNameET.getText().toString();
        final String getUserClass = userClassET.getText().toString();



        if (getUserName.equals("")) {
            Toast.makeText(this, "Họ tên không được để trống", Toast.LENGTH_SHORT).show();
        }else if (getUserClass.equals("")){
            Toast.makeText(this, "Lớp không được để trống", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setTitle("Đang cài đặt tài khoản");
            progressDialog.setMessage("Vui lòng đợi trong giây lát...");
            progressDialog.show();

            HashMap<String,Object> profileMap = new HashMap<>();
            profileMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
            profileMap.put("name",getUserName);
            profileMap.put("class",getUserClass);

            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();
                        Toast.makeText(SettingActivity.this, "Thông tin tài khoản đã được cập nhật", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void retrieveUserInfo() {
        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String imageDb = snapshot.child("image").getValue(String.class);
                    String nameDb = snapshot.child("name").getValue(String.class);
                    String classDb = snapshot.child("class").getValue(String.class);

                    // Set the retrieved values to the corresponding views
                    userNameET.setText(nameDb);
                    userClassET.setText(classDb);
                    Picasso.get().load(imageDb).placeholder(R.drawable.profile).into(profileImg);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }
}
