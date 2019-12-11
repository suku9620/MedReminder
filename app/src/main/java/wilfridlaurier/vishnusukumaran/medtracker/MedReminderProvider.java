//MedTracker project
//Author: Vishnu Sukumaran - Wilfrid Laurier University
// URI class to identify records or resources
//

package wilfridlaurier.vishnusukumaran.medtracker;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

public class MedReminderProvider extends ContentProvider {

    public static final String LOG_TAG = MedReminderProvider.class.getSimpleName();

    private static final int REMINDER = 100;

    private static final int REMINDER_ID = 101;


    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(MedReminderContract.CONTENT_AUTHORITY, MedReminderContract.PATH_VEHICLE, REMINDER);

        sUriMatcher.addURI(MedReminderContract.CONTENT_AUTHORITY, MedReminderContract.PATH_VEHICLE + "/#", REMINDER_ID);

    }

    private MedReminderDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new MedReminderDbHelper(getContext());
        return true;
    }

    /**
     * Function to query
     * @param uri uri
     * @param projection projection
     * @param selection selection
     * @param selectionArgs selectionArgs
     * @param sortOrder sortOrder
     * @return return
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                cursor = database.query(MedReminderContract.AlarmReminderEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case REMINDER_ID:
                selection = MedReminderContract.AlarmReminderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(MedReminderContract.AlarmReminderEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }


        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Function that returns content type
     * @param uri uri
     * @return return
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                return MedReminderContract.AlarmReminderEntry.CONTENT_LIST_TYPE;
            case REMINDER_ID:
                return MedReminderContract.AlarmReminderEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }


    /**
     * Function to insert the record
     * @param uri uri
     * @param contentValues contentValues
     * @return return
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                return insertReminder(uri, contentValues);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Function to insert the reminder
     * @param uri uri
     * @param values values
     * @return return
     */
    private Uri insertReminder(Uri uri, ContentValues values) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(MedReminderContract.AlarmReminderEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Function to delete the record
     * @param uri uri
     * @param selection selection
     * @param selectionArgs selectionArgs
     * @return return
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                rowsDeleted = database.delete(MedReminderContract.AlarmReminderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REMINDER_ID:
                selection = MedReminderContract.AlarmReminderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(MedReminderContract.AlarmReminderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    /**
     * Function to get data and pass to updatereminder()
     * @param uri uri
     * @param contentValues contentValues
     * @param selection selection
     * @param selectionArgs selectionArgs
     * @return return
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                return updateReminder(uri, contentValues, selection, selectionArgs);
            case REMINDER_ID:
                selection = MedReminderContract.AlarmReminderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateReminder(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Function to update the reminders
     * @param uri uri
     * @param values values
     * @param selection selection
     * @param selectionArgs selectionArgs
     * @return return
     */
    private int updateReminder(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(MedReminderContract.AlarmReminderEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }



}
