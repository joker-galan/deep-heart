package cc.blogx.minipro.api;

import cc.blogx.minipro.model.CalendarParam;
import cc.blogx.minipro.model.CalendarVO;
import cc.blogx.minipro.service.CalendarService;
import cc.blogx.utils.common.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @PostMapping(value = "info")
    public JsonResult<CalendarVO> getMonthInfo(CalendarParam param) {
        JsonResult<CalendarVO> jsonResult = JsonResult.getSuccess();
        try {
            jsonResult.setObj(calendarService.getMonthInfo(param));
        } catch (Exception e) {
            jsonResult.setException(e);
        }
        return jsonResult;
    }
}
