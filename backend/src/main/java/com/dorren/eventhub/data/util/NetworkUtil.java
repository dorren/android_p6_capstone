package com.dorren.eventhub.data.util;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by dorrenchen on 7/20/17.
 */

public class NetworkUtil {
    public static  String API_BASE_URL = "http://10.0.2.2:3000";



    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponse(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    /**
     * send http request, returns response string. For example.
     *
     *     query(url)
     *     query(url, "POST")
     *     query(url, "POST", bodyParams)   // bodyParams in JSONObject format
     *
     * @param url request url
     * @param settings additional request parameters
     * @return response string
     */
    public static String query(URL url, Object... settings) throws IOException{
        String response = null;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            if(settings.length > 0){
                String method = (String) settings[0];
                conn.setRequestMethod(method);
                //conn.setDoInput(true);
                if(method == "POST" || method == "PUT") {
                    conn.setDoOutput(true);
                }
            }

            if(settings.length > 1){
                JSONObject data = (JSONObject) settings[1];
                buildBodyParameters(conn, data);
            }

            conn.connect();

            InputStream in = conn.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                response = scanner.next();
            }
        }catch (Exception e) {
            e.printStackTrace();
            response = buildErrorResponse(e).toString();
        }finally {
            conn.disconnect();
        }
        return response;
    }

    private static JSONObject buildErrorResponse(Exception ex){
        JSONObject json = new JSONObject();
        try {
            json.put("error", ex.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    private static void buildBodyParameters(URLConnection conn, JSONObject data)
            throws JSONException, IOException{
        Uri.Builder builder = new Uri.Builder();

        Iterator keys = data.keys();
        while(keys.hasNext()){
            String key = (String) keys.next();
            String val = data.getString(key);
            builder.appendQueryParameter(key, val);
        }
        String reqBody = builder.build().getEncodedQuery();

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(reqBody);
        writer.flush();
        writer.close();
        os.close();
    }
}
