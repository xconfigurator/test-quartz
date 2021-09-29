package liuyang.testquartz.modules.scheduler.quartz.config;

import liuyang.testquartz.common.util.DateUtil;
import liuyang.testquartz.modules.scheduler.quartz.quartzjobbean.FooJob;
import liuyang.testquartz.modules.scheduler.quartz.quartzjobbean.Hello01Job;
import lombok.SneakyThrows;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.time.LocalDateTime;

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
//@Configuration
// public class QuartzSingleConfig {
public class QuartzSingleConfig implements SchedulerFactoryBeanCustomizer {
    private static final String JOB_GROUP_NAME = "LIUYANG_QUARTZ_SINGLE_JOB_GROUP_NAME";
    private static final String TRIGGER_GROUP_NAME = "LIUYANG_QUARTZ_SINGLE_TRIGGER_GROUP_NAME";

    // /////////////////////////////////////////////////////////////////////////////////////////////////////
    // 演示触发器
    @Bean
    public JobDetail fooJobDetail() {
        JobDetail jobDetail = JobBuilder.newJob(FooJob.class)
                .withIdentity("fooJobDetail", JOB_GROUP_NAME)
                //.usingJobData("param1", "hello from JobDetail")
                .storeDurably()
                .build();
        return jobDetail;
    }

    /**
     * SimpleTrigger
     * 以一定的时间间隔（单位是毫秒）执行的任务。
     * 指定起始时间和截止时间（时间段）
     * 指定时间间隔、执行次数。
     *
     * 官网：http://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/tutorial-lesson-05.html
     * 中文：https://www.w3cschool.cn/quartz_doc/quartz_doc-67a52d1f.html
     * @return
     */
    @Bean
    public Trigger demoSimpleTrigger01(JobDetail fooJobDetail) {
        // 每秒执行一次，一共执行四次（实际会执行五次，因为这个一旦start就会执行一次且不算做重复次数内。）
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(2)
                //.withRepeatCount(4);
                .repeatForever();

        // SimpleTrigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("demoSimpleTrigger01", TRIGGER_GROUP_NAME)
                .forJob(fooJobDetail)// 指定要触发的对象
                .withSchedule(simpleScheduleBuilder)
                .startNow()
                //.endAt(DateUtil.asDate(LocalDateTime.now().plusHours(2)))
                .endAt(null) // 如果传入的endTime是可选项 20210923测试 ok
                //.endAt(DateUtil.asDate(LocalDateTime.now().plusSeconds(10)))// 只执行10s
                .build();
        return trigger;
    }

    /**
     * CronTrigger
     * cron表达式（秒 分 时 日 月 周 年（可选））
     * 视频：https://www.bilibili.com/video/BV1zz4y1X71Z?p=2 （完整视频23分钟） cron表达详细使用从视频05:20开始。
     * 工具：https://www.bejson.com/othertools/cron/
     *      该工具的使用参见视频07:07演示。（校验工具会给出表达式预期执行的8个时间点。）
     *      "/":以秒为例
     *          0/3 从0秒开始 每3秒（步长）执行一次
     *          15/3 从第15秒开始执行 没3秒（步长）执行一次
     *      "?":一般用于日期、星期位置，以处理逻辑冲突
     *      "L":last,用在日期和星期（只写l代表周六）上。
     *          6l 放在星期上表示某月的最后一个周五
     *      "W":Work,工作日,放在日期字段上。
     *          10W 放在日期上，代表距离某月10日最近的一个工作日（六、日是非工作日），W不跨月。貌似跟中国节假日也没适配（改CRON表达式解析器来适配我国每年的节假日？）。
     *      "LW":当月的最后一个工作日
     *      "#": 用在星期字段上
     *          4#2 代表第二个周三
     * 官网：http://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/tutorial-lesson-06.html
     * 中文：https://www.w3cschool.cn/quartz_doc/quartz_doc-lwuv2d2a.html
     * @return
     */
    //@Bean
    public Trigger demoCronTrigger01(JobDetail fooJobDetail) {
        // 每4秒执行一次
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/4 * * * * ?");

        // CronTrigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("demoCronTrigger01", TRIGGER_GROUP_NAME)
                .forJob(fooJobDetail)
                .withSchedule(cronScheduleBuilder)
                // 可选属性
                .endAt(DateUtil.asDate(LocalDateTime.now().plusMinutes(1)))
                .build();

        return trigger;
    }

    // /////////////////////////////////////////////////////////////////////////////////////////////////////
    // 演示详细用法（传参等）
    //@Bean // 疑问点：JobDetail和Trigger有没有必要使用Spring容器托管？ 答：这是文档推荐写法
    public JobDetail helloJobDetail01() {
        JobDetail jobDetail = JobBuilder.newJob(Hello01Job.class)
                .withIdentity("helloJobDetail01", JOB_GROUP_NAME)
                //.usingJobData("param1", "hello") // 给Job传参
                //.usingJobData("param2", "quartz")
                .usingJobData("param1", "hello from JobDetail")
                .storeDurably() // storeDurably的意思是，当一个job没有trigger关联的时候是否应该被保存起来。Spring Boot这种写法
                // 经测试，如果不设置这个项，且采用scheduler.scheduleJob(jobDetail, trigger)方式触发，则在一次任务执行完毕后，该jobDetail会被quartz自动清理出scheduler的上下文。
                .build();
        return jobDetail;
    }

    //@Bean // 疑问点：JobDetail和Trigger有没有必要使用Spring容器托管？ 答；这是文档推荐写法。
    public Trigger helloJobTrigger01(JobDetail helloJobDetail01) {
        // 每隔*秒一次
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/25 * * * * ?");

        // 创建触发器
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(helloJobDetail01)// 关联JobDetail // 也可以通过SchedulerFactoryBean来建立联系，参见customize方法。（这个方法来自于SchedulerFactoryBeanCustomizer接口），貌似不可以！ 2021/5/31
                .withIdentity("helloJobTrigger01", TRIGGER_GROUP_NAME)
                //.usingJobData("", "")// 给Trigger传参
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
        // 注意：Scheduler是Quartz的核心组件，job和trigger都要注册到scheduler上才能生效。

        // 1. 订制SchedulerFactoryBean的属性
        // TODO 在这里配置名为quartzScheduler的Bean的属性。 详细参见QuartzAutoConfiguration.java

        // 2. 关联JobDetail和Trigger
        // 疑问点：JobDetail和Trigger有没有必要使用Spring容器托管？
        // TODO 这样不行，貌似这个时候还没有scheduler
        // schedulerFactoryBean.getScheduler().scheduleJob((JobDetail) applicationContext.getBean("helloJobDetail01"), (Trigger) applicationContext.getBean("helloJobTrigger01"));
    }
}
