package liuyang.testquartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync // @Async
@EnableScheduling // @Scheduled // Quartz不需要@EnableScheduling注解
@SpringBootApplication
public class TestQuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestQuartzApplication.class, args);
    }

}
