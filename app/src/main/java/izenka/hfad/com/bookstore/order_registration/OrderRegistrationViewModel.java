package izenka.hfad.com.bookstore.order_registration;


import android.arch.lifecycle.ViewModel;

import izenka.hfad.com.bookstore.DatabaseSingleton;

public class OrderRegistrationViewModel extends ViewModel{

//    private MutableLiveData<OrderRegistrationModel> orderLiveData;
//
//    public MutableLiveData<OrderRegistrationModel> getOrderLiveData(){
//        if(orderLiveData == null){
//            orderLiveData = new MutableLiveData<>();
//        }
//        return orderLiveData;
//    }

    void writeNewOrder(OrderRegistrationModel orderModel){
        DatabaseSingleton.getInstance().writeNewOrder(orderModel);
    }
}
