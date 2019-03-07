package izenka.hfad.com.bookstore.basket;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import izenka.hfad.com.bookstore.R;
import izenka.hfad.com.bookstore.order_registration.OrderRegistrationActivity;

public class BasketActivity extends AppCompatActivity implements BasketNavigator {

    @BindView(R.id.pbLoadingProgress)
    ProgressBar pbLoadingProgress;
    BasketViewModel viewModel;

    @Override
    public void setFragment(final Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    @Override
    public void onRegisterClicked(final List<BookIdAndCountModel> BookIdAndCountModelList, float totalPrice) {
        final Intent intent = new Intent(this, OrderRegistrationActivity.class);
        intent.putParcelableArrayListExtra("bookIDsAndCount", (ArrayList<? extends Parcelable>) BookIdAndCountModelList);
        intent.putExtra("totalPrice", totalPrice);
        intent.putExtra("bookID", getIntent().getStringExtra("bookID"));
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        viewModel.setBookInBasketLiveData();
        viewModel.getBookListLiveData().observe(this, bookList -> {
            if (bookList == null || bookList.isEmpty()) {
                setFragment(new EmptyBasketFragment());
            } else {
                setFragment(new BookInBasketListFragment());
            }
        });
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        ButterKnife.bind(this);

        setToolbar();

        viewModel = ViewModelProviders.of(this).get(BasketViewModel.class);
        viewModel.setNavigator(this);

        viewModel.getBookListLiveData().observe(this, bookList -> {
            pbLoadingProgress.setVisibility(View.GONE);
            if (bookList == null || bookList.isEmpty()) {
                setInitialFragment(new EmptyBasketFragment());
            } else {
                setInitialFragment(new BookInBasketListFragment());
            }
        });
    }

    private void setToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.basket);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    private void setInitialFragment(final Fragment fragment) {
        final Fragment basketFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (basketFragment == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
    }
}