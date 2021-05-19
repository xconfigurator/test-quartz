# 定时

## 澄清概念
Spring Boot Schedule 并不是Quartz！（CRON表达式在星期上略有不同）

## 开启 Spring Boot Schedule

```Java
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Scheduled(cron = "* * * * * *")
```

## 开启 Quartz


## 初始化工程
```shell
…or create a new repository on the command line
echo "# test-quartz" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin git@github.com:xconfigurator/test-quartz.git
git push -u origin main

…or push an existing repository from the command line
git remote add origin git@github.com:xconfigurator/test-quartz.git
git branch -M main
git push -u origin main
…or import code from another repository
You can initialize this repository with code from a Subversion, Mercurial, or TFS project.
```

## QuartzTest中测试一次调佣job是否清除的输出日志
```shell
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
```