package com.agropredict.infrastructure.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.agropredict.R;
import com.agropredict.application.service.INotificationService;
import com.agropredict.presentation.user_interface.screen.HomeActivity;

public final class CropNotificationService implements INotificationService {
    static final String CHANNEL_ALERTS = "agropredict_alerts";
    static final String CHANNEL_REMINDERS = "agropredict_reminders";
    private static final int ID_SEVERE_DIAGNOSTIC = 1001;

    private final Context context;

    public CropNotificationService(Context context) {
        this.context = context.getApplicationContext();
    }

    public static void createChannels(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;
        NotificationManager manager = context.getSystemService(NotificationManager.class);

        NotificationChannel alerts = new NotificationChannel(
                CHANNEL_ALERTS,
                context.getString(R.string.notif_channel_alerts),
                NotificationManager.IMPORTANCE_HIGH);
        alerts.setDescription(context.getString(R.string.notif_channel_alerts_desc));

        NotificationChannel reminders = new NotificationChannel(
                CHANNEL_REMINDERS,
                context.getString(R.string.notif_channel_reminders),
                NotificationManager.IMPORTANCE_DEFAULT);
        reminders.setDescription(context.getString(R.string.notif_channel_reminders_desc));

        manager.createNotificationChannel(alerts);
        manager.createNotificationChannel(reminders);
    }

    @Override
    public void alertSevereDiagnosis() {
        Intent intent = new Intent(context, HomeActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pending = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ALERTS)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.notif_severe_title))
                .setContentText(context.getString(R.string.notif_severe_text))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pending)
                .setAutoCancel(true);

        try {
            NotificationManagerCompat.from(context).notify(ID_SEVERE_DIAGNOSTIC, builder.build());
        } catch (SecurityException ignored) {
            // POST_NOTIFICATIONS not granted — silently skip
        }
    }
}
