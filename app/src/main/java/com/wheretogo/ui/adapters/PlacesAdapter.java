package com.wheretogo.ui.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wheretogo.R;
import com.wheretogo.models.SimplePlace;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {
    private OnPlaceClickListener onItemClickCallback;
    private List<SimplePlace> simplePlaces = Collections.emptyList();

    public PlacesAdapter(OnPlaceClickListener onItemClickCallback) {
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
        SimplePlace simplePlace = simplePlaces.get(position);
        holder.placeRoot.setOnClickListener((view) -> {
            onItemClickCallback.onPlaceClick(simplePlace.getId(), simplePlace.getPlaceName());
        });
        holder.placeName.setText(simplePlace.getPlaceName());
        Picasso.get()
                .load(simplePlace.getAvatar_url())
                .placeholder(new ColorDrawable(Color.GREEN))
                .into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return simplePlaces.size();
    }

    public void updatePlaces(List<SimplePlace> simplePlaces) {
        this.simplePlaces = simplePlaces;
        notifyDataSetChanged();
    }

    public interface OnPlaceClickListener {
        void onPlaceClick(int id, String name);
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView circleImageView;
        private final TextView placeName;
        private final ViewGroup placeRoot;

        PlaceViewHolder(View view) {
            super(view);
            circleImageView = view.findViewById(R.id.item_place_photo);
            placeName = view.findViewById(R.id.item_place_name);
            placeRoot = view.findViewById(R.id.item_place_root);

        }
    }
}
