package liuyang.testquartz;

import liuyang.testquartz.component.TestComponentInSpringEnv;
import liuyang.testquartz.quartz.Hello01Job;
import liuyang.testquartz.quartz.Hello02Job;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * 最简单的Quartz任务示例
 * 参考视频：https://www.bilibili.com/video/BV1zz4y1X71Z
 *
 * 结论：202105311546, 如果是一次性的job考虑使用触发方式1进行，并且不要使用.storeDurably() ，那样Quartz会自动清理没有跟Trigger关联的job。
 *
 * @author liuyang
 * @scine 2021/5/11
 */
@Slf4j
public class QuartzTest {

    private static final String JOB_GROUP_NAME = "LIUYANG_QUARTZ_SINGLE_JOB_GROUP_NAME_single_TEST";
    private static final String TRIGGER_GROUP_NAME = "LIUYANG_QUARTZ_SINGLE_TRIGGER_GROUP_NAME_single_TEST";

    @BeforeEach
    public void init() {

    }

    /**
     * 构建一个只运行一次的定时任务（这是一个脱离Spring容器的例子）
     * 问题：Hello01Job引入Spring容器内组件之后，再执行本用例就会报错。
     *      启动整个Spring应用没问题，但执行QuartzTest.runOnceTask会报错！（原因也很简单，runOnceTask的那种方式调用的并不是Spring托管的Bean）
     * 定位：并不是QuartzJobBean写法有问题，而是测试用例应从容器中获取已装配好的QuartzJobBean，参见QuartzSpringEnvTest.runOnceTask()
     */
    @Test
    public void runOnceTask() throws SchedulerException, InterruptedException {
        // 1. Scheduler
        // org.springframework.scheduling.quartz.SchedulerFactoryBean
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 2. JobDetail
        // org.springframework.scheduling.quartz.JobDetailFactoryBean
        JobDetail jobDetail = JobBuilder
                //.newJob(Hello01Job.class) // Hello01Job需要Spring容器支持
                .newJob(Hello02Job.class)
                .withIdentity("job1", JOB_GROUP_NAME)
                .usingJobData("param1", "hello, from QuartzTest JobDetail") // 从JobDetail可以放
                .storeDurably() // 这个对于触发方式2是必须的 org.quartz.SchedulerException: Jobs added with no trigger must be durable.
                // storeDurably的意思是，当一个job没有trigger关联的时候是否应该被保存起来。
                .build(); // HelloJob extends QuartzJobBean

        // 3。 Trigger
        // org.springframework.scheduling.quartz.CronTriggerFactoryBean
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)// 指定要触发的job （这一步对触发方式2是必须的）
                .withIdentity("trigger1", TRIGGER_GROUP_NAME)
                .startNow()
                // .withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(1)) // 貌似不设置Interval是不行的。
                // 只运行一次的要点：1. withRepeatCount(0)。 2. 设置withIntervalInSeconds(1), 只要不是0就好，否则报错。
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).withRepeatCount(0)) // 只运行一次
                .usingJobData("param2", "hello, from QuartzTest Trigger") // 从Trigger可以放
                .build();

        // 4. 将JobDetail和Trigger增加到scheduler中
        // 触发方式1：明确指定jobDetail和trigger之间的关系。
        // Spring Framework Env：这一步与单独使用Quartz略有不同。配置是通过把JobDetail注册到Trigger中来完成。
        /*
        scheduler.scheduleJob(jobDetail, trigger);
        */
        // 触发方式2：需要在trigger上指定要触发的job，但是前提告诉scheduler，都有哪些job。
        // Spring Framework Env: 似乎与这种触发方式更为接近。
        scheduler.addJob(jobDetail, true);// 第二个参数为true，表示如果有重名的job则替换。
        scheduler.scheduleJob(trigger);// 在这里就可以直接搞这个trigger了
        // 结论：202105311546, 如果是一次性的job考虑使用触发方式1进行，并且不要使用.storeDurably() ，那样Quartz会自动清理没有跟Trigger关联的job。

        // 5. 启动调度器
        log.info("scheduler.start()运行前 job1是否存在 ：" + scheduler.checkExists(JobKey.jobKey("job1", JOB_GROUP_NAME)));
        scheduler.start();// 启动
        log.info("scheduler.start()运行后 job1是否存在 ：" + scheduler.checkExists(JobKey.jobKey("job1", JOB_GROUP_NAME)));
        // scheduler.getCurrentlyExecutingJobs();

        // 听一下看效果
        TimeUnit.SECONDS.sleep(10);

        // 清理工作, 验证是否job1被自己清理掉。
        log.info("确定job1 已经运行完一次后查看 job1是否存在 ：" + scheduler.checkExists(JobKey.jobKey("job1", JOB_GROUP_NAME)));
        // 结论：如果是执行一次的任务，貌似是自己就清除了。

    }
}
