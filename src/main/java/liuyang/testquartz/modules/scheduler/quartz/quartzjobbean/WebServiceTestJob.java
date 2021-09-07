package liuyang.testquartz.modules.scheduler.quartz.quartzjobbean;

import liuyang.testquartz.modules.service.ws.stub.HelloInterface;
import liuyang.testquartz.modules.service.ws.stub.HelloServiceService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * wsdl2java -p liuyang.testquartz.modules.webserviceclientjob.stub -client http://localhost/soap/hello?wsdl
 *
 * @author liuyang
 * @scine 2021/6/2
 */
@Slf4j
public class WebServiceTestJob extends QuartzJobBean {

    private static final QName SERVICE_NAME = new QName("http://services.server.modules.testcxf.liuyang/", "HelloServiceService");
    private static URL wsdlURL = null;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("ws....");

        // URL
        // change url here
        try {
            wsdlURL = new URL("http://localhost/soap/hello?wsdl");
        } catch (MalformedURLException e) {
            // e.printStackTrace();
            log.error(e.getMessage(), e);
        }

        HelloServiceService ss = new HelloServiceService(wsdlURL, SERVICE_NAME);
        HelloInterface port = ss.getHelloServicePort();

        // Question: Memory Leak? 20210602
        log.info(port.sayHello("liuyang", 98));
    }
}
