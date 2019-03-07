package izenka.hfad.com.bookstore.orders;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import izenka.hfad.com.bookstore.R;
import mehdi.sakout.fancybuttons.FancyButton;

public class EmptyOrdersFragment extends Fragment {

    private FancyButton btnReturnFromOrders;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_empty_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view,
                              @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnReturnFromOrders = view.findViewById(R.id.btnReturnFromOrders);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final OrdersViewModel viewModel = ViewModelProviders.of(requireActivity()).get(OrdersViewModel.class);
        btnReturnFromOrders.setOnClickListener(btn->viewModel.onReturnClicked());
    }
}
