package liuyang.testquartz.modules.scheduler.quartz.quartzjobbean;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 为了配合触发器演示实验
 * @author liuyang
 * @scine 2021/9/9
 */
@Slf4j
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class FooJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("bar... FooJob被 {} 触发执行了！ ", context.getTrigger().getKey());
    }
}
