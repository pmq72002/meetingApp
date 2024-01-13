package com.example.meetingapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FileRoomActivity extends Activity {
    private static int PICCK_IMAGE_REQUEST =1;
    private static  int CHOOSE_FILE_REQUEST = 2;
    ImageView upload;
    TextView edt_file;
    Uri uri;
    TextView choosefile;
    String meetingID;
    StorageReference storageReference;
    DatabaseReference databaseReference, fileRef;
    RecyclerView rv_list_file;
    ArrayList<Files> myList;

    roomFileAdapter roomFileAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_room);


        rv_list_file = findViewById(R.id.rv_list_file);
        fileRef= FirebaseDatabase.getInstance().getReference("Files");
        myList = new ArrayList<>();
        rv_list_file.setLayoutManager(new LinearLayoutManager(this));
        roomFileAdapter  =new roomFileAdapter(this,myList);
        rv_list_file.setAdapter(roomFileAdapter);

        meetingID = getIntent().getStringExtra("Meeting_ID");

        fileRef.child(meetingID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Files files = dataSnapshot.getValue(Files.class);
                    myList.add(files);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        edt_file =findViewById(R.id.edt_file);
        choosefile = findViewById(R.id.choose_file_btn);
        upload = findViewById(R.id.upLoad_btn);
        upload.setEnabled(false);
        choosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_file.setText("");
                selectFile();
            }
        });


        LinearLayout layoutGet = findViewById(R.id.file_view);
        ViewGroup.LayoutParams layoutParams = layoutGet.getLayoutParams();
        int width = layoutParams.width;
        int height = layoutParams.height;

        getWindow().setLayout(width, height);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);
        ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();
        applyDim(root);
    }

    private void selectFile() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        String[] extraMimeTypes = {"application/pdf", "application/doc","image/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, extraMimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, CHOOSE_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String path = "";
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_FILE_REQUEST) {
                ClipData clipData = data.getClipData();
                //null and not null path
                if(clipData == null){
                    path += data.getData().toString();
                }else{
                    for(int i=0; i<clipData.getItemCount(); i++){
                        ClipData.Item item = clipData.getItemAt(i);
                        uri = item.getUri();
                        path += uri.toString() + "\n";
                    }
                }
            }
        }

        edt_file.setText(path);
        upload.setEnabled(true);
        upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadFileFirebase(data.getData());
                }
            });
    }

    private void uploadFileFirebase(Uri data){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        String fileName = edt_file.getText().toString();
        progressDialog.setTitle("Đang tải file lên...");
        progressDialog.show();
        StorageReference reference = storageReference.child("Files").child(System.currentTimeMillis()+"/"+fileName);
        reference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                uri = uriTask.getResult();
                String id = databaseReference.push().getKey();
                meetingID = getIntent().getStringExtra("Meeting_ID");
                final String getUserId = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString();
                Date dateAndTime = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());

                String date = dateFormat.format(dateAndTime);
                String time = timeFormat.format(dateAndTime);
                Files files = new Files(getUserId,uri.toString(),date,time);
                databaseReference.child("Files").child(meetingID).child(id).setValue(files);
                Toast.makeText(FileRoomActivity.this, "Tải tài liệu lên thành công", Toast.LENGTH_SHORT).show();
                edt_file.setText("");
                progressDialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress=(100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("Đang tải..."+(int) progress+ "%");
            }
        });
    }

    private static void applyDim(ViewGroup parent){
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0,0,parent.getWidth(),parent.getHeight());
        dim.setAlpha(200);

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }
}