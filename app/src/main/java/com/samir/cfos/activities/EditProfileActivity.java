package com.samir.cfos.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.google.firebase.firestore.CollectionReference;
import com.google.gson.GsonBuilder;
import com.samir.cfos.R;
import com.samir.cfos.constants.AppConstants;
import com.samir.cfos.databinding.ActivityEditProfileBinding;
import com.samir.cfos.helpers.ShowToast;
import com.samir.cfos.models.RegisterModel;
import com.samir.cfos.presenters.EditProfilePresenter;
import com.samir.cfos.utils.Utilities;
import com.samir.cfos.utils.UtilitiesFunctions;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity implements EditProfilePresenter.View {

    private ActivityEditProfileBinding binding;
    EditProfilePresenter editProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.editProfileToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Toolbar toolbar = findViewById(R.id.editProfileToolbar);
        setSupportActionBar(toolbar);

        initializeListener();
    }

    private void initializeListener() {
        editProfilePresenter = new EditProfilePresenter(this);
        editProfilePresenter.getUserData();

        binding.editProfileSave.setOnClickListener(v -> {
         if(UtilitiesFunctions.isNetworkAvailable(this)){
            validDetails();
         } else {
             ShowToast.withLongMessage("Internet error!");
         }
        });
    }

    private void showProgressBar() {
        binding.editProProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        binding.editProProgressBar.setVisibility(View.GONE);

    }

    private boolean validUserName() {
        if (!Objects.requireNonNull(binding.eName.getText()).toString().isEmpty()) {
            return true;
        } else {
            binding.eName.setError("Full name is required");
            hideProgressBar();
            return false;
        }
    }

    private boolean validGender(){
        if(!Objects.requireNonNull(binding.eGender.getText()).toString().isEmpty()){
            return true;
        } else {
            binding.eGender.setError("Gender is required");
            hideProgressBar();
            return false;
        }
    }

    private boolean validAddress(){
        if(!Objects.requireNonNull(binding.eAddress.getText()).toString().isEmpty()){
            return true;
        } else {
            binding.eAddress.setError("Address required");
            hideProgressBar();
            return false;
        }
    }

    private boolean validContactNumber() {

        if (Objects.requireNonNull(binding.eContact.getText()).toString().isEmpty()) {
            binding.eContact.setError("Contact number required");
            hideProgressBar();
            return false;

        } else {

//            retrun android.util.Patterns.PHONE.matcher(input).matches();
            if (Objects.requireNonNull(binding.eContact.getText()).length() > 10 || binding.eContact.getText().length() < 10) {
                binding.eContact.setError("contact length didn't matched");
                hideProgressBar();
                return false;
            } else {
                return true;
            }
        }
    }

    private boolean validEmail() {
        if (!Objects.requireNonNull(binding.eEmail.getText()).toString().isEmpty()) {
            return Patterns.EMAIL_ADDRESS.matcher(binding.eEmail.getText().toString()).matches();
        } else {
            binding.eEmail.setError("Email required");
            hideProgressBar();
            return false;
        }
    }

    private void validDetails(){
        if (!validUserName() | !validGender() | !validAddress() | !validContactNumber() | !validEmail()) {
            return;
        }
        showProgressBar();
        editProfilePresenter.editDetails( binding.eName.getText().toString(),
                binding.eGender.getText().toString(),
                binding.eContact.getText().toString(),
                binding.eAddress.getText().toString(),
                binding.eEmail.getText().toString());
    }

    @Override
    public void onSuccess(RegisterModel registerModel) {
        Log.d( "EditProfileRetSucc:", new GsonBuilder().create().toJson(registerModel));
        binding.eName.setText(registerModel.getName());
        binding.eAddress.setText(registerModel.getAddress());
        binding.eContact.setText(registerModel.getContact());
        binding.eEmail.setText(registerModel.getEmail());
        binding.eGender.setText(registerModel.getGender());
    }

    @Override
    public void onFailure(String s) {
        ShowToast.withLongMessage(s);
    }

    @Override
    public void onEditSuccess(String string) {
        hideProgressBar();
        startActivity(new Intent(this, ProfileActivity.class));
        finish();
        ShowToast.withLongMessage(string);
    }

    @Override
    public void onEditFailure(String fail) {
        ShowToast.withLongMessage(fail);
    }
}