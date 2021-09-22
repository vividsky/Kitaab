package com.example.api;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.URL;
import java.util.List;

public class BookAdapter extends ArrayAdapter {

    private static final String LOG_TAG = Book.class.getSimpleName();

    public BookAdapter(@NonNull Context context, @NonNull List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if(listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.book_list, parent, false);
        }

        Book book = (Book) getItem(position);

        TextView authorName = listView.findViewById(R.id.author_name);
        authorName.setText(book.getAuthor());

        TextView publishDate = listView.findViewById(R.id.publishDate);
        publishDate.setText(book.getPublishDate());

        TextView title = listView.findViewById(R.id.title);
        title.setText(book.getTitle());

        RatingBar rating = listView.findViewById(R.id.rating);
        rating.setRating(book.getRating());

        TextView amount = listView.findViewById(R.id.amount);
        amount.setText(book.getAmount());

        ImageView book_image = listView.findViewById(R.id.book_image);
        book_image.setImageBitmap(book.getImageLink());


        return listView;
    }
}
