package services;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import models.Location;

public class Locations {

    private static Locations instance;
    private double latitude;
    private double longitude;
    private double averageRating;
    private String locationName;
    private String locationDescription;
    private int numberOfRatings;
    private FirebaseFirestore db;

    private Locations() {
        db = FirebaseFirestore.getInstance();
        numberOfRatings = 0;
    }

    public static Locations getInstance() {
        if (instance == null) {
            instance = new Locations();
        }
        return instance;
    }

    public Task<Void> addLocation(double latitude, double longitude, String locationName, String locationDescription, String userId) {
        String id = UUID.randomUUID().toString();
        Map<String, Object> location = new HashMap<>();
        location.put("userId", userId);
        location.put("id", id);
        location.put("latitude", latitude);
        location.put("longitude", longitude);
        location.put("locationName", locationName);
        location.put("locationDescription", locationDescription);
        location.put("averageRating", 0);
        location.put("numberOfRatings", 0);
        return db.collection("locations").document(id).set(location);
    };

    public Task<Void> editLocation(Location location) {
        Map<String, Object> editedLocation = new HashMap<>();
        editedLocation.put("latitude", location.latitude);
        editedLocation.put("longitude", location.longitude);
        editedLocation.put("locationName", location.name);
        editedLocation.put("locationDescription", location.description);
        return db.collection("locations").document(location.id).set(editedLocation);
    };

    public  Task<QuerySnapshot>  getAllLocations() {
       return db.collection("locations").get();
    }

    public Task<Void> addRating(double rating, String locationId) {

        final DocumentReference locationRef = db.collection("locations").document(locationId);

        return db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(locationRef);
                double newAverageRating = (snapshot.getDouble("averageRating") * snapshot.getDouble("numberOfRatings") + rating) / (snapshot.getDouble("numberOfRatings") + 1);
                int newNumberOfRatings = snapshot.getDouble("numberOfRatings").intValue() + 1;
                transaction.update(locationRef, "averageRating", newAverageRating);
                transaction.update(locationRef, "numberOfRatings", newNumberOfRatings);

                // Success
                return null;
            }
        });

    }

    public Task<Void> deleteLocation(String locationId) {
        return db.collection("locations").document(locationId)
                .delete();
    }
}

