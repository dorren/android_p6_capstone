package com.dorren.eventhub.util;

import org.threeten.bp.OffsetDateTime;

/**
 * Created by dorrenchen on 7/19/17.
 */

public class TimeConverter {
    /**
     * convert ISO8601 string to Date object. like "2017-07-08T13:00:00.000Z"
     *
     * @param timeString time in string format
     * @return converted Date object
     */
    public static OffsetDateTime toDate(String timeString){
        OffsetDateTime time = OffsetDateTime.parse(timeString);
        return time;
    }
}
