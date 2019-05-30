package cc.blogx.minipro.model;

import cc.blogx.minipro.constant.CalendarConstant;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class LeapMonthInfo {

    private int year;
    private int beginYear;
    private int lunarTable;

    public void setYear(int year) {
        this.year = year;
        this.beginYear = CalendarConstant.INIT_LUNAR_YEAR;
        this.lunarTable = CalendarConstant.LUNAR_TABLE[year - this.beginYear];
    }

    public int getYear() {
        return year;
    }

    public int getBeginYear() {
        return beginYear;
    }

    public int getLunarTable() {
        return lunarTable;
    }

    public boolean isHasLeapMonth() {
        return (this.lunarTable & 0xf) != 0;
    }

    public int getLeapMonth() {
        return this.lunarTable & 0xf;
    }

    public int getLeapMonthDays() {
        return (this.lunarTable >> 16) > 0 ? 30 : 29;
    }

    // 当前农历年的总天数
    public int getThisYearCountDays() {
        int sum = 0;
        if (isHasLeapMonth()) {
            sum += getLeapMonthDays();
        }
//        getLunarTable()

        return sum;
    }

    // 从公历1901-01-01 当前间隔天数
    public int getUntilThisYearCountDays(DateTime dateTime) {
        DateTime init = DateTime.parse(CalendarConstant.INIT_1901_01_01);
        return Days.daysBetween(init, dateTime).getDays();
    }

}
