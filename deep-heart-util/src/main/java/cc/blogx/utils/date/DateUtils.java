package cc.blogx.utils.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class DateUtils {

    private final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private final static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String getCurDay() {
        DateTime dateTime = new DateTime();
        DateTimeFormatter formatter = DateTimeFormat.forPattern(YYYY_MM_DD);
        return dateTime.toString(formatter);
    }

    public static String getCurTime() {
        DateTime dateTime = new DateTime();
        DateTimeFormatter formatter = DateTimeFormat.forPattern(YYYY_MM_DD_HH_MM_SS);
        return dateTime.toString(formatter);
    }

    public static String getFirstDayOfCurMonOfWeek() {
        DateTime dateTime = new DateTime();
        return dateTime.dayOfMonth().withMinimumValue().dayOfWeek().getAsString();
    }

    public static String getLastDayOfCurMonOfWeek() {
        DateTime dateTime = new DateTime();
        return dateTime.dayOfMonth().withMaximumValue().dayOfWeek().getAsString();
    }

    public static String getDayOfMon() {
        DateTime dateTime = new DateTime();
        return dateTime.dayOfMonth().withMaximumValue().dayOfMonth().getAsString();
    }


    public static void main(String[] args) {
        DateTime dateTime = new DateTime();
        System.out.println(dateTime.minusDays(1).dayOfWeek().getAsString());
        System.out.println(dateTime.dayOfMonth().withMinimumValue().dayOfWeek().getAsString());
        System.out.println(getDayOfMon());
    }
}
