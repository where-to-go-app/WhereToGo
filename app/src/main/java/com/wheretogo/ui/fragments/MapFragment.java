package com.wheretogo.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.PermissionRequest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentContainer;
import com.wheretogo.R;
import com.wheretogo.data.BuildVars;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapWindow;
import com.yandex.mapkit.map.SizeChangedListener;
import com.yandex.mapkit.mapview.MapView;

import java.security.Permission;


public class MapFragment extends Fragment {

    private MapView mapView;
    private final int PERMISSION_REQUEST_CODE = 100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey(BuildVars.YA_MAP_API_KEY);
        MapKitFactory.initialize(getActivity());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMapWithGeoLocation(); // задаем координаты юзера
            }else{
                mapView.getMap().move(
                        new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 0),
                        null);
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = root.findViewById(R.id.mapView);


        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            initializeMapWithGeoLocation();

        } else{
            // Permission to access the location is missing. Show rationale and request permission
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }



        return root;
    }
    private void initializeMapWithGeoLocation(){
        MapKit mapKit = MapKitFactory.getInstance();
        mapKit.createLocationManager().requestSingleUpdate(
                new LocationListener() {
                    @Override
                    public void onLocationUpdated(@NonNull Location location) {
                        Log.d("TagCheck", "LocationUpdated " + location.getPosition().getLongitude());
                        Log.d("TagCheck", "LocationUpdated " + location.getPosition().getLatitude());
                        mapView.getMap().move(
                                new CameraPosition(location.getPosition(), 14.0f, 0.0f, 0.0f),
                                new Animation(Animation.Type.SMOOTH, 1),
                                null);

                    }
                    @Override
                    public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

                    }
                }
        );
        // TODO сделать отображение метки пользователя и меток мест, получаемых с сервера
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }
}
