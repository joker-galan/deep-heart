package cc.blogx.minipro.service.impl;

import cc.blogx.minipro.constant.CalendarConstant;
import cc.blogx.minipro.model.CalendarParam;
import cc.blogx.minipro.model.CalendarVO;
import cc.blogx.minipro.model.LeapMonthInfo;
import cc.blogx.minipro.service.CalendarService;
import cc.blogx.utils.date.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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
        // 公历当月第一天星期几
        calendar.setBeginWeek(DateUtils.getFirstDayOfMonOfWeek(dateTime));
        // 公历当月一共多少天
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
        int countGreDays = getGreUntilThisYearCountDays(dateTime);
        Map<String, Object> countLunarInfo = getLunarUntilThisYearCountDaysInfo(dateTime);
        int offset = CalendarConstant.INIT_OFFSET - (countGreDays - ((Integer) countLunarInfo.get("countLunarDays")));

        int[] curMonMapper = (int[]) countLunarInfo.get("curMonMapper");
        int monIndex = (int) countLunarInfo.get("monIndex");
        int leapMon = countLunarInfo.containsKey("leapMon") ? (int) countLunarInfo.get("leapMon") : 0;

        if (monIndex > 0) {
            if (offset < curMonMapper[monIndex]) {
                return curMonLunarInfo(dateTime.getYear(), dateTime.getYear(), dateTime.getMonthOfYear(),
                        (monIndex - 1 == leapMon ? leapMon : monIndex), days, curMonMapper[monIndex], curMonMapper[monIndex] - offset);
            } else {
                return curMonLunarInfo(dateTime.getYear(), dateTime.getMonthOfYear(), days, --monIndex, curMonMapper, offset, leapMon);
            }
        } else {
            return curMonLunarInfo(dateTime.getYear(), days, monIndex, curMonMapper, offset, leapMon);
        }
    }

    private Map<String, String> curMonLunarInfo(int year, int lunarYear, int lunarMon, int mon, int days, int lunarDays, int lunarBegin) {
        Map<String, String> map = new HashMap<>();
        for (int i = 1; i <= days; i++) {
            map.put(DateUtils.getDay(new DateTime(year, mon, i, 0, 0)),
                    DateUtils.getDay(new DateTime(lunarYear, lunarMon, lunarBegin, 0, 0)));
            lunarBegin++;
            if (lunarBegin == lunarDays) {
                lunarBegin = 1;
                lunarMon += 1;
            }
        }
        return map;
    }

    private Map<String, String> curMonLunarInfo(int year, int mon, int days, int index, int[] curMonMapper, int offset, int leapMon) {
        Map<String, String> map = new HashMap<>();
        offset -= curMonMapper[index];
        // 校准数据  如果当前农历的月份为1月 则需要获取上年的12月的数据
        if (index == 1) {
            LeapMonthInfo leapMonthInfo = new LeapMonthInfo();
            leapMonthInfo.setYear(year - 1);
            curMonMapper = leapMonthInfo.getMonthMapper();


        } else {
            index -= 1;
            if (offset < curMonMapper[index]) {
                return curMonLunarInfo(year, mon, (index - 1 == leapMon ? leapMon : index), days, curMonMapper[index], curMonMapper[index] - offset);
            } else {
                this.curMonLunarInfo(year, mon, days, index, curMonMapper, offset, leapMon);
            }
        }
        return null;
    }


    // 从公历1901-01-01 当前间隔天数
    private int getGreUntilThisYearCountDays(DateTime dateTime) {
        DateTime init = DateTime.parse(CalendarConstant.INIT_1901_01_01);
        return Days.daysBetween(init, dateTime).getDays();
    }

    private Map<String, Object> getLunarUntilThisYearCountDaysInfo(DateTime dateTime) {
        Map<String, Object> map = new HashMap<>();
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
            map.put("leapMon", leapMonthInfo.getLeapMonth());
            curMon += 1;
        }
        for (int i = 1; i <= curMon; i++) {
            countLunarDays += curMonMapper[i];
        }
        map.put("monIndex", curMon);
        map.put("countLunarDays", countLunarDays);
        map.put("curMonMapper", curMonMapper);
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


}
