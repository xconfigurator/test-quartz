package liuyang.testquartz.modules.scheduler.quartz.controller;

import liuyang.testquartz.common.util.DateUtil;
import liuyang.testquartz.common.util.IdUtils;
import liuyang.testquartz.common.util.R;
import liuyang.testquartz.modules.scheduler.quartz.quartzjobbean.FooJob;
import liuyang.testquartz.modules.scheduler.quartz.util.QuartzUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ResolvedMember;
import org.quartz.JobDataMap;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 测试QuartzUtils的动态操纵JOB的能力
 *
 * @author liuyang
 * @scine 2021/9/28
 */
@RestController
@Slf4j
//@Setter// 生成Setter 就可以完成组件的自动注入！ 实测，这样貌似不顶用
@AllArgsConstructor// 实测有效！并且spring也推荐通过构造函进行注入
public class QuartzUtilsTestController {

    // 这里只是演示一下这种注入方式。实际上还是用@Autowired的比较多。Spring自己的示例网站都是用@Autowired private这样去干的。
    private QuartzUtils quartzUtils;// 不使用@Autowired，Spring Framework借助由Lombok生成的有参构造函数成注入。

    // 测试 映射根路径
    @GetMapping
    public R hello() {
        log.info("测试quartzUtils是否被注入 {} " , quartzUtils == null ? "未注入" : "Spring Framework已通过有参构造函数完成了依赖注入");
        return R.ok("hello, world QuartzUtilsTestController");
    }

    // 立即执行一次任务
    // 202109280000 ok
    @GetMapping("/addonce")
    public R addOnce() {
        String taskId = IdUtils.nextTaskId();
        final String REMINDER = "addOnce taskId = " + taskId;
        log.info(REMINDER);

        quartzUtils.addOnce(taskId, FooJob.class);

        return R.ok(REMINDER);
    }

    // 立即执行一次任务 并向任务传递参数
    // 202109281508 ok
    @GetMapping("/addonceparam")
    public R addOnceParam() {
        String taskId = IdUtils.nextTaskId();
        final String REMINDER = "addOnceParam taskId = " + taskId;
        log.info(REMINDER);

        JobDataMap param = new JobDataMap();
        param.put("foo", "bar");
        quartzUtils.addOnce(taskId, FooJob.class, param);

        return R.ok(REMINDER);
    }

    // 在指定时刻执行一次任务  (Use Case action = 1 更改任务状态)
    // 202109281554 ok
    @GetMapping("/addonceat")
    public R addOnceAt() {
        String taskId = IdUtils.nextTaskId();
        final String REMINDER = "addOnceAt 测试环境为5秒之后执行。实际应用中可以指定任意时间点。 taskId = " + taskId;
        log.info(REMINDER);

        Date startTime = DateUtil.asDate(LocalDateTime.now().plusSeconds(5));// 5秒之后
        quartzUtils.addOnce(taskId, FooJob.class, startTime);

        return R.ok(REMINDER);
    }

    // 在指定时刻执行一次任务 并向任务传递参数 （Use Case action = 1 补报任务 传taskId complTaskId）
    // 202109291003 ok
    @GetMapping("/addonceatparam")
    public R addOnceAtParam() {
        String taskId = IdUtils.nextTaskId();
        final String REMINDER = "addOnceAtParam 测试环境为5秒之后执行。实际应用中可以指定任意时间点。 taskId = " + taskId;
        log.info(REMINDER);

        Date startTime = DateUtil.asDate(LocalDateTime.now().plusSeconds(5));// 5秒之后
        JobDataMap param = new JobDataMap();
        param.put("foo", "bar");
        quartzUtils.addOnce(taskId, FooJob.class, startTime, param);

        return R.ok(REMINDER);
    }

    // 周期任务 interval 不指定stopTime
    // 20210929 ok
    @GetMapping("/addperiodicstartnowwithoutstoptime")
    public R addPeriodicStartNowWithoutStopTime() {
        String taskId = IdUtils.nextTaskId();
        final String REMINDER = "addPeriodicWithoutStopTime 测试环境为立即开始，间隔2秒（interval），重复执行不停止。taskId = " + taskId;
        log.info(REMINDER);

        quartzUtils.addPeriodic(taskId, FooJob.class, null, null, 2, TimeUnit.SECONDS, null);

        return R.ok(REMINDER);
    }

