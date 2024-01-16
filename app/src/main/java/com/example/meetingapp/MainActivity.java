package com.example.meetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private DatabaseReference reference;
    private FirebaseUser user;
    private String  userID;
    GoogleApiClient mGoogleApiClient;
    ImageView img_profile;
    TextView txt_profile;
    Button btn_newMeet, btn_myMeet, btn_contentsMeet, btn_file;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();


        btn_newMeet = findViewById(R.id.btn_newMeet);
        btn_myMeet = findViewById(R.id.btn_myMeet);
        btn_contentsMeet = findViewById(R.id.btn_contentsMeet);
        btn_file = findViewById(R.id.btn_file);

        txt_profile = findViewById(R.id.txt_profile);
        img_profile = findViewById(R.id.img_profile);




        btn_newMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewMeet = new Intent(MainActivity.this, newMeet_Activity.class);
                startActivity(intentNewMeet);
            }
        });
        btn_myMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMyMeet = new Intent(MainActivity.this, myMeet_Activity.class);
                startActivity(intentMyMeet);
            }
        });

    retrieveUserInfoMain();

        btn_contentsMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contentIntent = new Intent(MainActivity.this, ContentsActivity.class);
                startActivity(contentIntent);
            }
        });

        btn_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileIntent = new Intent(MainActivity.this, MyFilesActivity.class);
                startActivity(fileIntent);
            }
        });
    }





    public void onShowMenu(View view) {
        PopupMenu menu = new PopupMenu(this, view);
        menu.setOnMenuItemClickListener(this);
        menu.inflate(R.menu.main_menu);
        menu.show();
        MenuItem setting = menu.getMenu().findItem(R.id.setting);
        setting.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                Intent settingItent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(settingItent);
                return true;
            }
        });
        MenuItem logout = menu.getMenu().findItem(R.id.logout);
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                AuthUI.getInstance().signOut(MainActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                });

                return true;
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    private void retrieveUserInfoMain() {
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile = snapshot.getValue(Users.class);
                if (userProfile != null) {
                    String name = userProfile.name;
                    String profile = userProfile.profile;

                    txt_profile.setText(name);

                    Picasso.get().load(profile).placeholder(R.drawable.profile).into(img_profile);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

