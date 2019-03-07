package izenka.hfad.com.bookstore.category;

import android.animation.Animator;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class CategorizedBooksAdapter extends PagedListAdapter<Book, CategorizedBooksAdapter.BookViewHolder> {

    private CategorizedBooksViewModel viewModel;
    private static boolean itemShouldBeScaled;

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View bookView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.book, parent, false);
        return new BookViewHolder(bookView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookViewHolder holder, final int position) {
        final Book book = getItem(position);
        if (book != null) {
            holder.tvBookName.setText(book.getTitle());
            holder.tvBookPrise.setText(book.getPrice());
            holder.itemView.setOnClickListener(view -> {
                animateView(view);
                viewModel.onBookClicked(book);
            });
            setAuthors(book, holder);
            setImage(book, holder);
        }
    }

    CategorizedBooksAdapter(@NonNull final DiffUtil.ItemCallback<Book> diffCallback) {
        super(diffCallback);
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {

        @BindView((R.id.imgBtnBook))
        ImageView imgBtnBook;
        @BindView((R.id.tvBookName))
        TextView tvBookName;
        @BindView((R.id.tvBookAuthor))
        TextView tvBookAuthor;
        @BindView((R.id.tvBookPrise))
        TextView tvBookPrise;

        BookViewHolder(final View itemView) {
            super(itemView);
            if (itemShouldBeScaled) {
                itemView.setScaleX(0.8f);
                itemView.setScaleY(0.8f);
            }
            ButterKnife.bind(this, itemView);
        }
    }

    void shouldBeScaled(final boolean scaled) {
        itemShouldBeScaled = scaled;
    }

    void setViewModel(final CategorizedBooksViewModel viewModel) {
        this.viewModel = viewModel;
    }

    private void animateView(final View view) {
        final int cx = view.getWidth() / 2;
        final int cy = view.getHeight() / 2;

        final float finalRadius = (float) Math.hypot(cx, cy);

        final Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    private void setImage(final Book book, final BookViewHolder holder) {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final String bookImage = book.getImagesPaths().get(0);
        final StorageReference imageRef = storage.getReference().child(bookImage);
        Glide.with(holder.itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .into(holder.imgBtnBook);
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
            holder.tvBookAuthor.setText(authorsStringBuilder);
        };
        DatabaseSingleton.getInstance().getAuthorList(book.getAuthorsIDs(), authorListCallback);
    }
}
