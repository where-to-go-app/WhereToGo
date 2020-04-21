package com.wheretogo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wheretogo.R;
import com.wheretogo.models.NewPlacePhoto;

import java.util.Collections;
import java.util.List;

public class NewPlacePhotosAdapter extends RecyclerView.Adapter<NewPlacePhotosAdapter.PlaceViewHolder>{
    private View.OnClickListener onItemClickCallback;

    private List<NewPlacePhoto> photos = Collections.emptyList();

    public NewPlacePhotosAdapter(List<NewPlacePhoto> photos) {
        this.photos = photos;
    }

    @NonNull
    @Override
    public NewPlacePhotosAdapter.PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_list_item, parent, false);

        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder placeViewHolder, int i) {

        placeViewHolder.index = i;
    }


    @Override
    public int getItemCount() {
        return photos.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        final ImageView mainImg;
        final ImageView deletePhoto;
        int index;
        PlaceViewHolder(View view) {
            super(view);
            mainImg = view.findViewById(R.id.imagePhoto);
            deletePhoto = view.findViewById(R.id.delete_photo);
            deletePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(photos.size());
                    photos.remove(index);
                    notifyItemRemoved(index);
                    notifyDataSetChanged();

                }
            });
        }
    }
}
