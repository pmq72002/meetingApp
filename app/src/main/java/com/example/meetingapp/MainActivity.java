package com.example.meetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    DBHelper dbHelper;



    ImageView img_profile;
    TextView txt_profile;
    Button btn_newMeet, btn_myMeet, btn_contentsMeet, btn_document;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        dbHelper = new DBHelper(this);
        btn_newMeet = findViewById(R.id.btn_newMeet);
        btn_myMeet = findViewById(R.id.btn_myMeet);
        btn_contentsMeet = findViewById(R.id.btn_contentsMeet);
        btn_document = findViewById(R.id.btn_document);

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
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);

                return true;
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    private void retrieveUserInfoMain() {
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String imageDb = snapshot.child("image").getValue(String.class);
                    String nameDb = snapshot.child("name").getValue(String.class);

                    // Set the retrieved values to the corresponding views
                    txt_profile.setText(nameDb);
                    Picasso.get().load(imageDb).placeholder(R.drawable.profile).into(img_profile);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }
}

