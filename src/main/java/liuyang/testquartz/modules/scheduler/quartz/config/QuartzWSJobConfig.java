package liuyang.testquartz.modules.scheduler.quartz.config;

import liuyang.testquartz.modules.scheduler.quartz.quartzjobbean.WebServiceTestJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuyang
 * @scine 2021/6/2
 */
//@Configuration // 20210907 重构项目结构后发现cxf的client出现JAXB的问题，先不处理了。后续再定位，或重新生成stub。
public class QuartzWSJobConfig {

    private static final String JOB_GROUP_NAME = "LIUYANG_QUARTZ_SINGLE_JOB_GROUP_NAME_WS";
    private static final String TRIGGER_GROUP_NAME = "LIUYANG_QUARTZ_SINGLE_TRIGGER_GROUP_NAME_WS";

    @Bean
    public JobDetail wsJob01() {
        JobDetail jobDetail = JobBuilder.newJob(WebServiceTestJob.class)
                .withIdentity("wsJob01", JOB_GROUP_NAME)
                .storeDurably()
                .build();
        return jobDetail;
    }

    @Bean // @Qualifier("wsJob01") // 实测不需要@Qualifier也能注册成功.加上@Qualifier也可以.
    public Trigger wsTrigger01(JobDetail wsJob01) {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/1 * * * * ?");
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(wsJob01)
                .withIdentity("wsTrigger01", TRIGGER_GROUP_NAME)
                .withSchedule(cronScheduleBuilder)
                .build();
        return trigger;
    }
}
