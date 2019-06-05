
import cc.blogx.ApplicationRunning;
import cc.blogx.minipro.model.CalendarParam;
import cc.blogx.minipro.service.impl.CalendarServiceImpl;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRunning.class)
public class CalendarTest {

    @Autowired
    CalendarServiceImpl calendarService;

    @Test
    public void test1() {
        CalendarParam param = new CalendarParam();
        System.out.println(new Gson().toJson(calendarService.getMonthInfo(param)));
    }
}
