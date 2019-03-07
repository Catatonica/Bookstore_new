package izenka.hfad.com.bookstore.basket;


public interface ButtonsClickListener {
    void enableButtonRegister(boolean enable);

    void addBookInBasketModel(BookIdAndCountModel bookIdAndCountModel);

    void removeBookInBasketModel(BookIdAndCountModel bookIdAndCountModel);

    void addToTotalPrice(float value);

    void subtractFromTotalPrice(float value);
}
