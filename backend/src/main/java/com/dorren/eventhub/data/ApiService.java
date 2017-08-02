package com.dorren.eventhub.data;

import android.net.Uri;
import android.util.Log;

import com.dorren.eventhub.data.model.Event;
import com.dorren.eventhub.data.model.User;
import com.dorren.eventhub.data.model.UserEvent;
import com.dorren.eventhub.data.util.NetworkUtil;

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
    public static final String errKey = "_error";

    public User authenticate(String email, String password) throws ApiException {
        User user = null;
        try {
            Uri uri = Uri.parse(NetworkUtil.getApiUrl()).buildUpon().
                    appendPath("users").appendPath("authenticate").build();
            URL url = new URL(uri.toString());

            JSONObject data = new JSONObject();
            data.put("email", email);
            data.put("password", password);

            String response = NetworkUtil.query(url, "POST", data);
            Log.d(TAG, "authenticate()\n " + response);
            JSONObject json = new JSONObject(response);

            if (json.has(errKey)) {
                String errorMsg = json.getString(errKey);
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
    public Event[] getEvents(String jsonStr) throws ApiException {
        Event[] events = null;

        try {
            if(jsonStr == null){
                jsonStr = "{}";
            }
            JSONObject jsonIn = new JSONObject(jsonStr);
            Uri.Builder builder = Uri.parse(NetworkUtil.getApiUrl()).buildUpon().appendPath("events");
            if(jsonIn.has("organizer_id")) {
                builder.appendQueryParameter("organizer_id", jsonIn.getString("organizer_id"));
            }

            Uri uri = builder.build();
            URL url = new URL(uri.toString());
            //String url_str = NetworkUtil.API_BASE_URL + "/events"; // unit test mocked Uri.parse.
            //URL url = new URL(url_str);


            String response = NetworkUtil.query(url);
            JSONObject json = new JSONObject(response);

            if (json.has(errKey)) {
                String errorMsg = json.getString(errKey);
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
     * to bookmark and confirm event.
     *
     * @param ue new UserEvent holding the incoming attributes
     * @return
     * @throws ApiException
     */
    public UserEvent bookmark(UserEvent ue) throws ApiException {
        UserEvent result = null;
        try {
            Uri uri = Uri.parse(NetworkUtil.getApiUrl()).buildUpon().
                    appendPath("events").
                    appendPath(ue.event_id).
                    appendPath(ue.user_action).  // "bookmark" or "confirm"
                    build();
            URL url = new URL(uri.toString());

            JSONObject data = new JSONObject();
            data.put("user_id", ue.user_id);

            String response = NetworkUtil.query(url, "PUT", data);
            JSONObject json = new JSONObject(response);

            if (json.has(errKey)) {
                String errorMsg = json.getString(errKey);
                Log.e(TAG, errorMsg);
                throw new ApiException(errorMsg);
            } else {
                result = UserEvent.fromJson(response);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        return result;
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

            Uri.Builder builder = Uri.parse(NetworkUtil.getApiUrl()).buildUpon().
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

            Log.d(TAG, url.toString());
            String response = NetworkUtil.query(url);
            json = new JSONObject(response);

            if (json.has(errKey)) {
                String errorMsg = json.getString(errKey);
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

    public Event createEvent(JSONObject json) throws ApiException {
        Event result = null;
        try {
            Uri uri = Uri.parse(NetworkUtil.getApiUrl()).buildUpon().
                    appendPath("events").
                    build();
            URL url = new URL(uri.toString());

            String response = NetworkUtil.query(url, "POST", json);
            JSONObject res = new JSONObject(response);

            if (res.has(errKey)) {
                String errorMsg = res.getString(errKey);
                Log.e(TAG, errorMsg);
                throw new ApiException(errorMsg);
            } else {
                result = Event.fromJson(response);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }catch(JSONException ex){
            ex.printStackTrace();
        }

        return result;
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
