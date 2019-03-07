package izenka.hfad.com.bookstore.search;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;

import java.util.ArrayList;

import izenka.hfad.com.bookstore.R;
import izenka.hfad.com.bookstore.book.BookActivity;

public class SearchActivity extends AppCompatActivity implements SearchNavigator {

    private int categoryID = -1;
    private SearchViewModel viewModel;
    private SearchedBookListAdapter adapter;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBookClicked(final String bookID) {
        final Intent intent = new Intent();
        intent.putExtra("bookID", bookID);
        intent.setClass(this, BookActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setIconified(false);
        searchView.requestFocus();
        //TODO: create ContentProvider for suggestions
        searchView.setQueryHint("Название книги / автор");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                onQueryTextChanged(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        setToolbar();

        categoryID = getIntent().getIntExtra("categoryID", -1);

        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        viewModel.setNavigator(this);

        final RecyclerView rvBookList = findViewById(R.id.rvBookList);
        final GridLayoutManager manager = new GridLayoutManager(this, 2);
        rvBookList.setLayoutManager(manager);
        adapter = (new SearchedBookListAdapter(new ArrayList<>(), viewModel, false));
        rvBookList.setAdapter(adapter);
    }

    private void setToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.search);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    private void onQueryTextChanged(final String newText) {
        if (newText.isEmpty()) {
            final int size = adapter.getBookList().size();
            adapter.getBookList().clear();
            adapter.notifyItemRangeRemoved(0, size);
        } else {
            if (categoryID == -1) {
                viewModel.getBookListLiveData(newText).observe(SearchActivity.this, bookList -> {
                    adapter.setBookList(bookList);
                    adapter.notifyDataSetChanged();
                });
            } else {
                viewModel.getBookListLiveData(String.valueOf(categoryID), newText).observe(SearchActivity.this, bookList -> {
                    adapter.setBookList(bookList);
                    adapter.notifyDataSetChanged();
                });
            }
        }
    }
}
