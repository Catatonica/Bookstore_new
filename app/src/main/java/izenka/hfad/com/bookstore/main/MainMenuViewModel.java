package izenka.hfad.com.bookstore.main;

import android.arch.lifecycle.ViewModel;

public class MainMenuViewModel extends ViewModel {

    private MainMenuNavigator navigator;

    public void setNavigator(final MainMenuNavigator navigator) {
        this.navigator = navigator;
    }

    void onCategoryClicked(final String categoryID) {
        navigator.onCategoryClicked(categoryID);
    }

    void onSearchClicked() {
        navigator.onSearchClicked();
    }
}
