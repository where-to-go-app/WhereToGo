package com.wheretogo.ui.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;

import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wheretogo.R;
import com.wheretogo.models.NewPlacePhoto;
import com.wheretogo.ui.adapters.NewPlacePhotosAdapter;
import com.wheretogo.ui.custom.CustomMapView;
import com.yandex.mapkit.MapKitFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import static android.app.Activity.RESULT_OK;

public class CreateFragment extends Fragment {
    private EditText placeName;
    private EditText placeDescription;
    private CustomMapView createMapView;
    private Button addPhotoButton;
    private Button submitButton;

    private RecyclerView photosList;
    private NewPlacePhotosAdapter photosAdapter;

    private ArrayList<NewPlacePhoto> placePhotos;


    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;

    private static final int READ_EXTERNAL_STORAGES_AND_CAMERA_PERMISSION_REQUEST_CODE = 10;
    private static final int READ_EXTERNAL_STORAGES_PERMISSION_REQUEST_CODE = 11;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 12;

    private static final int YES_GALLERY = 101;
    private static final int YES_GALLERY_AND_CAMERA = 102;
    private static final int YES_CAMERA = 103;
    private static final int NO_GALLERY_AND_CAMERA = 100;

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
        placePhotos = new ArrayList<NewPlacePhoto>();
        photosAdapter = new NewPlacePhotosAdapter(placePhotos);
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
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startDialog(YES_GALLERY_AND_CAMERA);
        } else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
            READ_EXTERNAL_STORAGES_PERMISSION_REQUEST_CODE);
        }
        else{
            System.out.println("4");
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    READ_EXTERNAL_STORAGES_AND_CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGES_AND_CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length==2){
                System.out.println(Arrays.toString(grantResults));
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startDialog(YES_GALLERY_AND_CAMERA);
                }
                else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDialog(YES_GALLERY);
                }
                else if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    startDialog(YES_GALLERY);
                }else{
                    startDialog(NO_GALLERY_AND_CAMERA);
                }
            }
        }else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startDialog(YES_GALLERY_AND_CAMERA);
            }else{
                startDialog(YES_GALLERY);
            }
        }else if (requestCode == READ_EXTERNAL_STORAGES_PERMISSION_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startDialog(YES_GALLERY_AND_CAMERA);
            }else{
                startDialog(YES_CAMERA);
            }
        }
    }
    private void startDialog(int type) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                getActivity());
        myAlertDialog.setTitle("Загрузка фотогравфии");
        myAlertDialog.setMessage("Как вы хотите загрузить изображение?");
        if (type == YES_GALLERY || type == YES_GALLERY_AND_CAMERA) {
            myAlertDialog.setPositiveButton("Gallery",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent pictureActionIntent = null;

                            pictureActionIntent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(
                                    pictureActionIntent,
                                    REQUEST_GALLERY);

                        }
                    });
        }
        if (type == YES_CAMERA || type == YES_GALLERY_AND_CAMERA) {
        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,
                                REQUEST_CAMERA);

                    }
                });
        }
        myAlertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CAMERA:

                if (data != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    placePhotos.add(new NewPlacePhoto(photo));
                    photosAdapter.notifyDataSetChanged();
                    break;
                    }

            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    final Uri imageUri = data.getData();
                    try {
                        placePhotos.add(new NewPlacePhoto(decodeUri(imageUri)));
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
