package com.wheretogo.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.wheretogo.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);



        ViewPager viewPager = findViewById(R.id.vpMain);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        //viewPager.setAdapter(vpAdapter);
        viewPager.setAdapter(imageAdapter);






        Button buttonBack = findViewById(R.id.buttonToMain);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch (Exception e){

                }
            }
        });
    }
}
