package com.samir.cfos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samir.cfos.R;
import com.samir.cfos.helpers.AppActivity;
import com.samir.cfos.helpers.AppRecyclerViewAdapter;
import com.samir.cfos.helpers.ShowToast;

public class GuestOrdersActivity extends AppActivity {

    private Toolbar toolbar;
    private RecyclerView guestOrderRv;
    GuestOrderRVAdapter guestOrderRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_orders);

        initializeView();
        initializeListeners();

        prepareGuestOrderRVAdapter();
    }

    @Override
    protected void initializeView() {
        toolbar = findViewById(R.id.canteen_home_guest_order_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        guestOrderRv = findViewById(R.id.guest_order_rv);
    }

    @Override
    protected void initializeListeners() {

    }

    private void prepareGuestOrderRVAdapter(){
        guestOrderRVAdapter = new GuestOrderRVAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        guestOrderRv.setLayoutManager(linearLayoutManager);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(guestOrderRv);
        guestOrderRv.setAdapter(guestOrderRVAdapter);
    }

    private class GuestOrderRVAdapter extends AppRecyclerViewAdapter {

        @Override
        public void add(Object object) {

        }

        @Override
        public void clear() {

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guest_order_items, parent, false);
            return new VHGuestOrders(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            VHGuestOrders vhGuestOrders = (VHGuestOrders) holder;

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        private class VHGuestOrders extends RecyclerView.ViewHolder implements View.OnClickListener {
            LinearLayout guest_order_list;
            TextView guestName, guestRole, orderedTime, foodPrice, foodName, foodQty, foodOrderedTime;
            ImageView guestImg;

            public VHGuestOrders(@NonNull View itemView) {
                super(itemView);

                guest_order_list = itemView.findViewById(R.id.guest_order_list);

                guestName = itemView.findViewById(R.id.guest_name);
                guestRole = itemView.findViewById(R.id.guest_role);
                orderedTime = itemView.findViewById(R.id.guest_ordered_time);
                guestImg = itemView.findViewById(R.id.guest_img);

                guest_order_list.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.guest_order_list) {

                    Log.e("onClick: ", "ordered details");
//                    ShowToast.withMessage("ordered details");
                        startActivity(new Intent(GuestOrdersActivity.this, GuestOrderDetailsActivity.class));
                }
            }
        }
    }
    ItemTouchHelper.SimpleCallback itemTouchHelper =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                    mList.remove(viewHolder.getAdapterPosition());
                    guestOrderRVAdapter.notifyDataSetChanged();
                }
            };
}
