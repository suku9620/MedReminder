
// MedTracker project
// Author: Vishnu Sukumaran - Wilfrid Laurier University
// Reciever class to accept alert/vibration


package wilfridlaurier.vishnusukumaran.medtracker;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;

public class MedBroadcastReceiver extends BroadcastReceiver {
    /**
     * On receive function for notification
     * @param context context
     * @param intent intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, context.getResources().getString(R.string.checkmed), Toast.LENGTH_SHORT).show();
        Vibrator vibrator=(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(1000,VibrationEffect.DEFAULT_AMPLITUDE));



    }
}
