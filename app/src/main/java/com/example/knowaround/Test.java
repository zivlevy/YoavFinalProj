package com.example.knowaround;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import services.Auth;
import services.Locations;

public class Test extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    Button btnTest;
    FloatingActionButton btnFab;
    EditText etLocationName, etLocationDescription, etLatitude, etLongitude;
    String locationName, locationDescription;
    double latitude, longitude;
    String userId;
    Auth auth = Auth.getInstance();


    ImageView imageView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            showImage();
        }
    }

    private void showImage() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        saveToDB(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);

//        imageView.setImageURI(contentUri);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Add customization options here

        Locations locations = Locations.getInstance();

        btnTest = findViewById(R.id.btAddLocation);
        imageView = findViewById(R.id.imageView);
        btnFab = findViewById(R.id.floating_action_button);
        btnFab.setOnClickListener(view -> {


//            MaterialAlertDialogBuilder buildr =  new MaterialAlertDialogBuilder(this);
//            buildr.setTitle("Title");
//            buildr.setMessage("Message");
//            buildr.setPositiveButton("OK", (dialogInterface, i) -> {
//                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
//            });
//            buildr.setNegativeButton("Cancel", (dialogInterface, i) -> {    Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
//            });
//            buildr.show();
        });
        Glide.with(this).load("https://res.cloudinary.com/du3ksi0bw/image/upload/v1680558377/imports/rzcjowxzowmmshqjh0kf.jpg")
                .override(500, 800)
                .fitCenter()
                .into(imageView);
//        if (ContextCompat.checkSelfPermission(Test.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(Test.this, new String[]{
//                    Manifest.permission.CAMERA
//            }, 100);
//        }


//        etLocationName = findViewById(R.id.locationName);
//        etLocationDescription = findViewById(R.id.locationDescription);
//        etLatitude = findViewById(R.id.latitude);
//        etLongitude = findViewById(R.id.longitude);

        btnTest.setOnClickListener(view -> {

            dispatchTakePictureIntent();
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

//            locationName = etLocationName.getText().toString();
//            locationDescription = etLocationDescription.getText().toString();
//            latitude = Double.parseDouble(etLatitude.getText().toString());
//            longitude = Double.parseDouble(etLongitude.getText().toString());
//            userId = auth.getCurrentUser().getUid();
//            locations.addLocation(latitude, longitude, locationName, locationDescription, userId)
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(this, "failer", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(e -> {
//                        Toast.makeText(this, "failer", Toast.LENGTH_SHORT).show();
//                    });




        });
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        if (true) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 100);
            }
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void saveToDB(String filePath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Uri file = Uri.fromFile(new File(filePath));
        StorageReference imageRef = storageRef.child("images/"+ new Date() + ".jpg");
        UploadTask uploadTask = imageRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        Log.d("url", url);
//                        return null;
                    }
                });
//                return null;
            }
        });
    }
}