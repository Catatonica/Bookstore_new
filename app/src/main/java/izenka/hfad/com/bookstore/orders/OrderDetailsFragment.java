package izenka.hfad.com.bookstore.orders;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import izenka.hfad.com.bookstore.R;
import izenka.hfad.com.bookstore.order_registration.OrderRegistrationModel;

public class OrderDetailsFragment extends Fragment {

    private RecyclerView rvBookList;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view,
                              @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvBookList = view.findViewById(R.id.rvBookList);
        rvBookList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final OrdersViewModel viewModel = ViewModelProviders.of(requireActivity()).get(OrdersViewModel.class);
        final BookListAdapter adapter = new BookListAdapter(new ArrayList<>());
        viewModel.getBookAndCountListLiveData(/*order*/).observe(this, adapter::setBookInOrderList);
        rvBookList.setAdapter(adapter);
    }
}
