package liuyang.testquartz.component;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * https://www.bilibili.com/video/BV1f54y1m7rW?p=8
 * 这个是视频作者推荐的最接近Quartz原生写法的方案。而且在Spring Boot和Spring Framework环境中通用。
 * liuyang:我觉得还是Spring Boot封装的写法更好！
 * @author liuyang
 * @scine 2021/5/31
 */
// @Component
public class JobInit {

    @Autowired
    Scheduler scheduler;

    @PostConstruct
    public void init() {
        // JobDetail
        // Trigger
        // scheduler.scheduleJob(jobDetail, trigger);
    }
}
