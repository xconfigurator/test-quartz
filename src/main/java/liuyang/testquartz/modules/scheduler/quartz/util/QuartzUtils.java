package liuyang.testquartz.modules.scheduler.quartz.util;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 通过taskId完成对任务的操作门面
 *
 * 功能：
 * 周期、单次任务“增、查重（同类型）、停止、继续、删”操作。
 *
 * 约定：
 * 1. *Once针对单次任务，*Periodic针对周期任务
 * 2. 所有操作面向taskId和任务（Trigger被视为任务的附属物）
 *
 * @author liuyang
 * @scine 2021/9/27
 *
 * TODO check*还没有测试！！！
 */
@Component
@Slf4j
public class QuartzUtils {

    @Autowired
    protected Scheduler scheduler;

    // 这些只在Quartz内部起标识作用
    private static final String ONCE_JOB_GROUP = "ONCE_JOB_GROUP";                  // 单次任务 组名
    private static final String ONCE_TRIGGER_GROUP = "ONCE_TRIGGER_GROUP";          // 单次任务 触发器组名
    private static final String PERIODIC_JOB_GROUP = "PERIODIC_JOBGROUP";           // 周期任务 组名
    private static final String PERIODIC_TRIGGER_GROUP = "PERIODIC_TRIGGER_GROUP";  // 周期任务 触发器组名

    /**
     * 立即执行一次任务
     * 注：这种用例主要是利用了Quartz管理的线程池，将任务异步化。
     * @param taskId
     * @param jobClass
     * @return
     */
    public boolean addOnce(String taskId, Class<? extends Job> jobClass) {
        return addOnce(taskId, jobClass, null, null, null);
    }

    /**
     * 立即执行一次任务 并向任务传递参数
     * 注：这种用例主要是利用了Quartz管理的线程池，将任务异步化。
     * @param taskId
     * @param jobClass
     * @param jobDataMap
     * @return
     */
    public boolean addOnce(String taskId, Class<? extends Job> jobClass, JobDataMap jobDataMap) {
        return addOnce(taskId, jobClass, null, null, jobDataMap);
    }

    /**
     * 在指定时刻执行一次任务
     * @param taskId
     * @param jobClass
     * @param startTime
     * @return
     */
    public boolean addOnce(String taskId, Class<? extends Job> jobClass, Date startTime) {
        return addOnce(taskId, jobClass, startTime, null, null);
    }

    /**
     * 在指定时刻执行一次任务 并向任务传递参数
     * @param taskId
     * @param jobClass
     * @param startTime
     * @param jobDataMap
     * @return
     */
    public boolean addOnce(String taskId, Class<? extends Job> jobClass, Date startTime, JobDataMap jobDataMap) {
        return addOnce(taskId, jobClass, startTime, null, jobDataMap);
    }

    /**
     * 在指定时间段内执行一次任务
     * @param taskId
     * @param jobClass
     * @param startTime
     * @param stopTime
     * @return
     */
    public boolean addOnce(String taskId, Class<? extends Job> jobClass, Date startTime, Date stopTime) {
        return addOnce(taskId, jobClass,  startTime, stopTime, null);
    }

