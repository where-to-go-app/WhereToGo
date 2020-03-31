package com.wheretogo.ui.fragments;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.text.NoCopySpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.wheretogo.R;
import com.wheretogo.ui.ImageAdapter;
import com.wheretogo.ui.LoginActivity;
import com.wheretogo.ui.MainActivity;
import com.wheretogo.ui.ImageAdapter;

import java.lang.annotation.Documented;

public class IntroFragment extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ViewPager viewPager = findViewById(R.id.vpMain);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        //viewPager.setAdapter(vpAdapter);
        viewPager.setAdapter(imageAdapter);
        Button buttonBack = findViewById(R.id.buttonToMain);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(IntroFragment.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                }
            }
        });
    }
}

