package com.wheretogo.ui;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.wheretogo.R;
import com.wheretogo.ui.adapters.IntroAdapter;

public class IntroActivity extends AppCompatActivity {

    TabLayout tabIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ViewPager viewPager = findViewById(R.id.introViewPager);
        IntroAdapter introAdapter = new IntroAdapter(this);
        viewPager.setAdapter(introAdapter);

        tabIndicator = findViewById(R.id.introTabIndicator);
        tabIndicator.setupWithViewPager(viewPager);

        Button buttonBack = findViewById(R.id.introLoginButton);
        buttonBack.setOnClickListener(v -> {
            // Аутентификация через вк sdk
        });
    }

}
