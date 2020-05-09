package com.wheretogo.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.Scene;
import androidx.transition.TransitionManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.wheretogo.R;
import com.wheretogo.data.BuildVars;
import com.wheretogo.data.local.PreferenceManager;
import com.wheretogo.data.remote.DefaultCallback;
import com.wheretogo.data.remote.RemoteActions;
import com.wheretogo.data.remote.RemoteClient;
import com.wheretogo.models.MapMark;
import com.wheretogo.models.Place;
import com.wheretogo.models.User;
import com.wheretogo.ui.adapters.PlacesAdapter;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.VisibleRegion;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class MapFragment extends Fragment{

    private MapView mapView;
    private ImageView mapPin;
    private ImageButton imageButton;
    private LinearLayout panelRoot;
    private TextView panelTitle;
    private FrameLayout panelPlaceholder;
    private BottomSheetBehavior panelBehavior;
    private Mode currentMode = Mode.AROUND_PLACES;
    private static final int LAYOUT_LIST = R.layout.layout_map_list;
    private static final int LAYOUT_ITEM = R.layout.layout_map_item;
    public static final String MODE_EXTRA = "mode_extra";

    // layout list
    private RecyclerView placesList;
    private PlacesAdapter adapter;
    private int placesListState;

    // layout item
    private ImageView photo;
    private TextView name;

    private PreferenceManager preferenceManager;

    private final int PERMISSION_REQUEST_CODE = 123;
    private HashMap<MapMark, PlacemarkMapObject> places;
    private ArrayList<MapMark> pointsToAddOnMap;
    private ImageProvider placeMarkImg;

    private CameraListener cameraListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey(BuildVars.YA_MAP_API_KEY);
        if (getActivity() != null) {
            MapKitFactory.initialize(getActivity());
        }

        preferenceManager = new PreferenceManager(getContext());
        pointsToAddOnMap = new ArrayList<>(16);
        places = new HashMap<>();
        placeMarkImg = ImageProvider.fromResource(getContext(), R.drawable.map_pin);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        findViews(root);
        mapPin.setVisibility(View.GONE);
        panelBehavior = BottomSheetBehavior.from(panelRoot);
        panelBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        cameraListener  = (map, cameraPosition, cameraUpdateSource, b) -> {
            if (b) {
                VisibleRegion mapVisibleRegion = map.getVisibleRegion();
                Point topLeft = mapVisibleRegion.getTopLeft();
                Point bottomRight = mapVisibleRegion.getBottomRight();
                User user = new PreferenceManager(getContext()).getUser();
                RemoteActions remoteActions = new RemoteActions(new RemoteClient());
                Log.d("some_key", topLeft.getLongitude()+" "+topLeft.getLatitude()+" "+
                        bottomRight.getLongitude()+ " " +bottomRight.getLatitude());
                remoteActions.getPlacesAround(user,
                        new RectF((float) topLeft.getLongitude(), (float) topLeft.getLatitude(),
                                (float) bottomRight.getLongitude(), (float) bottomRight.getLatitude()),
                        new DefaultCallback<List<Place>>() {
                            @Override
                            public void onSuccess(List<Place> data) {
                                if (getActivity() != null){
                                    showPlaces(map, data);
                                }
                            }

                            @Override
                            public void onError(int error) {

                            }
                        });

                // переводим лист мест в обычное состояние
                if (placesListState == LAYOUT_ITEM){
                    inflatePanelLayout(LAYOUT_LIST, "Места рядом");
                }

            }
        };
        mapView.getMap().addCameraListener(cameraListener);

        if (getArguments() != null) {
            currentMode = (Mode) getArguments().getSerializable(MODE_EXTRA);
            openTab();
        }
        return root;
    }

    private void findViews(View root) {
        mapView = root.findViewById(R.id.mapView);
        mapPin = root.findViewById(R.id.mapPin);
        imageButton = root.findViewById(R.id.geolocation_button);
        panelRoot = root.findViewById(R.id.panelRoot);
        panelTitle = root.findViewById(R.id.panelTitle);
        panelPlaceholder = root.findViewById(R.id.panelPlaceholder);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d("tag", Arrays.toString(grantResults));
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length==1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMapWithGeoLocation(); // задаем координаты юзера
            } else {
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
                //placemarkMapObject.setUserData(new MapMark(location.getPosition(), MapMark.USER_LOCATION));
            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {

            }
        };
        mapKit.createLocationManager().requestSingleUpdate(locationListener);


    }

    private void findGeoPosition() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            initializeMapWithGeoLocation();

        } else {
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
        imageButton.setOnClickListener(v -> findGeoPosition());
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    private void inflatePanelLayout(int res, String title) {
        panelTitle.setText(title);
        Scene scene = Scene.getSceneForLayout(panelPlaceholder, res, getContext());
        TransitionManager.go(scene, new Fade());
        if (res == LAYOUT_LIST) {
            placesList = panelPlaceholder.findViewById(R.id.mapListPlaces);
            adapter = new PlacesAdapter((view) -> inflatePanelLayout(LAYOUT_ITEM, "Place Name"));
            placesList.setAdapter(adapter);
            placesList.setNestedScrollingEnabled(false);
            placesList.setLayoutManager(new LinearLayoutManager(getContext()));
            placesListState = LAYOUT_LIST;
        } else if (res == LAYOUT_ITEM) {
            photo = panelPlaceholder.findViewById(R.id.mapItemPhoto);
            name = panelPlaceholder.findViewById(R.id.mapItemName);
            placesListState = LAYOUT_ITEM;
        }
    }

    private void showPlaces(Map map, List<Place> data){
        pointsToAddOnMap.clear();
        for (Place place:data
        ) {
            Point pt = new Point(place.getLatitude(), place.getLongitude());
            MapMark mark = new MapMark(pt,
                    place.getId(),
                    MapMark.PLACES_TO_SHOW,
                    place.getPlaceName()); // создаем объект, который мы привязываем к точке на карте
            pointsToAddOnMap.add(mark);
        }
        for (MapMark place :places.keySet()){
            map.getMapObjects().remove(places.get(place)); // удаляем прошлые метки
        }
        places.clear();
        for (MapMark mark: pointsToAddOnMap) {
            PlacemarkMapObject placeMark = map.getMapObjects().addPlacemark(
                    mark.getPt(),
                    placeMarkImg);
            MapObjectTapListener onPointTabListener = (mapObject, point) -> {
                inflatePanelLayout(LAYOUT_ITEM, ((MapMark) mapObject.getUserData()).getPlace_name());
                return  true;
            };
            placeMark.addTapListener(onPointTabListener);
            placeMark.setUserData(mark);
            places.put(mark, placeMark);
        }
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    public void setNewMode(Mode mode) {
        currentMode = mode;
        openTab();
    }

    private void openTab() {
        inflatePanelLayout(LAYOUT_LIST, currentMode.title);
        switch (currentMode) {
            case AROUND_PLACES:
                break;
            case LOVE_PLACES:
                break;
            case SEARCH_PLACES:
                break;
        }
    }

    public enum Mode {
        AROUND_PLACES("Места рядом"),
        LOVE_PLACES("Любимые места"),
        SEARCH_PLACES("Поиск места");

        String title;

        Mode(String title) {
            this.title = title;
        }
    }
}
