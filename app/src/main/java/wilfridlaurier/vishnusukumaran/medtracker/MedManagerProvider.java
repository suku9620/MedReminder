package wilfridlaurier.vishnusukumaran.medtracker;
import android.app.AlarmManager;
import android.content.Context;
public class MedManagerProvider {
    private static final String TAG = MedManagerProvider.class.getSimpleName();
    private static AlarmManager sAlarmManager;
    public static synchronized void injectAlarmManager(AlarmManager alarmManager) {
        if (sAlarmManager != null) {
            throw new IllegalStateException("Alarm Manager Already Set");
        }
        sAlarmManager = alarmManager;
    }

    /**
     * Function to call the alarm service
     * @param context context
     * @return AlarmManager
     */
    /*package*/ static synchronized AlarmManager getAlarmManager(Context context) {
        if (sAlarmManager == null) {
            sAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return sAlarmManager;
    }
}
