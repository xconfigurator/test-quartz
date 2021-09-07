package liuyang.testquartz.modules.scheduler.quartz.config;

import liuyang.testquartz.modules.scheduler.quartz.quartzjobbean.Hello01Job;
import lombok.SneakyThrows;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

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
// public class QuartzSingleConfig {
public class QuartzSingleConfig implements SchedulerFactoryBeanCustomizer {
    private static final String JOB_GROUP_NAME = "LIUYANG_QUARTZ_SINGLE_JOB_GROUP_NAME";
    private static final String TRIGGER_GROUP_NAME = "LIUYANG_QUARTZ_SINGLE_TRIGGER_GROUP_NAME";

    //@Autowired
    //private ApplicationContext applicationContext;

    @Bean // 疑问点：JobDetail和Trigger有没有必要使用Spring容器托管？ 答：这是文档推荐写法
    public JobDetail helloJobDetail01() {
        JobDetail jobDetail = JobBuilder.newJob(Hello01Job.class)
                .withIdentity("helloJobDetail01", JOB_GROUP_NAME)
                //.usingJobData("param1", "hello")
                //.usingJobData("param2", "quartz")
                .usingJobData("param1", "hello from JobDetail")
                .storeDurably() // storeDurably的意思是，当一个job没有trigger关联的时候是否应该被保存起来。Spring Boot这种写法
                // 经测试，如果不设置这个项，且采用scheduler.scheduleJob(jobDetail, trigger)方式触发，则在一次任务执行完毕后，该jobDetail会被quartz自动清理出scheduler的上下文。
                .build();
        return jobDetail;
    }

    @Bean // 疑问点：JobDetail和Trigger有没有必要使用Spring容器托管？ 答；这是文档推荐写法。
    public Trigger helloJobTrigger01(JobDetail helloJobDetail01) {
        // 每隔2秒一次
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/25 * * * * ?");

        // 创建触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(helloJobDetail01)// 关联JobDetail // 也可以通过SchedulerFactoryBean来建立联系，参见customize方法。（这个方法来自于SchedulerFactoryBeanCustomizer接口），貌似不可以！ 2021/5/31
                .withIdentity("helloJobTrigger01", TRIGGER_GROUP_NAME)
                .withSchedule(cronScheduleBuilder) // 触发策略(Simple, Cron, 日历)
                //.startNow()
                //.startAt()
                .usingJobData("param2", "quartz from Trigger")
                .build();

        return trigger;
    }

    @SneakyThrows // 这个注释研究一下！！
    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        // 1. 订制SchedulerFactoryBean的属性
        // TODO 在这里配置名为quartzScheduler的Bean的属性。 详细参见QuartzAutoConfiguration.java

        // 2. 关联JobDetail和Trigger
        // 疑问点：JobDetail和Trigger有没有必要使用Spring容器托管？
        // TODO 这样不行，貌似这个时候还没有scheduler
        // schedulerFactoryBean.getScheduler().scheduleJob((JobDetail) applicationContext.getBean("helloJobDetail01"), (Trigger) applicationContext.getBean("helloJobTrigger01"));
    }
}
