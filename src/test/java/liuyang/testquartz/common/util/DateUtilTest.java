package liuyang.testquartz.common.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author liuyang
 * @scine 2021/9/9
 */
@Slf4j
public class DateUtilTest {

    @Test
    void testDateUtil() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = DateUtil.asDate(LocalDateTime.now().plusHours(2));
        log.info("两小时以后的时间 = {}", sdf.format(date));
    }

    @Test
    void testDateUtilsTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        now.plusMinutes(1);
        long time = DateUtil.asDate(now).getTime();
        log.info("一分钟之后间戳：" + time);
    }
}
