package com.dorren.eventhub.data;

import android.net.Uri;
import android.util.Log;

import com.dorren.eventhub.model.Event;
import com.dorren.eventhub.model.User;
import com.dorren.eventhub.util.AppUtil;
import com.dorren.eventhub.util.NetworkUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * Created by dorrenchen on 7/25/17.
 */

public class ApiService {
    private static final String TAG = "ApiService";

    public User authenticate(String email, String password) throws ApiException {
        User user = null;
        try {
            Uri uri = Uri.parse(NetworkUtil.API_BASE_URL).buildUpon().
                    appendPath("users").appendPath("authenticate").build();
            URL url = new URL(uri.toString());

            JSONObject data = new JSONObject();
            data.put("email", email);
            data.put("password", password);

            String response = NetworkUtil.query(url, "POST", data);
            JSONObject json = new JSONObject(response);

            if (json.has(AppUtil.errKey)) {
                String errorMsg = json.getString(AppUtil.errKey);
                throw new ApiException(errorMsg);
            } else {
                user = User.fromJson(response);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        return user;
    }

    /**
     *
     * @return get events object from API.
     * @throws ApiException
     */
    public Event[] getEvents() throws ApiException {
        Event[] events = null;

        try {
            //Uri uri = Uri.parse(NetworkUtil.API_BASE_URL).buildUpon().appendPath("events").build();
            //URL url = new URL(uri.toString());
            String url_str = NetworkUtil.API_BASE_URL + "/events"; // unit test mocked Uri.parse.
            URL url = new URL(url_str);

            String response = NetworkUtil.query(url);
            JSONObject json = new JSONObject(response);

            if (json.has(AppUtil.errKey)) {
                String errorMsg = json.getString(AppUtil.errKey);
                Log.e(TAG, errorMsg);
                throw new ApiException(errorMsg);
            } else {
                events = fromJsonArray(response);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        return events;
    }

    /**
     *
     * @return get user bookmarked or confirmed events
     *
     * @throws ApiException
     */
    public Event[] getMyEvents(String options) throws ApiException {
        Event[] events = null;

        try {

            Uri.Builder builder = Uri.parse(NetworkUtil.API_BASE_URL).buildUpon().
                    appendPath("me").
                    appendPath("events");

            JSONObject json = new JSONObject(options);
            if(json.has("user_id")) {
                builder.appendQueryParameter("user_id", json.getString("user_id"));
            }
            if(json.has("bookmarked")) {
                builder.appendQueryParameter("bookmarked", String.valueOf(json.getBoolean("bookmarked")));
            }
            if(json.has("confirmed")) {
                builder.appendQueryParameter("confirmed", String.valueOf(json.getBoolean("confirmed")));
            }
            Uri uri = builder.build();
            URL url = new URL(uri.toString());

            String response = NetworkUtil.query(url);
            json = new JSONObject(response);

            if (json.has(AppUtil.errKey)) {
                String errorMsg = json.getString(AppUtil.errKey);
                Log.e(TAG, errorMsg);
                throw new ApiException(errorMsg);
            } else {
                events = fromJsonArray(response);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        return events;
    }

    private  Event[] fromJsonArray(String jsonStr) throws JSONException {
        JSONObject response = new JSONObject(jsonStr);
        JSONArray jsonEvents = response.getJSONArray("events");
        Event[] events = new Event[jsonEvents.length()];

        for(int i=0; i<jsonEvents.length(); i++){
            JSONObject jObj = jsonEvents.getJSONObject(i);
            events[i] = Event.fromJson(jObj.toString());
        }

        return events;
    }
}
