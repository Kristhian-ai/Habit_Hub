package com.example.HabitTracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // This method is called when a new message is received.
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM message here
        if (remoteMessage.getData().size() > 0) {
            // If there is data in the message, handle it
            String message = remoteMessage.getData().get("message");
            sendNotification(message);
        }

        if (remoteMessage.getNotification() != null) {
            // If there is a notification payload, handle it
            String body = remoteMessage.getNotification().getBody();
            sendNotification(body);
        }
    }

    private void sendNotification(String messageBody) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "habit_tracker_channel";

        // For Android 8.0 and above, create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Habit Tracker Notifications";
            String description = "Notifications for Habit Tracker app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        // Create a notification
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification_icon) // Set your notification icon
                .setContentTitle("Habit Tracker")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .build();

        // Show the notification
        notificationManager.notify(0, notification);
    }
}
