package com.eliorcohen123456.locationprojectroom.MapsDataPackage;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eliorcohen123456.locationprojectroom.CustomAdapterPackage.PlacesListAdapterSearch;
import com.eliorcohen123456.locationprojectroom.DataAppPackage.PlaceModel;
import com.eliorcohen123456.locationprojectroom.DataProviderPackage.NetworkDataProviderHistory;
import com.eliorcohen123456.locationprojectroom.DataProviderPackage.NetworkDataProviderSearch;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.ItemDecoration;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.NearByApplication;
import com.eliorcohen123456.locationprojectroom.R;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.IPlacesDataReceived;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.PlaceViewModelSearch;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import eliorcohen.com.googlemapsapi.GoogleMapsApi;

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
    private SharedPreferences prefsSeek, settingsQuery, settingsType, settingsPagePass, prefsOpen, prefsPage, prefsPre,
            prefsQuery, prefsType, prefsPageMe, prefsPageMy;
    private SharedPreferences.Editor editorQuery, editorType, editorPagePass, editorPage, editorPre, editorPageMe, editorPageMy;
    private int myRadius, myPage, myPageMy;
    private ImageView imagePre, imageNext, imagePreFirst;
    private TextView textPage;
    private String hasPage, pageTokenPre, provider, myType, myTypeSearch, myOpen, myStringPage, myStringQuery, myStringPageMe, myPageMeString, myQuery;
    private Location location;
    private LocationManager locationManager;
    private Criteria criteria;
    private GoogleMapsApi googleMapsApi;

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

    @Override
    public void onResume() {
        super.onResume();

        myType = prefsType.getString("mystringtypesearch", "");
        myStringQuery = prefsQuery.getString("mystringquerysearch", "");
        getCheckBtnSearch(myPage, myType, myStringQuery);
    }

    private void initUI() {
        recyclerView = mView.findViewById(R.id.places_list_search);

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

        swipeRefreshLayout = mView.findViewById(R.id.swipe_containerFrag);

        initPrefs();

        mFragmentSearch = this;

        dataProviderHistory = new NetworkDataProviderHistory();
        dataProviderSearch = new NetworkDataProviderSearch();
        mAdapterSearch = new PlacesListAdapterSearch(getContext());
        googleMapsApi = new GoogleMapsApi();

        myPageMy = prefsPageMy.getInt("mystringpagemy", 1);
        if (myPageMy == 1) {
            myPage = 1;
        } else if (myPageMy == 2) {
            myPage = 2;
        } else {
            myPage = 3;
        }

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

    private void initPrefs() {
        prefsSeek = PreferenceManager.getDefaultSharedPreferences(NearByApplication.getApplication());
        prefsOpen = PreferenceManager.getDefaultSharedPreferences(getContext());

        settingsQuery = getActivity().getSharedPreferences("mysettingsquery", Context.MODE_PRIVATE);
        settingsType = getActivity().getSharedPreferences("mysettingstype", Context.MODE_PRIVATE);
        settingsPagePass = getActivity().getSharedPreferences("mysettingspagepass", Context.MODE_PRIVATE);
        prefsQuery = Objects.requireNonNull(getContext()).getSharedPreferences("mysettingsquery", Context.MODE_PRIVATE);
        prefsPage = Objects.requireNonNull(getContext()).getSharedPreferences("mysettingspage", Context.MODE_PRIVATE);
        prefsPre = getContext().getSharedPreferences("mysettingspre", Context.MODE_PRIVATE);
        prefsType = getContext().getSharedPreferences("mysettingstype", Context.MODE_PRIVATE);
        prefsPageMe = getContext().getSharedPreferences("mysettingspageme", Context.MODE_PRIVATE);
        prefsPageMy = getContext().getSharedPreferences("mysettingspagemy", Context.MODE_PRIVATE);

        editorQuery = settingsQuery.edit();
        editorType = settingsType.edit();
        editorPagePass = settingsPagePass.edit();
        editorQuery = prefsQuery.edit();
        editorPage = prefsPage.edit();
        editorPre = prefsPre.edit();
        editorType = prefsType.edit();
        editorPageMe = prefsPageMe.edit();
        editorPageMy = prefsPageMy.edit();
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
        swipeRefreshLayout.setOnRefreshListener(() -> {
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
        SearchView searchView = (SearchView) menuItem.getActionView();
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
                    getConfigureSearchNearby(newText);

                    stopShowingProgressDialog();
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
                getConfigureSearchNearby("");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        myStringQuery = prefsQuery.getString("mystringquerysearch", "");
        myType = prefsType.getString("mystringtypesearch", "");
        myPageMy = prefsPageMy.getInt("mystringpagemy", 1);
        switch (v.getId()) {
            case R.id.btnBank:
                getConfigureBtn("bank");
                break;
            case R.id.btnBar:
                getConfigureBtn("bar|night_club");
                break;
            case R.id.btnBeauty:
                getConfigureBtn("beauty_salon|hair_care");
                break;
            case R.id.btnBooks:
                getConfigureBtn("book_store|library");
                break;
            case R.id.btnBusStation:
                getConfigureBtn("bus_station");
                break;
            case R.id.btnCars:
                getConfigureBtn("car_dealer|car_rental|car_repair|car_wash");
                break;
            case R.id.btnClothing:
                getConfigureBtn("clothing_store");
                break;
            case R.id.btnDoctor:
                getConfigureBtn("doctor");
                break;
            case R.id.btnGasStation:
                getConfigureBtn("gas_station");
                break;
            case R.id.btnGym:
                getConfigureBtn("gym");
                break;
            case R.id.btnJewelry:
                getConfigureBtn("jewelry_store");
                break;
            case R.id.btnPark:
                getConfigureBtn("park|amusement_park|parking|rv_park");
                break;
            case R.id.btnRestaurant:
                getConfigureBtn("food|restaurant|cafe|bakery");
                break;
            case R.id.btnSchool:
                getConfigureBtn("school");
                break;
            case R.id.btnSpa:
                getConfigureBtn("spa");
                break;
            case R.id.imageNext:
                myStringPage = prefsPage.getString("myStringPage", "");

                getTypeQuery(myStringPage, myType, myStringQuery);

                myPage++;

                getAllCheckPage(myPage);

                myPageMeString = myStringPage;
                break;
            case R.id.imagePre:
                if (myPage == 2 || myPageMy == 2) {
                    pageTokenPre = "";
                } else {
                    pageTokenPre = prefsPre.getString("mystringquerypre", "");
                }

                getTypeQuery(pageTokenPre, myType, myStringQuery);

                myPage--;

                getAllCheckPage(myPage);

                myPageMeString = pageTokenPre;
                break;
            case R.id.imagePreFirst:
                getTypeQuery("", myType, myStringQuery);

                myPage = 1;

                getAllCheckPage(myPage);

                myPageMeString = "";
                break;
        }
        editorType.putString("mystringtypesearch", myTypeSearch).apply();
        editorQuery.putString("mystringquerysearch", myQuery).apply();
        editorPageMe.putString("mystringpageme", myPageMeString).apply();
        editorPageMy.putInt("mystringpagemy", myPage).apply();
    }

    private void getConfigureSearchNearby(String query) {
        prefsPageMe.edit().clear().apply();
        prefsPageMy.edit().clear().apply();

        myPage = 1;
        getCheckBtnSearch(myPage, "", query);

        myPageMeString = "";

        editorType.putString("mystringtypesearch", "").apply();
        editorQuery.putString("mystringquerysearch", query).apply();
        editorPageMe.putString("mystringpageme", myPageMeString).apply();
        editorPageMy.putInt("mystringpagemy", myPage).apply();
    }

    private void getConfigureBtn(String type) {
        prefsPageMe.edit().clear().apply();
        prefsPageMy.edit().clear().apply();

        myPage = 1;
        getCheckBtnSearch(myPage, type, "");

        myTypeSearch = type;
        myPageMeString = "";
        myQuery = "";
    }

    private void getCheckBtnSearch(int page, String type, String query) {
        myStringPageMe = prefsPageMe.getString("mystringpageme", "");
        getTypeQuery(myStringPageMe, type, query);
        getAllCheckPage(page);
    }

    private void getAllCheckPage(int page) {
        getPage0(page);
        getPage1(page);
        getPageText();
    }

    private void getPage0(int page) {
        myPageMy = prefsPageMy.getInt("mystringpagemy", 1);
        if (page <= 0 || myPageMy <= 0) {
            myPage = 1;
        } else {
            imagePre.setVisibility(View.VISIBLE);
        }
        editorPageMy.putInt("mystringpagemy", myPage).apply();
    }

    private void getPage1(int page) {
        myPageMy = prefsPageMy.getInt("mystringpagemy", 1);
        if (page == 1 || myPageMy == 1) {
            imagePre.setVisibility(View.GONE);
        } else {
            imagePre.setVisibility(View.VISIBLE);
        }

        if (page > 2 || myPageMy > 2) {
            imagePreFirst.setVisibility(View.VISIBLE);
        } else {
            imagePreFirst.setVisibility(View.GONE);
        }
    }

    private void getPageText() {
        myPageMy = prefsPageMy.getInt("mystringpagemy", 1);
        textPage.setText(String.valueOf(myPageMy));
    }

    private void getTypeQuery(String pageToken, String type, String query) {
        if (!isConnected(Objects.requireNonNull(getContext()))) {
            dataProviderHistory.getPlacesByLocation(mFragmentSearch);
            buildDialog(getContext()).show();
        } else {
            if (query.equals("")) {
                myRadius = prefsSeek.getInt("seek", 5000);
            } else {
                myRadius = 50000;
            }

            myOpen = prefsOpen.getString("open", "");

            editorQuery.putString("mystringquery", query).apply();
            editorType.putString("mystringtype", type).apply();
            editorPagePass.putString("mystringpagepass", pageToken).apply();

            dataProviderSearch.getPlacesByLocation(myRadius, pageToken, myOpen, type, query, mFragmentSearch);

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
                            googleMapsApi.getStringGoogleMapsApi(location.getLatitude(), location.getLongitude(), myRadius, pageToken, myOpen, type, query, getString(R.string.api_key_search)), response -> {
                        try {
                            JSONObject mainObj = new JSONObject(response);
                            if (mainObj.has("next_page_token")) {
                                imageNext.setVisibility(View.VISIBLE);
                                hasPage = mainObj.getString("next_page_token");
                            } else {
                                imageNext.setVisibility(View.GONE);
                                hasPage = "";
                            }
                            editorPage.putString("myStringPage", hasPage).apply();

                            myPageMy = prefsPageMy.getInt("mystringpagemy", 1);
                            if (myPageMy == 1) {
                                editorPre.putString("mystringquerypre", hasPage).apply();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, error -> {

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
        builder.setMessage("You need to have Mobile Data or Wi-Fi to access this. Press OK to Resume");

        builder.setPositiveButton("OK", (dialog, which) -> {

        });
        return builder;
    }

    @Override
    public void onPlacesDataReceived(ArrayList<PlaceModel> results_) {
        // pass data result to mAdapterSearch
        mPlacesViewModelSearch.getAllPlaces().observe(this, placesSearches -> mAdapterSearch.setPlaces(placesSearches));
    }

    // stopShowingProgressDialog
    public static void stopShowingProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    // startShowingProgressDialog
    public static void startShowingProgressDialog() {
        mProgressDialog = ProgressDialog.show(mFragmentSearch.getActivity(), "Loading...",
                "Please wait...", true);
        mProgressDialog.show();
    }

}
