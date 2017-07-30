package com.dorren.eventhub.data;

import android.content.AsyncQueryHandler;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import com.dorren.eventhub.R;
import com.dorren.eventhub.model.Event;
import com.dorren.eventhub.model.User;
import com.dorren.eventhub.util.AppUtil;
import com.dorren.eventhub.util.NetworkUtil;
import com.dorren.eventhub.util.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class EventContentProvider extends ContentProvider {
    private static final String TAG = "EventContentProvider";

    public static final String AUTHORITY = "com.dorren.eventhub";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final int CODE_EVENTS = 100;
    public static final String PATH_EVENTS = "events";
    //public static final Uri EVENT_URI =  BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENTS).build();
    public static final Uri EVENT_URI =  Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EVENTS);

    public static final int CODE_MY_EVENTS = 101;
    public static final String PATH_MY_EVENTS = "me/events";
    public static final Uri MY_EVENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MY_EVENTS);

    private ApiService api;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, PATH_EVENTS, CODE_EVENTS);
        matcher.addURI(AUTHORITY, PATH_MY_EVENTS, CODE_MY_EVENTS);
        return matcher;
    }

    public EventContentProvider() {
        api = new ApiService();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        String result;

        switch (match){
            case CODE_EVENTS:{
                result = ContentResolver.CURSOR_DIR_BASE_TYPE;
                break;
            }
            case CODE_MY_EVENTS:{
                result = ContentResolver.CURSOR_DIR_BASE_TYPE;
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        return result;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;

        switch (sUriMatcher.match(uri)) {
            case CODE_EVENTS: {
                Event[] events = new Event[0];
                try {
                    events = api.getEvents();
                    cursor = buildCursor(events);
                } catch (ApiException e) {
                    e.printStackTrace();
                }

                break;
            }
            case CODE_MY_EVENTS: {
                Event[] events = new Event[0];
                try {
                    events = api.getMyEvents(selection);
                    cursor = buildCursor(events);
                } catch (ApiException e) {
                    e.printStackTrace();
                }

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return cursor;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    private Cursor buildCursor(Event[] events){
        String[] columns = new String[]{
                Event.COL_ID,
                Event.COL_TITLE,
                Event.COL_DETAIL,
                Event.COL_TIME_FROM,
                Event.COL_TIME_TO,
                Event.COL_IMAGE_URL,
                Event.COL_LOCATION};
        MatrixCursor cursor = new MatrixCursor(columns);

        for(Event event : events){
            cursor.newRow().
                add(Event.COL_ID, event.id).
                add(Event.COL_TITLE, event.title).
                add(Event.COL_DETAIL, event.detail).
                add(Event.COL_TIME_FROM, event.time_from).
                add(Event.COL_TIME_TO, event.time_to).
                add(Event.COL_IMAGE_URL, event.image_url).
                add(Event.COL_LOCATION, event.location);
        }

        return cursor;
    }
}
