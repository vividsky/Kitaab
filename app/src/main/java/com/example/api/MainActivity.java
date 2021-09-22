package com.example.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>{

    final String LOG_TAG = MainActivity.class.getSimpleName();

    private String BOOK_URL;
    private BookAdapter bookAdapter;
    static Bitmap defaultImage;
    private View book_list;
    private static int LOADER_COUNTS = 0;
    private int current_id = LOADER_COUNTS;
    private static HashMap<String, Integer> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.missingbook);
        book_list = findViewById(R.id.book_list);

        bookAdapter = new BookAdapter(this, new ArrayList<>());
        ListView bookListView = findViewById(R.id.listView);
        bookListView.setAdapter(bookAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Books");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                if (map.containsKey(query)) {
                    current_id = map.get(query);
                } else {
                    LOADER_COUNTS++;
                    current_id = LOADER_COUNTS;
                    map.put(query, LOADER_COUNTS);
                }
                Log.i(LOG_TAG, current_id + "^^^^^^^^^^^^^^^^^^");
                String[] userInput = query.split(" ");
                String s = userInput[0];
                for (int i = 1; i < userInput.length; i++) {
                    s += "+" + userInput[i];
                }
                BOOK_URL = "https://www.googleapis.com/books/v1/volumes?q=" + s + "&orderBy=relevance";

                BookAsyncTask task = new BookAsyncTask();
                task.execute(BOOK_URL);
                getSupportLoaderManager().initLoader(current_id, null, MainActivity.this).forceLoad();
                Log.i(LOG_TAG, "In initLoader########################");

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        getSupportLoaderManager().initLoader(current_id, null, MainActivity.this).forceLoad();
        Log.i(LOG_TAG, "In initLoader########################");


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i(LOG_TAG, "In onCreateLoader########################");
        return new BookLoader(this, BOOK_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> data) {
        Log.i(LOG_TAG, "In onLoadFinished########################");
        bookAdapter.clear();
        if(data != null && !data.isEmpty()) {
            bookAdapter.addAll(data);
        } else {
            Toast.makeText(MainActivity.this, "No data to show", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        bookAdapter.clear();
    }


    private class BookAsyncTask extends AsyncTask<String,Void, List<Book>>  {

        @Override
        protected List<Book> doInBackground(String... urls) {
            if (urls.length == 0) {
                return null;
            }
            return BookUtils.fetchBookData(urls[0]);
        }

        @Override
        protected void onPostExecute(List<Book> books) {
            super.onPostExecute(books);
            bookAdapter.clear();
            bookAdapter.addAll(books);
        }
    }
}