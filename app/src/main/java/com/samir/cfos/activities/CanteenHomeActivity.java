package com.samir.cfos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.GsonBuilder;
import com.samir.cfos.R;
import com.samir.cfos.databinding.ActivityCanteenHomeBinding;
import com.samir.cfos.helpers.AppActivity;
import com.samir.cfos.helpers.AppRecyclerViewAdapter;
import com.samir.cfos.helpers.ShowToast;
import com.samir.cfos.models.FoodModel;
import com.samir.cfos.models.OrdersModel;
import com.samir.cfos.presenters.CanteenHomePresenter;
import com.samir.cfos.utils.Utilities;
import com.samir.cfos.utils.UtilitiesFunctions;


import java.util.ArrayList;
import java.util.List;

public class CanteenHomeActivity extends AppActivity implements View.OnClickListener, CanteenHomePresenter.View {

    private ActivityCanteenHomeBinding binding;

    private RecyclerView recyclerView;

    private VHCanteenDrinksAdapter vhCanteenDrinksAdapter;
    private VHCanteenVegAdapter vhCanteenVegAdapter;
    private VHCanteenNonVegAdapter vhCanteenNonVegAdapter;

    private CanteenHomePresenter canteenHomePresenter;

//    private ProgressBar drinksProgressBar;
//    private ProgressBar vegProgressBar;
//    private ProgressBar nonVegProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCanteenHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        initializeView();
        initializeListeners();

