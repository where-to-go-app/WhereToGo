package com.wheretogo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vk.api.sdk.VK;
import com.wheretogo.R;
import com.wheretogo.ui.fragments.CreateFragment;
import com.wheretogo.ui.fragments.MapFragment;
import com.wheretogo.ui.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements SettingsFragment.OnLogoutListener {

    private BottomNavigationView bottomNavigation;
    private FrameLayout placeholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!VK.isLoggedIn()) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDarkTransparent));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        findViews();

        bottomNavigation.setOnNavigationItemSelectedListener((item) -> {
            Fragment nextFragment;
            MapFragment.Mode mapMode = null;

            switch (item.getItemId()) {
                case R.id.nav_places:
                    mapMode = MapFragment.Mode.AROUND_PLACES;
                    nextFragment = new MapFragment();
                    break;
                case R.id.nav_search:
                    mapMode = MapFragment.Mode.SEARCH_PLACES;
                    nextFragment = new MapFragment();
                    break;
                case R.id.nav_favorite: {
                    mapMode = MapFragment.Mode.LOVE_PLACES;
                    nextFragment = new MapFragment();
                    break;
                }
                case R.id.nav_create: {
                    nextFragment = new CreateFragment();
                    break;
                }
                case R.id.nav_settings: {
                    nextFragment = new SettingsFragment();
                    break;
                }
                default: return false;
            }

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.placeholder);

            if (nextFragment instanceof MapFragment && currentFragment instanceof MapFragment) {
                MapFragment mapFragment = ((MapFragment) currentFragment);
                if (mapFragment.getCurrentMode() != mapMode) {
                    mapFragment.setNewMode(mapMode);
                }
                return true;

            }
            if (nextFragment instanceof MapFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MapFragment.MODE_EXTRA, mapMode);
                nextFragment.setArguments(bundle);
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.placeholder, nextFragment)
                    .commit();
            return true;
        });


        // Если начальный фрагмент не поставлен - поставить фрагмент с "местами рядом"
        if (getSupportFragmentManager().findFragmentById(R.id.placeholder) == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.placeholder, new MapFragment())
                    .commit();

            bottomNavigation.setSelectedItemId(R.id.nav_places);
        }
    }

    private void findViews() {
        bottomNavigation = findViewById(R.id.bottomNavigationView);
        placeholder = findViewById(R.id.placeholder);
    }

    @Override
    public void onLogout() {
        VK.logout();
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
        finish();
    }
}
