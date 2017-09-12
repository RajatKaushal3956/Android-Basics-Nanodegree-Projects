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


public class ReligiousPlacesFragment extends Fragment {

    public ReligiousPlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list,container,false);
        final ArrayList<word> words= new ArrayList<word>();
        words.add(new word(R.string.temple_heading,R.string.temple_details,R.drawable.kalimatatemple));
        words.add(new word(R.string.gurudwara_Heading,R.string.gurudwara_Details,R.drawable.gurudwara));
        wordAdapter adapter = new wordAdapter(getActivity(),words);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        return rootView;
    }

}
