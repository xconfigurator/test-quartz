package liuyang.testquartz.modules.service;

import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * @author liuyang
 * @scine 2021/5/31
 */
@Component
@ToString
public class DemoService {

    public String hey() {
        return "hello, I'am from Spring Environment!";
    }
}
