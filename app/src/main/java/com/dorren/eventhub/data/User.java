package com.dorren.eventhub.data;

import com.google.gson.Gson;

/**
 * Created by dorrenchen on 7/18/17.
 */

public class User {
    private String id;
    private String email;
    private String name;

    public void setName(String name){ this.name = name; }
    public String getName() { return name; }
    public void setEmail(String email){ this.email = email; }
    public String getEmail() { return email; }

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
