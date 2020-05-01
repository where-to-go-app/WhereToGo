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
import com.vk.api.sdk.utils.VKUtils;
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
            switch (item.getItemId()) {

                case R.id.nav_places:
                case R.id.nav_search:
                case R.id.nav_favorite: {
                    replaceFragment(new MapFragment());
                    break;
                }
                case R.id.nav_create: {
                    replaceFragment(new CreateFragment());
                    break;
                }
                case R.id.nav_settings: {
                    replaceFragment(new SettingsFragment());
                    break;
                }
                default: return false;
            }
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

    private void replaceFragment(Fragment fragment) {
        if (fragment instanceof MapFragment && getSupportFragmentManager().findFragmentById(R.id.placeholder) instanceof  MapFragment) {
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.placeholder, fragment)
                .commit();
    }

    @Override
    public void onLogout() {
        VK.logout();
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
        finish();
    }
}
