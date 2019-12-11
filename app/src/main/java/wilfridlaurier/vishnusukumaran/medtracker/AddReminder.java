
 //MedTracker project
 //Author: Vishnu Sukumaran - Wilfrid Laurier University
 //Add Medicine Reminder form that helps use
 //

package wilfridlaurier.vishnusukumaran.medtracker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.AsyncTask;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.loader.content.CursorLoader;
import androidx.core.app.NavUtils;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import java.util.Calendar;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import java.text.DateFormat;
import java.util.Objects;
import android.app.NotificationManager;

public class AddReminder extends AppCompatActivity implements
         LoaderManager.LoaderCallbacks<Cursor>  {


    private static final int EXISTING_LOADER = 0;
    private EditText mTitleText;
    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private long mRepeatTime;
    private Switch mRepeatSwitch;
    private String mTitle;
    private String mTime;
    private String mDate;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;
    private Uri mCurrentReminderUri;
    private boolean mVehicleHasChanged = false;
    ProgressBar progressBar;


    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_ACTIVE = "active_key";


    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;


    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mVehicleHasChanged = true;
            return false;
        }
    };


    /**
     * Oncreate function to load the add record page UI or show the details of the record selected from Main activity
     * @param savedInstanceState reference Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        Intent intent = getIntent();
        mCurrentReminderUri = intent.getData();

        if (mCurrentReminderUri == null) {

            setTitle(R.string.editor_activity_title_new_reminder);
            invalidateOptionsMenu();
        } else {


            setTitle(R.string.editor_activity_title_new_reminder);
            LoaderManager.getInstance(this).initLoader(EXISTING_LOADER, null, this).forceLoad();

        }



        Toolbar mToolbar =  findViewById(R.id.toolbar);
        mTitleText =  findViewById(R.id.reminder_title);
        mDateText =  findViewById(R.id.set_date);
        mTimeText =  findViewById(R.id.set_time);
        mRepeatText =  findViewById(R.id.set_repeat);
        mRepeatNoText = findViewById(R.id.set_repeat_no);
        mRepeatTypeText =  findViewById(R.id.set_repeat_type);
        mRepeatSwitch =  findViewById(R.id.repeat_switch);
        mFAB1 =  findViewById(R.id.starred1);
        mFAB2 =  findViewById(R.id.starred2);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        //Initializing with defaults
        mActive = "true";
        mRepeat = "true";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "Hour";
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);
        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;

        // Setup Reminder Title EditText
        mTitleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                mTitleText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Setup TextViews using reminder values
        mDateText.setText(mDate);
        mTimeText.setText(mTime);
        mRepeatNoText.setText(mRepeatNo);
        mRepeatTypeText.setText(mRepeatType);
        mRepeatText.setText(getString(R.string.time_string,mRepeatNo,mRepeatType));

        // To save state on device rotation
        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            mTitleText.setText(savedTitle);
            mTitle = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            mDateText.setText(savedDate);
            mDate = savedDate;

            String saveRepeat = savedInstanceState.getString(KEY_REPEAT);
            mRepeatText.setText(saveRepeat);
            mRepeat = saveRepeat;

            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            mRepeatNoText.setText(savedRepeatNo);
            mRepeatNo = savedRepeatNo;

            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            mRepeatTypeText.setText(savedRepeatType);
            mRepeatType = savedRepeatType;

            mActive = savedInstanceState.getString(KEY_ACTIVE);
        }


        assert mActive != null;
        if (mActive.equals("false")) {
            mFAB1.show();
            mFAB2.hide();

        } else if (mActive.equals("true")) {
            mFAB1.hide();
            mFAB2.show();
        }

        setSupportActionBar(mToolbar);
        if(mCurrentReminderUri==null)
        {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.editor_activity_title_new_reminder);
        }
        else
        {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.editor_activity_title_edit_reminder);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setHomeButtonEnabled(true);
    }


    /**
     * Function to store the state of the object
     * @param outState bundle
     */
    @Override
    protected void onSaveInstanceState (@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_DATE, mDateText.getText());
        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_REPEAT_NO, mRepeatNoText.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, mRepeatTypeText.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);
    }
    Calendar now = Calendar.getInstance();

    /**
     * To set the time for the record
     * @param v view
     */

    public void setTime(View v){


        TimePickerDialog.OnTimeSetListener mTimeListener =
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        now.set(Calendar.MINUTE, minute);
                        mHour = hourOfDay;
                        mMinute = minute;
                        if (minute< 10) {
                            mTime = hourOfDay + ":" + "0" + minute;
                        } else {
                            mTime = hourOfDay+ ":" + minute;
                        }
                        mTimeText.setText(mTime);

                    }


                };


        new TimePickerDialog(AddReminder.this, mTimeListener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE), true).show();


    }

    /**
     * To set the date for the record
     * @param v view
     */
    public void setDate(View v){

        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                now.set(Calendar.YEAR, year);
                now.set(Calendar.MONTH, monthOfYear);
                now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                monthOfYear ++;
                mDay = dayOfMonth;
                mMonth = monthOfYear;
                mYear = year;
                mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
                mDateText.setText(mDate);
            }
        };


        new DatePickerDialog(AddReminder.this, mDateListener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)).show();


    }


    /**
     * Function to show the reminder is active
     * @param v view
     */
    public void selectFab1(View v) {
        mFAB1 = findViewById(R.id.starred1);
        mFAB1.hide();
        mFAB2 =  findViewById(R.id.starred2);
        mFAB2.show();
        mActive = "true";
    }

    /**
     * Function to show the record is inactive
     * @param v view
     */

    public void selectFab2(View v) {
        mFAB2 =  findViewById(R.id.starred2);
        mFAB2.hide();
        mFAB1 =  findViewById(R.id.starred1);
        mFAB1.show();
        mActive = "false";
    }

    /**
     * Function to mark the repetition checkbox
     * @param view view
     */
    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            mRepeat = "true";

            mRepeatText.setText(getString(R.string.time_string,mRepeatNo,mRepeatType));
        } else {
            mRepeat = "false";
            mRepeatText.setText(R.string.repeat_off);
        }
    }

    /**
     * Function to select the repeat type
     * @param v view
     */
    public void selectRepeatType(View v){
        final String[] items = new String[5];

        items[0] = "Minute";
        items[1] = "Hour";
        items[2] = "Day";
        items[3] = "Week";
        items[4] = "Month";


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mRepeatType = items[item];
                mRepeatTypeText.setText(mRepeatType);

                mRepeatText.setText(getString(R.string.time_string,mRepeatNo,mRepeatType));
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Function to set the repetition number
     * @param v view
     */
    public void setRepeatNo(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Number");


        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (input.getText().toString().length() == 0) {
                            mRepeatNo = Integer.toString(1);
                            mRepeatNoText.setText(mRepeatNo);

                            mRepeatText.setText(getString(R.string.time_string,mRepeatNo,mRepeatType));
                        }
                        else {
                            mRepeatNo = input.getText().toString().trim();
                            mRepeatNoText.setText(mRepeatNo);

                            mRepeatText.setText(getString(R.string.time_string,mRepeatNo,mRepeatType));
                        }
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }

    /**
     * Function to specify the menu
     * @param menu menu
     * @return menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }

    /**
     * Function to define the menu view
     * @param menu menu
     * @return menu
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mCurrentReminderUri == null) {
            MenuItem menuItem = menu.findItem(R.id.discard_reminder);
            menuItem.setVisible(false);
        }
        return true;
    }

    /**
     * Function to specify the actions for each menu item selected
     * @param item item
     * @return item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.save_reminder:


                if (mTitleText.getText().toString().length() == 0){

                    Snackbar.make(findViewById(R.id.toolbar), "Reminder title cannot be blank!",
                            Snackbar.LENGTH_SHORT)
                            .show();

                }

                else {
                    saveReminder();
                    finish();
                }
                return true;

            case R.id.discard_reminder:

                showDeleteConfirmationDialog();
                return true;

            case R.id.about:
                Toast.makeText(this, R.string.about_msg, Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                if (!mVehicleHasChanged) {
                    NavUtils.navigateUpFromSameTask(AddReminder.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                NavUtils.navigateUpFromSameTask(AddReminder.this);
                            }
                        };


                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Custom dialog to inform user about unsaved changes
     * @param discardButtonClickListener listener
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Custom dialog for showing delete confirmation
     */
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteReminder();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteReminder() {
        int rowsDeleted = getContentResolver().delete(mCurrentReminderUri, null, null);
        DeleteReminder deleteReminder = new DeleteReminder();
        deleteReminder.execute();
        if (rowsDeleted == 0) {

            Toast.makeText(this, getString(R.string.editor_delete_reminder_failed),
                    Toast.LENGTH_SHORT).show();

        } else {


            Toast.makeText(this, getString(R.string.editor_delete_reminder_successful),
                    Toast.LENGTH_SHORT).show();

        }


        finish();
    }
    private class DeleteReminder extends AsyncTask<String, Integer, String> {
        @Override
        protected  void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            if (mCurrentReminderUri != null) {
                publishProgress(25);
                new MedScheduler().cancelAlarm(getApplicationContext(),mCurrentReminderUri);
                publishProgress(50);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                assert notificationManager != null;
                publishProgress(75);
                notificationManager.deleteNotificationChannel(mTitle);
                publishProgress(100);

            }

            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * Function to save a reminder record
     */
    public void saveReminder(){

      /*if (mCurrentReminderUri == null ) {
            // Since no fields were modified, we can return early without creating a new reminder.
            return;
        }*/
        ContentValues values = new ContentValues();
        values.put(MedReminderContract.AlarmReminderEntry.KEY_TITLE, mTitle);
        values.put(MedReminderContract.AlarmReminderEntry.KEY_DATE, mDate);
        values.put(MedReminderContract.AlarmReminderEntry.KEY_TIME, mTime);
        values.put(MedReminderContract.AlarmReminderEntry.KEY_REPEAT, mRepeat);
        values.put(MedReminderContract.AlarmReminderEntry.KEY_REPEAT_NO, mRepeatNo);
        values.put(MedReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE, mRepeatType);
        values.put(MedReminderContract.AlarmReminderEntry.KEY_ACTIVE, mActive);



        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        long selectedTimestamp =  mCalendar.getTimeInMillis();


        switch (mRepeatType) {
            case "Minute":
                mRepeatTime = Integer.parseInt(mRepeatNo) * milMinute;
                break;
            case "Hour":
                mRepeatTime = Integer.parseInt(mRepeatNo) * milHour;
                break;
            case "Day":
                mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;
                break;
            case "Week":
                mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;
                break;
            case "Month":
                mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;
                break;
        }

        if (mCurrentReminderUri == null) {
            Uri newUri = getContentResolver().insert(MedReminderContract.AlarmReminderEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_reminder_successful),
                        Toast.LENGTH_SHORT).show();

            }
        } else {

            int rowsAffected = getContentResolver().update(mCurrentReminderUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }


        if (mActive.equals("true")) {
            if (mRepeat.equals("true")) {
                new MedScheduler().setRepeatAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri, mRepeatTime);
            } else if (mRepeat.equals("false")) {
                new MedScheduler().setAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri);
            }

            Toast.makeText(this, "Alarm time is " + DateFormat.getTimeInstance(DateFormat.SHORT).format(mCalendar.getTime()),
                    Toast.LENGTH_LONG).show();



        }
        Toast.makeText(getApplicationContext(), "Saved",
                Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    /**
     * Instantiate and return a new Loader for the given ID.
     * @param i i
     * @param bundle bundle
     * @return return
     */

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                MedReminderContract.AlarmReminderEntry._ID,
                MedReminderContract.AlarmReminderEntry.KEY_TITLE,
                MedReminderContract.AlarmReminderEntry.KEY_DATE,
                MedReminderContract.AlarmReminderEntry.KEY_TIME,
                MedReminderContract.AlarmReminderEntry.KEY_REPEAT,
                MedReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                MedReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                MedReminderContract.AlarmReminderEntry.KEY_ACTIVE,
        };


        return new CursorLoader(this,
                mCurrentReminderUri,
                projection,
                null,
                null,
                null);
    }

    /**
     * Called when a previously created loader has finished its load
     * @param loader loader
     * @param cursor cursor
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(MedReminderContract.AlarmReminderEntry.KEY_TITLE);
            int dateColumnIndex = cursor.getColumnIndex(MedReminderContract.AlarmReminderEntry.KEY_DATE);
            int timeColumnIndex = cursor.getColumnIndex(MedReminderContract.AlarmReminderEntry.KEY_TIME);
            int repeatColumnIndex = cursor.getColumnIndex(MedReminderContract.AlarmReminderEntry.KEY_REPEAT);
            int repeatNoColumnIndex = cursor.getColumnIndex(MedReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);
            int repeatTypeColumnIndex = cursor.getColumnIndex(MedReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE);
            int activeColumnIndex = cursor.getColumnIndex(MedReminderContract.AlarmReminderEntry.KEY_ACTIVE);


            String title = cursor.getString(titleColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String time = cursor.getString(timeColumnIndex);
            String repeat = cursor.getString(repeatColumnIndex);
            String repeatNo = cursor.getString(repeatNoColumnIndex);
            String repeatType = cursor.getString(repeatTypeColumnIndex);
            String active = cursor.getString(activeColumnIndex);




            mTitleText.setText(title);
            mDateText.setText(date);
            mTimeText.setText(time);
            mRepeatNoText.setText(repeatNo);
            mRepeatTypeText.setText(repeatType);

            mRepeatText.setText(getString(R.string.time_string,mRepeatNo,mRepeatType));
           
           
            if (repeat.equals("false")) {
                mRepeatSwitch.setChecked(false);
                mRepeatText.setText(R.string.repeat_off);

            } else if (repeat.equals("true")) {
                mRepeatSwitch.setChecked(true);
            }

        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }



}
