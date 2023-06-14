package com.example.knowaround;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.knowaround.databinding.ActivityMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import services.Auth;
import services.LocationService;

public class Map extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;
    private ActivityMapBinding binding;
    FloatingActionButton btnFab;
    private FirebaseFirestore db;
    private ListenerRegistration registration;

    private List<models.Location> locations = new ArrayList<>();

    private Auth auth = Auth.getInstance();
    private LocationService locationService  = LocationService.getInstance();

//    bottom drawer
    ImageView imageView;
    TextView tvLocationName, tvLocationDescription, tvLocationRating;

    SearchView searchView;
    ListView listView;

    ArrayAdapter<String> adapter;



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
                // find the loaction in the locations list by the id stored in the marker snippet
                locations.stream()
                        .filter(location -> location.id.equals(marker.getSnippet()))
                        .findFirst()
                        .ifPresent(location -> {
                            // open bottom drawer with location details
                            showBottomSheetDialog(location) ;
                });
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
                        String [] locationNames = new String[value.size()];
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

                                locationNames[locations.indexOf(location)] = location.name;
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


                        searchView = findViewById(R.id.searchView);
                        listView = findViewById(R.id.listView);
                        adapter = new ArrayAdapter<>(Map.this, android.R.layout.simple_list_item_1,  android.R.id.text1, locationNames);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                models.Location location = locations.get(i);
                                LatLng latLng = new LatLng(location.latitude, location.longitude);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                listView.setVisibility(View.GONE);
                            }
                        });


                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                listView.setVisibility(View.VISIBLE);
                                Map.this.adapter.getFilter().filter(query);
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                listView.setVisibility(View.VISIBLE);
                                Map.this.adapter.getFilter().filter(newText);
                                return false;
                            }

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

    private void showBottomSheetDialog(models.Location location) {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);
            imageView = bottomSheetDialog.findViewById(R.id.imageView);
            Glide.with(this).load(location.photoURL)
                    .override(500, 800)
                    .fitCenter()
                    .into(imageView);
            tvLocationDescription = bottomSheetDialog.findViewById(R.id.placeDesc);
            tvLocationDescription.setText(location.description);
            tvLocationName = bottomSheetDialog.findViewById(R.id.placeName);
            tvLocationName.setText(location.name);
            tvLocationRating = bottomSheetDialog.findViewById(R.id.placeRating);
            DecimalFormat formatter = new DecimalFormat("#0.00");
            RatingBar rating= bottomSheetDialog.findViewById(R.id.ratingBarLocation);
            Button btnReview = bottomSheetDialog.findViewById(R.id.btRateLocation);
            Button btnDelete = bottomSheetDialog.findViewById(R.id.btDelete);
            Button btnEdit = bottomSheetDialog.findViewById(R.id.btEdit);


            if (auth.getCurrentUser().getUid().equals(location.userId)){
                btnReview.setVisibility(View.GONE);
                rating.setVisibility(View.GONE);
            }

            else {
                tvLocationRating.setText("Average Rating: " +  formatter.format(location.averageRating));
                btnDelete.setVisibility(View.GONE);
                btnEdit.setVisibility(View.GONE);
            }
            btnReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationService.addRating(rating.getRating(), location.id);
                    bottomSheetDialog.dismiss();
                    //change it so if the user already rated the location he update his rating instead of adding a new one
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationService.deleteLocation(location.id);
                    bottomSheetDialog.dismiss();
                }
            });
            btnEdit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Map.this, AddLocation.class);
                    intent.putExtra("isEdit",true);
                    intent.putExtra("id",location.id);
                    intent.putExtra("name",location.name);
                    intent.putExtra("description",location.description);
                    intent.putExtra("latitude",location.latitude);
                    intent.putExtra("longitude",location.longitude);
                    intent.putExtra("photoURL",location.photoURL);
                    intent.putExtra("type",location.type);
                    intent.putExtra("averageRating",location.averageRating);
                    intent.putExtra("numOfReviews",location.numOfReviews);
                    intent.putExtra("userId",location.userId);
                    bottomSheetDialog.dismiss();
                    startActivity(intent);
                }
            });

            bottomSheetDialog.show();

    }

}