package com.example.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class BookUtils {
    BookUtils() {
    }

    private static String LOG_TAG = BookUtils.class.getSimpleName();

    static List<Book> fetchBookData(String requestURL) {
        URL url = BookUtils.createURL(requestURL);
        String JSONResponse = "";
        try {
            JSONResponse = BookUtils.makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Book> books = BookUtils.extractBooksFromJSON(JSONResponse);

        return books;
    }

    static URL createURL(String stringUrl) {
        URL url;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return url;
    }
    static String readFromInputStream(InputStream stream) throws IOException {

        StringBuilder output = new StringBuilder();

        if(stream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String s = reader.readLine();
            while (s != null) {
                output.append(s);
                s = reader.readLine();
            }
        }
        return output.toString();
    }
    static String makeHttpRequest(URL url) throws IOException {

        String JSONResponse = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            JSONResponse = readFromInputStream(inputStream);
        } catch(Exception e) {
            Log.i(LOG_TAG, e.getMessage() + "@@@@@@@@@@@@@@@@@@");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if(inputStream != null) {
                inputStream.close();
            }
        }
        return JSONResponse;
    }

    public static ArrayList<Book> extractBooksFromJSON(String JSONResponse)  {
        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(JSONResponse);
            JSONArray items = root.getJSONArray("items");

            for(int i = 0; i < items.length(); i++) {
                JSONObject itemsObject = items.getJSONObject(i);
                JSONObject volumeInfo = itemsObject.getJSONObject("volumeInfo");

                String title = volumeInfo.has("title") ? volumeInfo.getString("title") : "No Title";

                String publishDate = volumeInfo.has("publishDate") ? volumeInfo.getString("publishDate") : "No PublishDate";

                int rating = volumeInfo.has("averageRating") ? volumeInfo.getInt("averageRating") : 0;

                String author = volumeInfo.has("authors") ? volumeInfo.getJSONArray("authors").getString(0) : "No Author";

                String imageLink = volumeInfo.has("imageLinks") ? volumeInfo.getJSONObject("imageLinks").getString("thumbnail") : "No Image";

                JSONObject listPrice = volumeInfo.has("saleInfo") ? volumeInfo.getJSONObject("saleInfo").getJSONObject("listPrice") : null;
                String amount = listPrice != null ? listPrice.getInt("amount") + listPrice.getString("currencyCode") : "00.00 INR";

                books.add(new Book(author, amount, publishDate, title, rating, imageURLToBitMap(imageLink)));
            }
        } catch(Exception e) {
            Log.i(LOG_TAG, e.getMessage() + "@@@@@@@@@@@@@@");
        }
        return books;
    }

    private static Bitmap imageURLToBitMap(String imageLink) {

        URL url;
        Bitmap image;
        try {
            if (imageLink != "No Image") {
                imageLink = imageLink.substring(0, 4) + 's' + imageLink.substring(4);
                url = new URL(imageLink);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } else {
               image = MainActivity.defaultImage;
            }
            return image;
        } catch(IOException e) {
            Log.i(LOG_TAG, e.getMessage());
            return null;
        }
    }
}
