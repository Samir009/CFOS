package com.samir.cfos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.samir.cfos.R;
import com.samir.cfos.databinding.ActivityTotalOrderedFoodDetailsBinding;

import java.util.Objects;

public class TotalOrderedFoodDetailsActivity extends AppCompatActivity {

    private ActivityTotalOrderedFoodDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTotalOrderedFoodDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.totalOrderFoodDetailsToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }
}