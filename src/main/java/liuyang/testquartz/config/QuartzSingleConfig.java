package liuyang.testquartz.config;

import liuyang.testquartz.quartz.HelloJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 单实例最简使用配置
 * 参考：https://blog.csdn.net/pan_junbiao/article/details/109556822
 * 视频借鉴：https://www.bilibili.com/video/BV1zz4y1X71Z?p=3 这个视频是Quartz在Spring Framework环境中的使用方法，可以借鉴！个人感觉靠谱。
 * TODO 2021/5/12 本代码中的例子仅限于能跑起来。 待改进。
 * @author liuyang
 * @scine 2021/4/1
 *
 * 注1：单纯使用Spring Boot提供的 Scheduler是不需要这个配置类的
 */
@ConditionalOnProperty(prefix = "liuyang", name = "quartz.mode", havingValue = "single")
@Configuration
public class QuartzSingleConfig {

    private static final String JOB_GRPU_NAME = "LIUYANG_QUARTZ_SINGLE_JOB_GROUP_NAME";
    private static final String TRIGGER_GROUP_NAME = "LIUYANG_QUARTZ_SINGLE_TRIGGER_GROUP_NAME";

    @Bean
    public JobDetail helloJobDetail01() {
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
                .withIdentity("helloJobDetail01", JOB_GRPU_NAME)
                //.usingJobData("param1", "hello")
                //.usingJobData("param2", "quartz")
                .storeDurably() // 即使没有Trigger关联，也不需要删除JobDetail
                .build();
        return jobDetail;
    }

    @Bean
    public Trigger helloJobTrigger01(JobDetail helloJobDetail01) {
        // 每隔2秒一次
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/2 * * * * ?");

        // 创建触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(helloJobDetail01)// 关联JobDetail
                .withIdentity("helloJobTrigger01", TRIGGER_GROUP_NAME)
                .withSchedule(cronScheduleBuilder)
                .build();
        return trigger;
    }
}
