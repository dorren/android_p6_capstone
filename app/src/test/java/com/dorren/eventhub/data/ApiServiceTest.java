package com.dorren.eventhub.data;

import android.net.Uri;

import com.dorren.eventhub.model.Event;
import com.dorren.eventhub.util.NetworkUtil;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

/**
 * Created by dorrenchen on 7/25/17.
 */

public class ApiServiceTest {
    private ApiService api;

    @Before
    public void setup() {
        api = new ApiService();
        NetworkUtil.API_BASE_URL = "http://localhost:3000";
    }

    @Test
    public void getEvents() throws Exception{
        try {
            Event[] events = api.getEvents();
            assertNotNull(events);
        }catch (ApiException ex){
            ex.printStackTrace();
        }
    }
}
