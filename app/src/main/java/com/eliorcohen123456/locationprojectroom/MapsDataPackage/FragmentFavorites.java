package com.eliorcohen123456.locationprojectroom.MapsDataPackage;

import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eliorcohen123456.locationprojectroom.CustomAdapterPackage.PlacesListAdapterFavorites;
import com.eliorcohen123456.locationprojectroom.DataAppPackage.PlaceModel;
import com.eliorcohen123456.locationprojectroom.DataProviderPackage.NetworkDataProviderFavorites;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.ItemDecoration;
import com.eliorcohen123456.locationprojectroom.MainAndOtherPackage.MainActivity;
import com.eliorcohen123456.locationprojectroom.R;
import com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage.PlaceViewModelFavorites;
import com.eliorcohen123456.locationprojectroom.RoomFavoritesPackage.PlacesFavorites;
import com.eliorcohen123456.locationprojectroom.RoomSearchPackage.IPlacesDataReceived;

import java.util.ArrayList;

public class FragmentFavorites extends Fragment implements IPlacesDataReceived, NavigationView.OnNavigationItemSelectedListener {

    private PlaceViewModelFavorites mPlacesViewModelFavorites;
    private RecyclerView recyclerView;
    private View mView;
    private PlacesListAdapterFavorites mAdapterFavorites;
    private Paint p;
    private DrawerLayout drawer;
    private FragmentFavorites fragmentFavorites;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ItemDecoration itemDecoration;
    private NetworkDataProviderFavorites networkDataProviderFavorites;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_favorites_layout, container, false);

        initUI();
        drawerLayout();
        getData();
        myRecyclerView();
        enableSwipe();

        return mView;
    }

    private void initUI() {
        toolbar = mView.findViewById(R.id.toolbar);
        drawer = mView.findViewById(R.id.drawer_layout);
        navigationView = mView.findViewById(R.id.nav_view);

        recyclerView = mView.findViewById(R.id.places_list_favorites);

        fragmentFavorites = this;

        networkDataProviderFavorites = new NetworkDataProviderFavorites();
        p = new Paint();
    }

    private void drawerLayout() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mView.findViewById(R.id.myButton).setOnClickListener(v -> {
            // open right drawer
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else
                drawer.openDrawer(GravityCompat.END);
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void getData() {
        networkDataProviderFavorites.getPlacesByLocation(fragmentFavorites);
    }

    private void myRecyclerView() {
        try {
            mAdapterFavorites = new PlacesListAdapterFavorites(getContext());
            recyclerView.setAdapter(mAdapterFavorites);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            if (itemDecoration == null) {
                itemDecoration = new ItemDecoration(20);
                recyclerView.addItemDecoration(itemDecoration);
            }
        } catch (Exception e) {

        }

        mPlacesViewModelFavorites = ViewModelProviders.of(this).get(PlaceViewModelFavorites.class);
    }

    private void enableSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                PlacesFavorites mPlace = mAdapterFavorites.getPlaceAtPosition(position);

                if (direction == ItemTouchHelper.LEFT) {
                    Toast.makeText(getContext(), "Editing " + mPlace.getName(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getContext(), EditPlace.class);
                    intent.putExtra(getString(R.string.map_id), mPlace.getID());
                    intent.putExtra(getString(R.string.map_edit), mPlace);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Deleting " + mPlace.getName(), Toast.LENGTH_LONG).show();
                    mPlacesViewModelFavorites.deletePlace(mPlace);

                    Intent intentDeleteData = new Intent(getContext(), DeletePlace.class);
                    startActivity(intentDeleteData);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.deletedicon);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D80000"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.editicon);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.intentMainActivity) {
            Intent intentBackMainActivity = new Intent(getContext(), MainActivity.class);
            startActivity(intentBackMainActivity);
        } else if (id == R.id.deleteAllDataFavorites) {
            Intent intentDeleteAllData = new Intent(getContext(), DeleteAllDataFavorites.class);
            startActivity(intentDeleteAllData);
        }

        DrawerLayout drawer = mView.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onPlacesDataReceived(ArrayList<PlaceModel> results_) {
        // pass data result to mAdapterFavorites
        mPlacesViewModelFavorites.getAllPlaces().observe(this, placesFavorites -> {
            // Update the cached copy of the words in the mAdapterFavorites.
            mAdapterFavorites.setPlaces(placesFavorites);
        });
    }

}
