package izenka.hfad.com.bookstore.stores_map;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import izenka.hfad.com.bookstore.AccountListener;
import izenka.hfad.com.bookstore.R;
import izenka.hfad.com.bookstore.account.AccountActivity;
import izenka.hfad.com.bookstore.main.MainMenuActivity;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, AccountListener {

    private GoogleMap mMap;
    private StoresViewModel viewModel;


    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view) NavigationView mNavigationView;
    @BindView(R.id.pbLoadingProgress) ProgressBar pbLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);

        setToolbar();
        setNavigationDrawer();

        pbLoadingProgress.setVisibility(View.VISIBLE);
        viewModel = ViewModelProviders.of(this).get(StoresViewModel.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setNavigationDrawer() {
        mNavigationView.setCheckedItem(R.id.nav_map);
        if (getUser() != null) {
            setUserHeader();
        } else {
            setNewUserHeader();
        }
        mNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_catalogue:
                    Intent catalogueIntent = new Intent(MapsActivity.this, MainMenuActivity.class);
                    item.setChecked(true);
                    openScreen(catalogueIntent);
                    break;
                case R.id.nav_map:
                    item.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    break;
                case R.id.nav_profile:
                    Intent profileIntent = new Intent(MapsActivity.this, AccountActivity.class);
                    item.setChecked(true);
                    openScreen(profileIntent);
                    break;
                default:
                    mDrawerLayout.closeDrawers();
                    break;
            }
            return true;
        });
    }

    private void openScreen(Intent intent) {
        mDrawerLayout.closeDrawers();
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                startActivity(intent);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void setNewUserHeader() {
        View headerView = mNavigationView.getHeaderView(0);
        ImageButton ibAddNewUser = headerView.findViewById(R.id.ibAddNewUser);
        ibAddNewUser.setVisibility(View.VISIBLE);
        ibAddNewUser.setOnClickListener(btn -> {
            Intent intent = new Intent(MapsActivity.this, AccountActivity.class);
            startActivity(intent);
        });
        headerView.findViewById(R.id.ivUserPhoto).setVisibility(View.GONE);
        headerView.findViewById(R.id.rlUserEmailAndExit).setVisibility(View.GONE);
    }

    private void setUserHeader() {
        View headerView = mNavigationView.getHeaderView(0);
        headerView.findViewById(R.id.ibAddNewUser).setVisibility(View.GONE);
        headerView.findViewById(R.id.ivUserPhoto).setVisibility(View.VISIBLE);
        headerView.findViewById(R.id.rlUserEmailAndExit).setVisibility(View.VISIBLE);
        TextView tvUserEmail = headerView.findViewById(R.id.tvUserEmail);
        tvUserEmail.setText(getUserEmail());
        ImageButton btnSignOut = headerView.findViewById(R.id.ibSignOut);
        btnSignOut.setOnClickListener(btn -> {
            signOut();
            setNewUserHeader();
        });
    }

    private void setToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Карта магазинов");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        addMarkers();
        LatLng center = new LatLng(48, 12);
        mMap.setOnMarkerClickListener(marker -> {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15f));
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            Button btnBack = findViewById(R.id.btnBack);
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(btn -> dialWithStore(marker.getSnippet().replace("Тел. ", "")));
            fab.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.VISIBLE);
            btnBack.setOnClickListener(btn -> {
                btn.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 4.5f));
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            });
            return false;
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 3.5f));
    }

    private void dialWithStore(String phone) {
        Uri number = Uri.parse("tel:" + phone);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    private void addMarkers() {
        viewModel.getStoreListLiveData().observe(this, storeList -> {
            if (storeList != null) {
                for (StoreModel store : storeList) {
                    mMap.addMarker(new MarkerOptions()
                                           .position(new LatLng(store.getGeolocation().get("latitude"),
                                                                store.getGeolocation().get("longitude")))
                                           .title(store.getAddress())
                                           .snippet("Тел. " + store.getPhone()));
                }
            }
            pbLoadingProgress.setVisibility(View.GONE);
        });
    }
}
