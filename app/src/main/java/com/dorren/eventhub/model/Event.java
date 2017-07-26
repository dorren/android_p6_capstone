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
    public static final String COL_IMAGE_URL = "image_url";

    public String id;
    public String title;
    public String detail;
    public OffsetDateTime timeFrom;
    public OffsetDateTime timeTo;
    public String imageUrl;

    public Event(String id, String title, String detail,
                 String timeFrom, String timeTo, String imageUrl){
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.timeFrom = OffsetDateTime.parse(timeFrom);
        this.timeTo = OffsetDateTime.parse(timeTo);
        this.imageUrl = imageUrl;
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
            String imageUrl = jObj.getString(COL_IMAGE_URL);

            event = new Event(id, title, detail, timeFrom, timeTo, imageUrl);
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
