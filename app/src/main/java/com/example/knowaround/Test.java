package com.example.knowaround;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import services.Auth;
import services.Locations;

public class Test extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    Button btnTest;
    EditText etLocationName, etLocationDescription, etLatitude, etLongitude;
    String locationName, locationDescription;
    double latitude, longitude;
    String userId;
    Auth auth = Auth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Locations locations = Locations.getInstance();

        btnTest = findViewById(R.id.btAddLocation);
        etLocationName = findViewById(R.id.locationName);
        etLocationDescription = findViewById(R.id.locationDescription);
        etLatitude = findViewById(R.id.latitude);
        etLongitude = findViewById(R.id.longitude);

        btnTest.setOnClickListener(view -> {
//locations.deleteLocation("B0t3fSOYl3Chldhhu8xm")
//        .addOnSuccessListener(aVoid -> {
//            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
//        }).addOnFailureListener(e -> {
//            Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show();
//        });


//            locations.addRating(2,"B0t3fSOYl3Chldhhu8xm")
//                    .addOnSuccessListener(aVoid -> {
//                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
//                    }).addOnFailureListener(e -> {
//                        Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show();
//                    });


//            locations.getAllLocations().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Log.d(TAG, document.getId() + " => " + document.getData());
//                        }
//                    } else {
//                        Log.d(TAG, "Error getting documents: ", task.getException());
//                    }
//                }
//            });

            locationName = etLocationName.getText().toString();
            locationDescription = etLocationDescription.getText().toString();
            latitude = Double.parseDouble(etLatitude.getText().toString());
            longitude = Double.parseDouble(etLongitude.getText().toString());
            userId = auth.getCurrentUser().getUid();
            locations.addLocation(latitude, longitude, locationName, locationDescription, userId)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "failer", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(this, "failer", Toast.LENGTH_SHORT).show();
                    });

        });
    }
}