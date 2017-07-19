package com.dorren.eventhub.data;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.threeten.bp.OffsetDateTime;


import static org.junit.Assert.*;
/**
 * Created by dorrenchen on 7/19/17.
 */

public class UserTest {
    @Test
    public void testToJson() throws JSONException {
        User user = new User();
        user.setName("John");
        user.setEmail("john@gmail.com");

        String str = user.toJson();
        User clone = User.fromJson(str);
        assertEquals(clone.getName(), "John");
        assertEquals(clone.getEmail(), "john@gmail.com");
    }
}
