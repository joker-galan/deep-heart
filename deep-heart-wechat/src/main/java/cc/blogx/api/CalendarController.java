package cc.blogx.api;

import cc.blogx.common.JsonResult;
import cc.blogx.model.CalendarParam;
import cc.blogx.model.CalendarVO;
import cc.blogx.service.CalendarService;
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
