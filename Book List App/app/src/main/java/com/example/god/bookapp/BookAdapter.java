package com.example.god.bookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Context context, ArrayList<Book> Book){
        super(context,0, Book);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Book currentBook=getItem(position);
        TextView title_textView = (TextView) listItemView.findViewById(R.id.title);
        title_textView.setText(currentBook.getTitle());
        TextView Authors_text_view = (TextView) listItemView.findViewById(R.id.authors);
        Authors_text_view.setText(currentBook.getAuthorsString());
        TextView description_text_view = (TextView) listItemView.findViewById(R.id.description);
        description_text_view.setText(currentBook.getDescription());
            return listItemView;
    }
}
