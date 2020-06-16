package com.application.jpa.web.rest.exception;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;
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
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static com.application.jpa.common.constants.DateFormatConstants.*;

/**
 * 此类只能监听Controller和RestController的方法,其他运行异常需要重新处理
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    private static final DatePattern datePattern = new DatePattern();

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(LocalDate.parse(text, DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)));
            }
        });
        binder.registerCustomEditor(Instant.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(Instant.parse(text));
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

        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                String[] pattern = Arrays.stream(datePattern.getClass().getFields())
                        .filter(field -> Modifier.isPublic(field.getModifiers()))
                        .filter(field -> Objects.equals(field.getType(), String.class))
                        .map(field -> (String) BeanUtil.getFieldValue(datePattern, field.getName()))
                        .toArray(String[]::new);
                try {
                    setValue(DateUtils.parseDate(text, pattern));
                } catch (ParseException e) {
                    String date = text.replaceFirst("([+-])(0\\d)\\:(\\d{2})$", "$1$2$3");
                    date = date.replaceFirst("Z$", "+0000");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    try {
                        setValue(dateFormat.parse(date));
                        return;
                    } catch (Exception e1) {
                        log.warn("{}:{}日期格式不正确,转换异常", getClass().getSimpleName(), text);
                    }

                    org.joda.time.format.DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();
                    DateTime dateTime = dateTimeFormatter.parseDateTime(text);
                    org.joda.time.format.DateTimeFormatter timeFormatter = DateTimeFormat.mediumDateTime();
                    String print = timeFormatter.print(dateTime);
                    try {
                        setValue(DateUtils.parseDate(print, pattern));
                    } catch (ParseException e1) {
                        log.warn("{}:{}日期格式不正确,转换异常", getClass().getSimpleName(), text);
                    }
                } catch (RuntimeException e) {
                    //setValue(DateUtil.fromISO8601(text));
                }
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
