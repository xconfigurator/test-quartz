package liuyang.testquartz.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author liuyang
 * @scine 2021/3/30
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        log.error(e.getMessage(), e);
        return "Server ERROR!";
    }
}
