package wilfridlaurier.vishnusukumaran.medtracker;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

public class MedScheduler {
    public void setAlarm(Context context, long alarmTime, Uri reminderTask) {
        AlarmManager manager = MedManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderMedService.getReminderPendingIntent(context, reminderTask);


        if (Build.VERSION.SDK_INT >= 28) {

            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation);


        }  else {

            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        }
    }

    public void setRepeatAlarm(Context context, long alarmTime, Uri reminderTask, long RepeatTime) {
        AlarmManager manager = MedManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderMedService.getReminderPendingIntent(context, reminderTask);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation);


    }

    public void cancelAlarm(Context context, Uri reminderTask) {
        AlarmManager manager = MedManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderMedService.getReminderPendingIntent(context, reminderTask);

        manager.cancel(operation);

    }
}
