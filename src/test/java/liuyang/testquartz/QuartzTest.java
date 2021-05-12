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
        /*
        "C:\Program Files\Java\jdk-11.0.8\bin\java.exe" -ea -Didea.test.cyclic.buffer.size=1048576 "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2020.3.3\lib\idea_rt.jar=54819:C:\Program Files\JetBrains\IntelliJ IDEA 2020.3.3\bin" -Dfile.encoding=UTF-8 -classpath "C:\Program Files\JetBrains\IntelliJ IDEA 2020.3.3\lib\idea_rt.jar;C:\Users\liuyang\.m2\repository\org\junit\platform\junit-platform-launcher\1.7.1\junit-platform-launcher-1.7.1.jar;C:\Users\liuyang\.m2\repository\org\apiguardian\apiguardian-api\1.1.0\apiguardian-api-1.1.0.jar;C:\Users\liuyang\.m2\repository\org\junit\platform\junit-platform-engine\1.7.1\junit-platform-engine-1.7.1.jar;C:\Users\liuyang\.m2\repository\org\opentest4j\opentest4j\1.2.0\opentest4j-1.2.0.jar;C:\Users\liuyang\.m2\repository\org\junit\platform\junit-platform-commons\1.7.1\junit-platform-commons-1.7.1.jar;C:\Program Files\JetBrains\IntelliJ IDEA 2020.3.3\plugins\junit\lib\junit5-rt.jar;C:\Program Files\JetBrains\IntelliJ IDEA 2020.3.3\plugins\junit\lib\junit-rt.jar;C:\workspaces\workspace_idea_u\test-quartz\target\test-classes;C:\workspaces\workspace_idea_u\test-quartz\target\classes;C:\lib_maven\repository\org\springframework\boot\spring-boot-starter-quartz\2.4.4\spring-boot-starter-quartz-2.4.4.jar;C:\lib_maven\repository\org\springframework\boot\spring-boot-starter\2.4.4\spring-boot-starter-2.4.4.jar;C:\lib_maven\repository\org\springframework\boot\spring-boot\2.4.4\spring-boot-2.4.4.jar;C:\lib_maven\repository\org\springframework\boot\spring-boot-autoconfigure\2.4.4\spring-boot-autoconfigure-2.4.4.jar;C:\lib_maven\repository\org\springframework\boot\spring-boot-starter-logging\2.4.4\spring-boot-starter-logging-2.4.4.jar;C:\lib_maven\repository\ch\qos\logback\logback-classic\1.2.3\logback-classic-1.2.3.jar;C:\lib_maven\repository\ch\qos\logback\logback-core\1.2.3\logback-core-1.2.3.jar;C:\lib_maven\repository\org\apache\logging\log4j\log4j-to-slf4j\2.13.3\log4j-to-slf4j-2.13.3.jar;C:\lib_maven\repository\org\apache\logging\log4j\log4j-api\2.13.3\log4j-api-2.13.3.jar;C:\lib_maven\repository\org\slf4j\jul-to-slf4j\1.7.30\jul-to-slf4j-1.7.30.jar;C:\lib_maven\repository\jakarta\annotation\jakarta.annotation-api\1.3.5\jakarta.annotation-api-1.3.5.jar;C:\lib_maven\repository\org\yaml\snakeyaml\1.27\snakeyaml-1.27.jar;C:\lib_maven\repository\org\springframework\spring-context-support\5.3.5\spring-context-support-5.3.5.jar;C:\lib_maven\repository\org\springframework\spring-beans\5.3.5\spring-beans-5.3.5.jar;C:\lib_maven\repository\org\springframework\spring-context\5.3.5\spring-context-5.3.5.jar;C:\lib_maven\repository\org\springframework\spring-tx\5.3.5\spring-tx-5.3.5.jar;C:\lib_maven\repository\org\quartz-scheduler\quartz\2.3.2\quartz-2.3.2.jar;C:\lib_maven\repository\com\mchange\mchange-commons-java\0.2.15\mchange-commons-java-0.2.15.jar;C:\lib_maven\repository\org\slf4j\slf4j-api\1.7.30\slf4j-api-1.7.30.jar;C:\lib_maven\repository\org\springframework\boot\spring-boot-starter-thymeleaf\2.4.4\spring-boot-starter-thymeleaf-2.4.4.jar;C:\lib_maven\repository\org\thymeleaf\thymeleaf-spring5\3.0.12.RELEASE\thymeleaf-spring5-3.0.12.RELEASE.jar;C:\lib_maven\repository\org\thymeleaf\thymeleaf\3.0.12.RELEASE\thymeleaf-3.0.12.RELEASE.jar;C:\lib_maven\repository\org\attoparser\attoparser\2.0.5.RELEASE\attoparser-2.0.5.RELEASE.jar;C:\lib_maven\repository\org\unbescape\unbescape\1.1.6.RELEASE\unbescape-1.1.6.RELEASE.jar;C:\lib_maven\repository\org\thymeleaf\extras\thymeleaf-extras-java8time\3.0.4.RELEASE\thymeleaf-extras-java8time-3.0.4.RELEASE.jar;C:\lib_maven\repository\org\springframework\boot\spring-boot-starter-web\2.4.4\spring-boot-starter-web-2.4.4.jar;C:\lib_maven\repository\org\springframework\boot\spring-boot-starter-json\2.4.4\spring-boot-starter-json-2.4.4.jar;C:\lib_maven\repository\com\fasterxml\jackson\core\jackson-databind\2.11.4\jackson-databind-2.11.4.jar;C:\lib_maven\repository\com\fasterxml\jackson\core\jackson-annotations\2.11.4\jackson-annotations-2.11.4.jar;C:\lib_maven\repository\com\fasterxml\jackson\core\jackson-core\2.11.4\jackson-core-2.11.4.jar;C:\lib_maven\repository\com\fasterxml\jackson\datatype\jackson-datatype-jdk8\2.11.4\jackson-datatype-jdk8-2.11.4.jar;C:\lib_maven\repository\com\fasterxml\jackson\datatype\jackson-datatype-jsr310\2.11.4\jackson-datatype-jsr310-2.11.4.jar;C:\lib_maven\repository\com\fasterxml\jackson\module\jackson-module-parameter-names\2.11.4\jackson-module-parameter-names-2.11.4.jar;C:\lib_maven\repository\org\springframework\boot\spring-boot-starter-tomcat\2.4.4\spring-boot-starter-tomcat-2.4.4.jar;C:\lib_maven\repository\org\apache\tomcat\embed\tomcat-embed-core\9.0.44\tomcat-embed-core-9.0.44.jar;C:\lib_maven\repository\org\glassfish\jakarta.el\3.0.3\jakarta.el-3.0.3.jar;C:\lib_maven\repository\org\apache\tomcat\embed\tomcat-embed-websocket\9.0.44\tomcat-embed-websocket-9.0.44.jar;C:\lib_maven\repository\org\springframework\spring-web\5.3.5\spring-web-5.3.5.jar;C:\lib_maven\repository\org\springframework\spring-webmvc\5.3.5\spring-webmvc-5.3.5.jar;C:\lib_maven\repository\org\springframework\spring-aop\5.3.5\spring-aop-5.3.5.jar;C:\lib_maven\repository\org\springframework\spring-expression\5.3.5\spring-expression-5.3.5.jar;C:\lib_maven\repository\org\projectlombok\lombok\1.18.18\lombok-1.18.18.jar;C:\lib_maven\repository\org\springframework\boot\spring-boot-starter-test\2.4.4\spring-boot-starter-test-2.4.4.jar;C:\lib_maven\repository\org\springframework\boot\spring-boot-test\2.4.4\spring-boot-test-2.4.4.jar;C:\lib_maven\repository\org\springframework\boot\spring-boot-test-autoconfigure\2.4.4\spring-boot-test-autoconfigure-2.4.4.jar;C:\lib_maven\repository\com\jayway\jsonpath\json-path\2.4.0\json-path-2.4.0.jar;C:\lib_maven\repository\net\minidev\json-smart\2.3\json-smart-2.3.jar;C:\lib_maven\repository\net\minidev\accessors-smart\1.2\accessors-smart-1.2.jar;C:\lib_maven\repository\org\ow2\asm\asm\5.0.4\asm-5.0.4.jar;C:\lib_maven\repository\jakarta\xml\bind\jakarta.xml.bind-api\2.3.3\jakarta.xml.bind-api-2.3.3.jar;C:\lib_maven\repository\jakarta\activation\jakarta.activation-api\1.2.2\jakarta.activation-api-1.2.2.jar;C:\lib_maven\repository\org\assertj\assertj-core\3.18.1\assertj-core-3.18.1.jar;C:\lib_maven\repository\org\hamcrest\hamcrest\2.2\hamcrest-2.2.jar;C:\lib_maven\repository\org\junit\jupiter\junit-jupiter\5.7.1\junit-jupiter-5.7.1.jar;C:\lib_maven\repository\org\junit\jupiter\junit-jupiter-api\5.7.1\junit-jupiter-api-5.7.1.jar;C:\lib_maven\repository\org\apiguardian\apiguardian-api\1.1.0\apiguardian-api-1.1.0.jar;C:\lib_maven\repository\org\opentest4j\opentest4j\1.2.0\opentest4j-1.2.0.jar;C:\lib_maven\repository\org\junit\platform\junit-platform-commons\1.7.1\junit-platform-commons-1.7.1.jar;C:\lib_maven\repository\org\junit\jupiter\junit-jupiter-params\5.7.1\junit-jupiter-params-5.7.1.jar;C:\lib_maven\repository\org\junit\jupiter\junit-jupiter-engine\5.7.1\junit-jupiter-engine-5.7.1.jar;C:\lib_maven\repository\org\junit\platform\junit-platform-engine\1.7.1\junit-platform-engine-1.7.1.jar;C:\lib_maven\repository\org\mockito\mockito-core\3.6.28\mockito-core-3.6.28.jar;C:\lib_maven\repository\net\bytebuddy\byte-buddy\1.10.22\byte-buddy-1.10.22.jar;C:\lib_maven\repository\net\bytebuddy\byte-buddy-agent\1.10.22\byte-buddy-agent-1.10.22.jar;C:\lib_maven\repository\org\objenesis\objenesis\3.1\objenesis-3.1.jar;C:\lib_maven\repository\org\mockito\mockito-junit-jupiter\3.6.28\mockito-junit-jupiter-3.6.28.jar;C:\lib_maven\repository\org\skyscreamer\jsonassert\1.5.0\jsonassert-1.5.0.jar;C:\lib_maven\repository\com\vaadin\external\google\android-json\0.0.20131108.vaadin1\android-json-0.0.20131108.vaadin1.jar;C:\lib_maven\repository\org\springframework\spring-core\5.3.5\spring-core-5.3.5.jar;C:\lib_maven\repository\org\springframework\spring-jcl\5.3.5\spring-jcl-5.3.5.jar;C:\lib_maven\repository\org\springframework\spring-test\5.3.5\spring-test-5.3.5.jar;C:\lib_maven\repository\org\xmlunit\xmlunit-core\2.7.0\xmlunit-core-2.7.0.jar" com.intellij.rt.junit.JUnitStarter -ideVersion5 -junit5 liuyang.testquartz.QuartzTest,runOnceTask
13:47:35.155 [main] INFO org.quartz.impl.StdSchedulerFactory - Using default implementation for ThreadExecutor
13:47:35.161 [main] INFO org.quartz.simpl.SimpleThreadPool - Job execution threads will use class loader of thread: main
13:47:35.174 [main] INFO org.quartz.core.SchedulerSignalerImpl - Initialized Scheduler Signaller of type: class org.quartz.core.SchedulerSignalerImpl
13:47:35.174 [main] INFO org.quartz.core.QuartzScheduler - Quartz Scheduler v.2.3.2 created.
13:47:35.175 [main] INFO org.quartz.simpl.RAMJobStore - RAMJobStore initialized.
13:47:35.175 [main] INFO org.quartz.core.QuartzScheduler - Scheduler meta-data: Quartz Scheduler (v2.3.2) 'DefaultQuartzScheduler' with instanceId 'NON_CLUSTERED'
  Scheduler class: 'org.quartz.core.QuartzScheduler' - running locally.
  NOT STARTED.
  Currently in standby mode.
  Number of jobs executed: 0
  Using thread pool 'org.quartz.simpl.SimpleThreadPool' - with 10 threads.
  Using job-store 'org.quartz.simpl.RAMJobStore' - which does not support persistence. and is not clustered.

13:47:35.176 [main] INFO org.quartz.impl.StdSchedulerFactory - Quartz scheduler 'DefaultQuartzScheduler' initialized from default resource file in Quartz package: 'quartz.properties'
13:47:35.176 [main] INFO org.quartz.impl.StdSchedulerFactory - Quartz scheduler version: 2.3.2
13:47:35.187 [main] INFO liuyang.testquartz.QuartzTest - scheduler.start()运行前 job1是否存在 ：true
13:47:35.187 [main] INFO org.quartz.core.QuartzScheduler - Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED started.
13:47:35.189 [main] INFO liuyang.testquartz.QuartzTest - scheduler.start()运行后 job1是否存在 ：true
13:47:35.191 [DefaultQuartzScheduler_QuartzSchedulerThread] DEBUG org.quartz.core.QuartzSchedulerThread - batch acquisition of 1 triggers
13:47:35.197 [DefaultQuartzScheduler_QuartzSchedulerThread] DEBUG org.quartz.simpl.PropertySettingJobFactory - Producing instance of Job 'LIUYANG_QUARTZ_SINGLE_JOB_GROUP_NAME_single.job1', class=liuyang.testquartz.quartz.HelloJob
13:47:35.204 [DefaultQuartzScheduler_QuartzSchedulerThread] DEBUG org.quartz.core.QuartzSchedulerThread - batch acquisition of 0 triggers
13:47:35.205 [DefaultQuartzScheduler_Worker-1] DEBUG org.quartz.core.JobRunShell - Calling execute on job LIUYANG_QUARTZ_SINGLE_JOB_GROUP_NAME_single.job1
13:47:35.258 [DefaultQuartzScheduler_Worker-1] INFO liuyang.testquartz.quartz.HelloJob - hello Quartz!  in Spring Boot ENV at 2021-05-12T13:47:35.244891700
13:47:35.260 [DefaultQuartzScheduler_Worker-1] INFO liuyang.testquartz.quartz.HelloJob - JobDetail Group = LIUYANG_QUARTZ_SINGLE_JOB_GROUP_NAME_single
13:47:35.260 [DefaultQuartzScheduler_Worker-1] INFO liuyang.testquartz.quartz.HelloJob - JobDetail Name = job1
13:47:35.260 [DefaultQuartzScheduler_Worker-1] INFO liuyang.testquartz.quartz.HelloJob - Trigger Group = LIUYANG_QUARTZ_SINGLE_TRIGGER_GROUP_NAME_single
13:47:35.261 [DefaultQuartzScheduler_Worker-1] INFO liuyang.testquartz.quartz.HelloJob - Trigger Name = trigger1
13:47:55.203 [main] INFO liuyang.testquartz.QuartzTest - 确定job1 已经运行晚一次后查看 job1是否存在 ：false

Process finished with exit code 0

         */
    }
}
