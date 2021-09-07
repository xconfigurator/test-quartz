package liuyang.testquartz.modules.archived.init;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 在容器启动的时候启动Quartz
 * 注: 视频里介绍的这种使用方法好像有问题,暂时不采用这种方式!
 *
 * @author liuyang
 * @scine 2021/4/1
 */
@Deprecated
//@Component
public class QuartzSingleListerner implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private Scheduler scheduler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }
}
