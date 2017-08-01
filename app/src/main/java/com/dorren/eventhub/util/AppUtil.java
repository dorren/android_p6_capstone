package com.dorren.eventhub.util;

import android.content.Context;

import com.dorren.eventhub.data.util.NetworkUtil;

/**
 * Created by dorrenchen on 7/22/17.
 */

public class AppUtil {
    // request and result code used in startActivityForResult() calls
    public static final int LOGIN_REQUEST = 1;
    public static final int LOGIN_SUCCESS = 1;
    public static final int LOGIN_FAIL    = 2;

    public static final int SIGNUP_REQUEST = 2;
    public static final int SIGNUP_SUCCESS = 2;

    // loaders
    public static final int EVENTS_CURSOR_LOADER = 1;


    public static void init(Context context){
        NetworkUtil.setApiUrl(context);
    }

}
