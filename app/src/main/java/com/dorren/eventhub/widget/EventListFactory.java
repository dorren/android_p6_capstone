package com.dorren.eventhub.widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.dorren.eventhub.R;
import com.dorren.eventhub.data.EventContentProvider;
import com.dorren.eventhub.data.model.Event;
import com.dorren.eventhub.data.model.User;
import com.dorren.eventhub.data.util.TimeUtil;
import com.dorren.eventhub.util.PreferenceUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.OffsetDateTime;

import java.io.IOException;

/**
 * Created by dorrenchen on 7/31/17.
 */

public class EventListFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = EventListFactory.class.getSimpleName();

    private Context mContext;
    private int mAppWidgetId;
    private Cursor mCursor;


    public EventListFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");
    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "onDataSetChanged()");

        try {
            User user = PreferenceUtil.getCurrentUser(mContext);
            if(user != null) {
                String userId = user.id;
                JSONObject options = new JSONObject();
                options.put("user_id", userId);
                options.put("confirmed", true);
                mCursor = mContext.getContentResolver().query(
                        EventContentProvider.MY_EVENT_URI,
                        null, options.toString(), null, null);
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        mCursor = null;
    }

    @Override
    public int getCount() {
        if(mCursor!= null) {
            Log.d(TAG, "getCount() " + mCursor.getCount());
            return mCursor.getCount();
        }else{
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(TAG, "getViewAt() " + position);
        final RemoteViews row = new RemoteViews(mContext.getPackageName(), R.layout.event_widget_item);

        mCursor.moveToPosition(position);
        String id = mCursor.getString(mCursor.getColumnIndex(Event.COL_ID));
        String title = mCursor.getString(mCursor.getColumnIndex(Event.COL_TITLE));
        String detail = mCursor.getString(mCursor.getColumnIndex(Event.COL_DETAIL));
        String timeFrom = mCursor.getString(mCursor.getColumnIndex(Event.COL_TIME_FROM));
        String timeTo = mCursor.getString(mCursor.getColumnIndex(Event.COL_TIME_TO));
        String imageUrl = mCursor.getString(mCursor.getColumnIndex(Event.COL_IMAGE_URL));
        String location = mCursor.getString(mCursor.getColumnIndex(Event.COL_LOCATION));

        Event event = new Event(id, title, detail, timeFrom, timeTo, imageUrl, location);

        OffsetDateTime dt = OffsetDateTime.parse(timeFrom);
        String dateStr = TimeUtil.dateStringShort(dt);

        try {
            Bitmap bitmap = Picasso.with(mContext).load(imageUrl).get();
            row.setImageViewBitmap(R.id.event_image, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        row.setTextViewText(R.id.event_date, dateStr);
        row.setTextViewText(R.id.event_title, title);


        // set fillInIntent
        Bundle extras = new Bundle();
        String json = event.toString();
        extras.putString(Intent.EXTRA_TEXT, json);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        row.setOnClickFillInIntent(R.id.event_row, fillInIntent);

        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
