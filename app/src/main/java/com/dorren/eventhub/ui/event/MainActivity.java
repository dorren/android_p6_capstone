package com.dorren.eventhub.ui.event;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.dorren.eventhub.R;
import com.dorren.eventhub.data.EventContentProvider;
import com.dorren.eventhub.model.Event;
import com.dorren.eventhub.ui.user.LoginActivity;
import com.dorren.eventhub.util.AppUtil;
import com.dorren.eventhub.util.PreferenceUtil;

public class MainActivity extends AppCompatActivity implements
        EventListFragment.EventListFragmentListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MainActivity";
    private EventListFragment mEventListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEventListFragment = (EventListFragment) getFragmentManager().
                                findFragmentById(R.id.main_event_list_fragment);

        getSupportLoaderManager().initLoader(AppUtil.EVENTS_CURSOR_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_logout) {
            PreferenceUtil.logout(this);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, AppUtil.LOGIN_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                EventContentProvider.EVENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mEventListFragment.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(Event event) {
        //openDetailPage(event);
    }

    private void openDetailPage(Event event) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        String json = event.toString();
        intent.putExtra(Intent.EXTRA_TEXT, json);
        startActivity(intent);
    }
}
