package liuyang.testquartz.quartz;

import liuyang.testquartz.component.TestComponentInSpringEnv;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

/**
 * @author liuyang
 * @scine 2021/4/1
 *
 * 说明：还有的写法是 extends org.quartz.Job 覆盖的是execute方法。
 * 不过execute方法也会传入JobExecutionContext参数。
 *
 */
@Slf4j
@DisallowConcurrentExecution
//@PersistJobDataAfterExecution
public class Hello01Job extends QuartzJobBean {

    @Autowired
    private TestComponentInSpringEnv testComponentInSpringEnv;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 1. 演示获取运行时参数（传递参数参见QuartzSingleConfig）
        // String param1 = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("param1");
        // String param2 = (String) jobExecutionContext.getJobDetail().getJobDataMap().get("param2");
        // String param2 = (String) jobExecutionContext.getTrigger().getJobDataMap().get("param2");
        String param1 = jobExecutionContext.getMergedJobDataMap().getString("param1"); // key相同就会被覆盖
        String param2 = jobExecutionContext.getMergedJobDataMap().getString("param2"); // key相同就会被覆盖

        param1 = param1 == null || "".equals(param1) ? "hello Quartz! " : param1;
        param2 = param2 == null || "".equals(param2) ? "in Spring Boot ENV" : param2;
        
        log.info(param1 + " " + param2 + " at " + LocalDateTime.now());
        log.info("data from Spring Environment = " + testComponentInSpringEnv.hey()); // 启动整个Spring应用没问题，但执行QuartzTest.runOnceTask会报错！（原因也很简单，runOnceTask的那种方式调用的并不是Spring托管的Bean）

        // 2. 获取运行时标志
        // 2.1 JobDetail上的标志
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        log.info("JobDetail Group = " + jobKey.getGroup());
        log.info("JobDetail Name = " + jobKey.getName());
        
        // 2.2 Trigger上的标志
        TriggerKey triggerKey = jobExecutionContext.getTrigger().getKey();
        log.info("Trigger Group = " + triggerKey.getGroup());
        log.info("Trigger Name = " + triggerKey.getName());

        // 运行结束后删掉自己
        /*
        try {
            jobExecutionContext.getScheduler().deleteJob(jobExecutionContext.getJobDetail().getKey());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        */
    }
}
