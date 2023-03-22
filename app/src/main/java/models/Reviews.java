package models;

public class Reviews {

    public int rating;
    public String review;
    public int reviewerId;
    public String reviewerName;
    public int locationId;

    public Reviews(int rating, String review, int reviewerId, String reviewerName, int locationId) {
        this.rating = rating;
        this.review = review;
        this.reviewerId = reviewerId;
        this.reviewerName = reviewerName;
        this.locationId = locationId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
}
