package cc.blogx.service.impl;

import cc.blogx.model.CalendarParam;
import cc.blogx.model.CalendarVO;
import cc.blogx.service.CalendarService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CalendarServiceImpl implements CalendarService {


    @Override
    public CalendarVO getMonthInfo(CalendarParam param) {
        if (StringUtils.isEmpty(param.getYear()) && StringUtils.isEmpty(param.getMonth())) {
            DateTime dateTime = new DateTime();
            return getInfo(dateTime);
        }
        if (StringUtils.isEmpty(param.getYear()) || !StringUtils.isEmpty(param.getMonth())) {
            return getInfo(param);
        }
        DateTime dateTime = new DateTime();
        return getInfo(dateTime);
    }

    private CalendarVO getInfo(DateTime dateTime) {
        CalendarVO calendar = new CalendarVO();
        return calendar;
    }
}
