package izenka.hfad.com.bookstore.basket;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import izenka.hfad.com.bookstore.DatabaseCallback;
import izenka.hfad.com.bookstore.DatabaseSingleton;
import izenka.hfad.com.bookstore.R;
import izenka.hfad.com.bookstore.model.db_classes.Author;
import izenka.hfad.com.bookstore.model.db_classes.Book;

public class BookInBasketListAdapter extends RecyclerView.Adapter<BookInBasketListAdapter.BookViewHolder> {

    private final BasketViewModel viewModel;
    private List<BookInBasketModel> bookInBasketModelList;
    private boolean isChecked;
    private int checkedNum;

    BookInBasketListAdapter(final BasketViewModel viewModel,
                            final List<BookInBasketModel> bookInBasketModelList) {
        this.viewModel = viewModel;
        this.bookInBasketModelList = bookInBasketModelList;
    }

    @Override
    public int getItemCount() {
        if (bookInBasketModelList != null) {
            return bookInBasketModelList.size();
        }
        return 0;
    }

    public void setList(final List<BookInBasketModel> bookInBasketModelList) {
        this.bookInBasketModelList = bookInBasketModelList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                             final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_in_basket, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookViewHolder holder,
                                 final int position) {
        final BookInBasketModel bookInBasket = bookInBasketModelList.get(position);
        final Book book = bookInBasket.book;
        holder.tvTitle.setText(book.getTitle());
        holder.npBooksCount.setMinValue(1);
        holder.npBooksCount.setMaxValue(book.getCount());
        holder.npBooksCount.setValue(1);
        holder.tvPriseForOne.setText(book.getPrice());
        holder.checkBox.setChecked(isChecked);
        setAuthors(book, holder);
        setImage(book, holder);

        final BookIdAndCountModel bookIdAndCountModel = new BookIdAndCountModel(book.getBook_id(), holder.npBooksCount.getValue());

        holder.npBooksCount.setOnValueChangedListener((numberPicker, i, i1) -> {
            bookIdAndCountModel.count = i1;
            onBooksCountChanged(book, i, i1, holder);
        });

        holder.checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            onCheckBoxChangeState(b, bookIdAndCountModel, book, holder);
        });
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {

        @BindView((R.id.checkBox))
        CheckBox checkBox;
        @BindView((R.id.tvTitle))
        TextView tvTitle;
        @BindView((R.id.tvAuthor))
        TextView tvAuthor;
        @BindView((R.id.tvPriseForOne))
        TextView tvPriseForOne;
        @BindView((R.id.tvPriseForSeveral))
        TextView tvPriseForSeveral;
        @BindView((R.id.npBooksCount))
        NumberPicker npBooksCount;
        @BindView((R.id.ivBook))
        ImageView ivBook;
        @BindView((R.id.rlBackground))
        RelativeLayout rlBackground;
        @BindView((R.id.rlForeground))
        RelativeLayout rlForeground;

        BookViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    void checkAll() {
        isChecked = !isChecked;
        notifyDataSetChanged();
    }

    void removeItem(final RecyclerView.ViewHolder viewHolder) {
        final int position = viewHolder.getAdapterPosition();
        viewModel.deleteBookFromBasket(bookInBasketModelList.get(position).book.getBook_id());
        bookInBasketModelList.remove(position);
        ((CheckBox) viewHolder.itemView.findViewById(R.id.checkBox)).setChecked(false);
        if (bookInBasketModelList.isEmpty()) {
            viewModel.setEmptyBasket();
        }
        notifyItemRemoved(position);
    }

    private void setImage(final Book book, final BookViewHolder holder) {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final String bookImage = book.getImagesPaths().get(0);
        final StorageReference imageRef = storage.getReference().child(bookImage);
        Glide.with(holder.itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .into(holder.ivBook);
    }

    private void onCheckBoxChangeState(final boolean b,
                                       final BookIdAndCountModel bookIdAndCountModel,
                                       final Book book,
                                       final BookViewHolder holder) {
        if (b) {
            checkedNum++;
            viewModel.addBookInBasketModel(bookIdAndCountModel);
            viewModel.addToTotalPrice(Float.valueOf(book.getPrice().substring(0, book.getPrice().length() - 3)) *
                    holder.npBooksCount.getValue());
        } else {
            checkedNum--;
            viewModel.removeBookInBasketModel(bookIdAndCountModel);
            viewModel.subtractFromTotalPrice(Float.valueOf(book.getPrice().substring(0, book.getPrice().length() - 3)) *
                    holder.npBooksCount.getValue());
        }
        if (checkedNum > 0) {
            viewModel.enableButtonRegister(true);
        } else {
            viewModel.enableButtonRegister(false);
        }
    }

    private void onBooksCountChanged(final Book book,
                                     final int i,
                                     final int i1,
                                     final BookViewHolder holder) {
        final float priceForSeveral = Float.valueOf(book.getPrice().substring(0, book.getPrice().length() - 3)) * i1;
        Log.d("priceForSeveral", String.valueOf(priceForSeveral));
        holder.tvPriseForSeveral.setText(String.format(" / %s р.", String.valueOf(priceForSeveral)));
        if (holder.checkBox.isChecked()) {
            if (i < i1) {
                viewModel.addToTotalPrice(Math.abs(Float.valueOf(book.getPrice().substring(0, book.getPrice().length() - 3)) *
                        i - priceForSeveral));
            } else {
                viewModel.subtractFromTotalPrice(Math.abs(Float.valueOf(book.getPrice().substring(0, book.getPrice().length() - 3)) *
                        i - priceForSeveral));
            }
        }
    }

    private void setAuthors(final Book book, final BookViewHolder holder) {
        final DatabaseCallback<List<Author>> authorListCallback = authorList -> {
            final StringBuilder authorsStringBuilder = new StringBuilder();
            for (final Author author : authorList) {
                authorsStringBuilder.append(author.author_surname)
                        .append(" ")
                        .append(author.author_name.substring(0, 1))
                        .append("., ")
                        .append('\n');
            }
            authorsStringBuilder.delete(authorsStringBuilder.length() - 3, authorsStringBuilder.length());
            holder.tvAuthor.setText(authorsStringBuilder);
        };
        DatabaseSingleton.getInstance().getAuthorList(book.getAuthorsIDs(), authorListCallback);
    }
}
