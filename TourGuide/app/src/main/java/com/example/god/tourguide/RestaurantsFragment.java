package com.example.god.tourguide;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class RestaurantsFragment extends Fragment {
    public RestaurantsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);
        final ArrayList<word> words= new ArrayList<word>();
        words.add(new word(R.string.restaurant1_Heading,R.string.restaurant1_Details,R.drawable.rest2));
        words.add(new word(R.string.restaurant2_Heading,R.string.restaurant2_Details,R.drawable.rest1));
        wordAdapter adapter = new wordAdapter(getActivity(),words);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        return rootView;
    }
}
