package com.wheretogo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.wheretogo.R;
import com.wheretogo.ui.adapters.IntroAdapter;

import org.jetbrains.annotations.NotNull;

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
            VK.login(this);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        VKAuthCallback callback = new VKAuthCallback() {
            @Override
            public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                Log.d(IntroActivity.class.getSimpleName(), vkAccessToken.toString());
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLoginFailed(int i) {
                Log.d(IntroActivity.class.getSimpleName(), "Login failed: " + i);
                Toast.makeText(IntroActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
            }
        };

        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
