package com.dorren.eventhub.data.util;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Created by dorrenchen on 7/19/17.
 */

public class TimeUtil {
    private static ZoneOffset mOffset;
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


    public static String dateToString(OffsetDateTime dt){
        String pattern = "EEEE LLLL dd HH:mm a";
        String str = dt.format(DateTimeFormatter.ofPattern(pattern));

        return str;
    }

    public static ZoneOffset defaultOffset(){
        if(mOffset == null) {
            OffsetDateTime dt = OffsetDateTime.now();
            mOffset = dt.getOffset();
        }
        return mOffset;
    }
}
