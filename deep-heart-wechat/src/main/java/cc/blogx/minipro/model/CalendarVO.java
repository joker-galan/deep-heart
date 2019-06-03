package cc.blogx.minipro.model;

import java.io.Serializable;
import java.util.List;

public class CalendarVO implements Serializable {

    private int beginWeek;
    private int maxDay;
    private List<CalendarDay> days;

    public int getBeginWeek() {
        return beginWeek;
    }

    public void setBeginWeek(int beginWeek) {
        this.beginWeek = beginWeek;
    }

    public int getMaxDay() {
        return maxDay;
    }

    public void setMaxDay(int maxDay) {
        this.maxDay = maxDay;
    }

    public List<CalendarDay> getDays() {
        return days;
    }

    public void setDays(List<CalendarDay> days) {
        this.days = days;
    }


}
