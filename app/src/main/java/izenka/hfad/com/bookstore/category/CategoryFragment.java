package izenka.hfad.com.bookstore.category;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import izenka.hfad.com.bookstore.R;
import izenka.hfad.com.bookstore.model.db_classes.Book;

public class CategoryFragment extends Fragment {

    @BindView((R.id.rvBookList))
    RecyclerView rvBookList;
    @BindView((R.id.etCategorySearch))
    BootstrapEditText etSearchCategory;

    private CategorizedBooksViewModel viewModel;
    private boolean itemShouldBeScaled;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view,
                              @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        etSearchCategory.setOnClickListener(view1 -> {
            viewModel.onSearchInCategoryClicked();
        });

        GridLayoutManager layoutManager = null;
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                layoutManager = new GridLayoutManager(view.getContext(), 4);
                itemShouldBeScaled = true;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                layoutManager = new GridLayoutManager(view.getContext(), 2);
                itemShouldBeScaled = false;
                break;
        }
        rvBookList.setLayoutManager(layoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final CategorizedBooksAdapter adapter = new CategorizedBooksAdapter(DIFF_CALLBACK);
        adapter.shouldBeScaled(itemShouldBeScaled);
        viewModel = ViewModelProviders.of(requireActivity()).get(CategorizedBooksViewModel.class);
        adapter.setViewModel(viewModel);
        viewModel.getBookListLiveData().observe(this, adapter::submitList);
        rvBookList.setAdapter(adapter);
    }

    public static final DiffUtil.ItemCallback<Book> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Book>() {

                @Override
                public boolean areItemsTheSame(@NonNull final Book oldBook, @NonNull final Book newBook) {
                    return oldBook.getBook_id() == newBook.getBook_id();
                }

                @Override
                public boolean areContentsTheSame(@NonNull final Book oldBook, @NonNull final Book newBook) {
                    return oldBook.equals(newBook);
                }
            };
}
