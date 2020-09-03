package com.samir.cfos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.samir.cfos.R;
import com.samir.cfos.databinding.ActivityTotalOrderedFoodBinding;
import com.samir.cfos.helpers.AppActivity;
import com.samir.cfos.helpers.AppRecyclerViewAdapter;

import java.util.Objects;

public class TotalOrderedFoodActivity extends AppActivity {

    private ActivityTotalOrderedFoodBinding binding;

    TotalOrderdFoodRvAdapter totalOrderdFoodRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTotalOrderedFoodBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.totalOrderFoodToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initializeView();
        initializeListeners();
    }

    @Override
    protected void initializeView() {

    }

    @Override
    protected void initializeListeners() {
        prepareTotalOrderdFoodRvAdapter();

    }

    private void prepareTotalOrderdFoodRvAdapter(){
        totalOrderdFoodRvAdapter = new TotalOrderdFoodRvAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.totalOrderedFoodListRv.setLayoutManager(linearLayoutManager);
        binding.totalOrderedFoodListRv.setAdapter(totalOrderdFoodRvAdapter);
    }

    private class TotalOrderdFoodRvAdapter extends AppRecyclerViewAdapter{

        @Override
        public void add(Object object) {

        }

        @Override
        public void clear() {

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.total_ordered_food_list, parent, false);
            return new TotalOrderedFoodVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        private class TotalOrderedFoodVH extends RecyclerView.ViewHolder {

            LinearLayout OrderedDate;

            public TotalOrderedFoodVH(@NonNull View itemView) {
                super(itemView);

                OrderedDate = itemView.findViewById(R.id.guest_order_list);
                OrderedDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(v.getId()== R.id.guest_order_list){
                            startActivity(new Intent(getApplicationContext(), TotalOrderedFoodDetailsActivity.class));
                        }
                    }
                });
            }
        }
    }
}
