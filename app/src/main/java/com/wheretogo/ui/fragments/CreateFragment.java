package com.wheretogo.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wheretogo.R;
import com.wheretogo.data.BuildVars;
import com.wheretogo.models.NewPlacePhoto;
import com.wheretogo.ui.adapters.NewPlacePhotosAdapter;
import com.wheretogo.ui.custom.CustomMapView;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Geo;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateSource;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.mapview.MapView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class CreateFragment extends Fragment {
    private EditText placeName;
    private EditText placeDescription;
    private CustomMapView createMapView;
    private Button addPhotoButton;
    private Button submitButton;

    private RecyclerView photosList;
    private NewPlacePhotosAdapter photosAdapter;

    ArrayList<NewPlacePhoto> arr;


    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;

    private static final int READ_EXTERNAL_STORAGES_PERMISSION_REQUEST_CODE = 10;


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
        photosAdapter = new NewPlacePhotosAdapter(arr);
        photosList.setAdapter(photosAdapter);
        photosList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    private void createBinds(){
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPhotoPermission();
                photosAdapter.notifyDataSetChanged();
            }

        });
        // TODO при каждом изменении карты получать адрес центра карты. (нужен геокодер и ретрофит)

    }

    private void askPhotoPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            loadGallery();
        } else{
            // Permission to access the location is missing. Show rationale and request permission
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGES_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGES_PERMISSION_REQUEST_CODE) {
            if (grantResults.length==1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadGallery();
            }
        }
    }
    public void loadGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CAMERA:

                break;
            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    final Uri imageUri = data.getData();
                    try {
                        arr.add(new NewPlacePhoto(decodeUri(imageUri)));
                        photosAdapter.notifyDataSetChanged();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                getActivity().getContentResolver().openInputStream(selectedImage), null, op);

        final int REQUIRED_SIZE = 1000;

        int width_tmp = op.outWidth, height_tmp = op.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options op2 = new BitmapFactory.Options();
        op2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                getActivity().getContentResolver().openInputStream(selectedImage), null, op2);
    }
}
