package com.dorren.eventhub.data.model;

import com.google.gson.Gson;

/**
 * Created by dorrenchen on 7/18/17.
 */

public class User {
    public String id;
    public String email;
    public String name;


    public String toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(this);

        return json;
    }

    public static User fromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }

    public String toString(){
        return "User " + toJson();
    }
}
