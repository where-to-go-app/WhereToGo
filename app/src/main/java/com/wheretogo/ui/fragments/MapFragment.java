package com.wheretogo.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.wheretogo.R;
import com.wheretogo.data.BuildVars;
import com.wheretogo.models.MapMark;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.*;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class MapFragment extends Fragment{

    private MapView mapView;
    private ImageButton imageButton;
    private final int PERMISSION_REQUEST_CODE = 123;
    private HashMap<MapMark, PlacemarkMapObject> places;
    private ArrayList<MapMark> pointsToAddOnMap;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey(BuildVars.YA_MAP_API_KEY);
        if (getActivity() != null){
        MapKitFactory.initialize(getActivity());
        }
        pointsToAddOnMap = new ArrayList<>(16);
        places = new HashMap<>();



    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = root.findViewById(R.id.mapView);
        imageButton = root.findViewById(R.id.geolocation_button);
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        System.out.println(Arrays.toString(grantResults));
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length==1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMapWithGeoLocation(); // задаем координаты юзера
            }else{
                // initializeMapWithoutGeoLocation();
                mapView.getMap().move(
                        new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 0),
                        null);
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initializeMapWithGeoLocation(){
        MapKit mapKit = MapKitFactory.getInstance();

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationUpdated(@NonNull Location location) {
                Log.d("TagCheck", "LocationUpdated " + location.getPosition().getLongitude());
                Log.d("TagCheck", "LocationUpdated " + location.getPosition().getLatitude());
                mapView.getMap().move(
                        new CameraPosition(location.getPosition(), 14.0f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 1),
                        null);
                PlacemarkMapObject placemarkMapObject = mapView.getMap().getMapObjects().addPlacemark(location.getPosition());
                placemarkMapObject.setUserData(new MapMark(location.getPosition(), MapMark.USER_LOCATION));
            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

            }
        };
        mapKit.createLocationManager().requestSingleUpdate(locationListener);

        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraPositionChanged(@NonNull Map map, @NonNull CameraPosition cameraPosition, @NonNull CameraUpdateSource cameraUpdateSource, boolean b) {
                if (b){
                    final ImageProvider placeMarkImg = ImageProvider.fromResource(getContext(), R.drawable.place_point);
                    VisibleRegion mapVisibleRegion = map.getVisibleRegion();

                    // TODO сделать запрос к серверу и получить список мест
                    for (MapMark place :places.keySet()){
                        map.getMapObjects().remove(places.get(place)); // удаляем прошлые метки
                        places.remove(place);
                    }
                    // добавляем новые метки
                    MapMark mark = new MapMark(cameraPosition.getTarget(),  MapMark.PLACES_TO_SHOW); // создаем объект, который мы привязываем к точке на карте
                    PlacemarkMapObject placeMark = map.getMapObjects().addPlacemark(
                            cameraPosition.getTarget(),
                            placeMarkImg);
                    placeMark.setUserData(mark.getType());
                    places.put(mark, placeMark);
                    pointsToAddOnMap.add(mark);

                }

            }
        };
        mapView.getMap().addCameraListener(cameraListener);
        // TODO сделать отображение меток мест, получаемых с сервера

    }

    private void findGeoPosition(){
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            initializeMapWithGeoLocation();

        } else{
            // Permission to access the location is missing. Show rationale and request permission
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
        findGeoPosition();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findGeoPosition();
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }
}
