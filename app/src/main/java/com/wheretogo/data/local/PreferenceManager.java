package com.wheretogo.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.wheretogo.models.User;

public class PreferenceManager {
    private Context context;
    private static final String PREFERENCE_NAME = "wheretogo";
    private static final String USER_CLIENT_ID = "user_client_id";
    private static final String USER_TOKEN = "user_token";
    private static final String USER_FIRST_NAME = "user_first_name";
    private static final String USER_LAST_NAME = "user_last_name";

    public PreferenceManager(@NonNull Context context) {
        this.context = context;
    }

    public void updateToken(@NonNull String token) {
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
                .putString(USER_TOKEN, token)
                .apply();
    }

    public User getUser() {
        SharedPreferences sh = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        int id = sh.getInt(USER_CLIENT_ID, 0);
        String token = sh.getString(USER_TOKEN, "");
        String firstName = sh.getString(USER_FIRST_NAME, "");
        String lastName = sh.getString(USER_LAST_NAME, "");
        return new User(firstName, lastName, id, token);
    }

    public void saveUser(User user) {
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
                .putInt(USER_CLIENT_ID, user.getClientId())
                .putString(USER_TOKEN, user.getToken())
                .putString(USER_FIRST_NAME, user.getFirstName())
                .putString(USER_LAST_NAME, user.getLastName())
                .apply();
    }
}
