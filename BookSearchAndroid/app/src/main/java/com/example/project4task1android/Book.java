package com.example.project4task1android;

/**
 * Author: Qingyang Luo
 * Last Modified: Nov 9, 2019
 *
 * This class contains the necessary book information shown on the user interface
 * and provide getter methods to get them.
 *
 */

import android.graphics.Bitmap;

public class Book {
    private String bookInfo; // a string of the book information
    private Bitmap image;  // the picture of the book cover

    /**
     * A constructor with parameter
     * @param bookInfo
     * @param image
     */
    public Book(String bookInfo, Bitmap image) {
        this.bookInfo = bookInfo;
        this.image = image;
    }

    /**
     * Simple getter method
     * @return a string of book information
     */
    public String getBookInfo() {
        return bookInfo;
    }

    /**
     * Simple getter method
     * @return a Bitmap of image
     */
    public Bitmap getImage() {
        return image;
    }
}
