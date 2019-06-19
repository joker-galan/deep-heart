package cc.blogx.minipro.model;

import java.io.Serializable;

public class CalendarDay implements Serializable {
    private String surprise;
    private String gre;
    private String lunar;

    public String getGre() {
        return gre;
    }

    public void setGre(String gre) {
        this.gre = gre;
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
