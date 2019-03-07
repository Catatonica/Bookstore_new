package izenka.hfad.com.bookstore.account;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import izenka.hfad.com.bookstore.DatabaseSingleton;

public class AccountViewModel extends ViewModel {

    private MutableLiveData<User> userLiveData;
    private AccountActivityNavigator accountActivityNavigator;
    private RegistrationFragmentNavigator registrationFragmentNavigator;

    public MutableLiveData<User> getUserLiveData() {
        if (userLiveData == null) {
            userLiveData = new MutableLiveData<>();
            loadUser();
        }
        return userLiveData;
    }

    void createAccount(final Activity activity,
                       final String email,
                       final String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        createUserNode();
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password);
                        accountActivityNavigator.setFragment(new UserProfileFragment());
                        loadUser();
                    } else {
                        registrationFragmentNavigator.onFailedRegistration();
                    }
                });
    }

    void signIn(final Activity activity,
                final String email,
                final String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        accountActivityNavigator.setFragment(new UserProfileFragment());
                        loadUser();
                    } else {
                        registrationFragmentNavigator.onFailedRegistration();
                    }
                });
    }

    void signOut() {
        FirebaseAuth.getInstance().signOut();
        accountActivityNavigator.setFragment(new UserRegistrationFragment());
    }

    void setAccountActivityNavigator(final AccountActivityNavigator accountActivityNavigator) {
        this.accountActivityNavigator = accountActivityNavigator;
    }

    void setRegistrationFragmentNavigator(final RegistrationFragmentNavigator registrationFragmentNavigator) {
        this.registrationFragmentNavigator = registrationFragmentNavigator;
    }

    void saveChanges(final Map<String, Object> updates) {
        DatabaseSingleton.getInstance().updateUserInfo(updates);
        accountActivityNavigator.showMessage();
    }

    private void loadUser() {
        DatabaseSingleton.getInstance().getUser(user -> userLiveData.postValue(user));
    }

    private void createUserNode() {
        DatabaseSingleton.getInstance().createUser();
    }
}
