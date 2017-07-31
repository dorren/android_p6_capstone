package com.dorren.eventhub.util;
import org.junit.Test;
import org.threeten.bp.OffsetDateTime;


import static org.junit.Assert.*;
/**
 * Created by dorrenchen on 7/19/17.
 */

public class TimeUtilTest {
    @Test
    public void convertTime() throws Exception {
        String timeStr = "2017-07-08T13:30:00.000Z";
        OffsetDateTime time = TimeUtil.toDate(timeStr);

        assertEquals(time.getYear(), 2017);
        assertEquals(time.getMonthValue(), 7);
        assertEquals(time.getDayOfMonth(), 8);
        assertEquals(time.getHour(), 13);
        assertEquals(time.getMinute(), 30);
    }
}
