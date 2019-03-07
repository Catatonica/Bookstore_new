package izenka.hfad.com.bookstore.stores_map;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import izenka.hfad.com.bookstore.DatabaseSingleton;

public class StoresViewModel extends ViewModel {

    private MutableLiveData<List<StoreModel>> storeListLiveData;

    MutableLiveData<List<StoreModel>> getStoreListLiveData() {
        if (storeListLiveData == null) {
            storeListLiveData = new MutableLiveData<>();
            DatabaseSingleton.getInstance().getStoreList(storeList -> storeListLiveData.postValue(storeList));
        }
        return storeListLiveData;
    }
}
