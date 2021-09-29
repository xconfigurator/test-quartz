package liuyang.testquartz.common.util;

import java.util.UUID;

/**
 * @author liuyang
 * @scine 2021/9/27
 */
public class IdUtils {
    public static String nextTaskId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
