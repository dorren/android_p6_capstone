package com.dorren.eventhub.util;

import android.content.Context;
import android.os.Build;

import com.dorren.eventhub.R;
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
        String url = context.getString(R.string.api_url);
        NetworkUtil.setApiUrl(url);
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }
}
