package models;

import android.widget.ImageView;

public class Location {

    public String id;
    public String name;
    public String description;
    public double latitude;
    public double longitude;
    public String photoURL;
    public double averageRating;
    public int numOfReviews;

    public String userId;

    public Location(String name, String description, double averageRating, double lat, double lng, String photoURL, String userId) {
        this.name = "";
        this.description = "";
        this.latitude = lat;
        this.longitude = lng;
        this.photoURL = photoURL;
        this.averageRating = 0;
        this.numOfReviews = 0;
        this.userId = userId;

    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public double getLat() {
        return latitude;
    }

    public void setLat(double lat) {
        this.latitude = lat;
    }

    public double getLng() {
        return longitude;
    }

    public void setLng(double lng) {
        this.longitude = lng;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public int getNumOfReviews() {

        return numOfReviews;
    }

    public void setNumOfReviews(int numOfReviews) {
        this.numOfReviews = numOfReviews;
    }
}
