package izenka.hfad.com.bookstore.stores_map;


import java.util.HashMap;
import java.util.Map;

public class StoreModel {
    private Map<String, Float> geolocation = new HashMap<>();
    private String address;
    private String phone;

    public Map<String, Float> getGeolocation() {
        return geolocation;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
