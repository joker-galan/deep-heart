package cc.blogx.utils.date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class DateUtils {

    private final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private final static String YYYY_MM_DD = "yyyy-MM-dd";
    private final static String aaa = "aa";

    public static String getDay() {
        DateTime dateTime = new DateTime();
        return getDay(dateTime);
    }

    public static String getDay(DateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(YYYY_MM_DD);
        return dateTime.toString(formatter);
    }

    public static String getStrTime() {
        DateTime dateTime = new DateTime();
        return getStrTime(dateTime);
    }

    public static String getStrTime(DateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(YYYY_MM_DD_HH_MM_SS);
        return dateTime.toString(formatter);
    }

    public static int getFirstDayOfMonOfWeek() {
        DateTime dateTime = new DateTime();
        return getFirstDayOfMonOfWeek(dateTime);
    }

    public static int getFirstDayOfMonOfWeek(DateTime dateTime) {
        return dateTime.dayOfMonth().withMinimumValue().dayOfWeek().get();
    }

    public static int getLastDayOfMonOfWeek() {
        DateTime dateTime = new DateTime();
        return getLastDayOfMonOfWeek(dateTime);
    }

    public static int getLastDayOfMonOfWeek(DateTime dateTime) {
        return dateTime.dayOfMonth().withMaximumValue().dayOfWeek().get();
    }

    public static int getDaysOfMon() {
        DateTime dateTime = new DateTime();
        return getDaysOfMon(dateTime);
    }

    public static int getDaysOfMon(DateTime dateTime) {
        return dateTime.dayOfMonth().withMaximumValue().dayOfMonth().get();
    }

    public static void main(String[] args) {
//        DateTime dateTime = new DateTime();
//        System.out.println(dateTime.minusDays(1).dayOfWeek().getAsString());
//        System.out.println(dateTime.dayOfMonth().withMinimumValue().dayOfWeek().getAsString());
//        System.out.println(getDaysOfMon());
//        System.out.println(getLastDayOfMonOfWeek());
//        System.out.println(getFirstDayOfMonOfWeek());
//        System.out.printf( new DateTime().dayOfMonth().withMinimumValue().withMillisOfDay(0).toString());
    }
}
