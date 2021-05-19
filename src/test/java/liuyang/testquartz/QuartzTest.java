package liuyang.testquartz;

import liuyang.testquartz.quartz.HelloJob;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 最简单的Quartz任务示例
 * 参考视频：https://www.bilibili.com/video/BV1zz4y1X71Z
 *
 * @author liuyang
 * @scine 2021/5/11
 */
@Slf4j
public class QuartzTest {

    private static final String JOB_GROUP_NAME = "LIUYANG_QUARTZ_SINGLE_JOB_GROUP_NAME_single";
    private static final String TRIGGER_GROUP_NAME = "LIUYANG_QUARTZ_SINGLE_TRIGGER_GROUP_NAME_single";

    @BeforeEach
    public void init() {

    }

    /**
     * 构建一个只运行一次的定时任务
     */
    @Test
    public void runOnceTask() throws SchedulerException, InterruptedException {
        // 1. Scheduler
        // org.springframework.scheduling.quartz.SchedulerFactoryBean
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 2。 Trigger
        // org.springframework.scheduling.quartz.CronTriggerFactoryBean
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", TRIGGER_GROUP_NAME)
                .startNow()
                // .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(1)) // 貌似不设置Interval是不行的。
                // 只运行一次的要点：1. withRepeatCount(0)。 2. 设置withIntervalInSeconds(1), 只要不是0就好，否则报错。
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).withRepeatCount(0)) // 只运行一次
                .build();

        // 3. JobDetail
        // org.springframework.scheduling.quartz.JobDetailFactoryBean
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity("job1", JOB_GROUP_NAME).build(); // HelloJob extends QuartzJobBean

        // 4. 将JobDetail和Trigger增加到scheduler中
        // Spring Framewor Env：这一步与单独使用Quartz略有不同。配置是通过把JobDetail注册到Trigger中来完成。
        scheduler.scheduleJob(jobDetail, trigger);

        // 5. 启动调度器
        log.info("scheduler.start()运行前 job1是否存在 ：" + scheduler.checkExists(JobKey.jobKey("job1", JOB_GROUP_NAME)));
        scheduler.start();// 启动
        log.info("scheduler.start()运行后 job1是否存在 ：" + scheduler.checkExists(JobKey.jobKey("job1", JOB_GROUP_NAME)));
        // scheduler.getCurrentlyExecutingJobs();

        // 听一下看效果
        TimeUnit.SECONDS.sleep(20);

        // 清理工作, 验证是否job1被自己清理掉。
        log.info("确定job1 已经运行完一次后查看 job1是否存在 ：" + scheduler.checkExists(JobKey.jobKey("job1", JOB_GROUP_NAME)));
        // 结论：如果是执行一次的任务，貌似是自己就清除了。

    }
}
