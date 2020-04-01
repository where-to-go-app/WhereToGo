package com.wheretogo.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wheretogo.R;

public class SettingsFragment extends Fragment {

    public interface OnLogoutListener {
        void onLogout();
    }

    private OnLogoutListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnLogoutListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        Button logoutButton = root.findViewById(R.id.setLogoutBtn);
        logoutButton.setOnClickListener((view) -> {
            if (listener != null) {
                listener.onLogout();
            }
        });

        return root;
    }
}
