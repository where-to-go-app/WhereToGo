package com.wheretogo;

import android.content.Intent;

import androidx.room.Room;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKTokenExpiredHandler;
import com.wheretogo.localDB.AppDatabase;
import com.wheretogo.localDB.DatabaseActions;
import com.wheretogo.ui.IntroActivity;

public class Application extends android.app.Application {
    private AppDatabase db;
    public static DatabaseActions databaseActions;
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

        // Room db initialize
        db = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();
        databaseActions = new DatabaseActions(db);
    }

    public DatabaseActions getDatabaseActions() {
        return databaseActions;
    }
}
