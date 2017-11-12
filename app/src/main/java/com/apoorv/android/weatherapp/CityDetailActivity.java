package com.apoorv.android.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.apoorv.android.weatherapp.helper.CityDetailPageAdapter;

/**
 * An activity representing a single City detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CityListActivity}.
 */
public class CityDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        CityDetailPageAdapter pageAdapter;
        ViewPager mViewPager;


        pageAdapter = new CityDetailPageAdapter(getSupportFragmentManager(),this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(pageAdapter);


        int index = getIntent().getIntExtra("index", 0);
        mViewPager.setCurrentItem(index);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown.
            navigateUpTo(new Intent(this, CityListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
