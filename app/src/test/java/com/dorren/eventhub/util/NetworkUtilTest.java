package com.dorren.eventhub.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import static org.junit.Assert.*;
/**
 * Created by dorrenchen on 7/21/17.
 */

public class NetworkUtilTest {
    private final String weatherUrl = "http://samples.openweathermap.org/data/2.5/weather?zip=10001,us&appid=b1b15e88fa797225412429c1c50c122a1";

    @Test
    public void testQueryGet() throws IOException{
        URL url = new URL(weatherUrl);

        String response = NetworkUtil.query(url);
        assertNotNull(response);
    }

    @Test
    public void testQueryPost() throws IOException, JSONException {
        URL url = new URL(weatherUrl);

        String response = NetworkUtil.query(url, "POST");
        assertNotNull(response);
        JSONObject json = new JSONObject(response);
        assertTrue(json.has("error"));
    }
}
