package izenka.hfad.com.bookstore.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import izenka.hfad.com.bookstore.AccountListener;
import izenka.hfad.com.bookstore.R;
import izenka.hfad.com.bookstore.account.AccountActivity;
import izenka.hfad.com.bookstore.basket.BasketActivity;
import izenka.hfad.com.bookstore.category.CategoryActivity;
import izenka.hfad.com.bookstore.orders.OrdersActivity;
import izenka.hfad.com.bookstore.qr_code.QRCodeActivity;
import izenka.hfad.com.bookstore.search.SearchActivity;
import izenka.hfad.com.bookstore.stores_map.MapsActivity;

public class MainMenuActivity extends AppCompatActivity implements MainMenuNavigator, AccountListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_basket:
                openScreen(BasketActivity.class);
                break;

            case R.id.action_orders:
                openScreen(OrdersActivity.class);
                break;

            case R.id.action_search:
                openScreen(SearchActivity.class);
                break;
            case R.id.action_qrcode:
                openScreen(QRCodeActivity.class);
                break;

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCategoryClicked(final String categoryID) {
        final Intent intent = new Intent();
        intent.putExtra("categoryID", categoryID);
        intent.setClass(this, CategoryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSearchClicked() {
        final Intent intent = new Intent();
        intent.setClass(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        navigationView.setCheckedItem(R.id.nav_catalogue);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        ButterKnife.bind(this);

        final MainMenuViewModel viewModel = ViewModelProviders.of(this).get(MainMenuViewModel.class);
        viewModel.setNavigator(this);

        findViewById(R.id.tvRunningLine).setSelected(true);
        setToolbar();
        setNavigationDrawer();
        setFragment(new MainMenuFragment());
    }

    private void setNavigationDrawer() {
        navigationView.setCheckedItem(R.id.nav_catalogue);
        if (getUser() != null) {
            setUserHeader();
        }
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_catalogue:
                    item.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    break;
                case R.id.nav_map:
                    final Intent mapIntent = new Intent(MainMenuActivity.this, MapsActivity.class);
                    item.setChecked(true);
                    openScreen(mapIntent);
                    break;
                case R.id.nav_profile:
                    final Intent profileIntent = new Intent(MainMenuActivity.this, AccountActivity.class);
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

    private void openScreen(final Intent intent) {
        mDrawerLayout.closeDrawers();
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(@NonNull final View drawerView, final float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull final View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull final View drawerView) {
                startActivity(intent);
            }

            @Override
            public void onDrawerStateChanged(final int newState) {

            }
        });
    }

    private void setToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.bookstore);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }
    }

    private void setFragment(final Fragment fragment) {
        final Fragment mainMenuFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (mainMenuFragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
    }

    private void setNewUserHeader() {
        final View headerView = navigationView.getHeaderView(0);
        final ImageButton ibAddNewUser = headerView.findViewById(R.id.ibAddNewUser);
        ibAddNewUser.setVisibility(View.VISIBLE);
        ibAddNewUser.setOnClickListener(btn -> {
            final Intent intent = new Intent(MainMenuActivity.this, AccountActivity.class);
            startActivity(intent);
        });
        headerView.findViewById(R.id.ivUserPhoto).setVisibility(View.GONE);
        headerView.findViewById(R.id.rlUserEmailAndExit).setVisibility(View.GONE);
    }

    private void setUserHeader() {
        final View headerView = navigationView.getHeaderView(0);
        headerView.findViewById(R.id.ibAddNewUser).setVisibility(View.GONE);
        headerView.findViewById(R.id.ivUserPhoto).setVisibility(View.VISIBLE);
        headerView.findViewById(R.id.rlUserEmailAndExit).setVisibility(View.VISIBLE);
        final TextView tvUserEmail = headerView.findViewById(R.id.tvUserEmail);
        tvUserEmail.setText(getUserEmail());
        final ImageButton btnSignOut = headerView.findViewById(R.id.ibSignOut);
        btnSignOut.setOnClickListener(btn -> {
            signOut();
            setNewUserHeader();
        });
    }

    private void openScreen(final Class activityClass) {
        final Intent intent = new Intent();
        intent.setClass(this, activityClass);
        startActivity(intent);
    }
}