    /**
     * 在指定时间段内执行一次的任务 并向任务传递参数
     * 1. 若startTime和endTime同时传null则立即执行。
     * 2. startTime和endTime的业务逻辑由调用处进行校验，本方法只负责向Trigger设定对应值。
     *
     * @param taskId
     * @param jobClass
     * @param jobDataMap    需要向任务传递的参数, 可以为空
     * @param startTime
     * @param stopTime
     * @return
     */
    public boolean addOnce(String taskId, Class<? extends Job> jobClass, Date startTime, Date stopTime, JobDataMap jobDataMap) {
        try {
            // 1. JobDetail
            JobBuilder jobBuilder = JobBuilder.newJob(jobClass);
            jobBuilder.withIdentity(new JobKey(taskId, ONCE_JOB_GROUP));
            //jobBuilder.storeDurably();// 指定指定该选项, 则任务执行完毕后该任务还会被记录在QRTZ_JOB_DETAILS表中。
            if (jobDataMap != null) {// 传递参数
                jobBuilder.usingJobData(jobDataMap);
            }
            JobDetail jobDetail = jobBuilder.build();

            // 2. Trigger
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            triggerBuilder.withIdentity(new TriggerKey(taskId, ONCE_TRIGGER_GROUP));
            triggerBuilder.forJob(jobDetail);
            if (null == startTime) {// 约定不指定开始时间就立即执行
                triggerBuilder.startNow();
            }
            if (null != startTime) {
                triggerBuilder.startAt(startTime);
            }
            if (null != stopTime) {
                triggerBuilder.endAt(stopTime);
            }
            Trigger trigger = triggerBuilder.build();

            // 3. 注册
            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 在指定时间段内 以指定时间间隔 重复执行任务
     * @param taskId
     * @param jobClass
     * @param startTime
     * @param stopTime      如果不填写，则表示任务永久执行
     * @param interval      任务执行时间间隔（单位：分钟）
     * @param jobDataMap    需要向任务传递的参数, 可以为空
     * @return
     */
    public boolean addPeriodic(String taskId, Class<? extends Job> jobClass, Date startTime, Date stopTime, Integer interval, JobDataMap jobDataMap) {
       return addPeriodic(taskId, jobClass, startTime, stopTime, interval, TimeUnit.MINUTES, jobDataMap);
    }

    /**
     * 在指定时间段内 以指定时间间隔 重复执行任务
     * @param taskId
     * @param jobClass
     * @param startTime
     * @param stopTime
     * @param interval
     * @param timeUnit      TimeUnit.MINUTES | TimeUnit.HOURS | TimeUnit.SECONDS
     * @param jobDataMap    需要向任务传递的参数, 可以为空
     * @return
     */
    public boolean addPeriodic(String taskId, Class<? extends Job> jobClass, Date startTime, Date stopTime, Integer interval, TimeUnit timeUnit, JobDataMap jobDataMap) {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().repeatForever();
        switch (timeUnit) {
            case MINUTES:
                simpleScheduleBuilder.withIntervalInMinutes(interval);
                log.debug("TimeUnit = " + TimeUnit.MINUTES);
                break;
            case HOURS:
                simpleScheduleBuilder.withIntervalInHours(interval);
                log.debug("TimeUnit = " + TimeUnit.HOURS);
            default:
                simpleScheduleBuilder.withIntervalInSeconds(interval);
                log.debug("TimeUnit = " + TimeUnit.SECONDS);
                break;
        }
        return addPeriodic(taskId, jobClass, startTime, stopTime, simpleScheduleBuilder, jobDataMap);
    }

    /**
     * 在指定时间段内 根据CRON表达式 重复执行任务
     *
     * @param taskId
     * @param jobClass
     * @param starTime
     * @param stopTime          如果不填写，则表示任务永久执行
     * @param cronExpression    CRON表达式（必须符合Quartz规范。）
     * @param jobDataMap        需要向任务传递的参数, 可以为空
     * @return
     */
    public boolean addPeriodic(String taskId, Class<? extends Job> jobClass, Date starTime, Date stopTime, String cronExpression, JobDataMap jobDataMap) {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        // 如果使用了CRON表达式，则默认会根据表达式一直执行。所以并不需要额外说明stopTime为空的情况。
        return addPeriodic(taskId, jobClass, starTime, stopTime, cronScheduleBuilder, jobDataMap);
    }

    /**
     * 在指定时间段内 根据
     *
     * @param taskId
     * @param jobClass
     * @param startTime
     * @param stopTime          如果不填写，则表示任务永久执行
     * @param scheduleBuilder
     * @param jobDataMap        需要向任务传递的参数, 可以为空
     * @return
     */
    public boolean addPeriodic(String taskId, Class<? extends Job> jobClass, Date startTime, Date stopTime, ScheduleBuilder<? extends Trigger> scheduleBuilder, JobDataMap jobDataMap) {
        try {
            // 1. JobDetail
            JobBuilder jobBuilder = JobBuilder.newJob(jobClass);
            jobBuilder.withIdentity(new JobKey(taskId, PERIODIC_JOB_GROUP));
            //jobBuilder.storeDurably();// 指定指定该选项, 则任务执行完毕后该任务还会被记录在QRTZ_JOB_DETAILS表中。
            if (jobDataMap != null) {
                jobBuilder.usingJobData(jobDataMap);
            }
            JobDetail jobDetail = jobBuilder.build();

            // 2. Trigger
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            triggerBuilder.withIdentity(new TriggerKey(taskId, PERIODIC_TRIGGER_GROUP));
            triggerBuilder.forJob(jobDetail);
            if (null == startTime) {// 约定不指定开始时间就立即执行
                triggerBuilder.startNow();
            }
            if (null != startTime) {
                triggerBuilder.startAt(startTime);
            }
            if (null != stopTime) {
                triggerBuilder.endAt(stopTime);
            }
            triggerBuilder.withSchedule(scheduleBuilder);
            Trigger trigger = triggerBuilder.build();

            // 3. 注册
            scheduler.scheduleJob(jobDetail, trigger);
            return true;
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取单次任务详情
     *
     * @param taskId
     * @return
     */
    public JobDetail getOnce(String taskId) {
        JobKey jobKey = new JobKey(taskId, ONCE_JOB_GROUP);
        return get(jobKey);
    }

    /**
     * 获取周期任务详情
     *
     * @param taskId
      * @return
     */
    public JobDetail getPeriodic(String taskId) {
        JobKey jobKey = new JobKey(taskId, PERIODIC_JOB_GROUP);
        return get(jobKey);
    }

    /**
     * 检查单次任务是否存在
     *
     * @param taskId
     * @return
     */
    public boolean checkExistsOnce(String taskId) {
        JobKey jobKey = new JobKey(taskId, ONCE_JOB_GROUP);
        return checkExists(jobKey);
    }

    /**
     * 在单次任务组中查找是否有相同类型的任务
     *
     * @param jobClassName
     * @return
     */
    public boolean checkExistsJobClassOnce(String jobClassName) {
        return checkExistsJobClass(jobClassName, GroupMatcher.jobGroupEquals(ONCE_JOB_GROUP));
    }

    /**
     * 在周期任务组中查找是否有相同类型的任务
     *
     * @param jobClassName
     * @return
     */
    public boolean checkExistsJobClassPeriodic(String jobClassName) {
        return checkExistsJobClass(jobClassName, GroupMatcher.jobGroupEquals(PERIODIC_JOB_GROUP));
    }

    private boolean checkExistsJobClass(String jobClassName, GroupMatcher groupMatcher) {
        if (null == jobClassName || "".equals(jobClassName.trim())) {
            return false;
        }
        try {
            Set<JobKey> jobKeys = scheduler.getJobKeys(groupMatcher);
            for (JobKey jobKey : jobKeys) {
                if (jobClassName.equals(scheduler.getJobDetail(jobKey).getJobClass().getName())) {
                    return true;
                }
            }
            return false;
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 检查周期任务是否存在
     *
     * @param taskId
     * @return
     */
    public boolean checkExistsPeriodic(String taskId) {
        JobKey jobKey = new JobKey(taskId, PERIODIC_JOB_GROUP);
        return checkExists(jobKey);
    }


    public boolean pauseOnce(String taskId) {
        JobKey jobKey = new JobKey(taskId, ONCE_JOB_GROUP);
        return pause(jobKey);
    }

    public boolean pausePeriodic(String taskId) {
        JobKey jobKey = new JobKey(taskId, PERIODIC_JOB_GROUP);
        return pause(jobKey);
    }

    public boolean resumeOnce(String taskId) {
        JobKey jobKey = new JobKey(taskId, ONCE_JOB_GROUP);
        return resume(jobKey);
    }

    public boolean resumePeriodic(String taskId) {
        JobKey jobKey = new JobKey(taskId, PERIODIC_JOB_GROUP);
        return resume(jobKey);
    }

    public boolean deleteOnce(String taskId) {
        JobKey jobKey = new JobKey(taskId, ONCE_JOB_GROUP);
        return pause(jobKey) && delete(jobKey);
    }

    public boolean deletePeriodic(String taskId) {
        JobKey jobKey = new JobKey(taskId, PERIODIC_JOB_GROUP);
        return pause(jobKey) && delete(jobKey);
    }

    // /////////////////////////////////////////////////////////////////////
    // 下面的就是把scheduler API简单翻译了一下 方便上层调用

    public JobDetail get(JobKey jobKey) {
        try {
            return scheduler.getJobDetail(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public boolean checkExists(JobKey jobKey) {
        try {
            return scheduler.checkExists(jobKey);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean pause(JobKey jobKey) {
        try {
            scheduler.pauseJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public boolean resume(JobKey jobKey) {
        try {
            scheduler.resumeJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /*
    public boolean resume(TriggerKey triggerKey) {
        try{
            scheduler.resumeTrigger(triggerKey);
            return true;
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
     */

    public boolean delete(JobKey jobKey) {
        try {
            scheduler.deleteJob(jobKey);
            return true;
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}