package com.wheretogo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wheretogo.R;
import com.wheretogo.models.Place;

import java.util.Collections;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {
    private View.OnClickListener onItemClickCallback;
    private List<Place> places = Collections.emptyList();

    public PlacesAdapter(View.OnClickListener onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        // TODO
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public void refreshData(List<Place> places) {
        this.places = places;
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        PlaceViewHolder(View view) {
            super(view);
            view.setOnClickListener(onItemClickCallback);
        }
    }
}
