package com.apoorv.android.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apoorv.android.weatherapp.model.DefaultList;
import com.apoorv.android.weatherapp.helper.Constants;
import com.apoorv.android.weatherapp.helper.ExceptionMessageHandler;
import com.apoorv.android.weatherapp.helper.GetCurrentWeather;
import com.apoorv.android.weatherapp.helper.LogHelper;
import com.apoorv.android.weatherapp.helper.RequestClass;
import com.apoorv.android.weatherapp.helper.SettingsPreference;
import com.apoorv.android.weatherapp.mSwiper.SwipeHelper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.json.JSONException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
 public  class  CityListActivity extends AppCompatActivity {
    private boolean mTwoPane;
    private Place selectedPlace;
    public SimpleItemRecyclerViewAdapter cityListAdapter;
    private Handler mBackgroundHandler;
    private Handler mForegroundHandler;
    private boolean mPaused = false;
    private static final int START_API_MSG = 101;
    private static final int FIN_API_MSG = 303;
    PlaceAutocompleteFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Set city list layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        //Set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ExceptionMessageHandler.context = getApplicationContext();

        if (findViewById(R.id.city_detail_container) != null) {
            mTwoPane = true;
        }
        //Bind the butterknife library
        ButterKnife.bind(this);

        //Call the ReadMethod to read list from sharedPreferences
        DefaultList.readFromSharedInitial(this);




        View recyclerView = findViewById(R.id.city_list);
        assert recyclerView != null;
        this.cityListAdapter = new SimpleItemRecyclerViewAdapter(DefaultList.LISTPLACES);
        setupRecyclerView((RecyclerView) recyclerView);

        //Swipe Delete code
        ItemTouchHelper.Callback callback = new SwipeHelper(this.cityListAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView((RecyclerView) recyclerView);

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        
        
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                LogHelper.logMessage("Apoorv", "Place: " + place.getName());
                LogHelper.logMessage("Place","Place Name: "+place.getLatLng().latitude);
                selectedPlace = place;
                LogHelper.logMessage("Apoorv","PlaceID:"+place.getId());

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                LogHelper.logMessage("Apoorv", "An error occurred: " + status);
                ExceptionMessageHandler.handleError(getApplicationContext(), status.getStatusMessage(), new Exception(), null);
            }
        });

    }

    //Added a menu for Adding Cities with + icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listmenu,menu);
            menu.findItem(R.id.toggleUnits).setTitle(SettingsPreference.getToggleLabel());
        return true;

    }

    //Settings Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_city_button:
                Boolean cityAdded = null;

                try {
                    DefaultList.addCity(selectedPlace,getApplicationContext(), Constants.ACTION_UPDATE_CITY_LIST_UI_FOR_TIMEZONE, this, this.getCityListAdapter());
                    autocompleteFragment.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            case R.id.toggleUnits:
                SettingsPreference.toggleUnits(this,cityListAdapter);
                item.setTitle(SettingsPreference.getToggleLabel());

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public SimpleItemRecyclerViewAdapter getCityListAdapter(){
        return cityListAdapter;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(this.cityListAdapter);
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DefaultList.CityItem> mValues;
        private Context context;

        public SimpleItemRecyclerViewAdapter(List<DefaultList.CityItem> items) {
            Collections.sort(items);
            mValues = items;
        }



        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.city_list_content, parent, false);
            context = parent.getContext();
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.mNameView.setText(mValues.get(position).getCityName());
            holder.mContentView.setText(mValues.get(position).getCityDescription());
            holder.mCurrentTime.setText(mValues.get(position).getTimeString());
            if(holder.mItem.isCurrent) holder.mView.setBackgroundColor(Color.parseColor("#F0F4C3"));
            else
                holder.mView.setBackgroundColor(Color.parseColor("#1F000000"));

            //Create new Hashmap for API parameters
            HashMap<String, Object> weatherAPIMap = new HashMap<String, Object>();
            weatherAPIMap.put("adapter",cityListAdapter);
            weatherAPIMap.put("temperatureView",holder.mCurrentTemperature);


            //Start request Queue
            RequestClass.startRequestQueue();
            try {
                new GetCurrentWeather().processWeatherApiCurrent(getBaseContext(), holder.mItem.latitude,holder.mItem.longitude,Constants.ACTION_UPDATE_CITY_LIST_ITEM_FOR_TEMPERATURE,null,weatherAPIMap);
            } catch (JSONException e) {
                ExceptionMessageHandler.handleError(getApplicationContext(), e.getMessage(), e, null);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(CityDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        CityDetailFragment fragment = new CityDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.city_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CityDetailActivity.class);
                        intent.putExtra(CityDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        intent.putExtra("index", position);

                        context.startActivity(intent);
                    }
                }
            });

            holder.mView.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View view) {
                    LogHelper.logMessage("Apoorv", "Item long clicked"+holder.mItem.getUniqueDelimitedString());
                    DefaultList.updateCurrentCity(holder.getAdapterPosition(),holder.mItem,getApplicationContext());
                    Toast.makeText(getApplicationContext(),"Updated Current City",Toast.LENGTH_SHORT).show();
                    cityListAdapter.notifyDataSetChanged();
                   // holder.mView.setBackgroundColor(Color.parseColor("#F0F4C3"));
                    return  true;
                }
            });


        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        //Delete with Swipe

        public void deleteCityWithSwipe (int position) {
            DefaultList.deleteCity(position,this.context);
           LogHelper.logMessage("Apoorv","Size of arrayList after mValue removal"+DefaultList.LISTPLACES.size());

            this.notifyItemRemoved(position);
        }


        public class ViewHolder extends RecyclerView.ViewHolder  {
            public final View mView;
           // public final TextView mIdView;
            public final TextView mContentView;
            public DefaultList.CityItem mItem;
            public final TextView mNameView;
            public final TextView mCurrentTemperature;
            public final TextView mCurrentTime;
            public final TextView mCurrentPreferenceUnit;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                //mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
                mNameView = (TextView) view.findViewById(R.id.cityNameText);
                mCurrentTime = (TextView) view.findViewById(R.id.currentTimeText);
                mCurrentTemperature = (TextView) view.findViewById(R.id.currentTemperature);
                mCurrentPreferenceUnit = (TextView) view.findViewById(R.id.currentPreferenceUnit);

            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }


        }
    }
}
