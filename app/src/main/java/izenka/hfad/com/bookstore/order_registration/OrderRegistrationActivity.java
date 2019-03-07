package izenka.hfad.com.bookstore.order_registration;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import izenka.hfad.com.bookstore.AccountListener;
import izenka.hfad.com.bookstore.R;
import izenka.hfad.com.bookstore.account.AccountViewModel;
import izenka.hfad.com.bookstore.basket.BookIdAndCountModel;
import izenka.hfad.com.bookstore.book.BookActivity;
import izenka.hfad.com.bookstore.orders.OrdersActivity;
import mehdi.sakout.fancybuttons.FancyButton;

public class OrderRegistrationActivity extends AppCompatActivity implements AccountListener {

    private static final int PLACE_PICKER_REQUEST = 1;

    @BindView(R.id.etPhoneNumber) EditText etPhoneNumber;
    @BindView(R.id.etName) EditText etName;
    @BindView(R.id.etCity) EditText etCity;
    @BindView(R.id.etStreet) EditText etStreet;
    @BindView(R.id.etHouse) EditText etHouse;
    @BindView(R.id.etPorchNumber) EditText etPorchNumber;
    @BindView(R.id.etFlatNumber) EditText etFlatNumber;
    @BindView(R.id.etFloor) EditText etFloor;
    @BindView(R.id.etAddress) EditText etAddress;
    @BindView(R.id.btnRegister) FancyButton btnRegister;
    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.btnAddLocation) Button btnAddLocation;

    private float totalPrice;
    private Map<String, Integer> ordersMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        totalPrice = intent.getFloatExtra("totalPrice", 0);
        List<BookIdAndCountModel> bookIDsAndCount = intent.getParcelableArrayListExtra("bookIDsAndCount");
        ordersMap = new HashMap<>();
        for (BookIdAndCountModel b : bookIDsAndCount) {
            ordersMap.put(b.bookID, b.count);
        }

        initViews();
        setToolbar();

        AccountViewModel accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        accountViewModel.getUserLiveData().observe(this, user -> {
            if (user != null) {
                etEmail.setText(user.email);
                etName.setText(String.format("%s %s", user.name, user.surname));
                etPhoneNumber.setText(user.phone);
                if (user.Address != null) {
                    etCity.setText((String) user.Address.get("city"));
                    etStreet.setText((String) user.Address.get("street"));
                    etHouse.setText((String) user.Address.get("house"));
                    etFlatNumber.setText((String) user.Address.get("flat"));
                }
            }
        });
    }

    private void initViews() {
        Animation alpha = new AlphaAnimation(1f, 0f);
        btnAddLocation.setOnClickListener(btn ->{
            btn.startAnimation(alpha);
            addLocation();
        });
        btnRegister.setOnClickListener(btn -> {
            btn.startAnimation(alpha);
            register();
        });
    }

    private void addLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                etAddress.setText(place.getAddress());
                final String[] splitAddress = place.getAddress().toString().split(", ");
                String city = splitAddress[1].replaceAll("\\d", "");
                String street = splitAddress[0].replaceAll("\\d/\\d", "");
                String house = splitAddress[0].replaceAll("[^\\d/\\d]", "");
                etCity.setText(city);
                etStreet.setText(street);
                etHouse.setText(house);
                etFlatNumber.setText("");
                etPorchNumber.setText("");
                etFloor.setText("");
            }
        }
    }

    private void setToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar));
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.orderRegistration);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void register() {
        String phoneNumber = etPhoneNumber.getText().toString();
        String city = etCity.getText().toString();
        String street = etStreet.getText().toString();
        String house = etHouse.getText().toString();
        String flat = etFlatNumber.getText().toString();
        String porch = etPorchNumber.getText().toString();
        String floor = etFloor.getText().toString();
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();

        if (phoneNumber.isEmpty() || city.isEmpty() || street.isEmpty() || house.isEmpty()) {
            Toast.makeText(this, "Заполните все поля, помеченные знаком *", Toast.LENGTH_SHORT).show();
        } else {
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm", Locale.getDefault());
            String date = df.format(Calendar.getInstance().getTime()).replaceAll("\\.","");

            Map<String, Object> addressMap = new HashMap<>();
            addressMap.put("city", city);
            addressMap.put("street", street);
            addressMap.put("house", house);
            addressMap.put("flat", flat);
            addressMap.put("porch", porch);
            addressMap.put("floor", floor);

            String fullAddress = String.format("%s, %s, %s", city, street, house);

            OrderRegistrationModel order = new OrderRegistrationModel(date,
                                                                      totalPrice,
                                                                      name,
                                                                      phoneNumber,
                                                                      getUser().getUid(),
                                                                      email,
                                                                      fullAddress,
                                                                      ordersMap,
                                                                      addressMap,
                                                                      "выполняется"
            );
            OrderRegistrationViewModel orderViewModel = ViewModelProviders.of(this).get(OrderRegistrationViewModel.class);
            orderViewModel.writeNewOrder(order);
            showSucceedMessage();
        }
    }

    private void showSucceedMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setTitle(getString(R.string.registration));
        builder.setMessage(getString(R.string.succeed_message));

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText, (dialog, which) -> onBtnOKClicked());

        String negativeText = getString(R.string.to_orders);
        builder.setNegativeButton(negativeText,
                                  (dialog, which) -> onBtnToOrdersClicked());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void onBtnOKClicked() {
        Intent intent = new Intent(getApplicationContext(), BookActivity.class);
        intent.putExtra("bookID", getIntent().getStringExtra("bookID"));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void onBtnToOrdersClicked() {
        Intent intent = new Intent(this, OrdersActivity.class);
        startActivity(intent);
        finish();
    }
}
