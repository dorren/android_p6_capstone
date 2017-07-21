package com.dorren.eventhub.data;


import com.google.gson.Gson;

import org.threeten.bp.OffsetDateTime;

import java.util.Date;

/**
 * Created by dorrenchen on 7/18/17.
 */

public class Event {
    public String id;
    public String title;
    public String detail;
    public OffsetDateTime timeFrom;

    public Event(String id, String title, String detail){
        this.id = id;
        this.title = title;
        this.detail = detail;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }

    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }

    public Event fromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Event.class);
    }

}
