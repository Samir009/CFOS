package com.samir.cfos.fcm;

import android.util.Log;

import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class AndroidFirebaseMessagingService extends FirebaseMessagingService {

//    when new device install this application, the token of that device is shown here
    @Override
    public void onNewToken(String s) {

        super.onNewToken(s);

        Log.e("newToken", s);

    }

//    when application receives foreground notification data is loaded here
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e("onMessageReceived: ", remoteMessage.getData().toString() + remoteMessage.getMessageId() + "\n" + remoteMessage.getMessageType());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e("Message Noti Body: ", remoteMessage.getNotification().getTitle() +
                    "/n" + remoteMessage.getNotification().getBody() + "/n" + remoteMessage.getNotification().getChannelId());

//            sends notification
            sendNotification(remoteMessage.getNotification().getChannelId(), remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        } else {
            Log.e("onMessageReceived: ", "Notification message is empty !!!");
        }

    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);

        Log.e("onMessageSent: ", s);
    }

    @Override
    public void onSendError(@NonNull String s, @NonNull Exception e) {
        super.onSendError(s, e);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.e("onDeletedMessages: ", "Firebase deleted pending messages");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void sendNotification(String id, String title, String body) {

//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST /* Request code */, intent,
//                FLAG_UPDATE_CURRENT);
//
//        String channelId = getString(R.string.default_notification_channel_id);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, channelId)
//                        .setSmallIcon(R.drawable.ic_launcher_background)
//                        .setContentTitle(title)
//                        .setContentText(body)
//                        .setSmallIcon(R.drawable.ic_notifications_black_24dp)
//                        .setAutoCancel(true)
//                        .setColor(getResources().getColor(R.color.colorPrimary))
//                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_notifications_black_24dp))
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            NotificationChannel channel = new NotificationChannel(channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_HIGH);
//            channel.enableLights(true);
//            channel.enableVibration(true);
//            channel.setLightColor(R.color.colorPrimary);
//            channel.setVibrationPattern(new long[] {500, 500, 500, 500, 500});
//            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.createNotificationChannel(channel);
//            notificationManager.notify(NOTIFICATION_REQUEST /* ID of notification */, notificationBuilder.build());
//
//        } else {
//
//            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//            notificationManagerCompat.notify(NOTIFICATION_REQUEST, notificationBuilder.build());
//
//        }

    }

}