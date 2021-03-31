package liuyang.testquartz.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

/**
 * @author liuyang
 * @scine 2021/3/31
 */
@Service
@Slf4j
public class ScheduledService {

    /**
     * 说明：
     * The fields read from left to right are interpreted as follows.
     * second
     * minute
     * hour
     * day of month
     * month
     * day of week 周几 （0，7表示SUN）
     *
     * 详细参考：@Scheduled的源码注释
     */
    // @Scheduled(cron = "* * * * * *") // 每秒运行一次
    // @Scheduled(cron = "0 * * * * MON-SAT") // 周一到周六每分钟的第一秒打印
    // @Scheduled(cron = "0,1,2,3,4 * * * * 1-6") // 周一到周六每分钟的第1，2，3，4，5秒执行 // 现象(2021/3/31): 很奇怪 0的哪一项会被连续调用两次？！ 答：上面的注解并没有被注释，同时生效了！
    @Scheduled(cron = "0/4 * * * * 1-6") // 周一到周六每4秒执行一次
    public void hello() {
        log.info("Quartz method be invoked! at " + LocalTime.now());
    }
}
