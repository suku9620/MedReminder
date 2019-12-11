
// MedTracker project
// Author: Vishnu Sukumaran - Wilfrid Laurier University
// Set of functions to schedule alarm


package wilfridlaurier.vishnusukumaran.medtracker;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;


public class MedScheduler {
    /**
     * Function to setting an alarm for one time
     * @param context context
     * @param alarmTime alarmTime
     * @param reminderTask reminderTask
     */
    public void setAlarm(Context context, long alarmTime, Uri reminderTask) {
        AlarmManager manager = MedManagerProvider.getAlarmManager(context);

        Intent intent= new Intent(context, MedBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context , 23433, intent,0);

        PendingIntent operation =
                ReminderMedService.getReminderPendingIntent(context, reminderTask);


        if (Build.VERSION.SDK_INT >= 28) {

            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation);


        }  else {

            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        }
    }

    /**
     * Function to Repeating alarms
     * @param context context
     * @param alarmTime alarmTime
     * @param reminderTask reminderTask
     * @param RepeatTime RepeatTime
     */
    public void setRepeatAlarm(Context context, long alarmTime, Uri reminderTask, long RepeatTime) {
        AlarmManager manager = MedManagerProvider.getAlarmManager(context);
        Intent intent= new Intent(context, MedBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context , 23433, intent,0);
        PendingIntent operation =
                ReminderMedService.getReminderPendingIntent(context, reminderTask);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation);


    }

    /**
     * Function to cancel the alarm
     * @param context context
     * @param reminderTask reminderTask
     */
    public void cancelAlarm(Context context, Uri reminderTask) {
        AlarmManager manager = MedManagerProvider.getAlarmManager(context);
        Intent intent= new Intent(context, MedBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context , 23433, intent,0);
        PendingIntent operation =
                ReminderMedService.getReminderPendingIntent(context, reminderTask);

        manager.cancel(pendingIntent);

    }
}
