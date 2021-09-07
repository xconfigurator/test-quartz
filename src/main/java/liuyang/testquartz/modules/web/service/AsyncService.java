package liuyang.testquartz.modules.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author liuyang
 * @scine 2021/3/30
 */
@Service
@Slf4j
public class AsyncService {

    public String doSomething() throws InterruptedException {
        log.info("#################Sync begin");
        TimeUnit.SECONDS.sleep(20);
        log.info("#################Sync end");
        return "SUCCESS";
    }

    @Async
    public String doSomethingAsync() throws InterruptedException {
        log.info("#################Async begin");
        TimeUnit.SECONDS.sleep(20);
        log.info("#################Async end");
        return "SUCCESS";
    }
}
