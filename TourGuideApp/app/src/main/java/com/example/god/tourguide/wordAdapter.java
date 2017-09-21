package com.example.god.tourguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class wordAdapter extends ArrayAdapter<word> {
    public wordAdapter(Context context, ArrayList<word> words){
        super(context,0,words);
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        View listItemView=convertView;
        if(listItemView ==null){
        listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_view,parent,false);
        }
        word currentWord = getItem(position);
        TextView head_text_View = (TextView) listItemView.findViewById(R.id.head);
        head_text_View.setText(currentWord.getHeadingId());
        TextView add_text_view = (TextView) listItemView.findViewById(R.id.add);
        add_text_view.setText(currentWord.getAddressId());
        ImageView image = (ImageView) listItemView.findViewById(R.id.image1);
        if(currentWord.hasImage()){
            image.setImageResource(currentWord.getImageId());
            image.setVisibility(View.VISIBLE);
        }
        else{
            image.setVisibility(View.GONE);
        }
        return listItemView;

    }

}
