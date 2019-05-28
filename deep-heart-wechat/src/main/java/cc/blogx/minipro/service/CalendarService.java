package cc.blogx.minipro.service;

import cc.blogx.minipro.model.CalendarParam;
import cc.blogx.minipro.model.CalendarVO;

public interface CalendarService {
    CalendarVO getMonthInfo(CalendarParam param);
}
