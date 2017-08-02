package com.dorren.eventhub.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.dorren.eventhub.R;
import com.dorren.eventhub.ui.event.EventDetailActivity;
import com.dorren.eventhub.util.AppUtil;

/**
 * Implementation of App Widget functionality.
 */
public class EventWidget extends AppWidgetProvider {
    private static final String TAG = EventWidget.class.getSimpleName();
    private static final String ACTION_REFRESH = "com.dorren.eventhub.widget.action.REFRESH";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d(TAG, "updateAppWidget() " + appWidgetId);

        // Construct the RemoteViews object
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.event_widget);

        // setup listView
        Intent intent = new Intent(context, EventListService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        rv.setRemoteAdapter(R.id.appwidget_list_view, intent);

        // setup click handler
        Intent appIntent = new Intent(context, EventDetailActivity.class);
        appIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.appwidget_list_view, appPendingIntent);


        // setup refresh click
        Intent intent2 = new Intent(context, EventWidget.class);
        intent2.setAction(ACTION_REFRESH);
        intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setOnClickPendingIntent(R.id.appwidget_refresh_btn, pi);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive()");
        super.onReceive(context, intent);

        AppUtil.init(context);
        if (ACTION_REFRESH.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
                AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_list_view);

                if (appWidgetIds != null && appWidgetIds.length > 0) {
                    onUpdate(context, mgr, appWidgetIds);
                }
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate()");

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

