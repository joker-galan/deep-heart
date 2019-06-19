package cc.blogx.minipro.model;

import java.io.Serializable;

public class DayInfo implements Serializable {
    private String week;
    private String gre;
    private String day;
    private String month;
    private String lunar;
    private String buddha;
    private String festival;
    private boolean special;

    public String getDay() {
        return gre.substring(8);
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return gre.substring(0, 4) + "年" + gre.substring(5, 7) + "月";
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getLunar() {
        return lunar.substring(4);
    }

    public void setLunar(String lunar) {
        this.lunar = lunar;
    }

    public String getGre() {
        return gre;
    }

    public void setGre(String gre) {
        this.gre = gre;
    }

    public String getBuddha() {
        return buddha;
    }

    public void setBuddha(String buddha) {
        this.buddha = buddha;
    }

    public String getFestival() {
        return festival;
    }

    public void setFestival(String festival) {
        this.festival = festival;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }
}
