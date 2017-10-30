package com.apoorv.android.weatherapp.mSwiper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.apoorv.android.weatherapp.CityListActivity;

/**
 * Created by apoorv.mehta on 10/30/17.
 */

public class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    CityListActivity.SimpleItemRecyclerViewAdapter reAdapter;

    public SwipeHelper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    public SwipeHelper(CityListActivity.SimpleItemRecyclerViewAdapter reAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.reAdapter = reAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        reAdapter.deleteCityWithSwipe(viewHolder.getAdapterPosition());
    }
}
