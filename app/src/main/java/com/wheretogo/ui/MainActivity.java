package com.wheretogo.ui;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.wheretogo.R;
import com.wheretogo.ui.adapters.PlacesAdapter;
import com.wheretogo.ui.fragments.CreateFragment;
import com.wheretogo.ui.fragments.MapFragment;
import com.wheretogo.ui.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private FrameLayout placeholder;
    private View panelRoot;
    private TextView panelTitle;
    private RecyclerView panelList;
    private PlacesAdapter panelAdapter;
    private BottomSheetBehavior panelBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDarkTransparent));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        findViews();

        panelAdapter = new PlacesAdapter();
        panelList.setAdapter(panelAdapter);
        panelList.setLayoutManager(new LinearLayoutManager(this));


        panelBehavior = BottomSheetBehavior.from(panelRoot);
        panelBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        bottomNavigation.setOnNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {

                case R.id.nav_places: {
                    replaceFragment(new MapFragment());
                    panelBehavior.setHideable(false);
                    panelBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    panelTitle.setText(getString(R.string.nav_places));
                    break;
                }
                case R.id.nav_search: {
                    replaceFragment(new MapFragment());
                    panelBehavior.setHideable(false);
                    panelBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    panelTitle.setText(getString(R.string.nav_search));
                    break;
                }
                case R.id.nav_favorite: {
                    replaceFragment(new MapFragment());
                    panelBehavior.setHideable(false);
                    panelBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    panelTitle.setText(getString(R.string.nav_favorite));
                    break;
                }
                case R.id.nav_create: {
                    replaceFragment(new CreateFragment());
                    panelBehavior.setHideable(true);
                    panelBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    break;
                }
                case R.id.nav_settings: {
                    replaceFragment(new SettingsFragment());
                    panelBehavior.setHideable(true);
                    panelBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
        panelRoot = findViewById(R.id.panelRoot);
        panelList = findViewById(R.id.panelList);
        panelTitle = findViewById(R.id.panelTitle);
    }


    private void replaceFragment(Fragment fragment) {
        //
        if (fragment instanceof MapFragment && getSupportFragmentManager().findFragmentById(R.id.placeholder) instanceof  MapFragment) {
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.placeholder, fragment)
                .commit();
    }

}
