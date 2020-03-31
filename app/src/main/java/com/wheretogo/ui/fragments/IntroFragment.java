package com.wheretogo.ui.fragments;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.text.NoCopySpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.wheretogo.R;
import com.wheretogo.ui.LoginActivity;

import java.lang.annotation.Documented;

public class IntroFragment extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Intent intent = new Intent(IntroFragment.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
