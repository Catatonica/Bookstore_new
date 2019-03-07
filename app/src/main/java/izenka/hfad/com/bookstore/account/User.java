package izenka.hfad.com.bookstore.account;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    public String email;
    public String name;
    public String surname;
    public String phone;
    //    public String photoPath;
//    public String birthday;
    public Map<String, Object> Address = new HashMap<>();
    public Map<String, Integer> Basket = new HashMap<>();
    public Map<String, String> Orders = new HashMap<>();

    public User() {

    }

    public Map<String, Object> toMap() {
        final Map<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("name", name);
        result.put("surname", surname);
        result.put("phone", phone);
        result.put("Address", Address);

        return result;
    }
}
