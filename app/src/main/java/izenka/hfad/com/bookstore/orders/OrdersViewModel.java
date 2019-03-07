package izenka.hfad.com.bookstore.orders;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import izenka.hfad.com.bookstore.DatabaseSingleton;
import izenka.hfad.com.bookstore.order_registration.OrderRegistrationModel;

public class OrdersViewModel extends ViewModel {

    private MutableLiveData<List<OrderRegistrationModel>> orderListLiveData;
    private MutableLiveData<List<BookInOrderModel>> bookAndCountListLiveData;
    private OrdersNavigator navigator;

    public void setNavigator(final OrdersNavigator navigator) {
        this.navigator = navigator;
    }

    MutableLiveData<List<BookInOrderModel>> getBookAndCountListLiveData() {
        return bookAndCountListLiveData;
    }

    MutableLiveData<List<OrderRegistrationModel>> getOrderListLiveData() {
        if (orderListLiveData == null) {
            orderListLiveData = new MutableLiveData<>();
            DatabaseSingleton.getInstance().getUser(user -> {
                DatabaseSingleton.getInstance().getOrderList(user.Orders, orderList -> {
                    orderListLiveData.postValue(orderList);
                });
            });
        }
        return orderListLiveData;
    }

    void onReturnClicked() {
        navigator.onReturnClicked();
    }

    void openDetailsScreen(final OrderRegistrationModel order) {
        setBookIDAndCountList(order);
        navigator.openDetailsScreen(order);
    }

    private void setBookIDAndCountList(final OrderRegistrationModel order) {
        if (bookAndCountListLiveData == null) {
            bookAndCountListLiveData = new MutableLiveData<>();
        }
        DatabaseSingleton.getInstance().getBookAndCountList(order.getBooks(), bookAndCountList -> {
            bookAndCountListLiveData.postValue(bookAndCountList);
        });
    }
}
