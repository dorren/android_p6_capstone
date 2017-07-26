package com.dorren.eventhub.ui.event.dummy;

import com.dorren.eventhub.model.Event;

import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Event> ITEMS = new ArrayList<Event>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Event> ITEM_MAP = new HashMap<String, Event>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createEvent(i));
        }
    }

    private static void addItem(Event item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static Event createEvent(int position) {
        return new Event(
                String.valueOf(position),
                "Item " + position, makeDetails(position),
                "2017-12-31T07:00:00.0000-05:00",
                "2017-12-31T16:00:00.0000-05:00");
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
