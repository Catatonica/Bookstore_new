package izenka.hfad.com.bookstore.orders;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import izenka.hfad.com.bookstore.R;
import izenka.hfad.com.bookstore.order_registration.OrderRegistrationModel;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {

    private List<OrderRegistrationModel> orderList;
    private final OrdersViewModel viewModel;

    OrderListAdapter(final List<OrderRegistrationModel> orderList,
                     final OrdersViewModel viewModel) {
        this.orderList = orderList;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                              final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position) {
        final OrderRegistrationModel order = orderList.get(position);
        holder.tvDate.setText(order.getDate());
        holder.tvStatus.setText(order.getStatus());
        holder.tvPrice.setText(String.format("%.1f р.", order.getPrice()));
        holder.itemView.setOnClickListener(item -> {
            animateView(item);
            viewModel.openDetailsScreen(order);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void setList(final List<OrderRegistrationModel> orderList) {
        this.orderList = orderList;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.tvStatus)
        TextView tvStatus;

        OrderViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void animateView(final View view) {
        final int cx = view.getWidth() / 2;
        final int cy = view.getHeight() / 2;

        final float finalRadius = (float) Math.hypot(cx, cy);

        final Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        view.setVisibility(View.VISIBLE);
        anim.start();
    }
}
