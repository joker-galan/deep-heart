package cc.blogx;

import cc.blogx.utils.common.AppCtxUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ApplicationRunning implements CommandLineRunner {

    public static void main(String[] args) {
        ApplicationContext appCtx = SpringApplication.run(ApplicationRunning.class, args);
        AppCtxUtils.setApplicationContext(appCtx);
    }

    @Override
    public void run(String... args) throws Exception {
        // can do something
    }
}
