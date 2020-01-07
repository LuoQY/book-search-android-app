package com.example.project4task1android;

/**
 * Author: Qingyang Luo
 * Last Modified: Nov 9, 2019
 *
 * This method handles the data from the user interface and call methods in
 * FetchData object to process the data and give result.
 *
 */

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * The click listener will need a reference to this object, so that upon successfully finding the book information from Google Books.
         */
        final MainActivity ma = this;

        /*
         * Find the "submit" button, and add a listener to it
         */
        Button submitButton = findViewById(R.id.submit);

        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                String searchTerm = ((EditText) findViewById(R.id.searchTerm)).getText().toString();
                FetchData gp = new FetchData();
                gp.search(searchTerm, ma); // Done asynchronously in another thread.
            }
        });

    }

    /*
     * This is called by the FetchData when the book information is ready.  This allows for passing back the Book object
     *  which contains strings and Bitmap for updating the TextView and ImageView
     */
    public void bookReady(Book book) {
        TextView bookView = findViewById(R.id.GoogleBook);
        TextView searchView = (EditText)findViewById(R.id.searchTerm);
        TextView feedbackView = findViewById(R.id.feedback);
        ImageView pictureView = findViewById(R.id.bookPicture);
        // if the book exists, update the UI
        if (book != null) {
            bookView.setText(book.getBookInfo());
            bookView.setVisibility(View.VISIBLE);
            pictureView.setImageBitmap(book.getImage());
            pictureView.setVisibility(View.VISIBLE);
            String s = "Here is the information of the book "+searchView.getText();
            feedbackView.setText(s);
        } else {  // if the book doesn't exist, give the feedback
            bookView.setVisibility(View.INVISIBLE);
            pictureView.setVisibility(View.INVISIBLE);
            String s = "Sorry, Cannot find the information of the book "+searchView.getText();
            feedbackView.setText(s);
        }
        searchView.setText("");
        bookView.invalidate();
        pictureView.invalidate();
    }

}
