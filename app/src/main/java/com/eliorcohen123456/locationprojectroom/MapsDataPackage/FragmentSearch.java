package com.eliorcohen123456.locationprojectroom.MapsDataPackage;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.eliorcohen123456.locationprojectroom.CustomAdapterPackage.PlacesListAdapterSearch;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.ItemDecoration;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.NearByApplication;
import com.eliorcohen123456.locationprojectroom.R;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlacesSearch;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.IPlacesDataReceived;
import com.eliorcohen123456.locationprojectroom.DataAppPackage.PlaceModel;
import com.eliorcohen123456.locationprojectroom.DataProviderPackage.NetWorkDataProviderHistory;
import com.eliorcohen123456.locationprojectroom.DataProviderPackage.NetWorkDataProviderSearch;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlaceViewModelSearch;

public class FragmentSearch extends Fragment implements IPlacesDataReceived {

    private PlaceViewModelSearch mPlacesViewModelSearch;
    private RecyclerView recyclerView;
    private View mView;
    private static ProgressDialog mProgressDialog;
    private static FragmentSearch mFragmentSearch;
    private PlacesListAdapterSearch mAdapterSearch;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemDecoration itemDecoration;
    private Button btnBank, btnBar, btnBeauty, btnBooks, btnBusStation, btnCars, btnClothing, btnDoctor, btnGasStation,
            btnGym, btnJewelry, btnPark, btnRestaurant, btnSchool, btnSpa;
    private NetWorkDataProviderSearch dataProviderSearch;
    private NetWorkDataProviderHistory dataProviderHistory;
    private SharedPreferences prefsSeek, settingsQuery, settingsType;
    private SharedPreferences.Editor editorQuery, editorType;
    private int myRadius;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search_layout, container, false);

        initUI();
        myRecyclerView();
        refreshUI();

        return mView;
    }

    private void initUI() {
        btnBank = mView.findViewById(R.id.btnBank);
        btnBar = mView.findViewById(R.id.btnBar);
        btnBeauty = mView.findViewById(R.id.btnBeauty);
        btnBooks = mView.findViewById(R.id.btnBooks);
        btnBusStation = mView.findViewById(R.id.btnBusStation);
        btnCars = mView.findViewById(R.id.btnCars);
        btnClothing = mView.findViewById(R.id.btnClothing);
        btnDoctor = mView.findViewById(R.id.btnDoctor);
        btnGasStation = mView.findViewById(R.id.btnGasStation);
        btnGym = mView.findViewById(R.id.btnGym);
        btnJewelry = mView.findViewById(R.id.btnJewelry);
        btnPark = mView.findViewById(R.id.btnPark);
        btnRestaurant = mView.findViewById(R.id.btnRestaurant);
        btnSchool = mView.findViewById(R.id.btnSchool);
        btnSpa = mView.findViewById(R.id.btnSpa);

        swipeRefreshLayout = mView.findViewById(R.id.swipe_containerFrag);  // ID of the SwipeRefreshLayout of FragmentSearch

        recyclerView = mView.findViewById(R.id.places_list_search);

        mFragmentSearch = this;

        dataProviderHistory = new NetWorkDataProviderHistory();
        dataProviderSearch = new NetWorkDataProviderSearch();

        prefsSeek = PreferenceManager.getDefaultSharedPreferences(NearByApplication.getApplication());

        settingsQuery = getActivity().getSharedPreferences("mysettingsquery", Context.MODE_PRIVATE);
        editorQuery = settingsQuery.edit();

        settingsType = getActivity().getSharedPreferences("mysettingstype", Context.MODE_PRIVATE);
        editorType = settingsType.edit();

        setHasOptionsMenu(true);
    }

    private void myRecyclerView() {
        try {
            mAdapterSearch = new PlacesListAdapterSearch(getContext());
            recyclerView.setAdapter(mAdapterSearch);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            itemDecoration = new ItemDecoration(20);
            recyclerView.addItemDecoration(itemDecoration);
        } catch (Exception e) {

        }

        mPlacesViewModelSearch = ViewModelProviders.of(this).get(PlaceViewModelSearch.class);
    }

    private void refreshUI() {
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorOrange));  // Colors of the SwipeRefreshLayout of FragmentSearch
        // Refresh the MapDBHelper of app in ListView of MainActivity
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Vibration for 0.1 second
                Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(100);
                }

                getActivity().finish();
                startActivity(getActivity().getIntent());  // Refresh activity

                Toast toast = Toast.makeText(getContext(), "The list are refreshed!", Toast.LENGTH_SHORT);
                View view = toast.getView();
                view.getBackground().setColorFilter(getResources().getColor(R.color.colorLightBlue), PorterDuff.Mode.SRC_IN);
                TextView text = view.findViewById(android.R.id.message);
                text.setTextColor(getResources().getColor(R.color.colorDarkBrown));
                toast.show();  // Toast

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getTypeSearch();
    }

    private void getTypeSearch() {
        getTypeQuery("", "");

        btnBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("bank", "");
            }
        });

        btnBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("bar|night_club", "");
            }
        });

        btnBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("beauty_salon|hair_care", "");
            }
        });

        btnBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("book_store|library", "");
            }
        });

        btnBusStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("bus_station", "");
            }
        });

        btnCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("car_dealer|car_rental|car_repair|car_wash", "");
            }
        });

        btnClothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("clothing_store", "");
            }
        });

        btnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("doctor", "");
            }
        });

        btnGasStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("gas_station", "");
            }
        });

        btnGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("gym", "");
            }
        });

        btnJewelry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("jewelry_store", "");
            }
        });

        btnPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("park|amusement_park|parking|rv_park", "");
            }
        });

        btnRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("food|restaurant|cafe|bakery", "");
            }
        });

        btnSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("school", "");
            }
        });

        btnSpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTypeQuery("spa", "");
            }
        });
    }

    // Sets off the menu of activity_menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_menu, menu);

        // SearchView of FragmentSearch
        final MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        // Change colors of the searchView upper panel
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Set styles for expanded state here
                if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
                }
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Set styles for collapsed state here
                if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                }
                return true;
            }
        });

        // Continued of SearchView of FragmentSearch
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menuItem.getActionView();
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo((getActivity()).getComponentName()));
            searchView.setQueryHint(Html.fromHtml("<font color = #FFEA54>" + getResources().getString(R.string.hint) + "</font>"));
            searchView.setSubmitButtonEnabled(true);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getTypeQuery("", query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }
    }

    // Options in the activity_menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                break;
            case R.id.nearByMe:
                getTypeQuery("", "");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getTypeQuery(String type, String query) {
        if (!isConnected(getContext())) {
            dataProviderHistory.getPlacesByLocation(mFragmentSearch);
            buildDialog(getContext()).show();
        } else {
            if (query.equals("")) {
                myRadius = prefsSeek.getInt("seek", 5000);
            } else {
                myRadius = 50000;
            }

            editorQuery.putString("mystringquery", query);
            editorQuery.apply();

            editorType.putString("mystringtype", type);
            editorType.apply();

            dataProviderSearch.getPlacesByLocation(query, myRadius, type, mFragmentSearch);
        }
    }

    // Check network
    private boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }

    private AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Resume");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder;
    }

    @Override
    public void onPlacesDataReceived(ArrayList<PlaceModel> results_) {
        // pass data result to mAdapterSearch
        mPlacesViewModelSearch.getAllPlaces().observe(this, new Observer<List<PlacesSearch>>() {
            @Override
            public void onChanged(@Nullable final List<PlacesSearch> placesSearches) {
                // Update the cached copy of the words in the mAdapterSearch.
                mAdapterSearch.setPlaces(placesSearches);
            }
        });
    }

    // stopShowingProgressBar
    public static void stopShowingProgressBar() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    // startShowingProgressBar
    public static void startShowingProgressBar() {
        mProgressDialog = ProgressDialog.show(mFragmentSearch.getActivity(), "Loading...",
                "Please wait...", true);
        mProgressDialog.show();
    }

}
