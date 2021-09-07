package liuyang.testquartz;

import liuyang.testquartz.modules.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * @author liuyang
 * @scine 2021/5/31
 */
@Slf4j
@SpringBootTest
public class QuartzSpringEnvTest {

    // private static final String JOB_GROUP_NAME = "LIUYANG_QUARTZ_SINGLE_JOB_GROUP_NAME_single_TEST";
    private static final String TRIGGER_GROUP_NAME = "LIUYANG_QUARTZ_SINGLE_TRIGGER_GROUP_NAME_single_TEST";

    @Autowired
    private ApplicationContext ctx;
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private DemoService demoService;

    @Test
    void test() {
        System.out.println(demoService);
        log.info(demoService.hey());
    }

    /**
     * 使用Spring容器中的Job
     */
    @Test
    public void runOnceTask() throws SchedulerException {
        // Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail jobDetail = (JobDetail) ctx.getBean("helloJobDetail01");// 这个Bean是在QuartzSingleConfig中注册的

        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)// 指定要触发的job （这一步对触发方式2是必须的）
                .withIdentity("trigger1", TRIGGER_GROUP_NAME)
                .startNow()
                // .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(1)) // 貌似不设置Interval是不行的。
                // 只运行一次的要点：1. withRepeatCount(0)。 2. 设置withIntervalInSeconds(1), 只要不是0就好，否则报错。
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).withRepeatCount(0)) // 只运行一次
                .usingJobData("param2", "hello, from QuartzSpringEnvTest") // 从Trigger可以放
                .build();

        // scheduler.scheduleJob(jobDetail, trigger);// 用一个容器外的Trigger，去触发一个容器内的Job。
        scheduler.scheduleJob(trigger);// 用一个容器外的Trigger，去触发一个容器内的Job。
    }

    /**
     * 哭笑不得，原来执行一次的Trigger这么好写？！ 20210531
     * @throws SchedulerException
     */
    @Test
    public void rundOnceTask2() throws SchedulerException {
        JobDetail jobDetail = (JobDetail) ctx.getBean("helloJobDetail01");
        Trigger trigge = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("trigger1", TRIGGER_GROUP_NAME)
                .startNow()
                .build();
        scheduler.scheduleJob(trigge);
    }
}
