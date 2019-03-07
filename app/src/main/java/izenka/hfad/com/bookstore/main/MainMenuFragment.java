package izenka.hfad.com.bookstore.main;

import android.animation.Animator;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import izenka.hfad.com.bookstore.R;

public class MainMenuFragment extends Fragment {

    private MainMenuViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view,
                              @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(requireActivity()).get(MainMenuViewModel.class);
        view.findViewById(R.id.btnForeign).setOnClickListener(view1 -> {
            animateView(view1);
            viewModel.onCategoryClicked("foreign");
        });
        view.findViewById(R.id.btnKid).setOnClickListener(view1 -> {
            animateView(view1);
            viewModel.onCategoryClicked("kid");
        });
        view.findViewById(R.id.btnBusiness).setOnClickListener(view1 -> {
            animateView(view1);
            viewModel.onCategoryClicked("business");
        });
        view.findViewById(R.id.btnFiction).setOnClickListener(view1 -> {
            animateView(view1);
            viewModel.onCategoryClicked("fiction");
        });
        view.findViewById(R.id.btnStudy).setOnClickListener(view1 -> {
            animateView(view1);
            viewModel.onCategoryClicked("study");
        });
        view.findViewById(R.id.btnNonfiction).setOnClickListener(view1 -> {
            animateView(view1);
            viewModel.onCategoryClicked("nonfiction");
        });
        view.findViewById(R.id.etSearch).setOnClickListener(view1 -> viewModel.onSearchClicked());
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
