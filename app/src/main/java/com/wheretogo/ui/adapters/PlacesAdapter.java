package com.wheretogo.ui.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wheretogo.utils.DisplayUtils;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {


    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrameLayout root = new FrameLayout(parent.getContext());
        TextView textView = new TextView(parent.getContext());
        textView.setText("Placeholder");
        textView.setTextSize(DisplayUtils.spToPx(parent.getContext(), 14));
        root.addView(textView);
        return new PlacesViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 30;
    }

    static class PlacesViewHolder extends RecyclerView.ViewHolder {
        PlacesViewHolder(View view) {
            super(view);
        }
    }
}
