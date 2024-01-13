package com.example.meetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;

public class FileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

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
    private static void applyDim(ViewGroup parent){
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0,0,parent.getWidth(),parent.getHeight());
        dim.setAlpha(200);

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }
}