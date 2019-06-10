package cc.blogx.minipro.service.impl;

import cc.blogx.minipro.constant.CalendarConstant;
import cc.blogx.minipro.model.CalendarDay;
import cc.blogx.minipro.model.CalendarParam;
import cc.blogx.minipro.model.CalendarVO;
import cc.blogx.minipro.model.LeapMonthInfo;
import cc.blogx.minipro.service.CalendarService;
import cc.blogx.utils.common.RedisUtils;
import cc.blogx.utils.date.DateUtils;
import com.alibaba.fastjson.JSON;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
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

        String cacheKey = DateUtils.getDay(dateTime);
        String cacheValue = RedisUtils.get(cacheKey);
        if (StringUtils.isEmpty(cacheValue)) {
            CalendarVO calendar = new CalendarVO();
            // 公历当月第一天星期几
            calendar.setBeginWeek(DateUtils.getFirstDayOfMonOfWeek(dateTime));
            // 公历当月一共多少天
            calendar.setMaxDay(DateUtils.getDaysOfMon(dateTime));

            Map<String, String> lunarMap = getLunarInfo(dateTime);
            Map<String, String> surpriseMap = getSurpriseInfo(dateTime);
            Set<String> holidays = getHolidayInfo(dateTime);
            Set<String> workings = getWorkingInfo(dateTime);
            calendar.setDays(buildCalendarDays(lunarMap, surpriseMap, holidays, workings));
            RedisUtils.set(cacheKey, JSON.toJSONString(calendar));
            return calendar;
        } else {
            return JSON.parseObject(cacheValue, CalendarVO.class);
        }
    }

    private List<CalendarDay> buildCalendarDays(Map<String, String> lunarMap, Map<String, String> surpriseMap,
                                                Set<String> holidays, Set<String> workings) {
        List<CalendarDay> days = new ArrayList<>();

        lunarMap.forEach((key, value) -> {
            CalendarDay day = new CalendarDay();
            day.setGre(key);
            day.setLunar(value);
            if (surpriseMap.containsKey(key)) day.setSurprise(surpriseMap.get(key));
            if (holidays.contains(key)) day.setHappy(true);
            if (workings.contains(key)) day.setSad(true);
            days.add(day);
        });
        days.sort(Comparator.comparing(CalendarDay::getGre));
        return days;
    }


    private Map<String, String> getLunarInfo(DateTime dateTime) {
        int countGreDays = getGreUntilThisYearCountDays(dateTime);
        int countLunarDays = getLunarUntilThisYearCountDays(dateTime);
        int offset = CalendarConstant.INIT_OFFSET - (countGreDays - countLunarDays);
        return curMonLunarInfo(dateTime, dateTime.getYear(), dateTime.getMonthOfYear(), offset);
    }

    private Map<String, String> curMonLunarInfo(DateTime dateTime, int lunarYear, int lunarMon, int offset) {
        LeapMonthInfo leapMonthInfo = new LeapMonthInfo();
        leapMonthInfo.setYear(lunarYear);
        // 默认农历的月份是当前公历月份的上一个月
        lunarMon -= 1;
        // 如果为润月 则修正月份优先为润月的
        if (leapMonthInfo.getLeapMonth() == lunarMon) {
            lunarMon += 1;
        }
        //  如果当前是1月，则修正月份为12月
        if (lunarMon == 0) {
            lunarMon = 12;
            lunarYear -= 1;
        }
        int lunarMonDays = leapMonthInfo.getMonthMapper()[lunarMon];
        if (offset < lunarMonDays) {
            return buildLunarInfo(dateTime, lunarYear, lunarMon, lunarMonDays, lunarMonDays - offset);
        }
        return curMonLunarInfo(dateTime, lunarYear, lunarMon, offset - lunarMonDays);
    }

    private Map<String, String> buildLunarInfo(DateTime dateTime, int lunarYear, int lunarMon, int lunarDays, int lunarBegin) {
        Map<String, String> map = new HashMap<>();
        int days = DateUtils.getDaysOfMon(dateTime);
        for (int i = 1; i <= days; i++) {
            if (lunarBegin > lunarDays) {
                lunarBegin = 1;
                lunarMon += 1;
            }
            map.put(DateUtils.getDay(dateTime), getLunarDate(lunarYear, lunarMon, lunarBegin));
            lunarBegin++;
            dateTime = dateTime.plusDays(1);
        }
        return map;
    }

    private String getLunarDate(int year, int mon, int day) {
        String yearStr = CalendarConstant.LUNAR_YEAR[(year / 1000) % 10] + CalendarConstant.LUNAR_YEAR[(year / 100) % 10] +
                CalendarConstant.LUNAR_YEAR[(year / 10) % 10] + CalendarConstant.LUNAR_YEAR[year % 10];
        return yearStr + CalendarConstant.LUNAR_MONTHS[mon] + CalendarConstant.LUNAR_DAYS[day];
    }

    // 从公历1901-01-01 当前间隔天数
    private int getGreUntilThisYearCountDays(DateTime dateTime) {
        DateTime init = DateTime.parse(CalendarConstant.INIT_1901_01_01);
        return Days.daysBetween(init, dateTime).getDays();
    }

    private int getLunarUntilThisYearCountDays(DateTime dateTime) {
        int curYear = dateTime.getYear();
        int curMon = dateTime.getMonthOfYear() - 1;
        int preYear = curYear - 1;
        int countLunarDays = 0;
        LeapMonthInfo leapMonthInfo = new LeapMonthInfo();
        while (preYear >= CalendarConstant.INIT_1901) {
            leapMonthInfo.setYear(preYear);
            countLunarDays += leapMonthInfo.getThisYearCountDays();
            preYear--;
        }
        leapMonthInfo.setYear(curYear);
        int[] curMonMapper = leapMonthInfo.getMonthMapper();
        if (leapMonthInfo.isHasLeapMonth()) {
            curMon += 1;
        }
        for (int i = 1; i <= curMon; i++) {
            countLunarDays += curMonMapper[i];
        }
        return countLunarDays;
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


}
