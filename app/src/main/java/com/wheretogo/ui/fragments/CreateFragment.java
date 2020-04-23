package com.wheretogo.ui.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wheretogo.R;
import com.wheretogo.data.remote.DefaultCallback;
import com.wheretogo.data.remote.RemoteActions;
import com.wheretogo.data.remote.RemoteClient;
import com.wheretogo.data.remote.geocoder.GeocoderRemoteActions;
import com.wheretogo.data.remote.geocoder.GeocoderRemoteClient;
import com.wheretogo.models.NewPlacePhoto;
import com.wheretogo.models.Place;
import com.wheretogo.models.User;
import com.wheretogo.models.geocoderModel.AddressDetails;
import com.wheretogo.models.geocoderModel.Country;
import com.wheretogo.models.geocoderModel.GeocodeModel;
import com.wheretogo.ui.adapters.NewPlacePhotosAdapter;
import com.wheretogo.ui.custom.CustomMapView;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateSource;
import com.yandex.mapkit.map.Map;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class CreateFragment extends Fragment {
    private EditText placeName;
    private EditText placeDescription;
    private CustomMapView createMapView;
    private Button addPhotoButton;
    private Button submitButton;
    private TextView errorText;
    private TextView addressText;

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
        addressText = root.findViewById(R.id.createPlace_address);
        createMapView = root.findViewById(R.id.createMapView);
        placeName = root.findViewById(R.id.newPlaceName);
        placeDescription = root.findViewById(R.id.newPlaceDescription);
        addPhotoButton = root.findViewById(R.id.addPhoto_button);
        errorText = root.findViewById(R.id.error_text);
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
        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraPositionChanged(@NonNull Map map, @NonNull CameraPosition cameraPosition, @NonNull CameraUpdateSource cameraUpdateSource, boolean b) {
                if (b) {
                    Point point = map.getCameraPosition().getTarget();
                    String geocode = point.getLongitude() + ";" + point.getLatitude();
                    System.out.println(geocode);
                    GeocoderRemoteActions geocoderRemoteActions = new GeocoderRemoteActions(new GeocoderRemoteClient());
                    geocoderRemoteActions.getGeocoding(geocode, new Callback<GeocodeModel>() {
                        @Override
                        public void onResponse(Call<GeocodeModel> call, Response<GeocodeModel> response) {

                            Country addressDetails = response.body().getResponse().getGeoObjectCollection()
                                    .getFeatureMember()
                                    .get(0)
                                    .getGeoObject()
                                    .getMetaDataProperty()
                                    .getGeocoderMetaData()
                                    .getAddressDetails()
                                    .getCountry();
                            String address = addressDetails.getAddressLine();
                            if (addressDetails.getCountryName() != null) {
                                String country = addressDetails.getCountryName();
                            }
                            if (addressDetails.getAdministrativeArea() != null && addressDetails.getAdministrativeArea().getLocality() != null) {
                                String province = addressDetails
                                        .getAdministrativeArea()
                                        .getLocality()
                                        .getLocalityName();
                            }
                            addressText.setText("Адрес: "+address);
                    }

                        @Override
                        public void onFailure(Call<GeocodeModel> call, Throwable throwable) {
                            errorText.setText("Не получилось отправить запрос. Проверьте подключение к интернету");
                        }
                    });
                }
            }
        };

        createMapView.getMap().addCameraListener(cameraListener);
        // TODO при каждом изменении карты получать адрес центра карты. (нужен геокодер и ретрофит)
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (placeName.getText().length() == 0) {

                    errorText.setText("Название места не должно быть пустым");
                    errorText.setVisibility(View.VISIBLE);
                    return;
                }
                if (placeName.getText().length() > 255) {
                    errorText.setText("Название места не должно превышать 255 символов");
                    errorText.setVisibility(View.VISIBLE);
                    return;
                }
                if (placeDescription.getText().length() == 0) {
                    errorText.setText("Описание места не должно быть пустым");
                    errorText.setVisibility(View.VISIBLE);
                    return;
                }
                if (placeDescription.getText().length() > 1023) {
                    errorText.setText("Название места не должно превышать 1023 символов");
                    errorText.setVisibility(View.VISIBLE);
                    return;
                }
                if (placePhotos.size() == 0){
                    errorText.setText("Добавьте хотя бы одну фотографию");
                    errorText.setVisibility(View.VISIBLE);
                    return;
                }
                boolean isThereMainPhoto = false;
                for (NewPlacePhoto ph: placePhotos
                     ) {
                    if (ph.isMain()){
                        isThereMainPhoto = true;
                        break;
                    }
                }
                if (!isThereMainPhoto){
                    errorText.setText("Сделайте хотя бы одну фотографию главной");
                    errorText.setVisibility(View.VISIBLE);
                    return;
                }
                errorText.setVisibility(View.GONE);
                // TODO сделать вызов create_place
//                RemoteActions remoteActions = new RemoteActions(new RemoteClient());
//                HashMap<String, RequestBody> filesMap = new HashMap<String, RequestBody>();
//                remoteActions.createPlace(
//                        new Place(placeName.getText().toString(),
//                                placeDescription.getText().toString(),
//                                new Float(60.554),
//                                new Float(32.554),
//                                "Russia",
//                                "Nevskiy pr 24"),
//                        new User("vasya", "pupkin", 1, "12345"),
//                        filesMap,
//                        );
//
            }
        });
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
                        placePhotos.add(new NewPlacePhoto(decodeUriAndResize(imageUri)));
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

    private Bitmap decodeUriAndResize(Uri selectedImage) throws FileNotFoundException {
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
