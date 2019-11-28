
// MedTracker project
// Author: Vishnu Sukumaran - Wilfrid Laurier University
// Service class for maintaining service of reminding user about the alarm


package wilfridlaurier.vishnusukumaran.medtracker;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

public class ReminderMedService extends IntentService {

    private static final String TAG = ReminderMedService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;

    Cursor cursor;

    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, ReminderMedService.class);
        action.setData(uri);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderMedService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Uri uri = intent.getData();

        Intent action = new Intent(this, AddReminder.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        if(uri != null){
            cursor = getContentResolver().query(uri, null, null, null, null);
        }

        String description = "";
        try {
            if (cursor != null && cursor.moveToFirst()) {
                description = MedReminderContract.getColumnString(cursor, MedReminderContract.AlarmReminderEntry.KEY_TITLE);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(description,"MyChannel",importance);
        channel.setDescription(description);
        manager.createNotificationChannel(channel);
        Notification note = new NotificationCompat.Builder(this,description)
                .setContentTitle(getString(R.string.reminder_title))
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .build();

            manager.notify(NOTIFICATION_ID, note);
        Toast.makeText(this, "Check MedTracker for Medicine!!!", Toast.LENGTH_SHORT).show();
        Vibrator vibrator=(Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(1000,VibrationEffect.DEFAULT_AMPLITUDE));

    }
}
