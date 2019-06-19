package cc.blogx.minipro.service.impl;

import cc.blogx.minipro.constant.CalendarConstant;
import cc.blogx.minipro.model.*;
import cc.blogx.minipro.service.CalendarService;
import cc.blogx.utils.common.RedisUtils;
import cc.blogx.utils.date.DateUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class CalendarServiceImpl implements CalendarService {


    @Override
    public DayInfo getDayInfo(CalendarParam param) {

        return null;
    }

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
        return initDayInfo(getInfo(dateTime));
    }

    private CalendarVO initDayInfo(CalendarVO result) {
        DateTime date = new DateTime();
        String curDay = DateUtils.getDay(date);
        DayInfo day = new DayInfo();
        day.setGre(curDay);
        day.setLunar(result.getDays().get(date.getDayOfMonth() - 1).getLunar());
        day.setSpecial(true);
//        day.setFestival("中秋节");
        day.setBuddha(DateUtils.getDayChina(date.plusYears(CalendarConstant.BUDDHA_OFFSET)));
        day.setWeek(CalendarConstant.WEEKS[date.getDayOfWeek()]);
        result.setDayInfo(day);
        return result;
    }

    private CalendarVO getInfo(DateTime dateTime) {

        CalendarVO calendar = new CalendarVO();
        String cacheKey = DateUtils.getDay(dateTime);
        String cacheValue = RedisUtils.get(cacheKey);
        Map<String, String> surpriseMap = getSurpriseInfo(dateTime);
        Map<String, String> lunarMap;
        if (StringUtils.isEmpty(cacheValue)) {
            lunarMap = getLunarInfo(dateTime);
            RedisUtils.set(cacheKey, JSON.toJSONString(lunarMap));
        } else {
//            lunarMap = JSON.parseObject(cacheValue, Map.class);
            lunarMap = JSON.parseObject(cacheValue, new TypeReference<Map<String, String>>() {});
        }
        // 公历当月第一天星期几
        calendar.setBeginWeek(DateUtils.getFirstDayOfMonOfWeek(dateTime));
        // 公历当月一共多少天
        calendar.setMaxDay(DateUtils.getDaysOfMon(dateTime));
        calendar.setDays(buildCalendarDays(lunarMap, surpriseMap));
        return calendar;
    }

    private List<CalendarDay> buildCalendarDays(Map<String, String> lunarMap, Map<String, String> surpriseMap) {
        List<CalendarDay> days = new ArrayList<>();

        lunarMap.forEach((key, value) -> {
            CalendarDay day = new CalendarDay();
            day.setGre(key);
            day.setLunar(value);
            if (surpriseMap.containsKey(key)) day.setSurprise(surpriseMap.get(key));
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

    /**
     * 从数据库中获取当月已添加的纪念日
     *
     * @param dateTime
     * @return
     */
    private Map<String, String> getSurpriseInfo(DateTime dateTime) {
        Map<String, String> map = new HashMap<>();
        map.put("2019-06-28", "结婚纪念日");
        //TODO 从数据库中获取当月已添加的纪念日
        return map;
    }
}
