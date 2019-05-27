package cc.blogx.model;

import java.io.Serializable;
import java.util.List;

public class CalendarVO implements Serializable {

    private String beginWeek;
    private String maxDay;
    private List<CalendarDay> days;

    public String getBeginWeek() {
        return beginWeek;
    }

    public void setBeginWeek(String beginWeek) {
        this.beginWeek = beginWeek;
    }

    public String getMaxDay() {
        return maxDay;
    }

    public void setMaxDay(String maxDay) {
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
        private boolean surprise;
        private String inLunar;


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

        public boolean isSurprise() {
            return surprise;
        }

        public void setSurprise(boolean surprise) {
            this.surprise = surprise;
        }

        public String getInLunar() {
            return inLunar;
        }

        public void setInLunar(String inLunar) {
            this.inLunar = inLunar;
        }
    }


}
