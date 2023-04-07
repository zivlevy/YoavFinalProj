//package services;
//
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.location.Location;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//
//import com.example.knowaround.R;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.textview.MaterialTextView;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class Position {
//
//    private static Position instance;
//
//    private FusedLocationProviderClient fusedLocationClient;
//    private Double latitude;
//    private Double longitude;
//
//    private Position() {
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
//        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            // Logic to handle location object
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//
//                        }
//                    }
//                });
//
//    }
//
//    public static Position getInstance() {
//        if (instance == null) {
//            instance = new Position();
//        }
//        return instance;
//    }
//
//
//  public String getPosition() {
//        return "Position";
//
//   }
//
//
//}
