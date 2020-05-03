package com.wheretogo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wheretogo.R;
import com.wheretogo.models.SimplePlace;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.Collections;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {
    private View.OnClickListener onItemClickCallback;
    private List<SimplePlace> simplePlaces = Collections.emptyList();

    public PlacesAdapter(View.OnClickListener onItemClickCallback, List<SimplePlace> simplePlaces) {
        this.onItemClickCallback = onItemClickCallback;
        this.simplePlaces = simplePlaces;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.placeName.setText(simplePlaces.get(position).getPlaceName());
        holder.setSimplePlace(simplePlaces.get(position));

        //holder.circleImageView.setImageBitmap(); TODO
    }

    @Override
    public int getItemCount() {
        System.out.println(simplePlaces);
        return simplePlaces.size();
    }


    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView circleImageView;
        private final TextView placeName;
        private final TextView placeId;
        private SimplePlace simplePlace;

        PlaceViewHolder(View view) {
            super(view);
            view.setOnClickListener(onItemClickCallback);
            circleImageView = view.findViewById(R.id.item_place_photo);
            placeName = view.findViewById(R.id.item_place_name);
            placeId = view.findViewById(R.id.item_place_id);
        }

        public SimplePlace getSimplePlace() {
            return simplePlace;
        }

        public void setSimplePlace(SimplePlace simplePlace) {
            this.simplePlace = simplePlace;
            placeId.setText(String.valueOf(simplePlace.getId()));
        }
    }
}
