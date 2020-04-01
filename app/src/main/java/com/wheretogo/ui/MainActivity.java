package com.wheretogo.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.wheretogo.R;
import com.wheretogo.ui.fragments.IntroFragment;

public class MainActivity extends AppCompatActivity {

    private final String MY_SETTINGS = "hasVisited";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        // Если приложение запускается первый раз на устройстве, то выводим Intro
        boolean hasVisited = sp.getBoolean("hasVisited", false);
        if (!hasVisited) {
            Intent intent = new Intent(MainActivity.this, IntroFragment.class);
            startActivity(intent);
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.commit();
        }
    }

}
