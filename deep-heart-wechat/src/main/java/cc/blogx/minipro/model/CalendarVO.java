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

    public static class CalendarDay implements Serializable {
        private boolean happy;
        private boolean sad;
        private String surprise;
        private String lunar;


        public boolean isHappy() {
            return happy;
        }

        public void setHappy(boolean happy) {
            this.happy = happy;
        }

        public boolean isSad() {
            return sad;
        }

        public void setSad(boolean sad) {
            this.sad = sad;
        }

        public String getSurprise() {
            return surprise;
        }

        public void setSurprise(String surprise) {
            this.surprise = surprise;
        }

        public String getLunar() {
            return lunar;
        }

        public void setLunar(String lunar) {
            this.lunar = lunar;
        }
    }


}
