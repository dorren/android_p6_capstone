package com.dorren.eventhub.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by dorrenchen on 7/31/17.
 */

public class EventListService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new EventListFactory(this.getApplicationContext(), intent);
    }
}
