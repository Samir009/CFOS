package com.samir.cfos.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.samir.cfos.R;
import com.samir.cfos.databinding.ActivityProfileBinding;
import com.samir.cfos.helpers.ShowToast;
import com.samir.cfos.models.RegisterModel;
import com.samir.cfos.presenters.ProfilePresenter;
import com.samir.cfos.utils.UtilitiesFunctions;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements ProfilePresenter.View {

    private ActivityProfileBinding binding;
    private ProfilePresenter profilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.profileToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initializeListener();

    }

    private void initializeListener() {

        if (UtilitiesFunctions.isNetworkAvailable(this)) {
            profilePresenter = new ProfilePresenter(this);
            profilePresenter.retrieveUser();
        } else {
            ShowToast.withLongMessage("Internet error!");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_toolbar, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.edit_profile) {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProfileRetrieveSuccess(RegisterModel registerModel) {
        Log.e("onProfileRetrieveSuc: ", new GsonBuilder().create().toJson(registerModel));
        binding.profileName.setText(registerModel.getName());
        binding.address.setText(registerModel.getAddress());
        binding.contact.setText(registerModel.getContact());
        binding.gender.setText(registerModel.getGender());
        binding.profileEmail.setText(registerModel.getEmail());
    }

    @Override
    public void onProfileRetrieveFailure(String s) {
        Log.e("onProfileRetrieveFail: ", s);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}