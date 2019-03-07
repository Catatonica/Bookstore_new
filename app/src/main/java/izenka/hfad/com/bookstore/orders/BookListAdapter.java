package izenka.hfad.com.bookstore.orders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import izenka.hfad.com.bookstore.R;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookAndCountViewHolder> {

    private List<BookInOrderModel> bookList;

    BookListAdapter(final List<BookInOrderModel> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookAndCountViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                                     final int viewType) {
        final View bookView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_in_order, parent, false);
        return new BookAndCountViewHolder(bookView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookAndCountViewHolder holder,
                                 final int position) {
        final BookInOrderModel bookInOrder = bookList.get(position);
        if (bookInOrder != null) {
            holder.title.setText(String.format("\"%s\"", bookInOrder.getBook().getTitle()));
            holder.priceForOne.setText(bookInOrder.getBook().getPrice());
            holder.count.setText(String.valueOf(bookInOrder.getCount()));
        }
    }

    @Override
    public long getItemId(final int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    void setBookInOrderList(final List<BookInOrderModel> bookList) {
        this.bookList = bookList;
        notifyDataSetChanged();
    }

    static class BookAndCountViewHolder extends RecyclerView.ViewHolder {

        @BindView((R.id.tvTitle))
        TextView title;
        @BindView((R.id.tvPriceForOne))
        TextView priceForOne;
        @BindView((R.id.tvCount))
        TextView count;

        BookAndCountViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
