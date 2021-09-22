package com.example.api;

import android.graphics.Bitmap;

public class Book {

    private static final String LOG_TAG = Book.class.getSimpleName();

    private String author;
    private String title;
    private String publishDate;
    private String amount;
    private int rating;
    private Bitmap image;


    Book(String author, String amount, String publishDate, String title, int rating, Bitmap image) {

        this.author = author;
        this.amount = amount;
        this.publishDate = publishDate;
        this.title = title;
        this.rating = rating;
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getAmount() {
        return amount;
    }

    public int getRating() {
        return rating;
    }

    public Bitmap getImageLink() {
        return image;
    }
}
