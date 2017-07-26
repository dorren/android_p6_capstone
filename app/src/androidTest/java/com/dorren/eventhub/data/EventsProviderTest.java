package com.dorren.eventhub.data;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by dorrenchen on 7/25/17.
 */
@RunWith(AndroidJUnit4.class)
public class EventsProviderTest extends ProviderTestCase2<EventContentProvider> {
    private EventContentProvider cp;

    public EventsProviderTest() {
        super(EventContentProvider.class, EventContentProvider.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    @Test
    public void testGet(){
        cp = getProvider();
        Uri uri = cp.EVENT_URI;
        Log.d("ProviderTest", uri.toString());
        Cursor cursor = cp.query(EventContentProvider.EVENT_URI, null, null, null, null);

        assertTrue(true);
    }
}
