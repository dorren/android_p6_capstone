package com.dorren.eventhub.data;

import com.dorren.eventhub.model.Event;
import com.dorren.eventhub.util.OffsetDateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;
import org.threeten.bp.OffsetDateTime;

import static org.junit.Assert.*;

/**
 * Created by dorrenchen on 7/26/17.
 */

public class EventTest {
    @Test
    public void testJson(){
        Event event = new Event("1", "playing tennis", "playing tennis at park",
                                "2017-08-06T18:00:00.0000-05:00",
                                "2017-08-06T19:00:00.0000-05:00",
                                "https://s3.amazonaws.com/eventhubapp/tennis.jpg",
                                "Colden St, Flushing, NY 11355");
        String json = event.toString();

        Event event2 = Event.fromJson(json);
        assertEquals(event.time_from, event2.time_from);
        assertTrue(event.isSameDay());
    }

    @Test
    public void testDateTime() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeConverter());
        Gson gson = builder.create();

        OffsetDateTime dt = OffsetDateTime.parse("2017-08-06T18:00:00.0000-05:00");

        String str = gson.toJson(dt);
        OffsetDateTime dt2 = gson.fromJson(str, OffsetDateTime.class);

        assertEquals(dt, dt2);

    }
}
