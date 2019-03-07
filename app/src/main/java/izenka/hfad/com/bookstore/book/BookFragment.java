package izenka.hfad.com.bookstore.book;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import izenka.hfad.com.bookstore.R;
import izenka.hfad.com.bookstore.model.db_classes.Author;
import izenka.hfad.com.bookstore.model.db_classes.Book;
import mehdi.sakout.fancybuttons.FancyButton;

public class BookFragment extends Fragment {

    private BookViewModel viewModel;
    private Animation alpha;

    @BindView((R.id.tvTitle))
    TextView tvTitle;
    @BindView((R.id.tvYear))
    TextView tvYear;
    @BindView((R.id.tvAvailability))
    TextView tvAvailability;
    @BindView((R.id.tvPrise))
    TextView tvPrise;
    @BindView((R.id.tvAuthor))
    TextView tvAuthor;
    @BindView((R.id.tvPublisher))
    TextView tvPublisher;
    @BindView((R.id.tvDescription))
    TextView tvDescription;
    @BindView((R.id.putInBasket))
    mehdi.sakout.fancybuttons.FancyButton btnPutInBasket;
    @BindView((R.id.ivBookImage))
    ImageView ivBookImage;
    @BindView((R.id.ibExpand))
    ImageButton ibExpand;
    @BindView((R.id.btnNotify))
    FancyButton btnNotify;
    @BindView((R.id.tvCount))
    TextView tvCount;
    @BindView((R.id.tvPages))
    TextView tvPages;
    @BindView((R.id.tvCover))
    TextView tvCover;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_book, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view,
                              @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(requireActivity()).get(BookViewModel.class);
        viewModel.getBookLiveData().observe(this, book -> {
            if (book != null) {
                setBookInfo(book);
            }
        });
        btnNotify.setOnClickListener(btn -> {
            btn.startAnimation(alpha);
            viewModel.notifyOfBookAppearance();
        });
        btnPutInBasket.setOnClickListener(btn -> {
            btn.startAnimation(alpha);
            viewModel.onPutInBasketClicked();
        });
    }

    private void initViews(final View view) {
        alpha = new AlphaAnimation(1f, 0f);
        btnPutInBasket.setIconResource(R.drawable.ic_shopping_basket_black_24dp);
        btnPutInBasket.setIconPosition(FancyButton.POSITION_LEFT);
        btnPutInBasket.getIconImageObject().setLayoutParams(new LinearLayout.LayoutParams(60, 60));
        final boolean[] isExpanded = {false};
        ibExpand.setOnClickListener(btn -> {
            btn.startAnimation(alpha);
            if (!isExpanded[0]) {
                tvDescription.setMaxLines(30);
                ibExpand.setBackground(view.getContext().getDrawable(R.drawable.narrow));
                isExpanded[0] = true;
            } else {
                tvDescription.setMaxLines(5);
                ibExpand.setBackground(view.getContext().getDrawable(R.drawable.expand));
                isExpanded[0] = true;
            }
        });
    }

    private void setBookInfo(final Book book) {
        tvCount.setText(String.valueOf(book.getCount()));
        tvPages.setText(String.valueOf(book.getPages_number()));
        tvCover.setText(book.getCover());
        tvTitle.setText(String.format("\"%s\"", book.getTitle()));
        tvYear.setText(String.valueOf(book.getPublication_year()));
        tvPrise.setText(book.getPrice());
        if (book.getCount() != 0) {
            tvAvailability.setText("в наличии");
            btnPutInBasket.setVisibility(View.VISIBLE);
            btnNotify.setVisibility(View.GONE);
        } else {
            tvAvailability.setText("нет в наличии");
            btnPutInBasket.setVisibility(View.GONE);
            btnNotify.setVisibility(View.VISIBLE);
        }
        tvDescription.setText(book.getDescription());

        viewModel.getPublisherLiveData(String.valueOf(book.getBook_publisher_id())).observe(this, publisher -> {
            if (publisher != null) {
                tvPublisher.setText(publisher.publisher_name);
            }
        });

        viewModel.getAuthorListLiveData(book.getAuthorsIDs()).observe(this, authorList -> {
            if (authorList != null) {
                setAuthors(authorList);
            }
        });
        setImage(book);
    }

    private void setImage(final Book book) {
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        final String bookImage = book.getImagesPaths().get(0);
        final StorageReference imageRef = storage.getReference().child(bookImage);

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .into(ivBookImage);
    }

    private void setAuthors(final List<Author> authorList) {
        final StringBuilder authorsStringBuilder = new StringBuilder();
        for (final Author author : authorList) {
            authorsStringBuilder.append(author.author_surname)
                    .append(" ")
                    .append(author.author_name.substring(0, 1))
                    .append("., ")
                    .append('\n');
        }
        authorsStringBuilder.delete(authorsStringBuilder.length() - 3, authorsStringBuilder.length());
        tvAuthor.setText(authorsStringBuilder);
    }
}