        prepareCanteenHomeRVAdapter();

    }
    @Override
    protected void initializeView() {

        Toolbar toolbar = findViewById(R.id.canteen_home_toolbar);
        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setIcon(R.drawable.ic_profile_icon);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            Log.e("fu: ", firebaseUser.getEmail());

        } else {
            Log.e("fu: ", "null");

        }


        recyclerView = findViewById(R.id.canteen_home_rv);
        recyclerView.setHasFixedSize(true);

    }

    @Override
    protected void initializeListeners() {

//        initialize drinks presenter
        canteenHomePresenter = new CanteenHomePresenter(this);
        canteenHomePresenter.retrieveDrinksFromFirebase();
        canteenHomePresenter.retrieveNonVegFromFirebase();
        canteenHomePresenter.retrieveVegFromFirebase();

        swipeToRefresh();

//        making fab icons functional
        binding.addDrinksFab.setOnClickListener(this);
        binding.addVegItemFab.setOnClickListener(this);
        binding.addNonVegItemFab.setOnClickListener(this);
    }

    @Override
    public void onDrinksRetrieved(List<FoodModel> foodModel) {
//        mFoodModelList = new ArrayList<>();
        Log.e("onDrinksRetrieved: ", new GsonBuilder().create().toJson(foodModel));
        vhCanteenDrinksAdapter.add(foodModel);
        vhCanteenDrinksAdapter.notifyDataSetChanged();

        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onVegFoodRetrieved(List<FoodModel> mVegModelList) {
        Log.e("onVegRetrieved: ", new GsonBuilder().create().toJson(mVegModelList));
        if(mVegModelList == null){
            Log.e( "onNonVegFoodRetrieved: ", "empty");
        } else {
            vhCanteenVegAdapter.add(mVegModelList);
            vhCanteenVegAdapter.notifyDataSetChanged();

            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onNonVegFoodRetrieved(List<FoodModel> mFoodModel) {
        Log.e("onNonVegRetrieved: ", new GsonBuilder().create().toJson(mFoodModel));
        if(mFoodModel == null){
            Log.e( "onNonVegFoodRetrieved: ", "empty");
        } else {
            vhCanteenNonVegAdapter.add(mFoodModel);
            vhCanteenNonVegAdapter.notifyDataSetChanged();

            binding.progressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onDrinksRetrieveFailure() {

    }

    @Override
    public void onVegFoodRetrieveFailure() {

    }

    @Override
    public void onNonVegFoodRetrieveFailure() {

    }

    @Override
    public void onDrinksDeleted(String s) {
        vhCanteenDrinksAdapter.notifyDataSetChanged();
        canteenHomePresenter.retrieveDrinksFromFirebase();
        ShowToast.withLongMessage(s);
    }

    @Override
    public void onVegDeleted(String s) {
        vhCanteenVegAdapter.notifyDataSetChanged();
        canteenHomePresenter.retrieveVegFromFirebase();
        ShowToast.withLongMessage(s);
    }

    @Override
    public void onNonVegDeleted(String s) {
        vhCanteenNonVegAdapter.notifyDataSetChanged();
        canteenHomePresenter.retrieveNonVegFromFirebase();
        ShowToast.withLongMessage(s);
    }

    private void swipeToRefresh(){
        binding.swipeToRefresh.setOnRefreshListener(() -> {
            canteenHomePresenter.retrieveDrinksFromFirebase();
            canteenHomePresenter.retrieveNonVegFromFirebase();
            canteenHomePresenter.retrieveVegFromFirebase();
            binding.swipeToRefresh.setRefreshing(false);
        });
    }

    private void prepareCanteenHomeRVAdapter() {
        CanteenHomeRVAdapter canteenHomeRVAdapter = new CanteenHomeRVAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(canteenHomeRVAdapter);
    }

    private class CanteenHomeRVAdapter extends AppRecyclerViewAdapter {

        @Override
        public void add(Object object) {

        }

        @Override
        public void clear() {

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            if (viewType == 1) {
                View drinksView = LayoutInflater.from(parent.getContext()).inflate(R.layout.canteen_home_drinks_cat, parent, false);
                return new VHCanteenDrinks(drinksView);
            }
            else if (viewType == 2) {
                View vegView = LayoutInflater.from(parent.getContext()).inflate(R.layout.canteen_home_veg_cat, parent, false);
                return new VHCanteenVeg(vegView);
            }
            else if (viewType == 3) {
                View nonVegView = LayoutInflater.from(parent.getContext()).inflate(R.layout.canteen_home_non_veg_cat, parent, false);
                return new VHCanteenNonVeg(nonVegView);
            }
            throw new RuntimeException("There is no any matching type " + viewType + " make sure you are using types correctly");
        }

        @Override
        public int getItemViewType(int position) {
            if (isDrinks(position)) {
                return 1;
            }
            if (isVeg(position)) {
                return 2;
            }

            return 3;
        }

            private boolean isDrinks(int position) {
    //            Log.e("isDrinks: ", String.valueOf(position));
                return position == 0;
            }

            private boolean isVeg(int position) {
    //            Log.e("isVeg: ", String.valueOf(position));
                return position == 1;
            }

            private boolean isNonVeg(int position) {
    //            Log.e("isNonVeg: ", String.valueOf(position));
                return position == 2;
            }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof VHCanteenDrinks) {
                VHCanteenDrinks vhCanteenDrinks = (VHCanteenDrinks) holder;
                prepareVHCanteenDrinksAdapter(vhCanteenDrinks.vhCanteenDrinksRv);
            }
            else if (holder instanceof VHCanteenVeg) {
                VHCanteenVeg vhCanteenVeg = (VHCanteenVeg) holder;
                prepareVHCanteenVegAdapter(vhCanteenVeg.vhCanteenVegRv);
            }
            else if(holder instanceof VHCanteenNonVeg){
                VHCanteenNonVeg vhCanteenNonVeg = (VHCanteenNonVeg) holder;
                prepareVHCanteenNonVegAdapter(vhCanteenNonVeg.vhCanteenNonVegRv);
            }

        }

        @Override
        public int getItemCount() {
            return 3;
        }


        private class VHCanteenDrinks extends RecyclerView.ViewHolder {

            RecyclerView vhCanteenDrinksRv;

            public VHCanteenDrinks(@NonNull View itemView) {
                super(itemView);
                vhCanteenDrinksRv = itemView.findViewById(R.id.canteen_home_drinks_cat_rv);
                vhCanteenDrinksRv.setHasFixedSize(true);

            }

        }

        private class VHCanteenVeg extends RecyclerView.ViewHolder {
            RecyclerView vhCanteenVegRv;

            public VHCanteenVeg(View vegView) {
                super(vegView);
                vhCanteenVegRv = vegView.findViewById(R.id.canteen_home_veg_cat_rv);
                vhCanteenVegRv.setHasFixedSize(true);
            }
        }

        private class VHCanteenNonVeg extends RecyclerView.ViewHolder{
            RecyclerView vhCanteenNonVegRv;

            public VHCanteenNonVeg(@NonNull View itemView) {
                super(itemView);
                vhCanteenNonVegRv = itemView.findViewById(R.id.canteen_home_non_veg_cat_rv);
                vhCanteenNonVegRv.setHasFixedSize(true);
            }
        }

    }

//    for drinks
    private void prepareVHCanteenDrinksAdapter(RecyclerView recyclerView) {
        vhCanteenDrinksAdapter = new VHCanteenDrinksAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(vhCanteenDrinksAdapter);
    }

    private class VHCanteenDrinksAdapter extends AppRecyclerViewAdapter {

        FoodModel foodModel;
        List<FoodModel> mDrinksList = new ArrayList<>();

        @Override
        public void add(Object object) {
//            type casting object to list to retrieve data
            mDrinksList = (List<FoodModel>) object;

//        foodModel = DefineClassType.getType(object, FoodModel.class);
//            assert foodModel != null;
            Log.e("add: ", new GsonBuilder().create().toJson(mDrinksList));
        }

        @Override
        public void clear() {

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.canteen_home_drinks_items, parent, false);
            return new VHCanteenDrinksItem(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            VHCanteenDrinksItem vhCanteenDrinksItem = (VHCanteenDrinksItem) holder;

                foodModel = mDrinksList.get(position);
                vhCanteenDrinksItem.name.setText(foodModel.getName());
                vhCanteenDrinksItem.price.setText(foodModel.getPrice());
                Glide.with(getApplicationContext()).load(foodModel.getImageUrl())
                        .apply(RequestOptions.signatureOf(new CenterCrop())).into(vhCanteenDrinksItem.img);

        }

        @Override
        public int getItemCount() {
//            return drinksModelList.size();
            if(mDrinksList == null){
                Log.e("DrinksSize: ", "Food size is empty");
                return 4;
            } else {
                return mDrinksList.size();
            }

        }

        private class VHCanteenDrinksItem extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView name, price;
            private ImageView img, more;
            private RelativeLayout relImg;

            public VHCanteenDrinksItem(View drinksView) {
                super(drinksView);
                name = drinksView.findViewById(R.id.canteen_home_drinks_title);
                price = drinksView.findViewById(R.id.canteen_home_drinks_price);
                img = drinksView.findViewById(R.id.canteen_home_drinks_img);
                more = drinksView.findViewById(R.id.more);
                relImg = drinksView.findViewById(R.id.rel_img);

                relImg.setOnClickListener(this);
                more.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id){
                    case R.id.rel_img:
                        ShowToast.withLongMessage("Image clicked");
                        break;
                    case R.id.more:
                        PopupMenu popupMenu = new PopupMenu(getApplicationContext(), img);
                        popupMenu.getMenuInflater().inflate(R.menu.delete_drinks_item, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                canteenHomePresenter.deleteDrinksItem(mDrinksList.get(getAdapterPosition()).getName());
//                                ShowToast.withLongMessage(mDrinksList.get(getAdapterPosition()).getName());

                                return true;
                            }

                        });
                        popupMenu.show();
                        break;
                }
            }
        }
    }