    // 周期任务 interval 指定startTime stopTime
    // 202109291033 failure addPeriodic SimpleScheduleBuilder用错了。它的repeatForever是必选，否则只执行一次。这个跟stopTime是不同级别的。
    // 202109291041 ok
    @GetMapping("/addperiodicstartatwithstoptime")
    public R addPeriodicStartAtWithStopTime() {
        String taskId = IdUtils.nextTaskId();
        final String REMINDER = "addPeriodicStartAtWithStopTime 测试环境为5秒之后执行（startTime），间隔2秒（interval），10秒后停止（stopTime） taskId = " + taskId;
        log.info(REMINDER);

        Date startTime = DateUtil.asDate(LocalDateTime.now().plusSeconds(5));
        Date stopTime = DateUtil.asDate(LocalDateTime.now().plusSeconds(10));
        quartzUtils.addPeriodic(taskId, FooJob.class, startTime, stopTime, 2, TimeUnit.SECONDS, null);

        return R.ok(REMINDER);
    }

    // 周期任务 interval 指定startTime stopTime 并传参
    // 202109291056 ok
    @GetMapping("/addperiodicsartatwithstoptimewithjobdata")
    public R addPeriodicStartAtWithStopTimeWithJobData() {
        String taskId = IdUtils.nextTaskId();
        final String REMINDER = "addPeriodicStartAtWithStopTimeWithJobData 测试环境为5秒之后执行（startTime）， 间隔1秒（interval）， 10秒后停止（stopTime）并传参 taskId = " + taskId;
        log.info(REMINDER);

        Date startTime = DateUtil.asDate(LocalDateTime.now().plusSeconds(5));
        Date stopTime = DateUtil.asDate(LocalDateTime.now().plusSeconds(10));
        JobDataMap param = new JobDataMap();
        param.put("foo", "bar");
        quartzUtils.addPeriodic(taskId, FooJob.class, startTime, stopTime, 1, TimeUnit.SECONDS, param);

        return R.ok(REMINDER);
    }

    // 周期任务 CRON表达式 指定startTime stopTime 并传参

    @GetMapping("/addperiodiccronstartatwithstoptime")
    // 202109291058 failure! 哎，竟然是方法没写完！
    // 202109291101 ok
    public R addPeriodicCRON() {
        String taskId = IdUtils.nextTaskId();
        final String REMINDER = "addPeriodicCRON 测试环境为5秒之后执行（startTime）， 间隔1秒（interval）， 10秒后停止（stopTime）并传参 taskId = " + taskId;
        log.info(REMINDER);

        String cron = "0/1 * * * * ?";
        Date startTime = DateUtil.asDate(LocalDateTime.now().plusSeconds(5));
        Date stopTime = DateUtil.asDate(LocalDateTime.now().plusSeconds(10));
        JobDataMap param = new JobDataMap();
        param.put("foo", "bar");
        quartzUtils.addPeriodic(taskId, FooJob.class, startTime, stopTime, cron, param);

        return R.ok(REMINDER);
    }

    // 暂停周期任务 (Use Case)
    // 20210929 ok
    @GetMapping("/pauseperiodic/{taskId}")
    public R pausePeriodic(@PathVariable("taskId") String taskId) {
        final String REMINDER = "pausePeriodic taskId = " + taskId;
        log.info(REMINDER);

        quartzUtils.pausePeriodic(taskId);

        return R.ok(REMINDER);
    }

    // 恢复周期任务 (Use Case)
    // 202109291020 failure! 工具类写错了
    // 202109291027 ok
    @GetMapping("/resumeperiodic/{taskId}")
    public R resumePeriodic(@PathVariable("taskId") String taskId) {
        final String REMINDER = "resumePeriodic taskId = " + taskId;
        log.info(REMINDER);

        quartzUtils.resumePeriodic(taskId);

        return R.ok(REMINDER);
    }

    // 删除周期任务 (Use Case action = 3)
    // 202109291029 ok
    @GetMapping("/deleteperiodic/{taskId}")
    public R deletePeriodic(@PathVariable("taskId") String taskId) {
        final String REMINDER = "deletePeriodic taskId = " + taskId;
        log.info(REMINDER);

        quartzUtils.deletePeriodic(taskId);

        return R.ok(REMINDER);
    }
}
