package com.apoorv.android.weatherapp.helper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.apoorv.android.weatherapp.CityDetailFragment;
import com.apoorv.android.weatherapp.model.DefaultList;

/**
 * Created by apoorv.mehta on 11/11/17.
 */

public class CityDetailPageAdapter extends FragmentStatePagerAdapter {

    public Context mContext;

    public CityDetailPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        CityDetailFragment currentCityDetailFragment = new CityDetailFragment();
        Bundle args = new Bundle();
        args.putInt(CityDetailFragment.ARG_ITEM_ID, position);
        currentCityDetailFragment.setArguments(args);
        return currentCityDetailFragment;
    }

    @Override
    public int getCount() {
        return DefaultList.LISTPLACES.size();
    }
}
