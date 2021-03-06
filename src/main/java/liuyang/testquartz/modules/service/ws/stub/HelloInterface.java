package liuyang.testquartz.modules.service.ws.stub;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.4.3
 * 2021-06-02T15:42:14.470+08:00
 * Generated source version: 3.4.3
 *
 */
@WebService(targetNamespace = "http://services.server.modules.testcxf.liuyang/", name = "HelloInterface")
@XmlSeeAlso({ObjectFactory.class})
public interface HelloInterface {

    @WebMethod
    @RequestWrapper(localName = "sayHello", targetNamespace = "http://services.server.modules.testcxf.liuyang/", className = "liuyang.testquartz.modules.webserviceclientjob.stub.SayHello")
    @ResponseWrapper(localName = "sayHelloResponse", targetNamespace = "http://services.server.modules.testcxf.liuyang/", className = "liuyang.testquartz.modules.webserviceclientjob.stub.SayHelloResponse")
    @WebResult(name = "return", targetNamespace = "")
    public java.lang.String sayHello(

        @WebParam(name = "arg0", targetNamespace = "")
        java.lang.String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        int arg1
    );
}
