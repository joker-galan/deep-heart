import cc.blogx.ApplicationRunning;
import cc.blogx.minipro.model.CalendarParam;
import cc.blogx.minipro.service.impl.CalendarServiceImpl;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRunning.class)
public class CalendarTest {

    @Autowired
    CalendarServiceImpl calendarService;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    public void test1() {
        CalendarParam param = new CalendarParam();
        System.out.println(JSON.toJSON(calendarService.getMonthInfo(param)));
    }

}
