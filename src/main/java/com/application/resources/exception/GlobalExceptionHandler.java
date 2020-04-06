package com.application.resources.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 此类只能监听Controller和RestController的方法,其他运行异常需要重新处理
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 参数异常处理
     *
     * @param e 异常
     * @return String
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHttpMessageNotReadableException(MissingServletRequestParameterException e) {
        log.error("缺少请求参数,{}", e.getMessage());
        return ResponseEntity.badRequest().body("缺少请求参数");
    }

    /**
     * 空指针异常
     *
     * @param e 异常
     * @return String
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleTypeMismatchException(NullPointerException e) {
        log.error("空指针异常,{}", e.getMessage());
        return ResponseEntity.badRequest().body("空指针异常了");
    }

    /**
     * 锁机制异常
     *
     * @param e 异常
     * @return String
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> objectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException e) {
        log.error("并发修改异常,已加锁,{}", e.getMessage());
        return ResponseEntity.badRequest().body("并发修改异常了");
    }

    /**
     * 业务异常
     *
     * @param e 异常
     * @return String
     */
    @ExceptionHandler(BusinessErrorException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> businessErrorException(BusinessErrorException e) {
        log.error("业务异常,{}", e.getMsg());
        return ResponseEntity.ok().body(e.getMsg());
    }

    /**
     * 统一异常
     *
     * @param e 异常
     * @return String
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleUnexpectedServer(Exception e) {
        log.error("系统异常：", e);
        return ResponseEntity.badRequest().body("系统发生异常，请联系管理员");
    }
}
