package models;

import android.widget.ImageView;

public class Location {

    public String name;
    public String description;
    public int id;
    public double averageRating;
    public double lat; //latitude
    public double lng; //longitude
    public ImageView photo;
    public int numOfReviews;

    public Location(String name, String description, int id, double averageRating, double lat, double lng, ImageView photo) {
        this.name = "";
        this.description = "";
        this.id = id;
        this.averageRating = 0;
        this.lat = lat;
        this.lng = lng;
        this.photo = photo;
        this.numOfReviews = 0;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public ImageView getPhoto() {
        return photo;
    }

    public void setPhoto(ImageView photo) {
        this.photo = photo;
    }

    public int getNumOfReviews() {

        return numOfReviews;
    }

    public void setNumOfReviews(int numOfReviews) {
        this.numOfReviews = numOfReviews;
    }
}
