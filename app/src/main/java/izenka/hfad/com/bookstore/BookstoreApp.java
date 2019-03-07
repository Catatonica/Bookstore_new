package izenka.hfad.com.bookstore;


import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class BookstoreApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