//    for veg food
    private void prepareVHCanteenVegAdapter(RecyclerView recyclerView) {
        vhCanteenVegAdapter = new VHCanteenVegAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(vhCanteenVegAdapter);
    }

    private class VHCanteenVegAdapter extends AppRecyclerViewAdapter {

        FoodModel foodModel;
        List<FoodModel> foodModelList;

        @Override
        public void add(Object object) {
            foodModelList = (List<FoodModel>) object;
            Log.e("add: ", new GsonBuilder().create().toJson(foodModelList));
        }

        @Override
        public void clear() {

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.canteen_home_veg_items, parent, false);
            return new VHCanteenVegItem(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            VHCanteenVegItem vhCanteenVegItem = (VHCanteenVegItem) holder;

            foodModel = foodModelList.get(position);
            vhCanteenVegItem.name.setText(foodModel.getName());
            vhCanteenVegItem.price.setText(foodModel.getPrice());
            Glide.with(getApplicationContext()).load(foodModel.getImageUrl())
                    .apply(RequestOptions.signatureOf(new CenterCrop())).into(vhCanteenVegItem.img);
        }

        @Override
        public int getItemCount() {
            if(foodModelList == null){
                return 0;
            } else {
                return foodModelList.size();
            }
        }

        private class VHCanteenVegItem extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView name, price;
            private ImageView img, more;
            private RelativeLayout relImg;

            public VHCanteenVegItem(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.canteen_home_veg_title);
                price = itemView.findViewById(R.id.canteen_home_veg_price);
                img = itemView.findViewById(R.id.canteen_home_veg_img);
                relImg = itemView.findViewById(R.id.veg_rel_img);
                more = itemView.findViewById(R.id.veg_more);

                relImg.setOnClickListener(this);
                more.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                if(v == relImg){
                    ShowToast.withLongMessage("clicked");
                } else if(v == more){
                    PopupMenu popupMenu = new PopupMenu(getApplicationContext(), img);
                    popupMenu.getMenuInflater().inflate(R.menu.delete_veg_item, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            canteenHomePresenter.deleteVegItem(foodModelList.get(getAdapterPosition()).getName());
//                            ShowToast.withLongMessage(foodModelList.get(getAdapterPosition()).getName());

                            return true;
                        }

                    });
                    popupMenu.show();
                }
            }
        }
    }

