package com.wheretogo.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.wheretogo.R;

import java.io.File;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
        Log.d("supercool", String.valueOf(Environment.isExternalStorageRemovable()));

        File file = Environment.getExternalStorageDirectory();
        for (File f : Objects.requireNonNull(file.listFiles())) {
            if (f.isDirectory()) {
                Log.d("supercool", f.getName());
            }
        }
    }
}
