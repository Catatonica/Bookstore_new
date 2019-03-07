package izenka.hfad.com.bookstore.basket;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import izenka.hfad.com.bookstore.R;
import mehdi.sakout.fancybuttons.FancyButton;

public class BookInBasketListFragment extends Fragment implements ButtonsClickListener, RecyclerItemTouchHelperListener {

    private BasketViewModel viewModel;
    private final List<BookIdAndCountModel> bookInBasketModelList = new ArrayList<>();
    private boolean checkAll;

    @BindView(R.id.rvBookList)
    RecyclerView rvBookList;
    @BindView(R.id.btnRegister)
    FancyButton btnRegister;
    @BindView(R.id.btnChooseEth)
    FancyButton btnChooseEth;
    @BindView(R.id.tvTotalPriceForAll)
    TextView tvTotalPrise;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_filled_basket, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view,
                              @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvBookList.setLayoutManager(manager);

        final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvBookList);

        btnChooseEth.setOnClickListener(btn -> {
            if (checkAll) {
                ((FancyButton) btn).setText(getString(R.string.checkAll));
                checkAll = false;
            } else {
                ((FancyButton) btn).setText(getString(R.string.uncheckAll));
                checkAll = true;
            }
        });
        btnRegister.setEnabled(false);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Animation alpha = new AlphaAnimation(1f, 0f);
        viewModel = ViewModelProviders.of(requireActivity()).get(BasketViewModel.class);
        final List<BookInBasketModel> bookList = new ArrayList<>();
        final BookInBasketListAdapter adapter = new BookInBasketListAdapter(viewModel, bookList);
        viewModel.setButtonsClickListener(this);
        viewModel.getBookListLiveData().observe(this, adapter::setList);
        btnChooseEth.setOnClickListener(btn -> {
            btn.startAnimation(alpha);
            adapter.checkAll();
        });
        rvBookList.setAdapter(adapter);
        btnRegister.setOnClickListener(btn -> {
            btn.startAnimation(alpha);
            viewModel.onRegisterClicked(bookInBasketModelList, Float.valueOf(tvTotalPrise.getText().toString()));
        });
    }

    @Override
    public void enableButtonRegister(final boolean enable) {
        if (btnRegister.isEnabled() != enable) {
            btnRegister.setEnabled(enable);
        }
    }

    @Override
    public void addBookInBasketModel(final BookIdAndCountModel bookIdAndCountModel) {
        bookInBasketModelList.add(bookIdAndCountModel);
    }

    @Override
    public void removeBookInBasketModel(final BookIdAndCountModel bookIdAndCountModel) {
        bookInBasketModelList.remove(bookIdAndCountModel);
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void addToTotalPrice(final float value) {
        final float oldPrice = Float.valueOf(tvTotalPrise.getText().toString());
        final float newPrice = oldPrice + value;
        tvTotalPrise.setText(String.valueOf(newPrice));
    }

    @Override
    public void subtractFromTotalPrice(final float value) {
        final float oldPrice = Float.valueOf(tvTotalPrise.getText().toString());
        final float newPrice = oldPrice - value;
        tvTotalPrise.setText(String.valueOf(newPrice));
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder,
                         final int direction,
                         final int position) {
        if (viewHolder instanceof BookInBasketListAdapter.BookViewHolder) {
            ((BookInBasketListAdapter) rvBookList.getAdapter()).removeItem(viewHolder);
        }
    }
}
