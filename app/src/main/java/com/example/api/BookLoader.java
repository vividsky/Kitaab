package com.example.api;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private static String LOG_TAG = BookUtils.class.getSimpleName();

    String url;
    public BookLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Nullable
    @Override
    public List<Book> loadInBackground() {
        Log.i(LOG_TAG, "In loadInBackground########################");
        if (url == null) {
            return null;
        }
        return BookUtils.fetchBookData(url);
    }
}
