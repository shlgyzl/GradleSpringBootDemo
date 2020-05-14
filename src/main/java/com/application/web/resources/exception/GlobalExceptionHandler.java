package com.application.web.resources.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.application.constants.DateFormatConstants.*;

/**
 * 此类只能监听Controller和RestController的方法,其他运行异常需要重新处理
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
            }
        });
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDateTime.parse(text, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
            }
        });
        binder.registerCustomEditor(ZonedDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(ZonedDateTime.parse(text, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)));
            }
        });
        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalTime.parse(text, DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));
            }
        });
    }

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
        log.error("并发锁修改异常,已加版本锁,请确定数据版本,{}", e.getMessage());
        return ResponseEntity.badRequest().body("并发修改异常了,请确定当前数据版本正确");
    }


    /**
     * 不存在的数据异常
     *
     * @param e 异常
     * @return String
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> objectEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        log.error("操作不存在的数据异常,{}", e.getMessage());
        return ResponseEntity.badRequest().body("不存在的数据异常");
    }

    /**
     * 事务异常
     *
     * @param e 异常
     * @return String
     */
    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> objectTransactionSystemException(TransactionSystemException e) {
        log.error("操作校验失败的数据,{}", e.getMessage());
        return ResponseEntity.badRequest().body("操作校验失败的数据");
    }

    /**
     * 时间转换异常
     *
     * @param e 异常
     * @return String
     */
    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> objectDateTimeParseException(DateTimeParseException e) {
        log.error("时间参数转换异常,{}", e.getMessage());
        return ResponseEntity.badRequest().body("你的时间丢失了");
    }

    /**
     * 系统查询异常
     *
     * @param e 异常
     * @return String
     */
    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> responseStatusException(ResponseStatusException e) {
        log.error("系统查询不存在数据异常,{}", e.getMessage());
        return ResponseEntity.badRequest().body("你是魔鬼嘛,数据丢失了");
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
     * 系统异常
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
