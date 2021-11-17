package liuyang.testquartz;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.DateBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liuyang
 * @scine 2021/11/17
 */
@Slf4j
public class QuartzDateBuilderTest {

    @Test
    void testDateBuilder() {
        Date dateFuture = DateBuilder.futureDate(80, DateBuilder.IntervalUnit.SECOND);
        Date dateBefore = DateBuilder.futureDate(-80, DateBuilder.IntervalUnit.SECOND);

        // 输出
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("dateFuture = {}", sdf.format(dateFuture));
        log.info("dateBefore = {}", sdf.format(dateBefore));
    }
}
