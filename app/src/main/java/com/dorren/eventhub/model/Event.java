package com.dorren.eventhub.model;


import com.google.gson.Gson;

import org.json.JSONArray;
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

    public Event(String id, String title, String detail){
        this.id = id;
        this.title = title;
        this.detail = detail;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }

    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }

    public static Event fromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Event.class);
    }

}
