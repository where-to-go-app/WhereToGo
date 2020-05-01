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
import com.wheretogo.models.Place;
import com.wheretogo.models.User;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.mapview.MapView;

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
        return root;
    }

    private void findViews(View root) {
        createMapView = root.findViewById(R.id.mapView);
        panelRoot = root.findViewById(R.id.panelRoot);
        mapPin = root.findViewById(R.id.mapPin);
        panelTitle = root.findViewById(R.id.panelTitle);
        panelTitle.setText("55.899622, 37.210925"); // TODO
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
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] photo = stream.toByteArray();
        User user = new PreferenceManager(getContext()).getUser();
        Place place = new Place(1, placeName, placeDesc, 1.65f, 2.43f, "Canada", "Groove Streat, 1");
        remoteActions.createPlace(photo, place, user, new DefaultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                if (data) {
                    // Место успешно сохранено
                    Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                } else {
                    // Сервер вернул ошибку. Например CODE_AUTH_ERROR
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
