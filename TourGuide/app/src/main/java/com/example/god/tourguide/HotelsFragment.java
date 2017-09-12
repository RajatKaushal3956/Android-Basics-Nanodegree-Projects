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

public class HotelsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public HotelsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.word_list,container,false);
        final ArrayList<word> words = new ArrayList<word>();
        words.add(new word(R.string.hotel1_Heading,R.string.hotel1_Details,R.drawable.hotel1));
        words.add(new word(R.string.hotel2_Heading,R.string.hotel2_Details,R.drawable.hotel2));
        wordAdapter adapter = new wordAdapter(getActivity(),words);
        ListView listView= (ListView) rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        return rootView;

    }

}
