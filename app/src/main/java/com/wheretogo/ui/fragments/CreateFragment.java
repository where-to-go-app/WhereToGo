package com.wheretogo.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wheretogo.R;
import com.wheretogo.data.BuildVars;
import com.wheretogo.models.NewPlacePhoto;
import com.wheretogo.ui.adapters.NewPlacePhotosAdapter;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.mapview.MapView;

import java.util.ArrayList;

public class CreateFragment extends Fragment {
    private EditText placeName;
    private EditText placeDescription;
    private MapView createMapView;
    private Button addPhotoButton;
    private Button submitButton;

    private RecyclerView photosList;
    private NewPlacePhotosAdapter photosAdapter;

    ArrayList<NewPlacePhoto> arr;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create, container, false);
        findViews(root);
        createBinds();
        return root;
    }

    private void findViews(View root) {
        createMapView = root.findViewById(R.id.createMapView);
        placeName = root.findViewById(R.id.newPlaceName);
        placeDescription = root.findViewById(R.id.newPlaceDescription);
        addPhotoButton = root.findViewById(R.id.addPhoto_button);
        submitButton = root.findViewById(R.id.createPlace_submit);
        photosList = root.findViewById(R.id.newPlacePhotos);
        arr = new ArrayList<NewPlacePhoto>();
        arr.add(new NewPlacePhoto("fddfdgfdgfd"));
        arr.add(new NewPlacePhoto("fddfdgfdgfd"));
        photosAdapter = new NewPlacePhotosAdapter(arr);
        photosList.setAdapter(photosAdapter);
        photosList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    private void createBinds(){
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arr.add(new NewPlacePhoto("dffd"));
                photosAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        createMapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        createMapView.onStop();
        MapKitFactory.getInstance().onStop();
    }
}
