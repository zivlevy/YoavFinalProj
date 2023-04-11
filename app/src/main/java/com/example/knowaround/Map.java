package com.example.knowaround;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.knowaround.databinding.ActivityMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Map extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    private ActivityMapBinding binding;
    FloatingActionButton btnFab;
    private FirebaseFirestore db;
    private ListenerRegistration registration;

    private List<models.Location> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnFab = findViewById(R.id.btAddLocation);
        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Map.this, AddLocation.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        // init map
        LatLng israel = new LatLng(31.8157, 35.046);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(israel));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(8));


        // set marker click listener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                Log.d("TAG", "onMarkerClick: " + marker.getSnippet());

                return true;
            }
        });

        // get locations from firestore
        db = FirebaseFirestore.getInstance();
        registration = db.collection("locations")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        // convert firestore locations to Location objects
                        locations = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("latitude") != null) {
                                models.Location location = new models.Location(
                                        doc.getString("id"),
                                        doc.getString("name"),
                                        doc.getString("description"),
                                        doc.getDouble("latitude"),
                                        doc.getDouble("longitude"),
                                        doc.getString("photoURL"),
                                        doc.getString("userId"),
                                        doc.getDouble("averageRating"),
                                        doc.getDouble("numOfReviews").intValue(),
                                        doc.getString("type")

                                );
                                locations.add(location);
                            }
                        }

                        // clears the map from previous markers
                        mMap.clear();

                        // choose the marker icon based on location type
                        locations.forEach(location -> {
                            LatLng latLng = new LatLng(location.latitude, location.longitude);
                            String drawableName = "misc";
                            if (location.type.equals("Food & Drink")) {
                                drawableName = "food_drink";
                            } else if (location.type.equals("Medical")) {
                                drawableName = "medical";
                            } else if (location.type.equals("Resupply")) {
                                drawableName = "resupply";
                            } else if (location.type.equals("Attractions")) {
                                drawableName = "attraction";
                            } else if (location.type.equals("Housing")) {
                                drawableName = "housing";
                            } else if (location.type.equals("Water Supply")) {
                                drawableName = "water";
                            } else if (location.type.equals("Nature Attractions")) {
                                drawableName = "nature";
                            } else if (location.type.equals("Fun")) {
                                drawableName = "fun";
                            }


                            // add marker to map
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(latLng)
                                    .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap(drawableName, 113, 120)))
                                    .snippet(location.id);
                            mMap.addMarker(markerOptions);
                        });

                    }
                });
    }



    // Location listener methods to fulfill the interface
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }


    // remove listener for locations when activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        registration.remove();


    }

    // Resize the bitmap to fit the marker
    public Bitmap resizeBitmap(String drawableName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(drawableName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

}