package com.dorren.eventhub.data.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.dorren.eventhub.R;
import com.dorren.eventhub.data.model.User;

/**
 * Created by dorrenchen on 7/19/17.
 */

public class PreferenceUtil {
    private static final String KLASS = PreferenceUtil.class.getSimpleName();

    /**
     *
     * @param context current activity
     * @return
     */
    public static SharedPreferences getPreference(Context context){
        String prefs_name = context.getString(R.string.prefs_name);

        SharedPreferences prefs = (SharedPreferences) context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);

        return prefs;
    }

    public static void saveCurrentUser(Context context, User user){
        String json = (user == null) ? null : user.toJson();

        SharedPreferences prefs = getPreference(context);
        SharedPreferences.Editor editor = prefs.edit();
        String key_name = context.getString(R.string.current_user_json);
        editor.putString(key_name, json);
        editor.commit();
    }

    public static User getCurrentUser(Context context) {
        SharedPreferences prefs = getPreference(context);
        String key_name = context.getString(R.string.current_user_json);

        String json = prefs.getString(key_name, null);
        User user = null;
        if(json != null){
            user = User.fromJson(json);
        }

        return user;
    }

    public static void logout(Context context) {
        saveCurrentUser(context, null);
    }

}

