package services;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;
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
    private int numOfReviews;
    private FirebaseFirestore db;
    private Auth auth = Auth.getInstance();

    private Locations() {
        db = FirebaseFirestore.getInstance();
        numOfReviews = 0;
    }

    public static Locations getInstance() {
        if (instance == null) {
            instance = new Locations();
        }
        return instance;
    }

    public Task<Void> addLocation(String name, String description, double latitude, double longitude, String photoURL, String type) {

        String userId = auth.getCurrentUser().getUid();
        String id = UUID.randomUUID().toString();
        Map<String, Object> location = new HashMap<>();
        location.put("userId", userId);
        location.put("id", id);
        location.put("type", type);
        location.put("name", name);
        location.put("description", description);
        location.put("latitude", latitude);
        location.put("longitude", longitude);
        location.put("photoURL", photoURL);
        location.put("averageRating", 0);
        location.put("numOfReviews", 0);
        return db.collection("locations").document(id).set(location);
    };


    public Task<Void> updateLocation(String id, String userId, String name, String description, double latitude, double longitude, String photoURL, String type, double averageRating, int numOfReviews) {

        Map<String, Object> location = new HashMap<>();
        location.put("userId", userId);
        location.put("id", id);
        location.put("type", type);
        location.put("name", name);
        location.put("description", description);
        location.put("latitude", latitude);
        location.put("longitude", longitude);
        location.put("photoURL", photoURL);
        location.put("averageRating", averageRating);
        location.put("numOfReviews", numOfReviews);
        return db.collection("locations").document(id).set(location, SetOptions.merge());
    }
    public Task<Void> editLocation(Location location) {
        Map<String, Object> editedLocation = new HashMap<>();
        editedLocation.put("userId", location.userId);
        editedLocation.put("id", location.id);
        editedLocation.put("type", location.type);
        editedLocation.put("name", location.name);
        editedLocation.put("description", location.description);
        editedLocation.put("latitude", location.latitude);
        editedLocation.put("longitude", location.longitude);
        editedLocation.put("photoURL",location. photoURL);
        editedLocation.put("averageRating", location.averageRating);
        editedLocation.put("numOfReviews", location.numOfReviews);
        return db.collection("locations").document(location.id).set(editedLocation, SetOptions.merge());
    }

    ;

    public Task<QuerySnapshot> getAllLocations()  {
        return db.collection("locations").get();
    }

    public Task<Void> addRating(double rating, String locationId) {
        String userId = auth.getCurrentUser().getUid();
        final DocumentReference locationRef = db.collection("locations").document(locationId);
        final DocumentReference userLocationRateRef = db.collection("userLocationRating").document(userId+ "-"+ locationId);
        Map<String, Object> userRating = new HashMap<>();
        userRating.put("rating",rating);
        return db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot userRatingSnapshot = transaction.get(userLocationRateRef);
                double currentUserRating=-1;
                if (userRatingSnapshot.exists()) {
                    currentUserRating = userRatingSnapshot.getDouble("rating");
                }



                DocumentSnapshot snapshot = transaction.get(locationRef);
                double newAverageRating;
                int newnumOfReviews;
                if(currentUserRating!=-1){
                newAverageRating = (snapshot.getDouble("averageRating") * snapshot.getDouble("numOfReviews") - currentUserRating + rating) / (snapshot.getDouble("numOfReviews"));
                    newnumOfReviews =snapshot.getDouble("numOfReviews").intValue();
                } else{
                    newAverageRating = (snapshot.getDouble("averageRating") * snapshot.getDouble("numOfReviews") + rating) / (snapshot.getDouble("numOfReviews") + 1);
                    newnumOfReviews = snapshot.getDouble("numOfReviews").intValue() + 1;
                }

                transaction.update(locationRef, "averageRating", newAverageRating);
                transaction.update(locationRef, "numOfReviews", newnumOfReviews);
                transaction.set(userLocationRateRef, userRating, SetOptions.merge());


                // Success
                return null;
            }
        });

    }

    public Task<Void> deleteLocation(String locationId) {
        return db.collection("locations").document(locationId)
                .delete();
    }

    public Task<Uri> savePictureToDB(String filePath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Uri file = Uri.fromFile(new File(filePath));
        String url = "";
        StorageReference imageRef = storageRef.child("images/" + new Date() + ".jpg");
        UploadTask uploadTask = imageRef.putFile(file);

        return uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageRef.getDownloadUrl();
            }
        });

    }
}

