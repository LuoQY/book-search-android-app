package com.example.project4task1android;

/**
 * Author: Qingyang Luo
 * Last Modified: Nov 9, 2019
 *
 * This class works to send HTTP GET request to the web service and deals
 * with the response and returns it to the MainActivity class
 *
 *
 */

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.json.JSONObject;
import android.os.AsyncTask;

public class FetchData {

    MainActivity ip = null;
    // the url of the web service deployed on Heroku
    //private static String localUrl = "http://10.0.2.2:8080/Project4Task2_war_exploded/BookInfoServlet";
    private  static String localUrl = "https://safe-depths-52893.herokuapp.com/BookInfoServlet";
    //private  static String localUrl = "https://blooming-temple-63535.herokuapp.com/BookInfoServlet";

    /**
     * A public search method can be called.
     * @param searchTerm the search term
     * @param ip the MainActivity
     */
    public void search(String searchTerm, MainActivity ip) {
        this.ip = ip;
        new AsyncBookSearch().execute(searchTerm);
    }

    /*
     * AsyncTask provides a simple way to use a thread separate from the UI thread in which to do network operations.
     * doInBackground is run in the helper thread.
     * onPostExecute is run in the UI thread, allowing for safe UI updates.
     */
    private class AsyncBookSearch extends AsyncTask<String, Void, Book> {
        protected Book doInBackground(String... urls) {
            try {
                return search(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Book book) {
            ip.bookReady(book);
        }

        /*
         * Search Google Books for the searchTerm argument, and return a String of book information
         * and a Bitmap of book cover that can be put in an ImageView
         */
        private Book search(String searchTerm) throws Exception {
            String response = "";
            try {
                // Make a call to the local servlet
                URL url = new URL(localUrl+ "?search=" + searchTerm);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // set request method to GET
                conn.setRequestMethod("GET");
                // tell the servlet what format to be sent back
                conn.setRequestProperty("Accept", "text/plain");

                // wait for response
                int status = conn.getResponseCode();

                // If things went poorly, don't try to read any response, just return.
                if (status != 200) {
                    // not using msg
                    String msg = conn.getResponseMessage();
                    throw new Exception("Servlet Access Error: " + conn.getResponseCode());
                }

                String output = "";

                // things went well so let's read the response
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
                while ((output = br.readLine()) != null) {
                    response += output;
                }
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }   catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("response: "+response);
            if (response.equals("")) {
                return null;
            }

            // extract information from the json formatted response
            JSONObject doc = new JSONObject(response);
            StringBuilder output = new StringBuilder();
            // get the book information
            output.append("Title: ").append(doc.get("title").toString()).append("\n");
            output.append("Author(s): ").append(doc.get("authors").toString()).append("\n");
            output.append("Publisher: ").append(doc.get("publisher").toString()).append("\n");
            String imageLink = doc.get("imageLink").toString();
            Bitmap bookImage = null;
            // if the image link is null, report the error. Or, get the bit map of the image
            if (imageLink != null) {
                try {
                    URL u = new URL(imageLink);
                    bookImage = getRemoteImage(u);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception("Image URL has a problem");
                }
            }
            Book book = new Book(output.toString(), bookImage);

            return book;

        }

        /*
         * Given a URL referring to an image, return a bitmap of that image
         */
        private Bitmap getRemoteImage(final URL url) {
            try {
                final URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();
                return bm;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}

