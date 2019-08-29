package com.eliorcohen123456.locationprojectroom.MapsDataPackage;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eliorcohen123456.locationprojectroom.CustomAdapterPackage.PlacesListAdapterSearch;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.ItemDecoration;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.NearByApplication;
import com.eliorcohen123456.locationprojectroom.R;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlacesSearch;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.IPlacesDataReceived;
import com.eliorcohen123456.locationprojectroom.DataAppPackage.PlaceModel;
import com.eliorcohen123456.locationprojectroom.DataProviderPackage.NetworkDataProviderHistory;
import com.eliorcohen123456.locationprojectroom.DataProviderPackage.NetworkDataProviderSearch;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlaceViewModelSearch;

import org.json.JSONObject;

public class FragmentSearch extends Fragment implements View.OnClickListener, IPlacesDataReceived {

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
    private NetworkDataProviderSearch dataProviderSearch;
    private NetworkDataProviderHistory dataProviderHistory;
    private SharedPreferences prefsSeek, settingsQuery, settingsType, settingsPagePass, prefsPage, prefsPre;
    private SharedPreferences.Editor editorQuery, editorType, editorPagePass, editorPage, editorPre;
    private int myRadius, myPage = 1;
    private ImageView imagePre, imageNext, imagePreFirst;
    private TextView textPage;
    private String hasPage, myStringQuery1, myStringQuery2, myStringQuery3, pageTokenPre, provider;
    private Location location;
    private LocationManager locationManager;
    private Criteria criteria;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_search_layout, container, false);

        initUI();
        initListeners();
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
        imagePre = mView.findViewById(R.id.imagePre);
        imageNext = mView.findViewById(R.id.imageNext);
        imagePreFirst = mView.findViewById(R.id.imagePreFirst);
        textPage = mView.findViewById(R.id.textPage);

        swipeRefreshLayout = mView.findViewById(R.id.swipe_containerFrag);  // ID of the SwipeRefreshLayout of FragmentSearch

        recyclerView = mView.findViewById(R.id.places_list_search);

        getClearPrefs();

        mFragmentSearch = this;

        dataProviderHistory = new NetworkDataProviderHistory();
        dataProviderSearch = new NetworkDataProviderSearch();
        mAdapterSearch = new PlacesListAdapterSearch(getContext());

        prefsSeek = PreferenceManager.getDefaultSharedPreferences(NearByApplication.getApplication());

        settingsQuery = getActivity().getSharedPreferences("mysettingsquery", Context.MODE_PRIVATE);
        editorQuery = settingsQuery.edit();

        settingsType = getActivity().getSharedPreferences("mysettingstype", Context.MODE_PRIVATE);
        editorType = settingsType.edit();

        settingsPagePass = getActivity().getSharedPreferences("mysettingspagepass", Context.MODE_PRIVATE);
        editorPagePass = settingsPagePass.edit();

        setHasOptionsMenu(true);
    }

    private void initListeners() {
        btnBank.setOnClickListener(this);
        btnBar.setOnClickListener(this);
        btnBeauty.setOnClickListener(this);
        btnBooks.setOnClickListener(this);
        btnBusStation.setOnClickListener(this);
        btnCars.setOnClickListener(this);
        btnClothing.setOnClickListener(this);
        btnDoctor.setOnClickListener(this);
        btnGasStation.setOnClickListener(this);
        btnGym.setOnClickListener(this);
        btnJewelry.setOnClickListener(this);
        btnPark.setOnClickListener(this);
        btnRestaurant.setOnClickListener(this);
        btnSchool.setOnClickListener(this);
        btnSpa.setOnClickListener(this);
        imageNext.setOnClickListener(this);
        imagePre.setOnClickListener(this);
        imagePreFirst.setOnClickListener(this);
    }

    private void getClearPrefs() {
        prefsPage = getContext().getSharedPreferences("mysettingsquery", Context.MODE_PRIVATE);
        prefsPage.edit().clear().apply();

        editorPage = prefsPage.edit();

        prefsPre = getContext().getSharedPreferences("mysettingsquerypre", Context.MODE_PRIVATE);
        prefsPre.edit().clear().apply();

        editorPre = prefsPre.edit();
    }

    private void myRecyclerView() {
        try {
            recyclerView.setAdapter(mAdapterSearch);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            if (itemDecoration == null) {
                itemDecoration = new ItemDecoration(20);
                recyclerView.addItemDecoration(itemDecoration);
            }
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

        getResumeTypeQuery();
    }

    private void getResumeTypeQuery() {
        getCheckBtnSearch("", "");
    }

    private void getDataPrefsPage(String type, String query) {
        editorPage.putString("mystringquery2", type);
        editorPage.putString("mystringquery3", query);
        editorPage.apply();
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
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    getCheckBtnSearch("", newText);

                    stopShowingProgressBar();
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
                getCheckBtnSearch("", "");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        myStringQuery1 = prefsPage.getString("mystringquery1", "");
        myStringQuery2 = prefsPage.getString("mystringquery2", "");
        myStringQuery3 = prefsPage.getString("mystringquery3", "");
        switch (v.getId()) {
            case R.id.btnBank:
                getCheckBtnSearch("bank", "");
                break;
            case R.id.btnBar:
                getCheckBtnSearch("bar|night_club", "");
                break;
            case R.id.btnBeauty:
                getCheckBtnSearch("beauty_salon|hair_care", "");
                break;
            case R.id.btnBooks:
                getCheckBtnSearch("book_store|library", "");
                break;
            case R.id.btnBusStation:
                getCheckBtnSearch("bus_station", "");
                break;
            case R.id.btnCars:
                getCheckBtnSearch("car_dealer|car_rental|car_repair|car_wash", "");
                break;
            case R.id.btnClothing:
                getCheckBtnSearch("clothing_store", "");
                break;
            case R.id.btnDoctor:
                getCheckBtnSearch("doctor", "");
                break;
            case R.id.btnGasStation:
                getCheckBtnSearch("gas_station", "");
                break;
            case R.id.btnGym:
                getCheckBtnSearch("gym", "");
                break;
            case R.id.btnJewelry:
                getCheckBtnSearch("jewelry_store", "");
                break;
            case R.id.btnPark:
                getCheckBtnSearch("park|amusement_park|parking|rv_park", "");
                break;
            case R.id.btnRestaurant:
                getCheckBtnSearch("food|restaurant|cafe|bakery", "");
                break;
            case R.id.btnSchool:
                getCheckBtnSearch("school", "");
                break;
            case R.id.btnSpa:
                getCheckBtnSearch("spa", "");
                break;
            case R.id.imageNext:
                getTypeQuery(myStringQuery2, myStringQuery3, myStringQuery1);

                myPage++;

                getAllCheckPage(myPage);
                break;
            case R.id.imagePre:
                if (myPage == 2) {
                    pageTokenPre = "";
                } else {
                    pageTokenPre = prefsPre.getString("mystringquerypre1", "");
                }

                getTypeQuery(myStringQuery2, myStringQuery3, pageTokenPre);

                myPage--;

                getAllCheckPage(myPage);
                break;
            case R.id.imagePreFirst:
                getTypeQuery(myStringQuery2, myStringQuery3, "");

                myPage = 1;

                getAllCheckPage(myPage);
                break;
        }
    }

    private void getCheckBtnSearch(String type, String query) {
        getClearPrefs();
        getTypeQuery(type, query, "");
        getDataPrefsPage(type, query);

        myPage = 1;

        getAllCheckPage(myPage);
    }

    private void getAllCheckPage(int page) {
        getPage0();
        getPage1();
        getPageText(page);
    }

    private void getPage0() {
        if (myPage <= 0) {
            myPage = 1;
        } else {
            imagePre.setVisibility(View.VISIBLE);
        }
    }

    private void getPage1() {
        if (myPage == 1) {
            imagePre.setVisibility(View.GONE);
        } else {
            imagePre.setVisibility(View.VISIBLE);
        }

        if (myPage > 2) {
            imagePreFirst.setVisibility(View.VISIBLE);
        } else {
            imagePreFirst.setVisibility(View.GONE);
        }
    }

    private void getPageText(int page) {
        textPage.setText(String.valueOf(page));
    }

    private void getTypeQuery(String type, String query, String pageToken) {
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

            editorPagePass.putString("mystringpagepass", pageToken);
            editorPagePass.apply();

            dataProviderSearch.getPlacesByLocation(query, myRadius, type, pageToken, mFragmentSearch);

            locationManager = (LocationManager) NearByApplication.getApplication().getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, true);
            if (ActivityCompat.checkSelfPermission(NearByApplication.getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.checkSelfPermission(NearByApplication.getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION);
            }// TODO: Consider calling
//    ActivityCompat#requestPermissions
// here to request the missing permissions, and then overriding
//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                          int[] grantResults)
// to handle the case where the user grants the permission. See the documentation
// for ActivityCompat#requestPermissions for more details.
            if (provider != null) {
                location = locationManager.getLastKnownLocation(provider);
                // Search maps from that URL and put them in the SQLiteHelper
                if (location != null) {
                    // Get Pages
                    StringRequest stringRequest = new StringRequest(Request.Method.GET,
                            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                                    location.getLatitude() + "," + location.getLongitude() +
                                    "&radius=" + myRadius + "&sensor=true&rankby=prominence&pagetoken="
                                    + pageToken +
                                    "&types="
                                    + type +
                                    "&keyword="
                                    + query +
                                    "&key=" +
                                    getString(R.string.api_key_search), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject mainObj = new JSONObject(response);
                                if (mainObj.has("next_page_token")) {
                                    imageNext.setVisibility(View.VISIBLE);
                                    hasPage = mainObj.getString("next_page_token");
                                } else {
                                    imageNext.setVisibility(View.GONE);
                                    hasPage = "";
                                }
                                editorPage.putString("mystringquery1", hasPage);
                                editorPage.apply();

                                if (myPage == 1) {
                                    editorPre.putString("mystringquerypre1", hasPage);
                                    editorPre.apply();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(stringRequest);
                }
            }
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
