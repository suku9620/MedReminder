
// MedTracker project
// Author: Vishnu Sukumaran - Wilfrid Laurier University
// Main activity

package wilfridlaurier.vishnusukumaran.medtracker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.loader.app.LoaderManager;
import android.content.ContentUris;
import androidx.loader.content.Loader;
import androidx.loader.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton mAddReminderButton;
    private Toolbar mToolbar;
    MedCursorAdapter mCursorAdapter;
    MedReminderDbHelper alarmReminderDbHelper = new MedReminderDbHelper(this);
    ListView reminderListView;
    private static final int LOADER = 0;

    /**
     * Oncreate function to load the UI which displays saved reminders by default
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.setTitle(R.string.app_name);
        reminderListView =  findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        reminderListView.setEmptyView(emptyView);
        mCursorAdapter = new MedCursorAdapter(this, null);
        reminderListView.setAdapter(mCursorAdapter);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AddReminder.class);
                Uri currentVehicleUri = ContentUris.withAppendedId(MedReminderContract.AlarmReminderEntry.CONTENT_URI, id);
                intent.setData(currentVehicleUri);
                startActivity(intent);

            }
        });

        mAddReminderButton =  findViewById(R.id.fab);

        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddReminder.class);
                startActivity(intent);
            }
        });
        LoaderManager.getInstance(this).initLoader(LOADER, null, this).forceLoad();


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
                MedReminderContract.AlarmReminderEntry.KEY_ACTIVE

        };

        return new CursorLoader(this,
                MedReminderContract.AlarmReminderEntry.CONTENT_URI,
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
        mCursorAdapter.swapCursor(cursor);

    }

    /**
     * Called when a previously created loader is being reset, and thus making its data unavailable
     * @param loader loader
     */
    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }



}

