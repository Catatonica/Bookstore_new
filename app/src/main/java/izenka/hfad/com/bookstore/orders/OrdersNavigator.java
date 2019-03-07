package izenka.hfad.com.bookstore.orders;


import izenka.hfad.com.bookstore.order_registration.OrderRegistrationModel;

public interface OrdersNavigator {
    void onReturnClicked();

    void openDetailsScreen(OrderRegistrationModel order);
}
