package com.wheretogo.ui.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.wheretogo.R;
import com.wheretogo.data.local.PreferenceManager;
import com.wheretogo.data.remote.DefaultCallback;
import com.wheretogo.data.remote.RemoteActions;
import com.wheretogo.data.remote.RemoteClient;
import com.wheretogo.data.remote.geocoder.GeocoderRemoteActions;
import com.wheretogo.data.remote.geocoder.GeocoderRemoteClient;
import com.wheretogo.models.Place;
import com.wheretogo.models.User;
import com.wheretogo.models.geocoderModel.Country;
import com.wheretogo.models.geocoderModel.GeocodeModel;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateSource;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.mapview.MapView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class CreateFragment extends Fragment implements View.OnClickListener {
    private MapView createMapView;
    private ViewGroup panelRoot;
    private TextView panelTitle;
    private ImageView mapPin;


    private EditText placeNameEdit;
    private EditText placeDescriptionEdit;
    private ImageView placeImage;
    private Button submitButton;

    private RemoteActions remoteActions;

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_STORAGE = 2;
    private static final int PERMISSION_CAMERA = 3;
    private String country = "";
    private String address = "";

    CameraListener cameraListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        remoteActions = new RemoteActions(new RemoteClient());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        findViews(root);
        inflater.inflate(R.layout.layout_create, panelRoot, true);
        placeNameEdit = root.findViewById(R.id.create_name_edit);
        placeDescriptionEdit = root.findViewById(R.id.create_desc_edit);
        placeImage = root.findViewById(R.id.create_image);
        placeImage.setOnClickListener(this);
        submitButton = root.findViewById(R.id.create_apply_button);
        submitButton.setOnClickListener(this);
        cameraListener = new CameraListener() {
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
                            address = addressDetails.getAddressLine();
                            if (addressDetails.getCountryName() != null) {
                                country = addressDetails.getCountryName();
                            }
                            if (addressDetails.getAdministrativeArea() != null && addressDetails.getAdministrativeArea().getLocality() != null) {
                                String province = addressDetails
                                        .getAdministrativeArea()
                                        .getLocality()
                                        .getLocalityName();
                            }

                            panelTitle.setText(address);
                        }

                        @Override
                        public void onFailure(Call<GeocodeModel> call, Throwable throwable) {
                            panelTitle.setText("Не получилось отправить запрос. Проверьте подключение к интернету");
                        }
                    });
                }
            }
        };
        createMapView.getMap().addCameraListener(cameraListener);
        return root;
    }

    private void findViews(View root) {
        createMapView = root.findViewById(R.id.mapView);
        panelRoot = root.findViewById(R.id.panelRoot);
        mapPin = root.findViewById(R.id.mapPin);
        panelTitle = root.findViewById(R.id.panelTitle);
        panelTitle.setText("Адрес:"); // TODO
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA) {
            for (int i = 0; i < permissions.length; i++) {
                String p = permissions[i];
                if (p.equals(Manifest.permission.CAMERA) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                    break;
                }
            }
        }
    }

    private void showDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.layout_create_dialog);
        ViewGroup dialogCamera = dialog.findViewById(R.id.dialog_camera);
        dialogCamera.setOnClickListener(v -> {
            openCamera();
            dialog.dismiss();
        });
        ViewGroup dialogStorage = dialog.findViewById(R.id.dialog_storage);
        dialogStorage.setOnClickListener(v -> {
            openFilePicker();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.CAMERA}, PERMISSION_CAMERA);
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(getContext(), "Error has occured", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_STORAGE);
    }

    private void createPlace() {
        String placeName = placeNameEdit.getText().toString().trim();
        if (placeName.isEmpty()) {
            Toast.makeText(getContext(), "Empty name edit", Toast.LENGTH_SHORT).show();
            return;
        }
        String placeDesc = placeDescriptionEdit.getText().toString().trim();
        if (placeDesc.isEmpty()) {
            Toast.makeText(getContext(), "Desc is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Drawable drawable = placeImage.getDrawable();
        if (!(drawable instanceof BitmapDrawable)) {
            Toast.makeText(getContext(), "Необходимо установить фото", Toast.LENGTH_SHORT).show();
            return;
        }
        Point pt= createMapView.getMap().getCameraPosition().getTarget();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] photo = stream.toByteArray();
//        User user = new PreferenceManager(getContext()).getUser();
//        if (user == null){
        User user = new User("debug",  "debug", 1, "12345");
//        }
        Place place = new Place( placeName, placeDesc, new Float(pt.getLatitude()), new Float(pt.getLongitude()), country, address);
        remoteActions.createPlace(photo, place, user, new DefaultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                if (data) {
                    // Место успешно сохранено
                    if (getContext() != null)
                        Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                } else {
                    // Сервер вернул ошибку. Например CODE_AUTH_ERROR
                    if (getContext() != null)
                        Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int error) {
                // Произошла ошибка при попытке обратиться к серверу
                Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if ((requestCode == REQUEST_CAMERA || requestCode == REQUEST_STORAGE) && data.getData() != null) {
                placeImage.setImageURI(data.getData());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_image: {
                showDialog();
                break;
            }
            case R.id.create_apply_button: {
                createPlace();
                break;
            }
        }
    }
}
