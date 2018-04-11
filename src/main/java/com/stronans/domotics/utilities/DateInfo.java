package com.stronans.domotics.utilities;

import hirondelle.date4j.DateTime;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Immutable DateTime object which wraps the hirondelle.date4j.DateTime object.
 */
public class DateInfo {
    private static final String UNIVERSAL_FORMAT = "yyyyMMddHHmmssSSSS";
    private static final String SHORT_UNIVERSAL_FORMAT = "yyyyMMddHHmmss";

    private static final String ISO8601_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd";

    private Calendar calendar = null;
    private boolean notDefined = false;
    private DateInfoContent content = DateInfoContent.DATE_AND_TIME;

    private DateInfo() {
        // Calendar object always defaults to today/Now
        this.calendar = Calendar.getInstance();
        notDefined = false;
        content = DateInfoContent.DATE_AND_TIME;
    }

    /**
     * Factory method which returns defined object set to current date and time.
     *
     * @return defined DataInfo object set to now.
     */
    public static DateInfo getNow() {
        return new DateInfo();
    }

    /**
     * Factory method which returns undefined DateInfo object.
     *
     * @return undefined DataInfo object.
     */
    public static DateInfo getUndefined() {
        DateInfo dateInfo = new DateInfo();
        dateInfo.notDefined = true;
        return dateInfo;
    }

    /**
     * Factory method which returns defined object set to date and time corresponding to long millisecond value.
     *
     * @return defined DataInfo object set to corresponding millisecond value for Date/Time.
     */
    public static DateInfo fromLong(Long newDateInMilli) {
        DateInfo dateInfo = new DateInfo();
        if (newDateInMilli == Long.MIN_VALUE) {
            dateInfo.notDefined = true;
        } else {
            dateInfo.calendar.setTime(new Date(newDateInMilli));
            dateInfo.content = DateInfoContent.DATE_AND_TIME;
        }
        return dateInfo;
    }

    public static DateInfo fromDateInfo(DateInfo newDate) {
        DateInfo dateInfo = new DateInfo();
        dateInfo.setToJustPastMidnight();

        if (newDate != null) {
            if (newDate.notDefined) {
                dateInfo.notDefined = true;
            } else {
                dateInfo.calendar.setTimeInMillis(newDate.getMilliseconds());
                dateInfo.content = DateInfoContent.DATE_AND_TIME;
            }
        }
        return dateInfo;
    }

    public static DateInfo fromTimestamp(Timestamp timestamp) {
        return fromLong(timestamp.getTime());
    }

    public static DateInfo fromJavaDate(Date date) {
        return fromLong(date.getTime());
    }

    public static DateInfo fromDateOnly(int day, int month, int year) {
        DateInfo dateInfo = fromDate(day, month, year);
        dateInfo.content = DateInfoContent.DATE_ONLY;
        return dateInfo;
    }

    public static DateInfo fromDate(int day, int month, int year) {
        DateInfo dateInfo = new DateInfo();
        dateInfo.calendar.set(Calendar.YEAR, year);
        dateInfo.calendar.set(Calendar.MONTH, month);
        dateInfo.calendar.set(Calendar.DATE, day);

        dateInfo.setToJustPastMidnight();
        dateInfo.content = DateInfoContent.DATE_AND_TIME;

        return dateInfo;
    }

    public static DateInfo fromTimeOnly(int hours, int minutes, int seconds) {
        DateInfo dateInfo = fromDateTime(0, 0, 0, hours, minutes, seconds);
        dateInfo.content = DateInfoContent.TIME_ONLY;
        return dateInfo;
    }

    public static DateInfo fromDateTime(int date, int month, int year, int hours, int minutes, int seconds) {
        DateInfo dateInfo = new DateInfo();
        dateInfo.calendar.set(Calendar.YEAR, year);
        dateInfo.calendar.set(Calendar.MONTH, month);
        dateInfo.calendar.set(Calendar.DATE, date);

        dateInfo.calendar.set(Calendar.HOUR, hours);
        dateInfo.calendar.set(Calendar.MINUTE, minutes);
        dateInfo.calendar.set(Calendar.SECOND, seconds);

        dateInfo.content = DateInfoContent.DATE_AND_TIME;

        return dateInfo;
    }

    /**
     * @return number of days from today, positive for past, negative for future
     */
    public int intervalToToday() {
        DateTime today = DateTime.today(TimeZone.getDefault());

        DateTime current = getDateTimeAsDate(this);

        int diff = current.numDaysFrom(today);

        return diff;
    }

    static private DateTime getDateTimeAsDate(DateInfo dateToConvert) {
        return DateTime.forDateOnly(dateToConvert.calendar.get(Calendar.YEAR), dateToConvert.calendar.get(Calendar.MONTH) + 1,
                dateToConvert.calendar.get(Calendar.DATE));
    }

    /**
     * @return true is a given date falls into the range provided.
     */
    static public boolean inRange(DateInfo dateToCheck, DateInfo startOfRange, DateInfo endOfRange) {
        boolean result = false;

        if (dateToCheck.isDefined()) {
            DateTime comparison = getDateTimeAsDate(dateToCheck);
            DateTime start = getDateTimeAsDate(startOfRange);
            DateTime end = getDateTimeAsDate(endOfRange);

            if (comparison.gteq(start) && comparison.lteq(end)) {
                result = true;
            }
        }

        return result;
    }

    public boolean isDefined() {
        return !notDefined;
    }

