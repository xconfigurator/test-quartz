package liuyang.testquartz.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 集群配置
 *
 * @author liuyang
 * @scine 2021/4/1
 */
@ConditionalOnProperty(prefix = "liuyang", name = "quartz.mode", havingValue = "cluster")
@Configuration
public class QuartzClusterConfig {

    @Autowired
    private Properties quartzProperties;

    @Autowired
    private Executor executorThreadPool;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Bean
    public Scheduler scheduler() {
        return schedulerFactoryBean.getScheduler();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        // SchedulerFactoryBean 用于读取配置文件
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setSchedulerName("cluster_scheduler_name"); // 不设置这个也没关系
        schedulerFactoryBean.setDataSource(null); // 各个节点交换数据使用的数据源
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("application"); // 不设置这个也没关系
        schedulerFactoryBean.setQuartzProperties(quartzProperties);
        schedulerFactoryBean.setTaskExecutor(executorThreadPool);
        schedulerFactoryBean.setStartupDelay(10);

        return schedulerFactoryBean;
    }

    // 读配置文件
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/spring-quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    // 配置线程池
    @Bean
    public Executor executorThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
        executor.setQueueCapacity(Runtime.getRuntime().availableProcessors());
        return executor;
    }
}
