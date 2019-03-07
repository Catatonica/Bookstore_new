package izenka.hfad.com.bookstore.account;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import izenka.hfad.com.bookstore.R;

public class UserProfileFragment extends Fragment {

    private AccountViewModel viewModel;

    @BindView(R.id.btnSignOut)
    Button btnSignOut;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etSurname)
    EditText etSurname;
    @BindView(R.id.etHouse)
    EditText etHouse;
    @BindView(R.id.etFlat)
    EditText etFlat;
    @BindView(R.id.etCity)
    EditText etCity;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etStreet)
    EditText etStreet;
    @BindView(R.id.btnSaveChanges)
    Button btnSaveChanges;
    @BindView(R.id.pbLoadingProgress)
    ProgressBar pbLoadingProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view,
                              @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pbLoadingProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(requireActivity()).get(AccountViewModel.class);
        viewModel.getUserLiveData().observe(this, user -> {
            pbLoadingProgress.setVisibility(View.GONE);
            if (user != null) {
                fillViews(user);
            }
        });
        btnSignOut.setOnClickListener(btn -> {
            viewModel.signOut();
        });
    }

    private void fillViews(final User user) {
        etName.setText(user.name);
        etSurname.setText(user.surname);
        etPhone.setText(user.phone);
        if (user.Address != null) {
            etCity.setText((String) user.Address.get("city"));
            etStreet.setText((String) user.Address.get("street"));
            etHouse.setText((String) user.Address.get("house"));
            etFlat.setText((String) user.Address.get("flat"));
        }

        tvEmail.setText(user.email);
        btnSaveChanges.setOnClickListener(btn -> {
            user.name = etName.getText().toString();
            user.surname = etSurname.getText().toString();
            user.phone = etPhone.getText().toString();
            final Map<String, Object> Address = new HashMap<>();
            Address.put("city", etCity.getText().toString());
            Address.put("street", etStreet.getText().toString());
            Address.put("house", etHouse.getText().toString());
            Address.put("flat", etFlat.getText().toString());
            user.Address = Address;
            viewModel.saveChanges(user.toMap());
        });
    }
}
