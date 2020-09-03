package com.samir.cfos.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samir.cfos.R;
import com.samir.cfos.databinding.ActivityGuestOrderDetailsBinding;
import com.samir.cfos.helpers.AppActivity;
import com.samir.cfos.helpers.AppRecyclerViewAdapter;
import com.samir.cfos.helpers.NotificationHelper;

import java.util.Objects;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class GuestOrderDetailsActivity extends AppActivity {

    private ActivityGuestOrderDetailsBinding binding;
    private GuestOrderRVAdapter guestOrderRVAdapter;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGuestOrderDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initializeView();
        prepareGuestOrderRVAdapter();
        initializeListeners();
    }

    @Override
    protected void initializeView() {

        setSupportActionBar(binding.guestOrderDetailsToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.guest_order_rv);
    }

    @Override
    protected void initializeListeners() {


        binding.idFoodReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new NotificationHelper(getApplicationContext());

//                Intent intent = new Intent(getApplicationContext(), GuestOrderDetailsActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 111 /* Request code */, intent,
//                        FLAG_UPDATE_CURRENT);
//
//                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//                NotificationCompat.Builder notificationBuilder =
//                        new NotificationCompat.Builder(getApplicationContext(), "channelId")
//                                .setSmallIcon(R.drawable.ic_launcher_background)
//                                .setContentTitle("title")
//                                .setContentText("body")
//                                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
//                                .setAutoCancel(true)
//                                .setSound(defaultSoundUri)
//                                .setContentIntent(pendingIntent);
//
//
//                // Since android Oreo notification channel is needed.
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                    AudioAttributes attributes = new AudioAttributes.Builder()
//                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                            .build();
//
//                    NotificationChannel channel = new NotificationChannel("channelId",
//                            "Channel human readable title",
//                            NotificationManager.IMPORTANCE_HIGH);
//                    channel.enableLights(true);
//                    channel.enableVibration(true);
//                    channel.setLightColor(Color.BLUE);
//                    channel.setSound(defaultSoundUri, attributes);
//                    channel.setVibrationPattern(new long[] {
//                            500,
//                            500,
//                            500,
//                            500,
//                            500
//                    });
//                    channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//
//                    NotificationManager notificationManager =  getSystemService(NotificationManager.class);
//                    notificationManager.createNotificationChannel(channel);
//                    notificationManager.notify(999 /* ID of notification */, notificationBuilder.build());
//
//                } else {
//
//                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
//                    notificationManagerCompat.notify(999, notificationBuilder.build());
//
//                }

            }
        });
    }

    private void prepareGuestOrderRVAdapter(){
        guestOrderRVAdapter = new GuestOrderRVAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.guestOrderRv.setLayoutManager(linearLayoutManager);
        binding.guestOrderRv.setAdapter(guestOrderRVAdapter);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_ordered_rv_item, parent, false);
            return new GuestOrderVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

        private class GuestOrderVH extends RecyclerView.ViewHolder{

            public GuestOrderVH(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
