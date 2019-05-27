package cc.blogx.model;

import java.io.Serializable;

public class CalendarParam implements Serializable {

    private String year;
    private String month;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
