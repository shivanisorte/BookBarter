package com.shivani.bookbarter;

import android.app.Activity;
//import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shivani.bookbarter.R;

import java.util.List;


public class AuthorList extends ArrayAdapter<Authors> {
    //private final List<Authors> authors;
    private Activity context;
    List<Authors> authors;

    public AuthorList(Activity context, List<Authors> authors) {
        super(context, R.layout.layout_author_list, authors);
        this.context = context;
        this.authors= authors;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_author_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.textViewGenre);

        Authors author = authors.get(position);
        textViewName.setText(author.getAuthorName());
        textViewGenre.setText(author.getAuthorGenre());

        return listViewItem;
    }
}