//    for non-veg food
    private void prepareVHCanteenNonVegAdapter(RecyclerView recyclerView){
        vhCanteenNonVegAdapter = new VHCanteenNonVegAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(vhCanteenNonVegAdapter);
    }

    private class VHCanteenNonVegAdapter extends AppRecyclerViewAdapter {

        FoodModel foodModel;
        List<FoodModel> foodModelList = new ArrayList<>();
        @Override
        public void add(Object object) {
            foodModelList = (List<FoodModel>) object;

            Log.e("NonVeg data: ", new GsonBuilder().create().toJson(foodModelList));
        }

        @Override
        public void clear() {

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.canteen_home_non_veg_items, parent, false);
            return new VHCanteenNonVegItem(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            VHCanteenNonVegItem vhCanteenNonVegItem = (VHCanteenNonVegItem) holder;

            foodModel = foodModelList.get(position);
            vhCanteenNonVegItem.name.setText(foodModel.getName());
            vhCanteenNonVegItem.price.setText(foodModel.getPrice());
            Glide.with(getApplicationContext()).load(foodModel.getImageUrl())
                    .apply(RequestOptions.signatureOf(new CenterCrop())).into(vhCanteenNonVegItem.img);
        }

        @Override
        public int getItemCount() {

            if(foodModelList.isEmpty()){
                Log.e("nonVegModel: ","empty" );
                return 0;
            } else {
                return foodModelList.size();
            }

        }

        private class VHCanteenNonVegItem extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView name, price;
            private ImageView img, more;
            private RelativeLayout relImg;

            public VHCanteenNonVegItem(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.canteen_home_non_veg_title);
                price = itemView.findViewById(R.id.canteen_home_non_veg_price);
                img = itemView.findViewById(R.id.canteen_home_non_veg_img);
                relImg = itemView.findViewById(R.id.non_veg_rel_img);
                more = itemView.findViewById(R.id.non_veg_more);

                relImg.setOnClickListener(this);
                more.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                if(v == relImg){
                    ShowToast.withLongMessage("clicked");
                } else if(v == more){
                    PopupMenu popupMenu = new PopupMenu(getApplicationContext(), img);
                    popupMenu.getMenuInflater().inflate(R.menu.delete_non_veg_item, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            canteenHomePresenter.deleteNonVegItem(foodModelList.get(getAdapterPosition()).getName());
//                            ShowToast.withLongMessage(foodModelList.get(getAdapterPosition()).getName());

                            return true;
                        }

                    });
                    popupMenu.show();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_drinks_fab:
                startActivity(new Intent(this, AddDrinksActivity.class));
                break;
            case R.id.add_veg_item_fab:
                startActivity(new Intent(this, AddVegActivity.class));
                break;
            case R.id.add_non_veg_item_fab:
                startActivity(new Intent(this, AddNonVegActivity.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.canteen_home_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.canteenGuestOrder) {
            startActivity(new Intent(CanteenHomeActivity.this, GuestOrdersActivity.class));
        }
        if(id == R.id.profile){
            startActivity(new Intent(CanteenHomeActivity.this, ProfileActivity.class));
        }
        if(id == R.id.totalOrderedFood){
            startActivity(new Intent(CanteenHomeActivity.this, TotalOrderedFoodActivity.class));
        }
        if(id == R.id.signOut){
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
//        get current user
        FirebaseUser firebaseUser = Utilities.firebaseAuth().getCurrentUser();
        if(firebaseUser != null) {
            Log.e("signOut: ", "user occupied");
            Utilities.firebaseAuth().signOut();
            Log.e("signOut: ", "sign out successful");
            Utilities.setLoggedIn(false);
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Do you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }


}
