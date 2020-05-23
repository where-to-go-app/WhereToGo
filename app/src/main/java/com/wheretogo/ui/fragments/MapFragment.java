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
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;
import com.wheretogo.Application;
import com.wheretogo.R;
import com.wheretogo.data.BuildVars;
import com.wheretogo.data.local.PreferenceManager;
import com.wheretogo.data.remote.DefaultCallback;
import com.wheretogo.data.remote.RemoteActions;
import com.wheretogo.data.remote.RemoteClient;
import com.wheretogo.localDB.LocalPhoto;
import com.wheretogo.localDB.LocalPlace;
import com.wheretogo.models.MapMark;
import com.wheretogo.models.SimplePlace;
import com.wheretogo.models.User;
import com.wheretogo.models.onePlace.Photo;
import com.wheretogo.models.onePlace.Place;
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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MapFragment extends Fragment{

    private MapView mapView;
    private ImageView mapPin;
    private ImageButton imageButton;
    private LinearLayout panelRoot;
    private TextView panelTitle;
    private FrameLayout panelPlaceholder;
    private BottomSheetBehavior panelBehavior;
    private static final int LAYOUT_LIST = R.layout.layout_map_list;
    private static final int LAYOUT_ITEM = R.layout.layout_map_item;
    private int currentLayout = -1;
    private Mode currentMode = Mode.AROUND_PLACES;
    public static final String MODE_EXTRA = "mode_extra";

    // layout list
    private RecyclerView placesList;
    private PlacesAdapter adapter;

    // layout item
    private ImageView photo;
    private TextView name;
    private TextView desc;
    private TextView country;
    private TextView address;
    private ImageView mapItemBack;

    private PreferenceManager preferenceManager;

    private final int PERMISSION_REQUEST_CODE = 123;
    private HashMap<MapMark, PlacemarkMapObject> places;
    private ArrayList<MapMark> pointsToAddOnMap;
    private ImageProvider placeMarkImg;
    private RemoteActions remoteActions;

    private CameraListener cameraListener;

    private ExecutorService service;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.setApiKey(BuildVars.YA_MAP_API_KEY);
        if (getActivity() != null) {
            MapKitFactory.initialize(getActivity());
        }
        preferenceManager = new PreferenceManager(getContext());
        remoteActions = new RemoteActions(new RemoteClient());
        pointsToAddOnMap = new ArrayList<>(16);
        places = new HashMap<>();
        placeMarkImg = ImageProvider.fromResource(getContext(), R.drawable.map_pin);
        service = Executors.newSingleThreadExecutor();
        adapter = new PlacesAdapter((id, placeName) -> {
            inflatePanelLayout(LAYOUT_ITEM, placeName);
            setPlaceInfo(id);
        });
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
                requestPlaces();
            }
        };
        mapView.getMap().addCameraListener(cameraListener);
        mapView.getMap().setRotateGesturesEnabled(false);


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

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length==1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMapWithGeoLocation(); // задаем координаты юзера
            } else {
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
        if (currentLayout != res || currentLayout == -1) {
            Scene scene = Scene.getSceneForLayout(panelPlaceholder, res, getContext());
            TransitionManager.go(scene, new Fade());
        }
        if (res == LAYOUT_LIST) {
            panelTitle.setText(title);
            placesList = panelPlaceholder.findViewById(R.id.mapListPlaces);
            placesList.setAdapter(adapter);
            placesList.setNestedScrollingEnabled(false);
            placesList.setLayoutManager(new LinearLayoutManager(getContext()));
            currentLayout = LAYOUT_LIST;
        } else if (res == LAYOUT_ITEM){
            panelTitle.setText(title);
            photo = panelPlaceholder.findViewById(R.id.mapItemPhoto);
            name = panelPlaceholder.findViewById(R.id.mapItemName);
            desc = panelPlaceholder.findViewById(R.id.mapItemDesc);
            address = panelPlaceholder.findViewById(R.id.mapItemAddress);
            country = panelPlaceholder.findViewById(R.id.mapItemCountry);
            mapItemBack = panelPlaceholder.findViewById(R.id.mapItemBack);
            mapItemBack.setOnClickListener(v -> {
                openTab();
            });
            currentLayout = LAYOUT_ITEM;
        }
    }

    private void setPlaceInfo(int id) {

        service.execute(() -> {
            LocalPlace place = Application.databaseActions.getPlaceById(id);
            List<LocalPhoto> localPhotos = Application.databaseActions.getPhotosToPlace(id);
            List<LocalPhoto> localPhotos1 = Application.databaseActions.getAll();
            if (place != null){
                getActivity().runOnUiThread(() ->{
                    panelTitle.setText(place.getPlaceName());
                    name.setText(place.getPlaceName());
                    desc.setText(place.getPlaceDescription());
                    address.setText(place.getAddress());
                    country.setText(place.getCountry());
                    for (LocalPhoto placePhoto : localPhotos) {
                        if (placePhoto.isMain()) {
                            Picasso.get()
                                    .load(placePhoto.getPhotoUrl())
                                    .into(photo);
                        }
                    }
                });
            }
            else {
                remoteActions.getPlaceById(preferenceManager.getUser(), id,
                        new DefaultCallback<Place>() {
                            @Override
                            public void onSuccess(Place place) {
                                for (Photo placePhoto : place.getPhotos()) {
                                    if (placePhoto.isMain()) {
                                        Picasso.get()
                                                .load(placePhoto.getPhotoUrl())
                                                .into(photo);
                                    }
                                }
                                name.setText(place.getPlaceName());
                                desc.setText(place.getPlaceDesc());
                                address.setText(place.getAddress());
                                country.setText(place.getCountry());
                                service.execute(() -> {
                                    addPlaceToCache(place, false);
                                });
                                Toast.makeText(getContext(), getString(R.string.add_to_cash), Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError(int error) {
                                Toast.makeText(getContext(), getString(R.string.error_occured) + error ,Toast.LENGTH_LONG).show();
                            }
                        });
            }

        });



    }
    private void addPlaceToCache(Place place, boolean isLovePlace){
        LocalPlace localPlace = new LocalPlace();
        localPlace.setId(place.getId());
        localPlace.setLatitude(place.getLatitude());
        localPlace.setLongitude(place.getLongitude());
        localPlace.setPlaceName(place.getPlaceName());
        localPlace.setPlaceDescription(place.getPlaceDesc());
        localPlace.setCreator_id(place.getCreatorId());
        localPlace.setCountry(place.getCountry());
        localPlace.setAddress(place.getAddress());
        localPlace.setLovePlace(isLovePlace);
        Application.databaseActions.addPlace(localPlace);
        for (Photo ph: place.getPhotos()){
            LocalPhoto localPhoto = new LocalPhoto();
            localPhoto.setPhotoUrl(ph.getPhotoUrl());
            localPhoto.setMain(ph.isMain());
            localPhoto.setPlaceId(place.getId());
            Application.databaseActions.addPhoto(localPhoto);
        }

    }

    private void showPlacesOnMap(Map map, List<SimplePlace> data){
        pointsToAddOnMap.clear();
        for (SimplePlace simplePlace : data) {
            Point pt = new Point(simplePlace.getLatitude(), simplePlace.getLongitude());

            MapMark mark = new MapMark(pt,
                    simplePlace.getId(),
                    MapMark.PLACES_TO_SHOW,
                    simplePlace.getPlaceName()); // создаем объект, который мы привязываем к точке на карте
            pointsToAddOnMap.add(mark);
        }
        for (MapMark place : places.keySet()){
            map.getMapObjects().remove(places.get(place)); // удаляем прошлые метки
        }
        places.clear();
        for (MapMark mark: pointsToAddOnMap) {
            PlacemarkMapObject placeMark = map.getMapObjects().addPlacemark(
                    mark.getPt(),
                    placeMarkImg);
            MapObjectTapListener onPointTabListener = (mapObject, point) -> {
                inflatePanelLayout(LAYOUT_ITEM, mark.getPlace_name());
                setPlaceInfo(mark.getId());
                return true;
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
        inflatePanelLayout(LAYOUT_LIST, this.getString(currentMode.title));
        requestPlaces();
    }

    private void requestPlaces() {
        switch (currentMode) {
            case AROUND_PLACES:
                requestPlacesAround();
                break;
            case LOVE_PLACES:
                requestLovePlaces();
                break;
            case SEARCH_PLACES:
                requestSearchPlaces();
                break;
        }
    }

    private void requestPlacesAround() {
        VisibleRegion mapVisibleRegion = mapView.getMap().getVisibleRegion();
        Point topLeft = mapVisibleRegion.getTopLeft();
        Point bottomRight = mapVisibleRegion.getBottomRight();
        User user = preferenceManager.getUser();
        remoteActions.getPlacesAround(user,
                new RectF( (float) topLeft.getLongitude(), (float)topLeft.getLatitude(),
                        (float) bottomRight.getLongitude(), (float)bottomRight.getLatitude()),
                new DefaultCallback<List<SimplePlace>>() {
                    @Override
                    public void onSuccess(List<SimplePlace> data) {
                        if (getActivity() != null){
                            getActivity().runOnUiThread(() -> showPlacesOnMap(mapView.getMap(), data));
                            adapter.updatePlaces(data);
                        }
                    }

                    @Override
                    public void onError(int error) {
                        Toast.makeText(getContext(), getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestLovePlaces() {

    }

    private void requestSearchPlaces() {
    }

    public enum Mode {
        AROUND_PLACES( R.string.nav_places),
        LOVE_PLACES(R.string.nav_favorite),
        SEARCH_PLACES(R.string.nav_search);

        int title;

        Mode(int title) {
            this.title = title;
        }
    }
}
