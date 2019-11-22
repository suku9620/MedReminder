package wilfridlaurier.vishnusukumaran.medtracker;

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
import androidx.appcompat.widget.*;

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


    private static final int VEHICLE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);


        reminderListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        reminderListView.setEmptyView(emptyView);

        mCursorAdapter = new MedCursorAdapter(this, null);
        reminderListView.setAdapter(mCursorAdapter);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, AddReminder.class);

                Uri currentVehicleUri = ContentUris.withAppendedId(MedReminderContract.AlarmReminderEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentVehicleUri);

                startActivity(intent);

            }
        });


        mAddReminderButton = (FloatingActionButton) findViewById(R.id.fab);

        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddReminder.class);
                startActivity(intent);
            }
        });
        LoaderManager.getInstance(this).initLoader(VEHICLE_LOADER, null, this).forceLoad();
       // getSupportLoaderManager().initLoader(VEHICLE_LOADER, null, this);



    }

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

        return new CursorLoader(this,   // Parent activity context
                MedReminderContract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }

    }

