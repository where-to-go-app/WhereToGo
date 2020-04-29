package com.wheretogo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;
import com.wheretogo.R;
import com.wheretogo.data.local.PreferenceManager;
import com.wheretogo.data.remote.DefaultCallback;
import com.wheretogo.data.remote.RemoteActions;
import com.wheretogo.data.remote.RemoteClient;
import com.wheretogo.data.remote.VkClientRequest;
import com.wheretogo.models.User;
import com.wheretogo.ui.adapters.IntroAdapter;

import org.jetbrains.annotations.NotNull;

public class IntroActivity extends AppCompatActivity {

    private TabLayout tabIndicator;
    private RemoteActions remoteActions;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        preferenceManager = new PreferenceManager(this);
        remoteActions = new RemoteActions(new RemoteClient());
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
                getVkClientUser(vkAccessToken.getAccessToken());
            }

            @Override
            public void onLoginFailed(int i) {
                Toast.makeText(IntroActivity.this, R.string.error_message, Toast.LENGTH_SHORT).show();
            }
        };

        if (!VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getVkClientUser(String token) {
        VK.execute(new VkClientRequest(), new VKApiCallback<User>() {

            @Override
            public void success(User user) {
                user.setToken(token);
                preferenceManager.saveUser(user);
                authUser(user);
            }

            @Override
            public void fail(@NotNull Exception e) {

            }
        });
    }

    private void authUser(User user) {
        remoteActions.auth(user, new DefaultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                if (data) {
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(IntroActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int error) {
                Toast.makeText(IntroActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
