package wilfridlaurier.vishnusukumaran.medtracker;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MedReminderDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "medreminder.db";

    private static final int DATABASE_VERSION = 1;

    public MedReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_ALARM_TABLE =  "CREATE TABLE " + MedReminderContract.AlarmReminderEntry.TABLE_NAME + " ("
                + MedReminderContract.AlarmReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MedReminderContract.AlarmReminderEntry.KEY_TITLE + " TEXT NOT NULL, "
                + MedReminderContract.AlarmReminderEntry.KEY_DATE + " TEXT NOT NULL, "
                + MedReminderContract.AlarmReminderEntry.KEY_TIME + " TEXT NOT NULL, "
                + MedReminderContract.AlarmReminderEntry.KEY_REPEAT + " TEXT NOT NULL, "
                + MedReminderContract.AlarmReminderEntry.KEY_REPEAT_NO + " TEXT NOT NULL, "
                + MedReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE + " TEXT NOT NULL, "
                + MedReminderContract.AlarmReminderEntry.KEY_ACTIVE + " TEXT NOT NULL " + " );";


        sqLiteDatabase.execSQL(SQL_CREATE_ALARM_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
