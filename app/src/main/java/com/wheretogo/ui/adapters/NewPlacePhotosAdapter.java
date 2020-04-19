package com.wheretogo.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.net.sip.SipSession;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.common.images.ImageManager;
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
        if (photos.get(i).isMain()){
            placeViewHolder.isMainImg.setVisibility(View.VISIBLE);
        }
        else{
            placeViewHolder.isMainImg.setVisibility(View.INVISIBLE);
        }
        placeViewHolder.mainImg.setImageBitmap(photos.get(i).getBitmap());
    }


    @Override
    public int getItemCount() {
        return photos.size();
    }

    class PlaceViewHolder extends RecyclerView.ViewHolder {
        final ImageView mainImg;
        final ImageView deletePhoto;
        final ImageView isMainImg;
        int index;

        PlaceViewHolder(View view) {
            super(view);
            mainImg = view.findViewById(R.id.imagePhoto);
            isMainImg = view.findViewById(R.id.is_main_photo);
            deletePhoto = view.findViewById(R.id.delete_photo);
            deletePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    photos.remove(index);
                    notifyItemRemoved(index);
                    notifyDataSetChanged();

                }
            });
            mainImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (photos.get(index).isMain()){
                        photos.get(index).setMain(false);
                    }else{
                        for (NewPlacePhoto ph:
                             photos) {
                            ph.setMain(false);

                        }
                        photos.get(index).setMain(true);
                    }
                    notifyDataSetChanged();
                }
            });

        }
    }
}
