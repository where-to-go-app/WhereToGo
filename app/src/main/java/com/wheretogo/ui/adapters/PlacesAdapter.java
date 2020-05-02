package com.wheretogo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wheretogo.R;
import com.wheretogo.models.Place;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.Collections;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {
    private View.OnClickListener onItemClickCallback;
    private List<Place> places = Collections.emptyList();

    public PlacesAdapter(View.OnClickListener onItemClickCallback, List<Place> places) {
        this.onItemClickCallback = onItemClickCallback;
        this.places = places;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.placeName.setText(places.get(position).getPlaceName());
        holder.setPlace(places.get(position));

        //holder.circleImageView.setImageBitmap(); TODO
    }

    @Override
    public int getItemCount() {
        System.out.println(places);
        return places.size();
    }


    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView circleImageView;
        private final TextView placeName;
        private final TextView placeId;
        private Place place;

        PlaceViewHolder(View view) {
            super(view);
            view.setOnClickListener(onItemClickCallback);
            circleImageView = view.findViewById(R.id.item_place_photo);
            placeName = view.findViewById(R.id.item_place_name);
            placeId = view.findViewById(R.id.item_place_id);
        }

        public Place getPlace() {
            return place;
        }

        public void setPlace(Place place) {
            this.place = place;
            placeId.setText(String.valueOf(place.getId()));
        }
    }
}
