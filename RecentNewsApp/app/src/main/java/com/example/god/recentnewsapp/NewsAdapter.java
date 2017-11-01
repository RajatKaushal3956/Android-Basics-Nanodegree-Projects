package com.example.god.recentnewsapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, ArrayList<News> newsArrayList) {
        super(context, 0, newsArrayList);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView title_text_view = (TextView) convertView.findViewById(R.id.title);
        TextView authorTV_text_view = (TextView) convertView.findViewById(R.id.author);
        title_text_view.setText(news.getTitle());
        authorTV_text_view.setText(news.getAuthor());
        return convertView;
    }
}

