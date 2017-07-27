package com.dorren.eventhub.model;


import com.dorren.eventhub.util.OffsetDateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

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
    public OffsetDateTime time_from;
    public OffsetDateTime time_to;
    public String image_url;

    public Event(String id, String title, String detail,
                 String timeFrom, String timeTo, String imageUrl){
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.time_from = OffsetDateTime.parse(timeFrom);
        this.time_to = OffsetDateTime.parse(timeTo);
        this.image_url = imageUrl;
    }

    public static Event fromJson(String json){
//        Event event = null;
//        try {
//            JSONObject jObj = new JSONObject(json);
//            String id = jObj.getString(COL_ID);
//            String title = jObj.getString(COL_TITLE);
//            String detail = jObj.getString(COL_DETAIL);
//            String time_from = jObj.getString(COL_TIME_FROM);
//            String time_to = jObj.getString(COL_TIME_TO);
//            String image_url = jObj.getString(COL_IMAGE_URL);
//
//            event = new Event(id, title, detail, time_from, time_to, image_url);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Gson gson = getGson();
        Event event = gson.fromJson(json, Event.class);

        return event;
    }

    public String dateStr(){
        String str = time_from.format(DateTimeFormatter.ofPattern("EEE MM/dd"));
        return str;
    }

    public String toString() {
        Gson gson = getGson();
        String json = gson.toJson(this);
        return json;
    }

    private static Gson getGson(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeConverter());
        Gson gson = builder.create();

        return gson;
    }

}
