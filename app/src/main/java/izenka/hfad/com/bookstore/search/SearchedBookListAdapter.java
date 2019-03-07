package izenka.hfad.com.bookstore.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import izenka.hfad.com.bookstore.DatabaseCallback;
import izenka.hfad.com.bookstore.DatabaseSingleton;
import izenka.hfad.com.bookstore.R;
import izenka.hfad.com.bookstore.model.db_classes.Author;
import izenka.hfad.com.bookstore.model.db_classes.Book;

public class SearchedBookListAdapter extends RecyclerView.Adapter<SearchedBookListAdapter.BookViewHolder> {

    private List<Book> bookList;
    private final SearchViewModel viewModel;
    private static boolean shouldBeScaled;

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                             final int viewType) {
        final View bookView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.book, parent, false);
        return new BookViewHolder(bookView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookViewHolder holder,
                                 final int position) {
        final Book book = bookList.get(position);
        holder.tvBookName.setText(book.getTitle());
        holder.tvBookPrise.setText(book.getPrice());

        setAuthors(book, holder);
        setImage(book, holder);
    }

    public void setBookList(final List<Book> bookList) {
        this.bookList = bookList;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    SearchedBookListAdapter(final List<Book> bookList,
                            final SearchViewModel viewModel,
                            final boolean itemShouldBeScaled) {
        this.bookList = bookList;
        this.viewModel = viewModel;
        shouldBeScaled = itemShouldBeScaled;
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBtnBook;
        TextView tvBookName;
        TextView tvBookAuthor;
        TextView tvBookPrise;

        BookViewHolder(final View itemView) {
            super(itemView);
            if (shouldBeScaled) {
                itemView.setScaleX(0.8f);
                itemView.setScaleY(0.8f);
            }
            imgBtnBook = itemView.findViewById(R.id.imgBtnBook);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvBookPrise = itemView.findViewById(R.id.tvBookPrise);
        }
    }

    private void setImage(final Book book,
                          final BookViewHolder holder) {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final String bookImage = book.getImagesPaths().get(0);
        final StorageReference imageRef = storage.getReference().child(bookImage);
        Glide.with(holder.itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .into(holder.imgBtnBook);
        holder.itemView.setOnClickListener(view -> viewModel.onBookClicked(book));
    }

    private void setAuthors(final Book book,
                            final BookViewHolder holder) {
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
