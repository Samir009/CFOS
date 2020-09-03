package com.samir.cfos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.samir.cfos.R;
import com.samir.cfos.databinding.ActivityRegistrationBinding;
import com.samir.cfos.helpers.ShowToast;
import com.samir.cfos.models.RegisterModel;
import com.samir.cfos.presenters.RegisterPresenter;
import com.samir.cfos.utils.Utilities;
import com.samir.cfos.utils.UtilitiesFunctions;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity implements RegisterPresenter.View {

    private ActivityRegistrationBinding binding;
    private RegisterPresenter registerPresenter;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(binding.RegisterToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(binding.RegisterToolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        initializeListener();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeListener() {

        fillFields();
        registerPresenter = new RegisterPresenter(this);

        binding.GenderId.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(view == null){

                    ShowToast.withLongMessage("popup view is not null");

                } else {
                    selectGender();
                }

                return true;
            }
        });

        binding.SubmitBtnId.setOnClickListener(v -> {

            if(UtilitiesFunctions.isNetworkAvailable(getApplicationContext())){
                confirmDetails();
            } else {
                ShowToast.withLongMessage("Internet connection failed");
                hideProgressBar();
            }

        });
    }

    private void selectGender() {
        PopupMenu popupMenu = new PopupMenu(this, binding.GenderId);
        popupMenu.dismiss();
        popupMenu.getMenuInflater().inflate(R.menu.select_gender, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if(item.getItemId() == R.id.male){
                        binding.GenderId.setText("male");
                    } else {
//                            ShowToast.withLongMessage("female");
                        binding.GenderId.setText("female");
                    }
                    return true;
                }
            });
            popupMenu.show();

    }

    private void fillFields(){
        binding.AddressId.setText("harion");
        binding.FullNameId.setText("name");
        binding.ContactId.setText("9087654345");
        binding.EmailId.setText("samirshrestha010@gmail.com");
        binding.PasswordId.setText("12345678qwerty");
        binding.RePasswordId.setText("12345678qwerty");
    }

    private void showProgressBar() {
        binding.RegProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        binding.RegProgressBar.setVisibility(View.GONE);

    }

    private boolean validUserName() {
        if (!Objects.requireNonNull(binding.FullNameId.getText()).toString().isEmpty()) {
            return true;
        } else {
            binding.FullNameId.setError("Full name is required");
            hideProgressBar();
            return false;
        }
    }

    private boolean validGender(){
        if(!Objects.requireNonNull(binding.GenderId.getText()).toString().isEmpty()){
            return true;
        } else {
            binding.GenderId.setError("Gender is required");
            hideProgressBar();
            return false;
        }
    }

    private boolean validAddress(){
        if(!Objects.requireNonNull(binding.AddressId.getText()).toString().isEmpty()){
            return true;
        } else {
            binding.AddressId.setError("Address required");
            hideProgressBar();
            return false;
        }
    }

    private boolean validContactNumber() {

        if (Objects.requireNonNull(binding.ContactId.getText()).toString().isEmpty()) {
            binding.ContactId.setError("Contact number required");
            hideProgressBar();
            return false;

        } else {

//          retrun android.util.Patterns.PHONE.matcher(input).matches();
            if (Objects.requireNonNull(binding.ContactId.getText()).length() > 10 || binding.ContactId.getText().length() < 10) {
                binding.ContactId.setError("contact length didn't matched");
                hideProgressBar();
                return false;
            } else {
                return true;
            }
        }
    }

    private boolean validEmail() {
        if (!Objects.requireNonNull(binding.EmailId.getText()).toString().isEmpty()) {
            return Patterns.EMAIL_ADDRESS.matcher(binding.EmailId.getText().toString()).matches();
        } else {
            binding.EmailId.setError("Email required");
            hideProgressBar();
            return false;
        }
    }

    private boolean validPassword() {
        if (!Objects.requireNonNull(binding.PasswordId.getText()).toString().isEmpty()) {
            return true;
        } else {
            binding.PasswordId.setError("Password required");
            hideProgressBar();
            return false;
        }
    }

    private boolean validRePassword() {
        if (!Objects.requireNonNull(binding.RePasswordId.getText()).toString().isEmpty()) {
            return true;
        } else {
            binding.PasswordId.setError("Re-Password required");
            hideProgressBar();
            return false;
        }
    }

    private boolean checkPassRePass() {
        if (!Objects.requireNonNull(binding.PasswordId.getText()).toString().equals(Objects.requireNonNull(binding.RePasswordId.getText()).toString())) {
            binding.PasswordId.setError("password and re-password didn't matched");
            binding.RePasswordId.setError("password and re-password didn't matched");
            hideProgressBar();
            return false;
        } else {
            return true;
        }
    }

    public void confirmDetails() {
        if (!validUserName() | !validGender() | !validAddress() | !validContactNumber() | !validEmail() | !validPassword() | !validRePassword() | !checkPassRePass()) {
            return;
        }

        showProgressBar();

//        define role of user as canteen staff
        String role = "canteen staff";
        registerPresenter.registerUser(
                Objects.requireNonNull(binding.FullNameId.getText()).toString(),
                Objects.requireNonNull(binding.GenderId.getText()).toString(),
                Objects.requireNonNull(binding.AddressId.getText()).toString(),
                Objects.requireNonNull(binding.ContactId.getText()).toString(),
                Objects.requireNonNull(binding.EmailId.getText()).toString(),
                Objects.requireNonNull(binding.PasswordId.getText()).toString(),
                role
        );

    }

    //    if registration is success or failed
    @Override
    public void onRegisterSuccess(RegisterModel r) {
        String name = r.getName();
        String gender = r.getGender();
        String address = r.getAddress();
        String contact = r.getContact();
        String email = r.getContact();
        String token = r.getDevice_token();
        String role = r.getRole();
        Log.e( "onRegisterSuccess: ", name + gender + address + contact + email + token + role);
        Utilities.saveUserDetails(name, gender, address, contact, email, token,role);
        hideProgressBar();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onAlreadySignedIn(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        hideProgressBar();
    }

    @Override
    public void onRegisterFailure(String failure) {
        Log.e("onRegisterFailure: ", failure );
        hideProgressBar();
    }
}