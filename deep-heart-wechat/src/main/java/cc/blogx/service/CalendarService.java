package cc.blogx.service;

import cc.blogx.model.CalendarParam;
import cc.blogx.model.CalendarVO;

public interface CalendarService {
    CalendarVO getMonthInfo(CalendarParam param);
}
