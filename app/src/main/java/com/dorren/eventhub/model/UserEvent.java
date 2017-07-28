package com.dorren.eventhub.model;

import com.dorren.eventhub.util.OffsetDateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.threeten.bp.OffsetDateTime;

/**
 * Created by dorrenchen on 7/28/17.
 */

public class UserEvent {
    public static final String ACTION_BOOKMARK = "bookmark";
    public static final String ACTION_CONFIRM  = "confirm";

    public  String event_id;
    public  String user_id;
    public transient String user_action;

    public UserEvent(String event_id, String user_id){
        this.event_id = event_id;
        this.user_id = user_id;
    }

    public String toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(this);

        return json;
    }

    public static UserEvent fromJson(String json){
        Gson gson = getGson();
        UserEvent ue = gson.fromJson(json, UserEvent.class);

        return ue;
    }

    private static Gson getGson(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeConverter());
        Gson gson = builder.create();

        return gson;
    }
    public String toString(){
        return "EventUser " + toJson();
    }
}
