package com.dorren.eventhub.model;


import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.threeten.bp.OffsetDateTime;

/**
 * Created by dorrenchen on 7/18/17.
 */

public class Event {
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_DETAIL = "detail";
    public static final String COL_TIME_FROM = "time_from";
    public static final String COL_TIME_TO = "time_to";

    public String id;
    public String title;
    public String detail;
    public OffsetDateTime timeFrom;
    public OffsetDateTime timeTo;

    public Event(String id, String title, String detail,
                 String timeFrom, String timeTo){
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.timeFrom = OffsetDateTime.parse(timeFrom);
        this.timeTo = OffsetDateTime.parse(timeTo);
    }

    public static Event fromJson(String json){
        Event event = null;
        try {
            JSONObject jObj = new JSONObject(json);
            String id = jObj.getString(COL_ID);
            String title = jObj.getString(COL_TITLE);
            String detail = jObj.getString(COL_DETAIL);
            String timeFrom = jObj.getString(COL_TIME_FROM);
            String timeTo = jObj.getString(COL_TIME_TO);

            event = new Event(id, title, detail, timeFrom, timeTo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return event;
    }

    public String toString() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

}
