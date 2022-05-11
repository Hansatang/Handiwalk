package com.example.handiwalk.Models;

import java.util.HashMap;
import java.util.Map;

public class ReviewModel {

    private Map<String, Float> review = new HashMap<>();

    public ReviewModel(String userId, float ratingValue){
        review.put(userId, ratingValue);
    }

    public Map<String, Float> getReview() {
        return review;
    }

    public void setReviews(Map<String, Float> reviews) {
        this.review = reviews;
    }
}
