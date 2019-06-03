package cc.blogx.minipro.model;

import cc.blogx.minipro.constant.CalendarConstant;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Arrays;

public class LeapMonthInfo {

    private int year;
    private int lunarTable;

    public void setYear(int year) {
        this.year = year;
        this.lunarTable = CalendarConstant.LUNAR_TABLE[year - CalendarConstant.INIT_TABLE_INDEX];
    }

    public int getYear() {
        return year;
    }

    public int getLunarTable() {
        return lunarTable;
    }

    public boolean isHasLeapMonth() {
        return (this.lunarTable & 0xf) != 0;
    }

    public int getLeapMonth() {
        int mon = this.lunarTable & 0xf;
        if (mon > 0) {
            return mon;
        }
        return -1;
    }

    public int getLeapMonthDays() {
        return (this.lunarTable >> 16) > 0 ? 30 : 29;
    }

    // 当前农历年的总天数
    public int getThisYearCountDays() {
        int sum = 0;
        int[] monMapper = getMonthMapper();
        sum += Arrays.stream(monMapper).sum();
        return sum;
    }

    public int[] getMonthMapper() {
        int table = getLunarTable() >> 4;
        int months[] = new int[isHasLeapMonth() ? 14 : 13];
        for (int i = isHasLeapMonth() ? 13 : 12; i > 0; i--) {
            if (isHasLeapMonth() && ((i - 1) == getLeapMonth())) {
                months[i] = getLeapMonthDays();
                continue;
            }
            if ((table & 0x1) > 0) {
                months[i] = 30;
            } else {
                months[i] = 29;
            }
            table = table >> 1;
        }
        return months;
    }

}
