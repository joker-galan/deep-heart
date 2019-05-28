package cc.blogx.minipro.service.impl;

import cc.blogx.minipro.model.CalendarParam;
import cc.blogx.minipro.model.CalendarVO;
import cc.blogx.minipro.service.CalendarService;
import cc.blogx.utils.date.DateUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CalendarServiceImpl implements CalendarService {


    @Override
    public CalendarVO getMonthInfo(CalendarParam param) {
        if (!StringUtils.isEmpty(param.getYear()) || !StringUtils.isEmpty(param.getMonth())) {
            int year = StringUtils.isEmpty(param.getYear()) ? DateTime.now().getYear() : Integer.valueOf(param.getYear());
            int month = StringUtils.isEmpty(param.getMonth()) ? DateTime.now().getMonthOfYear() : Integer.valueOf(param.getMonth());
            DateTime dateTime = new DateTime(year, month, 1, 0, 0);
            return getInfo(dateTime);
        }
        // 当月第一天0点
        DateTime dateTime = new DateTime().dayOfMonth().withMinimumValue().withMillisOfDay(0);
        return getInfo(dateTime);
    }

    private CalendarVO getInfo(DateTime dateTime) {

        CalendarVO calendar = new CalendarVO();

        calendar.setBeginWeek(DateUtils.getFirstDayOfMonOfWeek(dateTime));
        calendar.setMaxDay(DateUtils.getDaysOfMon(dateTime));

        Map<String, String> lunarMap = getLunarInfo(dateTime, calendar.getMaxDay());
        Map<String, String> surpriseMap = getSurpriseInfo(dateTime);
        Set<String> holidays = getHolidayInfo(dateTime);
        Set<String> workings = getWorkingInfo(dateTime);

        calendar.setDays(buildCalendarDays(lunarMap, surpriseMap, holidays, workings));
        return calendar;
    }

    private List<CalendarVO.CalendarDay> buildCalendarDays(Map<String, String> lunarMap, Map<String, String> surpriseMap,
                                                           Set<String> holidays, Set<String> workings) {
        List<CalendarVO.CalendarDay> days = new ArrayList<>();
        lunarMap.forEach((key, value) -> {
            CalendarVO.CalendarDay day = new CalendarVO.CalendarDay();
            day.setLunar(lunarMap.get(value));
            if (surpriseMap.containsKey(key)) day.setSurprise(surpriseMap.get(key));
            if (holidays.contains(key)) day.setHappy(true);
            if (workings.contains(key)) day.setSad(true);
            days.add(day);
        });
        return days;
    }

    private Map<String, String> getLunarInfo(DateTime dateTime, int days) {
        Map<String, String> map = new HashMap<>();



        for (int i = 0; i < days; i++) {

            dateTime = dateTime.plusDays(0);

        }
        return map;
    }

    private Map<String, String> getSurpriseInfo(DateTime dateTime) {
        Map<String, String> map = new HashMap<>();
        //TODO 从数据库中获取当月已添加的纪念日
        return map;
    }

    private Set<String> getWorkingInfo(DateTime dateTime) {
        Set<String> set = new HashSet<>();
        return set;
    }

    private Set<String> getHolidayInfo(DateTime dateTime) {
        Set<String> set = new HashSet<>();
        return set;
    }

    private void calcLunarCalendar(DateTime dateTime, int days) {
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();





    }

}