    static private DateInfo fromformattedString(String dateTimeInfo, String formatLong, String formatShort) {
        DateInfo result = DateInfo.getUndefined();
        Date date = null;

        try {
            SimpleDateFormat dt = new SimpleDateFormat(formatLong);
            date = dt.parse(dateTimeInfo);
        } catch (ParseException pe) {
            try {
                SimpleDateFormat dt = new SimpleDateFormat(formatShort);
                date = dt.parse(dateTimeInfo);
            } catch (ParseException pe2) {
                date = null;
            }
        }

        if (date != null) {
            return DateInfo.fromLong(date.getTime());
        }

        return result;
    }

    static public DateInfo fromISOTimestampString(String dateTimeInfo) {
        return fromformattedString(dateTimeInfo, ISO8601_TIMESTAMP_FORMAT, ISO8601_DATE_FORMAT);
    }

    static public String toISOTimestampString(DateInfo timeInfo) {
        return timeInfo.format(ISO8601_TIMESTAMP_FORMAT);
    }

    public String ISOTimestamp() {
        return format(ISO8601_TIMESTAMP_FORMAT);
    }

    public String ISODate() {
        return format(ISO8601_DATE_FORMAT);
    }

    /**
     * Formats the dateTime in this instance into a displayable string. If undefined then returns a blank string.
     *
     * @param format Must conform to the formatting characters from
     * @return String holding formatted date/Time of blank if undefined.
     */
    public String format(String format) {
        String result = "";

        if (!notDefined) {

            SimpleDateFormat dt = new SimpleDateFormat(format);
            result = dt.format(calendar.getTime());
        }

        return result;
    }

    public DateInfo addToDate(int interval) {
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.add(Calendar.DATE, interval);

        return fromLong(newCalendar.getTime().getTime());
    }

    public boolean after(DateInfo other) {
        return calendar.after(other.calendar);
    }

    public boolean before(DateInfo other) {
        return calendar.before(other.calendar);
    }

    /**
     * @return The first day of the current month (based on ordinal of Weekday enum)
     */
    public int getFirstDayOfMonth() {
        Calendar c = (Calendar) calendar.clone();
        c.set(Calendar.DATE, 1);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * @return
     */
    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    public int getDateInMonth() {
        return calendar.get(Calendar.DATE);
    }

    /**
     * @return The current weekday of the current month
     */
    public int getCurrentDayOfMonth() {
        return (calendar.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * @return The number of days in the current selected month
     */
    public int getDaysInMonth() {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Shorthand method for boolean for today
     *
     * @return true if date value is within today.
     */
    public boolean isToday() {
        return equals(new DateInfo());
    }

    /**
     * Method for boolean result if to DateInfo structures are equal in year, month and date.
     *
     * @return true if date value is equal to compare.
     */
    public boolean equals(DateInfo compare) {
        boolean result = false;
        if (calendar.get(Calendar.YEAR) == compare.calendar.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == compare.calendar.get(Calendar.DAY_OF_YEAR))
            result = true;

        return result;
    }

    public void setToJustPastMidnight() {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    public void setToMidnight() {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    /**
     * @return The current selected date value in milliseconds
     */
    public Long getMilliseconds() {
        long x = Long.MIN_VALUE;

        if (!notDefined) {
            x = calendar.getTime().getTime();
        }
        return x;
    }

    /**
     * @return The current selected date value (note ignore time value)
     */
    public void setMonth(int month) {
        calendar.set(Calendar.MONTH, month);
    }

    /**
     * @return The new selected date value one month forward from selected date (note ignore time value)
     */
    public void rollMonthForward() {
        calendar.roll(Calendar.MONTH, true);
    }

    /**
     * @return The new selected date value one month back from selected date (note ignore time value)
     */
    public void rollMonthBack() {
        calendar.roll(Calendar.MONTH, true);
    }

    static public String toUniversalString(DateInfo timeInfo) {
        return timeInfo.format(UNIVERSAL_FORMAT);
    }

    static public DateInfo fromUniversalString(String dateTimeInfo) {
        return fromformattedString(dateTimeInfo, UNIVERSAL_FORMAT, SHORT_UNIVERSAL_FORMAT);
    }

    static public boolean isUniversalString(String dateTimeInfo) {
        return (fromUniversalString(dateTimeInfo).isDefined());
    }

    static public String getTimeString(DateInfo timeInfo) {
        return timeInfo.format("hh:mm");
    }

    static public String getDateString(DateInfo timeInfo) {
        return timeInfo.format("WWWW, MMMM D, YYYY");
    }

    static public String getDateTimeString(DateInfo timeInfo) {
        return timeInfo.format("DD/MM/YYYY hh:mm:ss");
    }

    static public String getTimeDateString(DateInfo timeInfo) {
        return timeInfo.format("hh:mm:ss DD/MM/YYYY");
    }

    static public String getYearString(DateInfo timeInfo) {
        return timeInfo.format("YYYY");
    }

    static public String getMonthString(DateInfo timeInfo) {
        return timeInfo.format("MMMM");
    }

    static public String getShortWeekdayString(DateInfo timeInfo) {
        return timeInfo.format("WWW");
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        DateTime current = DateTime.forInstant(calendar.getTimeInMillis(), TimeZone.getDefault());

        return "DateInfo [notDefined=" + notDefined + ", date=" + current.format("YYYY-MM-DD hh:mm:ss") + "]";
    }
}
