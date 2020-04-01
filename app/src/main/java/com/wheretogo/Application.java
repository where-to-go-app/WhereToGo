package com.wheretogo;

import android.content.Intent;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKTokenExpiredHandler;
import com.wheretogo.ui.IntroActivity;

public class Application extends android.app.Application {

    private VKTokenExpiredHandler tokenTracker = () -> {
        // Токен поменялся
        Intent intent = new Intent(this, IntroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    };

    @Override
    public void onCreate() {
        super.onCreate();
        VK.addTokenExpiredHandler(tokenTracker);
    }
}
