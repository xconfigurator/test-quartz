package liuyang.testquartz.modules.scheduler.quartz.quartzjobbean;

import com.alibaba.fastjson.JSON;
import liuyang.testquartz.modules.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * 为了配合触发器演示实验
 * @author liuyang
 * @scine 2021/9/9
 */
//@Component
@Slf4j
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class FooJob extends QuartzJobBean {
//public class FooJob implements Job {// 注意：虽然可以这样做，但还是推荐使用Spring Boot文档中推荐的写法 extends QuartzJobBean

    @Autowired // Spring自己的示例工程也是这样注入的。虽然Spring建议是通过构造方法注入。
    private DemoService demoService;// 在Job中享受IoC（实测，使用implements Job也可享受Spring提供的IoC服务, 虽然）

    // QuartzJobBean // 推荐用这种写法
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("xx bar Job... FooJob 被TritterKey = {} 的触发器触发了！ ", context.getTrigger().getKey());
        /*
        log.info("bar Job... IoC测试 demoService = " + demoService);
        log.info("bar Job... IoC测试 demoService.hey() = {}", demoService.hey() );
        log.info("bar Job... Param测试 JobDataMap = {}", JSON.toJSONString(context.getJobDetail().getJobDataMap()));
        */
    }

    // Job // 只是验证一下能不能这样搞
    /*
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("bar Job... FooJob 被TritterKey = {} 的触发器触发了！ ", jobExecutionContext.getTrigger().getKey());
        log.info("bar Job... FooJob JobKey = {}", jobExecutionContext.getJobDetail().getKey());
        log.info("bar Job... IoC测试 demoService = " + demoService);
        log.info("bar Job... IoC测试 demoService.hey() = {}", demoService.hey() );
        log.info("bar Job... Param测试 JobDataMap = {}", JSON.toJSONString(jobExecutionContext.getJobDetail().getJobDataMap()));
    }
    */
}
