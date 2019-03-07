package izenka.hfad.com.bookstore.orders;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import izenka.hfad.com.bookstore.R;
import izenka.hfad.com.bookstore.order_registration.OrderRegistrationModel;

public class OrdersActivity extends AppCompatActivity implements OrdersNavigator {

    private ProgressBar pbLoadingProgress;

    @Override
    public void onReturnClicked() {
        onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        final Fragment detailsFragmentPortrait = getSupportFragmentManager().findFragmentByTag("details");
        final Fragment detailsFragmentLand = getSupportFragmentManager().findFragmentById(R.id.flOrder);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && detailsFragmentPortrait != null) {
            getSupportActionBar().setTitle(R.string.orders);
            if (detailsFragmentLand == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(detailsFragmentPortrait)
                        .replace(R.id.flBase, new OrderListFragment(), "list")
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(detailsFragmentPortrait)
                        .remove(detailsFragmentLand)
                        .replace(R.id.flBase, new OrderListFragment(), "list")
                        .commit();
            }
        } else {
            onBackPressed();
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void openDetailsScreen(OrderRegistrationModel order) {
        final OrderDetailsFragment fragment = new OrderDetailsFragment();
//        fragment.setOrder(order);
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                findViewById(R.id.flBase).setVisibility(View.GONE);
                findViewById(R.id.flOrderList).setVisibility(View.VISIBLE);
                findViewById(R.id.flOrder).setVisibility(View.VISIBLE);
                setFragment(R.id.flOrder, fragment, "details");
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                findViewById(R.id.flBase).setVisibility(View.VISIBLE);
                setFragment(R.id.flBase, fragment, "details");
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        pbLoadingProgress = findViewById(R.id.pbLoadingProgress);
        setToolbar();

        final OrdersViewModel viewModel = ViewModelProviders.of(this).get(OrdersViewModel.class);
        viewModel.setNavigator(this);

        if (viewModel.getOrderListLiveData().getValue() == null) {
            pbLoadingProgress.setVisibility(View.VISIBLE);
        }

        viewModel.getOrderListLiveData().observe(this, orderList -> {
            pbLoadingProgress.setVisibility(View.GONE);
            if (orderList == null || orderList.isEmpty()) {
                setEmptyFragment();
            } else {
                setOrderListFragment();
            }
        });
    }

    private void setToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.orders);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    private void setEmptyFragment() {
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                findViewById(R.id.flBase).setVisibility(View.VISIBLE);
                findViewById(R.id.flOrderList).setVisibility(View.GONE);
                findViewById(R.id.flOrder).setVisibility(View.GONE);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                findViewById(R.id.flBase).setVisibility(View.VISIBLE);
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flBase, new EmptyOrdersFragment())
                .commit();
    }

    private void setOrderListFragment() {
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                findViewById(R.id.flBase).setVisibility(View.GONE);
                findViewById(R.id.flOrderList).setVisibility(View.VISIBLE);
                findViewById(R.id.flOrder).setVisibility(View.VISIBLE);
                setFragment(R.id.flOrderList, new OrderListFragment(), "list");
                final Fragment orderDetailsFragment = getSupportFragmentManager().findFragmentByTag("details");
                if (orderDetailsFragment != null) {
                    setFragment(R.id.flOrder, new OrderDetailsFragment(), "details");
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                findViewById(R.id.flBase).setVisibility(View.VISIBLE);
                final Fragment orderDetailsFragment2 = getSupportFragmentManager().findFragmentByTag("details");
                if (orderDetailsFragment2 == null) {
                    setFragment(R.id.flBase, new OrderListFragment(), "list");
                } else {
                    getSupportActionBar().setTitle(R.string.order);
                    setFragment(R.id.flBase, new OrderDetailsFragment(), "details");
                }
                break;
        }
    }

    private void setFragment(final int contentFrameID,
                             final Fragment fragment,
                             final String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(contentFrameID, fragment, tag)
                .commit();
    }
}