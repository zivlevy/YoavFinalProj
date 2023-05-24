package com.example.knowaround;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import services.Locations;

//import services.Position;


public class AddLocation extends AppCompatActivity {

    //    private Position positionService = Position.getInstance();
    private FusedLocationProviderClient fusedLocationClient;
    private Boolean isEdit = false;
    private Double latitude;
    private Double longitude;
    private AutoCompleteTextView autoCompleteTextView;
    private Button btnAddLocation;
    private Button btnTakePicture;
    private TextView tvTitle;
    private ImageView imageView;
    private Locations locationsService = Locations.getInstance();


    // for edit vars
    private String id;
    private String photoURL;
    double averageRating;
    int numOfReviews;

    // when new image taken on edit
    boolean isEditNewImage = false;
    String userId;

    TextInputEditText etLocationName, etLocationDescription;
    String currentPhotoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        btnAddLocation = findViewById(R.id.btn_saveLoction);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        autoCompleteTextView = findViewById(R.id.AutoCompleteTextview);
        etLocationName = findViewById(R.id.locationName);
        etLocationDescription = findViewById(R.id.locationDescription);
        tvTitle = findViewById(R.id.textView_loginTitle);
        imageView = findViewById(R.id.imageView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isEdit = extras.getBoolean("isEdit");
            if (isEdit) {
                tvTitle.setText("Edit Location");
                id = extras.getString("id");
                etLocationName.setText(extras.getString("name"));
                etLocationDescription.setText(extras.getString("description"));
                latitude = extras.getDouble("latitude");
                longitude = extras.getDouble("longitude");
                photoURL = extras.getString("photoURL");
                autoCompleteTextView.setText(extras.getString("type"));
                averageRating = extras.getDouble("averageRating");
                numOfReviews = extras.getInt("numOfReviews");
                userId = extras.getString("userId");
                Glide.with(this).load(photoURL)
                        .into(imageView);
            }

            //The key argument here must match that used in the other activity
        }
        //We will use this data to inflate the drop-down items
        String[] Subjects = new String[]{"Food & Drink", "Resupply", "Medical", "Attractions", "Housing", "Water Supply", "Nature Attractions", "Fun", " Misc"};

        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, Subjects);
        autoCompleteTextView.setAdapter(adapter);

        //to get selected value add item click listener
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "" + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null && !isEdit) {  //  update location only if not in edit mode
                            // Logic to handle location object
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
//                        return null;
                    }
                });


        btnTakePicture.setOnClickListener(view -> {
            dispatchTakePictureIntent();
        });

        btnAddLocation.setOnClickListener(view -> {
            // check if location is available
            if (latitude == null || longitude == null) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddLocation.this);
                builder.setTitle("Location not available");
                builder.setMessage("Location is not available, please try again later.");
                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                });

                builder.show();
                return;
            }

            // check if all fields are filled
            if (etLocationName.getText().toString().isEmpty() || etLocationDescription.getText().toString().isEmpty() || autoCompleteTextView.getText().toString().isEmpty()) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddLocation.this);
                builder.setTitle("All fields are required");
                builder.setMessage("Please fill all fields before saving.");
                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                });

                builder.show();
                return;
            }

            // check if image is taken
            if (!isEdit && currentPhotoPath == null) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddLocation.this);
                builder.setTitle("Image is required");
                builder.setMessage("Please take a picture before saving.");
                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                });

                builder.show();
                return;
            }
            if (isEdit) {// save updated location
                if (isEditNewImage) {
                    locationsService.savePictureToDB(currentPhotoPath)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String url = downloadUri.toString();
                                    locationsService.updateLocation(
                                            id,
                                            userId,
                                            etLocationName.getText().toString(),
                                            etLocationDescription.getText().toString(),
                                            latitude,
                                            longitude,
                                            url,
                                            autoCompleteTextView.getText().toString(),
                                            averageRating,
                                            numOfReviews
                                    ).addOnCompleteListener(saveTask -> {
                                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddLocation.this);
                                        builder.setTitle("Success");
                                        builder.setMessage("The location saved successfully .");
                                        builder.setPositiveButton("OK", (dialogInterface, i) -> {
                                            finish();
                                        });

                                        builder.show();
                                    });

                                }
                            });
                } else {
                    locationsService.updateLocation(
                            id,
                            userId,
                            etLocationName.getText().toString(),
                            etLocationDescription.getText().toString(),
                            latitude,
                            longitude,
                            photoURL,
                            autoCompleteTextView.getText().toString(),
                            averageRating,
                            numOfReviews
                    ).addOnCompleteListener(saveTask -> {
                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddLocation.this);
                        builder.setTitle("Success");
                        builder.setMessage("The location saved successfully .");
                        builder.setPositiveButton("OK", (dialogInterface, i) -> {
                            finish();
                        });

                        builder.show();
                    });

                }

            } else {// add new location
                // if yes, save to DB
                locationsService.savePictureToDB(currentPhotoPath)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                String url = downloadUri.toString();
                                locationsService.addLocation(
                                        etLocationName.getText().toString(),
                                        etLocationDescription.getText().toString(),
                                        latitude,
                                        longitude,
                                        url,
                                        autoCompleteTextView.getText().toString()
                                ).addOnCompleteListener(saveTask -> {
                                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddLocation.this);
                                    builder.setTitle("Success");
                                    builder.setMessage("The location saved successfully .");
                                    builder.setPositiveButton("OK", (dialogInterface, i) -> {
                                        finish();
                                    });

                                    builder.show();
                                });
                            } else {
                                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(AddLocation.this);
                                builder.setTitle("Error");
                                builder.setMessage("Something went wrong, please try again later.");
                                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                                });

                                builder.show();

                            }
                        });
            }

//
        });
    }


//    ************
//    CAMERA CODE
//    ************

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            showImage();
        } else {
//            imageView.setImageDrawable(null);
//            currentPhotoPath = null;
        }
    }

    private void showImage() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        imageView.setImageURI(contentUri);
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
                isEditNewImage = true;
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 100);
            }
        }
    }


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


}

