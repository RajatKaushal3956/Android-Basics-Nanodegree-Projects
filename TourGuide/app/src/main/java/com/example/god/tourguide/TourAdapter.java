package com.example.god.tourguide;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TourAdapter extends FragmentPagerAdapter {
    private Context mContext;
    TourAdapter(Context context, FragmentManager fm){
        super(fm);
        mContext=context;
    }
    @Override
    public Fragment getItem(int position){
        if(position == 0){
            return new HotelsFragment();
        }
        else if(position==1){
            return new RestaurantsFragment();
        }
        else if(position==2){
            return new ReligiousPlacesFragment();
        }
        else{
            return new AttractionsFragment();
        }
    }
    @Override
    public int getCount(){
        return 4;
    }
    @Override
    public CharSequence getPageTitle(int position){
        if(position == 0){
            return mContext.getString(R.string.Tab1);
        }
        else if(position==1){
            return mContext.getString(R.string.Tab2);
        }
        else if(position==2) {
            return mContext.getString(R.string.Tab3);
        }
        else{
            return mContext.getString(R.string.Tab4);
        }
    }
}
