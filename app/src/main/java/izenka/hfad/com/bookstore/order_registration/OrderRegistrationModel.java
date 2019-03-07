package izenka.hfad.com.bookstore.order_registration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OrderRegistrationModel implements Serializable {

    private float price;
    private String date;
    private String userName;
    private String userPhone;
    private String userID;
    private String userEmail;
    private String fullAddress;
    private String status;
    private Map<String, Integer> Books = new HashMap<>();
    private Map<String, Object> Address = new HashMap<>();

    public OrderRegistrationModel() {

    }

    OrderRegistrationModel(String date, float price, String userName, String userPhone, String userID, String userEmail,
                           String fullAddress, Map<String, Integer> Books, Map<String, Object> Address, String status) {
        this.date = date;
        this.price = price;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userID = userID;
        this.userEmail = userEmail;
        this.fullAddress = fullAddress;
        this.Books = Books;
        this.Address = Address;
        this.status = status;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("price", price);
        result.put("userName", userName);
        result.put("userPhone", userPhone);
        result.put("userID", userID);
        result.put("userEmail", userEmail);
        result.put("fullAddress", fullAddress);
        result.put("Books", Books);
        result.put("Address", Address);
        result.put("status", status);
        return result;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Integer> getBooks() {
        return Books;
    }

    public void setBooks(Map<String, Integer> books) {
        Books = books;
    }

    public Map<String, Object> getAddress() {
        return Address;
    }

    public void setAddress(Map<String, Object> address) {
        Address = address;
    }
}
