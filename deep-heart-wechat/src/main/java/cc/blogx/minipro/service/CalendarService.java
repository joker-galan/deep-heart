package cc.blogx.minipro.service;

import cc.blogx.minipro.model.CalendarParam;
import cc.blogx.minipro.model.CalendarVO;
import cc.blogx.minipro.model.DayInfo;

public interface CalendarService {
    CalendarVO getMonthInfo(CalendarParam param);

    DayInfo getDayInfo(CalendarParam param);
}
