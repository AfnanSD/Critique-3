package com.example.critique;

public class Store {
    private String name;
    private String[] reviews;
    private String rating;

    public Store(String name, String[] reviews, String rating) {
        this.name = name;
        this.reviews = reviews;
        this.rating = rating;
    }

    public Store(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String[] getReviews() {
        return reviews;
    }

}
