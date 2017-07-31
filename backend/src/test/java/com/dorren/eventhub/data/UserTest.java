package com.dorren.eventhub.data;

import com.dorren.eventhub.data.model.User;

import org.json.JSONException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dorrenchen on 7/19/17.
 */

public class UserTest {
    @Test
    public void testToJson() throws JSONException {
        User user = new User();
        user.name = "John";
        user.email = "john@gmail.com";

        String str = user.toJson();
        User clone = User.fromJson(str);
        assertEquals(clone.name, "John");
        assertEquals(clone.email, "john@gmail.com");
    }
}
